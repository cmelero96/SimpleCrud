package com.agilecontent.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.agilecontent.test.controllers.UserController;
import com.agilecontent.test.models.User;
import com.agilecontent.test.models.User.Gender;
import com.agilecontent.test.services.UserService;

/**
 * Tests for Service. Check basic functionality of our services, invalid operations, etc.
 * 
 * @author Carlos Melero
 *
 */
class UserServiceTests {

	/**
	 * Common mock elements to be used in our tests.
	 */
	UserService userService;
	final String username1 = "test", username2 = "test2";
	User user1 = new User(username1, "Test", "test@gmail.com", Gender.MALE, "https://test.com/image.jpg");
	User user2 = new User(username2, "Test 2", "test2@gmail.com", Gender.FEMALE, "https://test2.com/image.jpg");

	/**
	 * Generate the Service putting our 2 mock users inside the list.
	 */
	@BeforeEach
	void initService() {
		List<User> list = new ArrayList<User>();
		list.add(user1);
		list.add(user2);
		userService = new UserService(list);
	}

	/**
	 * Instances a new UserController using a new UserService.
	 */
	@Test
	void instanceController() {
		assertThat(new UserController(new UserService())).isNotNull();
	}

	/**
	 * Return the current page of users, then makes sure the next page is null (since only 1 page)
	 */
	@Test
	void getCurrentPage() {
		assertThat(userService.getCurrentPage()).isNotNull().isNotEmpty();
	}

	/**
	 * Return the current page of users, then makes sure the next page is null (since only 1 page)
	 */
	@Test
	void getNextPage() {
		assertThat(userService.getNextPage(0)).isNotNull().isNotEmpty();
		assertThat(userService.getNextPage(0)).isNull();
	}

	/**
	 * Return the current page of users, then makes sure the previous page is null (since only 1 page)
	 */
	@Test
	void getPrevPage() {
		assertThat(userService.getPreviousPage(0)).isNotNull().isNotEmpty();
		assertThat(userService.getPreviousPage(0)).isNull();
	}

	/**
	 * Return one of our mock Users.
	 */
	@Test
	void getOneUser() {		
		User resp = userService.getOne(username1);
		assertThat(resp).hasFieldOrPropertyWithValue("username", username1);
	}

	/**
	 * Create a new mock User.
	 */
	@Test
	void createUser() {
		User resp = userService.create(new User("create", "Test", "test@gmail.com", Gender.MALE, "https://test.com/image.jpg"));
		assertThat(resp).isNotNull();
	}

	/**
	 * Update an existing mock User.
	 */
	@Test
	void updateUser() {
		String name = "Modified Name", email = "modif@mail.com", picture = "picture.mod";
		Gender gender = Gender.FEMALE;
		user1.setName(name);
		user1.setEmail(email);
		user1.setGender(gender);
		user1.setPicture(picture);

		User resp = userService.update(user1);
		assertThat(resp).hasFieldOrPropertyWithValue("name", name);
		assertThat(resp).hasFieldOrPropertyWithValue("email", email);
		assertThat(resp).hasFieldOrPropertyWithValue("gender", gender);
		assertThat(resp).hasFieldOrPropertyWithValue("picture", picture);
	}

	/**
	 * Tries to update a non-existing User; this should throw an exception.
	 */
	@Test
	void updateWrongUser() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			userService.update(new User("error", "name", "email@mail.com", Gender.OTHER, "picture.jpg"));
		});
	}

	/**
	 * Deletes one of our mock users.
	 */
	@Test
	void deleteUser() {
		assertThat(userService.delete(username1)).hasFieldOrPropertyWithValue("username", username1);
	}

	/**
	 * Tries to delete a non-existing User. This should throw an exception.
	 */
	@Test
	void deleteWrongUser() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			userService.delete("error");
		});
	}

	/**
	 * Generates 10 random users through the random-gen api.
	 */
	@Test
	void generateTenRandomUsers() {
		List<User> resp = userService.generateRandom(10);

		assertThat(resp).hasSize(10);
	}

	/**
	 * Makes a call to the random-gen api using a negative number. This should return 0 results.
	 */
	@Test
	void generateNegativeRandomUsers() {
		List<User> resp = userService.generateRandom(-4);

		assertThat(resp).hasSize(0);
	}

	/**
	 * Makes a large call to the random-gen api, larger than the max allowed in 1 call (5000).
	 * This checks multi-calls are working fine.
	 */
	@Test
	void generateManyRandomUsers() {
		List<User> resp = userService.generateRandom(5010);

		assertThat(resp).hasSize(5010);
	}

}
