package com.example.task.service.impl;

import com.example.task.dto.TaskRequestDTO;
import com.example.task.dto.TaskResponseDTO;
import com.example.task.entity.Task;
import com.example.task.exception.InvalidStatusException;
import com.example.task.exception.TaskNotFoundException;
import com.example.task.dto.Status;
import com.example.task.exception.TitleAlreadyExistsException;
import com.example.task.mapper.TaskMapper;
import com.example.task.repository.TaskRepository;
import com.example.task.service.TaskService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper=taskMapper;
    }
    @Override
    public TaskResponseDTO createTask(TaskRequestDTO task) {
        if(taskRepository.existsByTitle(task.getTitle())) {
            throw new TitleAlreadyExistsException("Title already exists "+task.getTitle());
        }
        Status status=validateAndConvertStatus(task.getStatus());
        task.setStatus(status.name());
        return taskMapper.toResponseDTO(taskRepository.save(taskMapper.toEntity(task)));
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        Task task=taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task not Found"));
        return taskMapper.toResponseDTO(task);
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::toResponseDTO).toList();
    }

    @Override
    public Task updateTask(Long id, TaskRequestDTO updateTask) {
        if(taskRepository.existsByTitle(updateTask.getTitle())) {
            throw new TitleAlreadyExistsException("Title already exists "+updateTask.getTitle());
        }
        Task existingTask=taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task not Found"));
        Status status=validateAndConvertStatus(updateTask.getStatus());
        existingTask.setStatus(status);
        return taskRepository.save(taskMapper.updateEntity(updateTask,existingTask));
    }

    @Override
    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id)){
           throw new TaskNotFoundException("Task Not Found");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public String updateTaskStatus(Long id, String status) {
         Task task=taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("Task Not Found"));
         task.setStatus(validateAndConvertStatus(status));
         taskRepository.save(task);
        return "Status Updated Successfully to "+ task.getStatus();
    }

    private Status validateAndConvertStatus(@NotNull(message = "status is required") String status) {
        try{
            return Status.valueOf(status.toUpperCase());
        }
        catch(IllegalArgumentException ex)
        {
            throw new InvalidStatusException("Invalid Status "+ status+ " accepted values are"+ Arrays.toString(Status.values()));
        }
    }

}
