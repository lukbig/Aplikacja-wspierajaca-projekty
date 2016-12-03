package com.bigos.awp.service.impl;

import com.bigos.awp.domain.Task;
import com.bigos.awp.domain.repository.TaskRepository;
import com.bigos.awp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bigos on 03.12.16.
 */

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
