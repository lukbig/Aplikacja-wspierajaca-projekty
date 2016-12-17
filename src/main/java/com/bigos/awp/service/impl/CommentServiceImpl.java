package com.bigos.awp.service.impl;

import com.bigos.awp.domain.Comment;
import com.bigos.awp.domain.repository.CommentRepository;
import com.bigos.awp.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findOne(long commentId) {
        return commentRepository.findOne(commentId);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public void delete(long commentId) {
        commentRepository.delete(commentId);
    }

    @Override
    public void delete(List<Comment> comments) {
        commentRepository.delete(comments);
    }

    @Override
    public List<Comment> findAll(Sort sort) {
        return commentRepository.findAll(sort);
    }

    @Override
    public List<Comment> findAll(List<Long> commentIds) {
        return commentRepository.findAll(commentIds);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Page<Comment> findAll(int page, int size) {
        return commentRepository.findAll(new PageRequest(page, size));
    }
}
