package com.bigos.awp.domain.repository;

import com.bigos.awp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bigos on 03.12.16.
 */

public interface UserRepository extends JpaRepository<User, Long>{

}
