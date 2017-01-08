package com.bigos.awp.service;

import com.bigos.awp.domain.ChangeStatus;
import com.bigos.awp.domain.Comment;
import com.bigos.awp.domain.Task;
import com.bigos.awp.domain.TaskStatus;

import java.util.List;

/**
 * Created by bigos on 03.12.16.
 */

public interface TaskService extends TemplateService<Task> {
    List<Task> getAllByEditor(long userId);

    List<Task> getAllByPRFT(long userId);

    List<Task> getAllByProgrammer(long userId);

    List<Task> getAllByTester(long userId);

    List<Task> getAllByStatus(TaskStatus status);

    void changeStatus(long taskId, long userId, ChangeStatus updown);
}
