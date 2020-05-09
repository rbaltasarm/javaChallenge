package com.javachallenge.test.db;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.javachallenge.entity.User;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserEntityTest {

	@Autowired
	private TestEntityManager entityManager;

	@Rule
	public ExpectedException thrown = ExpectedException.none();



	private User firstUser;
	private User secondUser;

	@Before
	public void setUp() {
                firstUser= new User("FirstUser");
                secondUser = new User("secondUser");
	}

	@Test
	public void createUser() {
                User saved=this.entityManager.persistAndFlush(firstUser);
		assertThat(saved.getName()).isEqualTo("FirstUser");

	}

	@Test
	public void createUserNullNameException() throws Exception {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("User name can not be empty");
		new User("");

	}

        @Test
	public void createUserBiggerNameException() throws Exception {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("User name lenght has to be lower than 20");
		new User("NewUserWithHigerLengththan20");

	}
        
        @Test
	public void createUserComplexNameException() throws Exception {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("User name lenght has to be an unique word");
		new User("New User");

	}
        
        @Test
	public void compareUsers() {
		User firsUserSaved=this.entityManager.persistAndFlush(firstUser);
                User secondUserSaved=this.entityManager.persistAndFlush(secondUser);
		assertThat(! firsUserSaved.equals(secondUserSaved));

	}
	

}
