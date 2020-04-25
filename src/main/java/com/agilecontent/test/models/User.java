package com.agilecontent.test.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.agilecontent.test.utils.ErrorUtils;

/**
 * Local User model class. Entity annotation provides support for a possible JPA Repository implementation,
 * plus lets Swagger retrieve the class information to show on the Swagger page.
 * 
 * @author Carlos Melero
 *
 */
@Entity
public class User {

	/**
	 * Enumeration for all the 3 gender options: Non-binary inclusive.
	 * @author Carlos Melero
	 *
	 */
	public static enum Gender {
		MALE, FEMALE, OTHER;
	}

	@Id @NotNull
	private String username;
	private String name;
	private String email;
	private Gender gender;
	private String picture;

	/**
	 * Parameterized constructor: Raw creation from input data (self-explanatory parameters)
	 * 
	 * @param username
	 * @param name
	 * @param email
	 * @param gender
	 * @param picture
	 */
	public User (String username, String name, String email, Gender gender, String picture) {
		if (username == null) {
			throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.NULL_USERNAME.getMsg()));
		}
		this.username = username;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.picture = picture;
	}

	/**
	 * Parameterized constructor: Map from results of the random user generator api.
	 * 
	 * @param genUser
	 */
	public User (GeneratedUser genUser) {
		if (genUser.login.username == null) {
			throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.PARSING_ERROR.getMsg()));
		}
		this.username = genUser.login.username;
		this.name = genUser.name.first + " " + genUser.name.last;
		this.email = genUser.email;
		this.picture = genUser.picture.medium;

		switch (genUser.gender) {
		case "male":
			this.gender = Gender.MALE;
			break;
		case "female":
			this.gender = Gender.FEMALE;
			break;
		// Notice the api is not non-binary inclusive, but that may change in the future
		default: 
			this.gender = Gender.OTHER;
		}
	}

	/**
	 * Generate a simple String output. Not pretty, but grep-friendly in case of need.
	 */
	@Override
	public String toString() {
		return "username: " + username + "; name: " + name + "; email: " + email +
				"; gender: " + this.gender + "; picture: " + picture;
	}

	/*
	 * Getters and setters
	 */
	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
}
