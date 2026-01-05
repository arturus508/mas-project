package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.*;
import com.example.healthfitness.model.Unit;
import com.example.healthfitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class MealMacroService {

    @Autowired private MealRepository mealRepository;
    @Autowired private MealItemRepository mealItemRepository;
    @Autowired private FoodItemRepository foodItemRepository;
    @Autowired private UserRepository userRepository;

    public Meal getOrCreateMeal(Long userId , LocalDate date , MealType type) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        var existing = mealRepository.findByUserAndDateAndMealType(user, date, type);
        if (existing != null) return existing;
        var m = new Meal();
        m.setUser(user);
        m.setDate(date);
        m.setMealType(type);
        return mealRepository.save(m);
    }

    public MealItem addItem(Long userId, Long mealId, Long foodItemId, Integer quantity) {
        var meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + mealId));
        if (!meal.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Meal does not belong to current user");
        }
        var fi   = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + foodItemId));
        var mi = new MealItem();
        mi.setMeal(meal);
        mi.setFoodItem(fi);
        mi.setQuantity(quantity == null ? 1 : Math.max(1, quantity));
        mi.setSourceType(MealItemSourceType.FOOD_DB);
        return mealItemRepository.save(mi);
    }

    public MealItem addCustomItem(Long userId, Long mealId, String name, Integer kcal, Integer p, Integer f, Integer c, Integer quantity) {
        var meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + mealId));
        if (!meal.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Meal does not belong to current user");
        }
        String trimmedName = name == null ? "" : name.trim();
        int qty = quantity == null ? 1 : Math.max(1, quantity);

        var mi = new MealItem();
        mi.setMeal(meal);
        mi.setName(trimmedName);
        mi.setKcal(kcal);
        mi.setProtein(p);
        mi.setFat(f);
        mi.setCarbs(c);
        mi.setQuantity(qty);
        mi.setSourceType(MealItemSourceType.CUSTOM);
        MealItem saved = mealItemRepository.save(mi);

        if (!trimmedName.isBlank()) {
            var existing = foodItemRepository.findByNameIgnoreCase(trimmedName);
            if (existing.isEmpty()) {
                int baseQty = quantity == null ? 100 : Math.max(1, quantity);
                FoodItem food = new FoodItem();
                food.setName(trimmedName);
                food.setUnit(Unit.G);
                food.setKcal100(scalePer100(kcal, baseQty));
                food.setProtein100(scalePer100(p, baseQty));
                food.setFat100(scalePer100(f, baseQty));
                food.setCarbs100(scalePer100(c, baseQty));
                foodItemRepository.save(food);
            }
        }
        return saved;
    }

    private int scalePer100(Integer value, int quantity) {
        int safe = value == null ? 0 : value;
        return (int) Math.round(safe * 100.0 / quantity);
    }

    public void removeItem(Long userId, Long itemId) {
        MealItem item = mealItemRepository.findWithMealById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal item not found with id: " + itemId));
        if (!item.getMeal().getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Meal item does not belong to current user");
        }
        mealItemRepository.delete(item);
    }

    public void setManual(Long userId, Long mealId, Integer kcal, Integer p, Integer f, Integer c) {
        var m = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + mealId));
        if (!m.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Meal does not belong to current user");
        }
        m.setKcalManual(kcal);  m.setProteinManual(p);  m.setFatManual(f);  m.setCarbsManual(c);
        mealRepository.save(m);
    }

    public Map<String,Integer> totalsForMeal(Long mealId) {
        var m = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + mealId));
        if (m.getKcalManual()!=null && m.getProteinManual()!=null && m.getFatManual()!=null && m.getCarbsManual()!=null) {
            return map(m.getKcalManual(), m.getProteinManual(), m.getFatManual(), m.getCarbsManual());
        }
        int kcal=0, p=0, f=0, c=0;
        for (var it : mealItemRepository.findByMealIdWithFoodItem(mealId)) {
            var fi = it.getFoodItem();
            if (fi != null) {
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
            } else {
                kcal += it.getKcal() == null ? 0 : it.getKcal();
                p    += it.getProtein() == null ? 0 : it.getProtein();
                f    += it.getFat() == null ? 0 : it.getFat();
                c    += it.getCarbs() == null ? 0 : it.getCarbs();
            }
        }
        return map(kcal,p,f,c);
    }

    public Map<String,Integer> totalsForDay(Long userId , LocalDate date) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        var list = mealRepository.findByUserAndDate(user, date);
        int kcal=0, p=0, f=0, c=0;
        for (var m : list) {
            var t = totalsForMeal(m.getMealId());   // <-- było m.getId()
            kcal += t.get("kcal");  p += t.get("protein");  f += t.get("fat");  c += t.get("carbs");
        }
        return map(kcal,p,f,c);
    }

    public List<MealItem> getItems(Long userId, Long mealId) {
        var meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + mealId));
        if (!meal.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Meal does not belong to current user");
        }
        return mealItemRepository.findByMealIdWithFoodItem(mealId);
    }

    @Transactional
    public void copyDay(Long userId , LocalDate from , LocalDate to) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        var srcMeals = mealRepository.findByUserAndDate(user, from);
        for (var src : srcMeals) {
            var dst = getOrCreateMeal(userId, to, src.getMealType());
            dst.setKcalManual(src.getKcalManual());
            dst.setProteinManual(src.getProteinManual());
            dst.setFatManual(src.getFatManual());
            dst.setCarbsManual(src.getCarbsManual());
            mealRepository.save(dst);
            var items = mealItemRepository.findByMealIdWithFoodItem(src.getMealId());
            for (var it : items) {
                var copy = new MealItem();
                copy.setMeal(dst);
                copy.setFoodItem(it.getFoodItem());
                copy.setQuantity(it.getQuantity() == null ? 1 : it.getQuantity());
                copy.setName(it.getName());
                copy.setKcal(it.getKcal());
                copy.setProtein(it.getProtein());
                copy.setFat(it.getFat());
                copy.setCarbs(it.getCarbs());
                var source = it.getSourceType();
                if (source == null) {
                    source = it.getFoodItem() == null ? MealItemSourceType.CUSTOM : MealItemSourceType.FOOD_DB;
                }
                copy.setSourceType(source);
                dst.getItems().add(copy);
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
