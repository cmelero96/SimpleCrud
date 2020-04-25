package com.agilecontent.test.models;

/**
 * A Model class to map the results of the random user generator API. This models each of the "results" list
 * elements of the api response, only getting the fields that are relevant for our application.
 * It is separated into another file for easier conversion to local User type through a dedicated User constructor.
 * 
 * @author Carlos Melero
 *
 */
public class GeneratedUser {
	String gender;
	Name name;
	String email;
	Login login;
	Picture picture;
	
	// No need to directly access any other field but the username
	public String getUsername() {
		return login.username;
	}
}

class Name {
	String first;
	String last;
}

class Login {
	String username;
}

class Picture {
	String medium;
}