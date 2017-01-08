package com.bigos.awp.domain.repository;

import com.bigos.awp.domain.Position;
import com.bigos.awp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bigos on 03.12.16.
 */

public interface UserRepository extends JpaRepository<User, Long>{

    Long countByNickName(String nickName);

    Long countByUserId(long userId);

    /*The @Modifying annotation must be placed on the updateMaterialInventory method, along to the @Query annotation,
    to let Spring-data know that the query is not a query used to select values, but to update values.*/
    @Modifying
    @Query("update User u set u.email=?1, u.firstName=?2, u.nickName=?3, u.permissions=?4, u.position=?5, u.secondName=?6 where u.userId=?7")
    void updateUser(String email, String firstName, String nickName, String permissions, Position position, String secondName, long userId);

    List<User> findByNickName(String nickName);
    List<User> findByFirstName(String firstName);
    List<User> findBySecondName(String secondName);
    List<User> findByEmail(String email);
    List<User> findByPosition(Position position);
    List<User> findByPermissions(String permissions);

    User findOneByNickName(String nickName);
}
