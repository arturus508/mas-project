package com.example.healthfitness.config;

import com.example.healthfitness.model.FoodItem;
import com.example.healthfitness.repository.FoodItemRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class FoodItemSeedConfig {

    @Bean
    CommandLineRunner seedFood(FoodItemRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(make("Chicken breast" , "g" ,165,31,3,0));
            repo.save(make("Rice  cooked"  , "g" ,130,2,0,28));
            repo.save(make("Oats  dry"     , "g" ,389,17,7,66));
            repo.save(make("Egg"           , "piece" ,78,6,5,0));
            repo.save(make("Banana"        , "piece" ,105,1,0,27));
            repo.save(make("Olive oil"     , "g" ,884,0,100,0));
            repo.save(make("Whey scoop"    , "piece" ,120,24,2,3));
            repo.save(make("Greek yogurt"  , "g" ,59,10,0,3));
            repo.save(make("Apple"         , "piece" ,95,0,0,25));
        };
    }

    private FoodItem make(String n , String u , int kcal , int p , int f , int c) {
        var x = new FoodItem();
        x.setName(n); x.setUnit(u);
        x.setKcal100(kcal); x.setProtein100(p); x.setFat100(f); x.setCarbs100(c);
        return x;
    }
}
