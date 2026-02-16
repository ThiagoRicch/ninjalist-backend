package com.example.ninjalist.service;

import com.example.ninjalist.model.TaskModel;
import com.example.ninjalist.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskModel createTask(TaskModel taskModel) {
        return taskRepository.save(taskModel);
    }

    public List<TaskModel> listTaskByUser(Long userId) {
        Sort sort = Sort.by("prioridade").descending()
                .and(Sort.by("titulo").ascending());

        return taskRepository.findByUserId(userId, sort);
    }

    public TaskModel updateTask(Long id, TaskModel updatedTask) {

        TaskModel existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existing.setTitulo(updatedTask.getTitulo());
        existing.setDescricao(updatedTask.getDescricao());
        existing.setPrioridade(updatedTask.getPrioridade());
        existing.setAtivo(updatedTask.isAtivo());

        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
