package com.example.healthfitness.service;

import com.example.healthfitness.model.Guide;
import com.example.healthfitness.model.GuideCategory;
import com.example.healthfitness.repository.GuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class GuideFlowService {

    @Autowired private GuideRepository guideRepository;

    public List<Guide> listAll(GuideCategory category){
        var all = guideRepository.findAll(Sort.by(Sort.Direction.ASC , "id"));
        if (category == null) return all;
        return all.stream().filter(g -> g.getCategory() == category).toList();
    }

    public Guide add(String title , GuideCategory category , String content , String mediaUrl){
        var g = new Guide();
        g.setTitle(title);
        g.setCategory(category);
        g.setContentHtml(content);
        g.setImageUrl(mediaUrl);

        var base = slugify(title);
        var slug = base;
        int i = 2;
        while (guideRepository.existsBySlug(slug)) {
            slug = base + "-" + i++;
        }
        g.setSlug(slug);

        return guideRepository.save(g);
    }

    public void delete(Long id){
        guideRepository.deleteById(id);
    }

    private static String slugify(String input){
        if (input == null) return "guide";
        String n = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+" , "");
        n = n.toLowerCase()
             .replaceAll("[^a-z0-9]+","-")
             .replaceAll("^-+","")
             .replaceAll("-+$","")
             .replaceAll("-{2,}","-");
        return n.isBlank()? "guide" : n;
    }
}

