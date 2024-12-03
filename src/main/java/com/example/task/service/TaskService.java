package com.example.task.service;

import com.example.task.dto.TaskRequestDTO;
import com.example.task.dto.TaskResponseDTO;
import com.example.task.entity.Task;

import java.util.List;

public interface TaskService {
    public TaskResponseDTO createTask(TaskRequestDTO task);
    public TaskResponseDTO getTaskById(Long id);
    public List<TaskResponseDTO> getAllTasks();
    public Task updateTask(Long id,TaskRequestDTO task);
    public void deleteTask(Long id);
    public String updateTaskStatus(Long id, String status);
}
