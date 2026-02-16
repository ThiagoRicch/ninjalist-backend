package com.example.ninjalist.servicetests;

import com.example.ninjalist.model.TaskModel;
import com.example.ninjalist.prioridades.PrioridadesEnum;
import com.example.ninjalist.repository.TaskRepository;
import com.example.ninjalist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTask()
    {
        TaskModel task = new TaskModel();
        task.setTitulo("Test Task");

        when(taskRepository.save(task)).thenReturn(task);

        TaskModel saved = taskService.createTask(task);

        assertEquals("Test Task", saved.getTitulo());
        verify(taskRepository, times(1)).save(task);

    }

    @Test
    void shouldUpdateTask()
    {
        TaskModel existing = new TaskModel();
        existing.setId(1L);
        existing.setTitulo("Antigo");

        TaskModel updated = new TaskModel();
        updated.setTitulo("Novo");
        updated.setDescricao("Descricao");
        updated.setPrioridade(PrioridadesEnum.HIGH);
        updated.setAtivo(true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(taskRepository.save(existing))
                .thenReturn(existing);

        TaskModel result = taskService.updateTask(1L, updated);

        assertEquals("Novo", result.getTitulo());
        assertEquals("Descricao", result.getDescricao());
        verify(taskRepository).save(existing);

    }

    @Test
    void shouldDeleteTask()
    {
        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound()
    {
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskService.updateTask(99L, new TaskModel())
        );

        assertEquals("Task not found", exception.getMessage());
    }

}

