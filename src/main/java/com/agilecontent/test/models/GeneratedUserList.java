package com.agilecontent.test.models;

import java.util.List;

/**
 * Model class to map the response of the random generator api.
 * 
 * @author Carlos Melero
 *
 */
public class GeneratedUserList {

	List<GeneratedUser> results;
	
	// Get the random user list
	public List<GeneratedUser> getUsers() {
		return results;
	}
	
}