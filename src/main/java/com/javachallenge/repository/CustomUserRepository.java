/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javachallenge.repository;

import com.javachallenge.entity.User;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**Extends the userRepository with custom methods
 *
 * @author Baltasar
 */
public interface CustomUserRepository {
     public Set<User> findAllFriends(User user);
     
     
}
