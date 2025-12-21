package com.example.healthfitness.service;

import com.example.healthfitness.model.*;
import com.example.healthfitness.model.Unit;
import com.example.healthfitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class MealMacroService {

    @Autowired private MealRepository mealRepository;
    @Autowired private MealItemRepository mealItemRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private UserRepository userRepository;

    public Meal getOrCreateMeal(Long userId , LocalDate date , MealType type) {
        var user = userRepository.findById(userId).orElseThrow();
        var existing = mealRepository.findByUserAndDateAndMealType(user, date, type);
        if (existing != null) return existing;
        var m = new Meal();
        m.setUser(user);
        m.setDate(date);
        m.setMealType(type);
        return mealRepository.save(m);
    }

    public MealItem addItem(Long mealId , Long foodItemId , Integer quantity) {
        var meal = mealRepository.findById(mealId).orElseThrow();
        var fi   = foodItemRepository.findById(foodItemId).orElseThrow();
        var mi = new MealItem();
        mi.setMeal(meal);
        mi.setFoodItem(fi);
        mi.setQuantity(quantity);
        return mealItemRepository.save(mi);
    }

    public void removeItem(Long itemId) {
        mealItemRepository.deleteById(itemId);
    }

    public void setManual(Long mealId , Integer kcal , Integer p , Integer f , Integer c) {
        var m = mealRepository.findById(mealId).orElseThrow();
        m.setKcalManual(kcal);  m.setProteinManual(p);  m.setFatManual(f);  m.setCarbsManual(c);
        mealRepository.save(m);
    }

    public Map<String,Integer> totalsForMeal(Long mealId) {
        var m = mealRepository.findById(mealId).orElseThrow();
        if (m.getKcalManual()!=null && m.getProteinManual()!=null && m.getFatManual()!=null && m.getCarbsManual()!=null) {
            return map(m.getKcalManual(), m.getProteinManual(), m.getFatManual(), m.getCarbsManual());
        }
        int kcal=0, p=0, f=0, c=0;
        for (var it : m.getItems()) {
            var fi = it.getFoodItem();
            // Use the Unit enum to determine how to interpret quantity.
            if (fi.getUnit() == Unit.G) {
                double k = it.getQuantity() / 100.0;
                kcal += Math.round( fi.getKcal100()    * k );
                p    += Math.round( fi.getProtein100() * k );
                f    += Math.round( fi.getFat100()     * k );
                c    += Math.round( fi.getCarbs100()   * k );
            } else {
                // Assume quantity is number of pieces.
                kcal += fi.getKcal100()    * it.getQuantity();
                p    += fi.getProtein100() * it.getQuantity();
                f    += fi.getFat100()     * it.getQuantity();
                c    += fi.getCarbs100()   * it.getQuantity();
            }
        }
        return map(kcal,p,f,c);
    }

 public Map<String,Integer> totalsForDay(Long userId , LocalDate date) {
        var user = userRepository.findById(userId).orElseThrow();
        var list = mealRepository.findByUserAndDate(user, date);
        int kcal=0, p=0, f=0, c=0;
        for (var m : list) {
            var t = totalsForMeal(m.getMealId());   // <-- było m.getId()
            kcal += t.get("kcal");  p += t.get("protein");  f += t.get("fat");  c += t.get("carbs");
        }
        return map(kcal,p,f,c);
    }

    public void copyDay(Long userId , LocalDate from , LocalDate to) {
        var user = userRepository.findById(userId).orElseThrow();
        var srcMeals = mealRepository.findByUserAndDate(user, from);
        for (var src : srcMeals) {
            var dst = getOrCreateMeal(userId, to, src.getMealType());
            dst.setKcalManual(src.getKcalManual());
            dst.setProteinManual(src.getProteinManual());
            dst.setFatManual(src.getFatManual());
            dst.setCarbsManual(src.getCarbsManual());
            mealRepository.save(dst);
            for (var it : src.getItems()) {
                var copy = new MealItem();
                copy.setMeal(dst);
                copy.setFoodItem(it.getFoodItem());
                copy.setQuantity(it.getQuantity());
                mealItemRepository.save(copy);
            }
        }
    }

    private Map<String,Integer> map(int kcal,int p,int f,int c){
        var m = new HashMap<String,Integer>();
        m.put("kcal",kcal); m.put("protein",p); m.put("fat",f); m.put("carbs",c);
        return m;
    }
}
