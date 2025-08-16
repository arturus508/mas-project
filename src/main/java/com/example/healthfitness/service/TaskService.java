package com.example.healthfitness.service;

import com.example.healthfitness.model.Task;
import com.example.healthfitness.repository.TaskRepository;
import com.example.healthfitness.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;

    public List<Task> listForDate(Long userId , LocalDate date){
        var u = userRepository.findById(userId).orElseThrow();
        return taskRepository.findByUserAndDateOrderByIdAsc(u , date);
    }

    public Task add(Long userId , LocalDate date , String title){
        var u = userRepository.findById(userId).orElseThrow();
        var t = new Task();
        t.setUser(u);  t.setDate(date);  t.setTitle(title);
        return taskRepository.save(t);
    }

    public void toggle(Long id){
        var t = taskRepository.findById(id).orElseThrow();
        boolean nd = !t.isDone();
        t.setDone(nd);
        t.setCompletedDate(nd ? LocalDate.now() : null);
        taskRepository.save(t);
    }

    public void delete(Long id){ taskRepository.deleteById(id); }
}
