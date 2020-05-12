package com.javachallenge.test.db;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import com.javachallenge.entity.User;
import com.javachallenge.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

/** User Repository testing
 *
 * @author Baltasar
 * @version 1.0
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    EntityManager em;

    private User firstUser;
    private User secondUser;
    private static Random rnd = new Random();

   
   
   
    @Test
    public void createUser() {
        String userName = "user_" + rnd.nextInt(10000);
        User newUser = new User(userName);
        User saved = repository.save(newUser);
        assertThat(saved.getName()).isEqualTo(userName);
        assertThat(saved.getId().longValue() != 0);

    }

    @Test
    public void tryDuplicateUser() throws Exception {

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            String userName = "user_" + rnd.nextInt(10000);
            User newUser = new User(userName);
            User saved = repository.save(newUser);
            //this should be an entity update
            User saved2 = repository.save(newUser);
            User duplicatedUser = new User(userName);
            //this should raise the exception
            repository.save(duplicatedUser);
        });

    }

    @Test
    public void compareDifferentsUsers() {
        String userName = "user_" + rnd.nextInt(10000);
        this.firstUser = new User(userName);
        userName = "user_" + rnd.nextInt(10000);
        this.secondUser = new User(userName);
        User firsUserSaved = repository.save(firstUser);
        User secondUserSaved = repository.save(secondUser);
        assertThat(!firsUserSaved.equals(secondUserSaved));
    }

    @Test
    public void addFriend() {
        String userName = "user_" + rnd.nextInt(10000);
        this.firstUser = new User(userName);
        userName = "user_" + rnd.nextInt(10000);
        this.secondUser = new User(userName);
        firstUser = repository.save(firstUser);
        secondUser = repository.save(secondUser);
        firstUser.addFriend(secondUser);
        firstUser = repository.save(firstUser);
        assertThat(firstUser.getFriends().toArray()[0].equals(secondUser));

    }

    @Test
    public void getFriendOf() {
        Long userId;
        String userName = "user_" + rnd.nextInt(10000);
        this.firstUser = new User(userName);
        userName = "user_" + rnd.nextInt(10000);
        this.secondUser = new User(userName);
        firstUser = repository.save(firstUser);
        secondUser = repository.save(secondUser);
        firstUser.addFriend(secondUser);
        firstUser = repository.save(firstUser);
        //firsUser has a list of friends
        assertThat(firstUser.getFriends().toArray()[0].equals(secondUser));
        userId=secondUser.getId();
        EntityGraph entityGraph = em.getEntityGraph("friendOf-entityGraph");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        secondUser = em.find(User.class, userId, properties);
        assertThat(secondUser.getFriendOf().toArray()[0].equals(firstUser));
    }
    
    
     @Test
    public void getFriends() {
        Long userId;
        String userName = "user_" + rnd.nextInt(10000);
        this.firstUser = new User(userName);
        userName = "user_" + rnd.nextInt(10000);
        this.secondUser = new User(userName);
        firstUser = repository.save(firstUser);
        secondUser = repository.save(secondUser);
        firstUser.addFriend(secondUser);
        firstUser = repository.save(firstUser);
        //firsUser has a list of friends
        assertThat(firstUser.getFriends().toArray()[0].equals(secondUser));
        userId=firstUser.getId();
        EntityGraph entityGraph = em.getEntityGraph("friends-entityGraph");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        firstUser = em.find(User.class, userId, properties);
        assertThat(firstUser.getFriends().toArray()[0].equals(secondUser));
    }

    
     @Test
    public void getAllFriends() {
        
        List<User> users=new ArrayList();
        User user= new User("user");
        repository.save(user);
        
        for(int i=1; i<6; i++){
            User amigo=new User("amigo_"+i);
            amigo= repository.save(amigo);
            users.add(amigo);
            user.addFriend(amigo);
        }
        
        User amigoDe= new User("userAmigode");
        amigoDe=repository.save(amigoDe);
        amigoDe.addFriend(user);
        amigoDe=repository.save(amigoDe);
        
        
        User control= new User("control");
        control=repository.save(control);
        //persistence for generatin Ids
        repository.save(user);
        
        Set<User> allFriends= repository.findAllFriends(user);
        Iterator<User>it= users.listIterator();
        while( it.hasNext()){
            assertThat(allFriends.contains(it.next()));
        }
        assertThat(allFriends.contains(amigoDe));
        assertThat(!allFriends.contains(control));
        
    }

    
    
    @Test
    public void findByName() {
        String userName = "userFind_" + rnd.nextInt(10000);
        this.firstUser = new User(userName);
        firstUser = repository.save(firstUser);
        secondUser = repository.findByName(userName);
        assertThat(firstUser.equals(secondUser));
    }
    
}
