package com.bigos.awp.controller;

import com.bigos.awp.domain.Comment;
import com.bigos.awp.exception.EntityNotFoundException;
import com.bigos.awp.service.CommentService;
import com.bigos.awp.utilities.SortUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * Created by bigos on 04.12.16.
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< queries >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Comment read(@RequestParam(value = "commentId") long commentId) {
        final Comment comment = commentService.findOne(commentId);
        if (comment == null) {
            throw new EntityNotFoundException(commentId);//TODO
        }
        return comment;
    }

    @RequestMapping(params = { "page", "size" }, method = RequestMethod.GET)
    @ResponseBody
    public List<Comment> findPaginated(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        final Page<Comment> resultPage = commentService.findAll(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return resultPage.getContent();
    }

    @RequestMapping(value = "/all/{attribute}")
    @ResponseBody
    public List<Comment> getAll(@PathVariable("attribute") String attribute, @RequestParam("direction") String direction) {
        List<Comment> all = commentService.findAll(SortUtility.orderBy(direction, attribute));
        if (!(all.size() > 0)) {
            throw new javax.persistence.EntityNotFoundException();
        }
        return all;
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< deletes >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping(value = "/delete/{commentId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "commentId") long commentId) {
        commentService.delete(commentId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody Comment comment) {
        commentService.delete(comment);
    }


    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< updates >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @RequestMapping( value = "/add", method = RequestMethod.PUT)
    public @ResponseBody Comment addUser(@RequestBody Comment comment) {
        comment.setCreateDate(new Date());
        return commentService.save(comment);
    }

}