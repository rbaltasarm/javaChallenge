package com.javachallenge.test.db;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import com.javachallenge.entity.User;
import com.javachallenge.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
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

}
