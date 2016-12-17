package com.bigos.awp.controller;

import com.bigos.awp.domain.Task;
import com.bigos.awp.exception.EntityNotFoundException;
import com.bigos.awp.service.TaskService;
import com.bigos.awp.utilities.SortUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by bigos on 04.12.16.
 */

@RestController
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< queries >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Task read(@RequestParam(value = "taskId") long taskId) {
        final Task comment = taskService.findOne(taskId);
        if (comment == null) {
            throw new EntityNotFoundException(taskId);
        }
        return comment;
    }

    @RequestMapping(params = { "page", "size" }, method = RequestMethod.GET)
    @ResponseBody
    public List<Task> findPaginated(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        final Page<Task> resultPage = taskService.findAll(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return resultPage.getContent();
    }

    @RequestMapping(value = "/all/{attribute}")
    @ResponseBody
    public List<Task> getAll(@PathVariable("attribute") String attribute, @RequestParam("direction") String direction) {
        List<Task> all = taskService.findAll(SortUtility.orderBy(direction, attribute));
        if (!(all.size() > 0)) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return all;
    }

    @RequestMapping("/{user}")
    @ResponseBody
    public List<Task> getAllTaskByEditor(@PathVariable String user, @RequestParam("userId") long userId) {
        switch (user) {
            case "editor" :
                return taskService.getAllByEditor(userId);
            case "prft" :
                return taskService.getAllByPRFT(userId);
            case "programmer" :
                return taskService.getAllByProgrammer(userId);
            case "tester" :
                return taskService.getAllByTester(userId);
        }
        return Collections.emptyList();
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< deletes >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(value = "/delete/{taskId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "taskId") long taskId) {
        taskService.delete(taskId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody Task task) {
        taskService.delete(task);
    }


    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< updates >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping( value = "/add", method = RequestMethod.PUT)
    public @ResponseBody Task addUser(@RequestBody Task task) {
        task.setCreateDate(new Date());
        return taskService.save(task);
    }
}
