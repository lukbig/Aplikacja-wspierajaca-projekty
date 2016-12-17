package com.bigos.awp.service.impl;

import com.bigos.awp.domain.Task;
import com.bigos.awp.domain.repository.TaskRepository;
import com.bigos.awp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Task findOne(long taskId) {
        return taskRepository.findOne(taskId);
    }

    @Override
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public void delete(long taskId) {
        taskRepository.delete(taskId);
    }

    @Override
    public void delete(List<Task> tasks) {
        taskRepository.delete(tasks);
    }

    @Override
    public List<Task> findAll(Sort sort) {
        return taskRepository.findAll(sort);
    }

    @Override
    public List<Task> findAll(List<Long> taskIds) {
        return taskRepository.findAll(taskIds);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Page<Task> findAll(int page, int size) {
        return taskRepository.findAll(new PageRequest(page, size));
    }

    @Override
    public List<Task> getAllByEditor(long userId) {
        return taskRepository.getAllByEditor(userId);
    }

    @Override
    public List<Task> getAllByPRFT(long userId) {
        return taskRepository.getAllByPRFT(userId);
    }

    @Override
    public List<Task> getAllByProgrammer(long userId) {
        return taskRepository.getAllByProgrammer(userId);
    }

    @Override
    public List<Task> getAllByTester(long userId) {
        return taskRepository.getAllByTester(userId);
    }
}
