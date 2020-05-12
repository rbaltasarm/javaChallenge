/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javachallenge.repository;

import com.javachallenge.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/** 
 *
 * @author Baltasar
 * @version 1.0
 */
public interface UserRepository extends CrudRepository<User, Long>,CustomUserRepository {

    @Query(value = "SELECT * FROM tbl_users u WHERE u.name = :name", 
        nativeQuery = true)
     public User findByName(@Param("name") String name);

}