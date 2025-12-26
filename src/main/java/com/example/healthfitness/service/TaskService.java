package com.example.healthfitness.service;

import com.example.healthfitness.exception.ForbiddenException;
import com.example.healthfitness.exception.ResourceNotFoundException;
import com.example.healthfitness.model.Task;
import com.example.healthfitness.repository.TaskRepository;
import com.example.healthfitness.repository.UserRepository;
import com.example.healthfitness.web.form.FlowTaskForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;

    public List<Task> listForDate(Long userId , LocalDate date){
        var u = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return taskRepository.findByUserAndDateOrderByIdAsc(u , date);
    }

    public Task create(Long userId, FlowTaskForm form){
        var u = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        var t = new Task();
        t.setUser(u);
        t.setDate(form.getDate());
        t.setTitle(form.getTitle());
        t.setNotes(form.getNotes());
        t.setDueDate(form.getDueDate());
        return taskRepository.save(t);
    }

    public void toggle(Long userId, Long id){
        var t = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        if (!t.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Task does not belong to current user");
        }
        boolean nd = !t.isDone();
        t.setDone(nd);
        t.setCompletedDate(nd ? LocalDate.now() : null);
        taskRepository.save(t);
    }

    public void delete(Long userId, Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        if (!task.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Task does not belong to current user");
        }
        taskRepository.delete(task);
    }
}
