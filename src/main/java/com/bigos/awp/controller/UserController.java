package com.bigos.awp.controller;

import com.bigos.awp.domain.User;
import com.bigos.awp.exception.EntityNotFoundException;
import com.bigos.awp.exception.NotUniqueNickname;
import com.bigos.awp.service.UserService;
import com.bigos.awp.utilities.SortUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
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

    @RequestMapping(params = { "page", "size" }, method = RequestMethod.GET)
    @ResponseBody
    public List<User> findPaginated(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        final Page<User> resultPage = userService.findAll(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return resultPage.getContent();
    }

    @RequestMapping(value = "/all/{attribute}")
    @ResponseBody
    public List<User> getAll(@PathVariable("attribute") String attribute, @RequestParam("direction") String direction) {
        List<User> all = userService.findAll(SortUtility.orderBy(direction, attribute));
        if (!(all.size() > 0)) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return all;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< deletes >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "userId") long userId) {
        userService.delete(userId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody User user) {
        userService.delete(user);
    }


    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< updates >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping( value = "/add", method = RequestMethod.PUT)
    public @ResponseBody User addUser(@RequestBody @Valid User user) {
        User savedUser = userService.save(user);
        if (savedUser == null) {
            throw new NotUniqueNickname(user.getNickName());
        }
        return savedUser;
    }
}