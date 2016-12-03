package com.bigos.awp.domain.repository;

import com.bigos.awp.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bigos on 03.12.16.
 */

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
