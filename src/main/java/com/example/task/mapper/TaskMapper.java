package com.example.task.mapper;

import com.example.task.dto.Status;
import com.example.task.dto.TaskRequestDTO;
import com.example.task.dto.TaskResponseDTO;
import com.example.task.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper {
    public Task toEntity(TaskRequestDTO taskRequestDTO)
    {
        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setStatus(Status.valueOf(taskRequestDTO.getStatus()));
        return task;
    }

    public TaskResponseDTO toResponseDTO(Task task)
    {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setTitle(task.getTitle());
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setStatus(task.getStatus().toString());
        taskResponseDTO.setCreateTime(task.getCreatedAt());
        taskResponseDTO.setUpdateTime(task.getUpdatedAt());
        return taskResponseDTO;
    }

    public Task updateEntity(TaskRequestDTO updateTask,Task existingTask) {
        if(updateTask.getTitle()!=null)
        {
            existingTask.setTitle(updateTask.getTitle());
        }
        if(updateTask.getDescription()!=null)
        {
            existingTask.setDescription(updateTask.getDescription());
        }
        existingTask.setUpdatedAt(LocalDateTime.now());
        return existingTask;
    }
}
