package com.bigos.awp.controller;

import com.bigos.awp.domain.Position;
import com.bigos.awp.domain.User;
import com.bigos.awp.exception.EntityNotFoundException;
import com.bigos.awp.service.UserService;
import com.bigos.awp.utilities.SortUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by bigos on 03.12.16.
 */


@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< queries >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody User read(@RequestParam(value = "userId") long userId) {
        User user = userService.findOne(userId);
        if (user == null) {
            throw new EntityNotFoundException(userId);
        }
        return user;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public @ResponseBody User readUserByNickName(@RequestParam(value = "nickName") String nickName) {
        return userService.getUserByNickName(nickName);
    }

    @RequestMapping(value = "/custom", method = RequestMethod.GET)
    public @ResponseBody List<User> advancedSearch(@RequestParam(value = "nickName", required = false) String nickName,
        @RequestParam(value = "firstName", required = false) String firstName, @RequestParam(value = "secondName", required = false) String secondName,
        @RequestParam(value = "email", required = false) String email, @RequestParam(value = "permissions", required = false) String permissions,
        @RequestParam(value = "position", required = false) Position position) {
        return userService.findUsersWithCustomAttribute(nickName, firstName, secondName, email, position, permissions);
    }

    @RequestMapping(params = { "page", "size" }, method = RequestMethod.GET)
    @ResponseBody
    public List<User> findPaginated(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        final Page<User> resultPage = userService.findAll(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return resultPage.getContent();
    }

    @RequestMapping(value = "/{attribute}")
    @ResponseBody
    public List<User> getAll(@PathVariable("attribute") String attribute, @RequestParam("direction") String direction) {
        List<User> all = userService.findAll(SortUtility.orderBy(direction, attribute));
        if (!(all.size() > 0)) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return all;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< deletes >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(value = "userId")  long userId) {
        userService.delete(userId);
    }


    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< updates >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody @Valid User user) {
        userService.save(user);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody User user) {
        userService.update(user);
    }
}