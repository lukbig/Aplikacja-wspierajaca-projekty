package com.bigos.awp.domain.repository;

import com.bigos.awp.domain.Task;
import com.bigos.awp.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bigos on 03.12.16.
 */

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> getAllByStatus(TaskStatus status);

    @Query("select t from Task t where t.editor.id = ?1")
    List<Task> getAllByEditor(long userId);

    @Query("select t from Task t where t.personResponsibleForTask.id = ?1")
    List<Task> getAllByPRFT(long userId);

    @Query("select t from Task t where t.programmer.id = ?1")
    List<Task> getAllByProgrammer(long userId);

    @Query("select t from Task t where t.tester.id = ?1")
    List<Task> getAllByTester(long userId);

}
