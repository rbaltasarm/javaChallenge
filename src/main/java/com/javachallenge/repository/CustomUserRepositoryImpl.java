/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javachallenge.repository;

import com.javachallenge.entity.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Baltasar
 */
public class CustomUserRepositoryImpl implements CustomUserRepository{
        @PersistenceContext
        private EntityManager em;

        @Override
        public Set<User> findAllFriends(User user){
             Set<User> allFriends= new HashSet();
             //retrieve friendsOf set
             EntityGraph entityGraphFriendsOF = em.getEntityGraph("friendOf-entityGraph");
             Map<String, Object> properties = new HashMap<>();
             properties.put("javax.persistence.fetchgraph", entityGraphFriendsOF);
             user = em.find(User.class, user.getId(), properties);
              //retrieve friendsOf set
             EntityGraph entityGraphFriends = em.getEntityGraph("friends-entityGraph");
             properties.clear();
             properties.put("javax.persistence.fetchgraph", entityGraphFriends);
             User userFrieds = em.find(User.class, user.getId(), properties);
             allFriends.addAll(userFrieds.getFriends());
             allFriends.addAll(user.getFriendOf());
             return allFriends;
         }
        
        
}
