package com.example.healthfitness.config;

import com.example.healthfitness.model.FoodItem;
import com.example.healthfitness.model.Unit;
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
            repo.save(make("Chicken breast" , Unit.G    , 165, 31, 3 , 0));
            repo.save(make("Rice  cooked"  , Unit.G    , 130, 2 , 0 , 28));
            repo.save(make("Oats  dry"     , Unit.G    , 389, 17, 7 , 66));
            repo.save(make("Egg"           , Unit.PIECE, 78 , 6 , 5 , 0));
            repo.save(make("Banana"        , Unit.PIECE, 105, 1 , 0 , 27));
            repo.save(make("Olive oil"     , Unit.G    , 884, 0 , 100, 0));
            repo.save(make("Whey scoop"    , Unit.PIECE, 120, 24, 2 , 3));
            repo.save(make("Greek yogurt"  , Unit.G    , 59 , 10, 0 , 3));
            repo.save(make("Apple"         , Unit.PIECE, 95 , 0 , 0 , 25));
        };
    }

    private FoodItem make(String name, Unit unit, int kcal, int protein, int fat, int carbs) {
        var item = new FoodItem();
        item.setName(name);
        item.setUnit(unit);
        item.setKcal100(kcal);
        item.setProtein100(protein);
        item.setFat100(fat);
        item.setCarbs100(carbs);
        return item;
    }
}

