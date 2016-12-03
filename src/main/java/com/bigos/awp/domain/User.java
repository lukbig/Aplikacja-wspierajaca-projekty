package com.bigos.awp.domain;

import com.bigos.awp.utilities.BCryptPasswordDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * Created by bigos on 03.12.16.
 */

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Size(min = 4, max = 30)
    private String firstName;

    @Size(min = 2, max = 50)
    private String secondName;

    // The value of the password will always have a length of
    // 60 thanks to BCrypt
    @Size(min = 60, max = 60)
    @Column(name="password", nullable = false, length = 60)
    @JsonDeserialize(using = BCryptPasswordDeserializer.class )
    private String password;

    @NotNull
    @Email
    private String email;

    @NotNull
    private UserPermissions permissions;

    @OneToMany(mappedBy = "editor")
    private List<Task> userAsEditorTasks;

    @OneToMany(mappedBy = "personResponsibleForTask")
    private List<Task> userAsPRFTTasks;

    @OneToMany(mappedBy = "programmer")
    private List<Task> userAsProgrammerTasks;

    @OneToMany(mappedBy = "tester")
    private List<Task> userAsTesterTasks;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public List<Task> getUserAsEditorTasks() {
        return userAsEditorTasks;
    }

    public void setUserAsEditorTasks(List<Task> userAsEditorTasks) {
        this.userAsEditorTasks = userAsEditorTasks;
    }

    public List<Task> getUserAsPRFTTasks() {
        return userAsPRFTTasks;
    }

    public void setUserAsPRFTTasks(List<Task> userAsPRFTTasks) {
        this.userAsPRFTTasks = userAsPRFTTasks;
    }

    public List<Task> getUserAsProgrammerTasks() {
        return userAsProgrammerTasks;
    }

    public void setUserAsProgrammerTasks(List<Task> userAsProgrammerTasks) {
        this.userAsProgrammerTasks = userAsProgrammerTasks;
    }

    public List<Task> getUserAsTesterTasks() {
        return userAsTesterTasks;
    }

    public void setUserAsTesterTasks(List<Task> userAsTesterTasks) {
        this.userAsTesterTasks = userAsTesterTasks;
    }

    public UserPermissions getPermissions() {
        return permissions;
    }

    public void setPermissions(UserPermissions permissions) {
        this.permissions = permissions;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
