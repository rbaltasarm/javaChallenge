package com.javachallenge.test.db;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.javachallenge.entity.User;
import java.util.Random;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** User entity testing
 *
 * @author Baltasar
 * @version 1.0
 */
@DataJpaTest
public class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

   
       
     
    @Test
    public void createUser() {
        User firstUser= new User("FirstUser");
        User saved = this.entityManager.persistAndFlush(firstUser);
        assertThat(saved.getName()).isEqualTo("FirstUser");
        assertThat(saved.getId().longValue() != 0);

    }

    @Test
    public void createUserNullNameException() throws Exception {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User("");
        });

    }

    @Test
    public void createUserBiggerNameException() throws Exception {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User("NewUserWithHigerLengththan20");
        });
    }

    @Test
    public void createUserComplexNameException() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new User("New User");
        });

    }

    @Test
    public void compareUsers() {
        User firstUser= new User("FirstUser");
        User secondUser= new User("SeconUser");
        User firsUserSaved = this.entityManager.persistAndFlush(firstUser);
        User secondUserSaved = this.entityManager.persistAndFlush(secondUser);
        assertThat(!firsUserSaved.equals(secondUserSaved));

    }

    @Test
    public void addFriend() {
        User firstUser= new User("FirstUser");
        User secondUser= new User("SeconUser");
        User firsUserSaved = this.entityManager.persistAndFlush(firstUser);
        User secondUserSaved = this.entityManager.persistAndFlush(secondUser);
        firsUserSaved.addFriend(secondUserSaved);
        firsUserSaved = this.entityManager.persist(firsUserSaved);
        assertThat(firsUserSaved.getFriends().toArray()[0].equals(secondUserSaved));

    }

    @Test
    public void addDuplicateUser() throws Exception {
        Assertions.assertThrows(PersistenceException.class, () -> {
            Random rand = new Random();
            String username = "user_" + Integer.toString(rand.nextInt(1000));
            User user = new User(username);
            User repeated = new User(username);
            this.entityManager.persistAndFlush(user);
            this.entityManager.persistAndFlush(repeated);
        });

    }

    @Test
    public void addSomeUsers() {
        for (int i = 0; i < 10; i++) {
            String username = "user_" + i;
            User user = new User(username);
            this.entityManager.persist(user);
        }
    }

}
