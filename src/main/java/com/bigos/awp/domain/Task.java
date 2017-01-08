package com.bigos.awp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by bigos on 03.12.16.
 */

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskId;

    @NotNull
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "editorId")
    private User editor;

    @ManyToOne
    @JoinColumn(name = "prftId")
    private User personResponsibleForTask;

    @ManyToOne
    @JoinColumn(name = "programmerId")
    @JsonInclude(content= JsonInclude.Include.NON_NULL)
    private User programmer;

    @ManyToOne
    @JoinColumn(name = "testerId")
    @JsonInclude(content= JsonInclude.Include.NON_NULL)
    private User tester;

    @Column(columnDefinition="TEXT")
    @NotNull
    private String description;

    @NotNull
    private TaskPriority priority;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToMany(mappedBy = "task")
    @JsonInclude(content= JsonInclude.Include.NON_NULL)
    @JsonManagedReference
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", status=" + status +
                ", editor=" + editor.getUserId() +
                ", personResponsibleForTask=" + personResponsibleForTask.getUserId() +
                ", programmer=" + programmer.getUserId() +
                ", tester=" + tester.getUserId() +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", createDate=" + createDate +
                '}';
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public User getPersonResponsibleForTask() {
        return personResponsibleForTask;
    }

    public void setPersonResponsibleForTask(User personResponsibleForTask) {
        this.personResponsibleForTask = personResponsibleForTask;
    }

    public User getProgrammer() {
        return programmer;
    }

    public void setProgrammer(User programmer) {
        this.programmer = programmer;
    }

    public User getTester() {
        return tester;
    }

    public void setTester(User tester) {
        this.tester = tester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
