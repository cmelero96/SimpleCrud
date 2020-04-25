package com.agilecontent.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.agilecontent.test.models.User;
import com.agilecontent.test.models.User.Gender;

/**
 * Tests for Model data integrity. Since our models are simple we only have one test
 * for now, but it's useful to have it separated in another class anyway.
 * 
 * @author Carlos Melero
 *
 */
class UserModelTests {

	/**
	 * Verifies that we cannot create a User whose username is null
	 */
	@Test
	void checkUserNameCannotBeNull() {
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				new User(null, "Test", "test@gmail.com", Gender.MALE, "https://test.com/image.jpg");
			});

	}

}
