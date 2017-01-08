package com.bigos.awp.service.impl;

import com.bigos.awp.domain.*;
import com.bigos.awp.domain.repository.TaskRepository;
import com.bigos.awp.domain.repository.UserRepository;
import com.bigos.awp.exception.IncorrectUserPosition;
import com.bigos.awp.exception.UnauthorizedChangeTaskStatus;
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

    private UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    private void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Task save(Task task) {
        User programmer = userRepository.getOne(task.getProgrammer().getUserId());
        if (!programmer.getPosition().equals(Position.PROGRAMMER)) {
            throw new IncorrectUserPosition(programmer);
        }
        User tester = userRepository.getOne(task.getTester().getUserId());
        if (!tester.getPosition().equals(Position.TESTER)) {
            throw new IncorrectUserPosition(tester);
        }
        User prft = userRepository.getOne(task.getPersonResponsibleForTask().getUserId());
        if (!prft.getPosition().equals(Position.ADMINISTRATOR) && !prft.getPosition().equals(Position.ARCHITECT)
                && !prft.getPosition().equals(Position.PRODUCT_MANAGER) && !prft.getPosition().equals(Position.CONSULTANT)) {
            throw new IncorrectUserPosition(prft);
        }
        return taskRepository.save(task);
    }

    @Override
    public void changeStatus(long taskId, long userId, ChangeStatus updown) {
        Task task = findOne(taskId);
        if (task.getEditor().getUserId() != userId) {
            throw new UnauthorizedChangeTaskStatus();
        }
        switch(task.getEditor().getPosition()) {
            case PROGRAMMER:
                if (task.getStatus().equals(TaskStatus.CREATED)) {
                    if (task.getComments().size() > 0 &&
                            task.getComments().get(0).getCommentType().equals(CommentType.TASK_DESCRIPTION)) {
                        task.setStatus(TaskStatus.IN_PROCESS);
                    } else {
                        throw new UnauthorizedChangeTaskStatus(
                                "Task description should be added before change status to IN PROCESS");
                    }
                } else if (task.getStatus().equals(TaskStatus.IN_PROCESS)) {
                    if (updown.equals(ChangeStatus.UP)) {
                        List<Comment> comments = task.getComments();
                        if (comments.size() > 0 && comments.get(comments.size() - 1).getCommentType().
                                equals(CommentType.AFTER_DEVELOPMENT)) {
                            task.setStatus(TaskStatus.TESTS);
                            task.setEditor(task.getTester());
                        } else {
                            throw new UnauthorizedChangeTaskStatus(
                                    "After development comment should be added before change status to TESTS");
                        }
                    } else if (updown.equals(ChangeStatus.DOWN)) {
                        List<Comment> comments = task.getComments();
                        if (comments.size() > 0 && comments.get(comments.size() - 1).getCommentType().
                                equals(CommentType.REGULAR)) {
                            task.setStatus(TaskStatus.CONSULTATION_REQUIRED);
                            task.setEditor(task.getPersonResponsibleForTask());
                        } else {
                            throw new UnauthorizedChangeTaskStatus(
                                    "Comment should be added before change status to CONSULTATION REQUIRED");
                        }
                    } else {
                        throw new UnauthorizedChangeTaskStatus();
                    }
                } else {
                    throw new UnauthorizedChangeTaskStatus();
                }
                break;
            case TESTER:
                if (task.getStatus().equals(TaskStatus.TESTS)) {
                    if (updown.equals(ChangeStatus.UP)) {
                        List<Comment> comments = task.getComments();
                        if (comments.size() > 0 && comments.get(comments.size() - 1).getCommentType().
                                equals(CommentType.AFTER_TESTS)) {
                            task.setStatus(TaskStatus.FINISHED);
                            task.setEditor(task.getPersonResponsibleForTask());
                        }  else {
                            throw new UnauthorizedChangeTaskStatus(
                                    "After tests comment should be added before change status to FINISHED");
                        }
                    } else if (updown.equals(ChangeStatus.DOWN)) {
                        List<Comment> comments = task.getComments();
                        if (comments.size() > 0 && comments.get(comments.size() - 1).getCommentType().
                                equals(CommentType.REGULAR)) {
                            task.setStatus(TaskStatus.IN_PROCESS);
                            task.setEditor(task.getProgrammer());
                        } else {
                            throw new UnauthorizedChangeTaskStatus(
                                    "Comment should be added before change status to IN PROCESS");
                        }
                    } else {
                        throw new UnauthorizedChangeTaskStatus();
                    }
                }
                break;
            case PRODUCT_MANAGER:
            case ADMINISTRATOR:
            case ARCHITECT:
                if (task.getStatus().equals(TaskStatus.CREATED)) {
                    task.setEditor(task.getProgrammer());
                } else if (task.getStatus().equals(TaskStatus.CONSULTATION_REQUIRED)) {
                    List<Comment> comments = task.getComments();
                    if (comments.size() > 0 && comments.get(comments.size() - 1).getCommentType().
                            equals(CommentType.REGULAR)) {
                        task.setEditor(task.getProgrammer());
                        task.setStatus(TaskStatus.IN_PROCESS);
                    }  else {
                        throw new UnauthorizedChangeTaskStatus(
                                "Comment should be added before change status to IN PROCESS");
                    }
                } else if (task.getStatus().equals(TaskStatus.FINISHED)) {
                    task.setStatus(TaskStatus.COMPLETED);
                } else {
                    throw new UnauthorizedChangeTaskStatus();
                }
                break;
            case CONSULTANT:
                if (task.getStatus().equals(TaskStatus.CONSULTATION_REQUIRED)) {
                    List<Comment> comments = task.getComments();
                    if (comments.size() > 0 && comments.get(comments.size() - 1).getCommentType().
                            equals(CommentType.REGULAR)) {
                    task.setEditor(task.getProgrammer());
                    task.setStatus(TaskStatus.IN_PROCESS);
                    } else {
                        throw new UnauthorizedChangeTaskStatus(
                                "Comment should be added before change status to IN PROCESS");
                    }
                } else {
                    throw new UnauthorizedChangeTaskStatus();
                }
                break;
        }
        save(task);
    }

    @Override
    public List<Task> getAllByStatus(TaskStatus status) {
        return taskRepository.getAllByStatus(status);
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
