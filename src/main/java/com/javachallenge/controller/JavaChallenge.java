/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javachallenge.controller;

import com.javachallenge.entity.User;
import com.javachallenge.repository.UserRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;



/**
 *
 * @author Baltasar
 */
@Controller 
@RequestMapping(path="/javaChallenge") 
public class JavaChallenge {
  @Autowired 
  private UserRepository userRepository;

  @PostMapping(path="/user/add") 
  ResponseEntity<User> addNewUser(@RequestParam String name) {
    User newUser=null;
    HttpStatus result;
    
    newUser=userRepository.findByName(name);
    if( newUser == null){
        //We have created the user properly
        newUser= new User(name);
        newUser=userRepository.save(newUser);
        result= HttpStatus.CREATED;
    }
    else{
        //We could not create the user. It existed before
        result= HttpStatus.CONFLICT;
    }

    //anycase, the resource exists in the system.
    return new ResponseEntity<>(newUser, result);
  }

    @PostMapping(path="/user/{id}/connect") 
    ResponseEntity<String> connect(@PathVariable Long id,@RequestParam Long to) {
        
        Optional<User> user=userRepository.findById(id);
        Optional<User> friend=userRepository.findById(to);
        if( user.isPresent() && friend.isPresent()){
            user.get().addFriend(friend.get());
            User modified=userRepository.save(user.get());
            return new ResponseEntity<>(modified.toString(), HttpStatus.OK);
        }else{
            //Error, the user with that id does not exist in the system
            return new ResponseEntity<>("{error: user not found}",HttpStatus.BAD_REQUEST);
        }
    }   
    
    
    @GetMapping(path="/user/{id}/connected") 
    ResponseEntity<String> connected(@PathVariable Long id) {
        
        Optional<User> user=userRepository.findById(id);
        
        if( user.isPresent() ){
            Set<User> connectedTo= userRepository.findAllFriends(user.get());
            String result= "{id:"+user.get().getId().toString()+", connectedTo: ["+User.getIds(connectedTo)+"]}";
            return new ResponseEntity<>( result, HttpStatus.OK);
        }else{
            //Error, the user with that id does not exist in the system
            return new ResponseEntity<>("{error: user not found}",HttpStatus.BAD_REQUEST);
        }
            
   
    
  }

  
  
}
