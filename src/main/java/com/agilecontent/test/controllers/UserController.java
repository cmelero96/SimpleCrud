package com.agilecontent.test.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.agilecontent.test.models.User;
import com.agilecontent.test.services.UserService;
import com.agilecontent.test.utils.ErrorUtils;

/**
 * Controller for the User Services. Supports the 4 basic CRUD operations, plus the random user generator api call.
 * 
 * @author Carlos Melero
 *
 */
@RestController
@RequestMapping(path = "/user", produces = "application/json")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * Default constructor.
	 * 
	 * @param userService The Service to make the CRUD operations to our User list
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Gets the current page of users and then moves the cursor to the next page
	 * 
	 * @return The current page of users as a List
	 */
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public List<User> getPage () {
		
		return userService.getCurrentPage();
	}

	/**
	 * Moves forward N pages, then returns the page, then moves forward one page.
	 * 
	 * @return The page with users after moving back N times.
	 */
	@RequestMapping(path = "/next/{n}", method = RequestMethod.GET)
	public List<User> movePage (
			@PathVariable(value = "n")	Integer pageIndex
			) {
		
		int page = 0;
		
		if (pageIndex != null && pageIndex.intValue() > 0)
			page = pageIndex;
		
		return userService.getNextPage(page);
	}

	/**
	 * Moves back N pages, then returns the page, then moves back one page.
	 * 
	 * @return The page with users after moving back N times.
	 */
	@RequestMapping(path = "/prev/{n}", method = RequestMethod.GET)
	public List<User> getPreviousPage (
			@PathVariable(value = "n")	Integer pageIndex
			) {
		
		int page = 0;
		
		if (pageIndex != null && pageIndex.intValue() > 0)
			page = pageIndex;
		
		return userService.getPreviousPage(page);
	}

	/**
	 * Gets a User, identified by their username.
	 * Throws an IllegalArgumentException if no User is found.
	 * 
	 * @param username Field to identify the User by
	 * @return The found User
	 */
	@RequestMapping(path = "/{username}", method = RequestMethod.GET)
	public User getUser (
			@PathVariable(value = "username")	String username
			) {

		User u = userService.getOne(username);
		
		if (u == null)
			throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.USERNAME_NOT_FOUND.getMsg(), username));
		
		return u;
	}

	/**
	 * Creates a new User using the RequestBody information, and puts it in our list.
	 * Throws an IllegalArgumentException if the username within the URI is not the same as the one in the RequestBody.
	 * 
	 * @param username The username to create
	 * @param user The info to create our User with
	 * @return The created User
	 */
	@RequestMapping(path = "/{username}", method = RequestMethod.POST, consumes = "application/json")
	public User createUser (
			@PathVariable(value = "username")	String username,
			@RequestBody 						User user
			) {
		
		if (!user.getUsername().equals(username))
			throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.INCONSISTENT_DATA.getMsg(), username)); 
		
		return userService.create(user);
	}

	/**
	 * Updates an existing User using the RequestBody information, and puts it in our list.
	 * Throws an IllegalArgumentException if the username within the URI is not the same as the one in the RequestBody.
	 * 
	 * @param username The username to update
	 * @param user The info to update our User with
	 * @return The updated User
	 */
	@RequestMapping(path = "/{username}", method = RequestMethod.PUT, consumes = "application/json")
	public User updateUser (
			@PathVariable(value = "username")	String username,
			@RequestBody 						User user
			) {
		
		if (!user.getUsername().equals(username))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format(ErrorUtils.ErrorMsg.INCONSISTENT_DATA.getMsg(), username)); 
		
		return userService.update(user);
	}

	/**
	 * Deletes a User from our list.
	 * 
	 * @param username The username to identify our User by
	 * @return The deleted User
	 */
	@RequestMapping(path = "/{username}", method = RequestMethod.DELETE)
	public User deleteUser (
			@PathVariable(value = "username")	String username
			) {
		
		return userService.delete(username);
	}

	/**
	 * Generates a number of Users at random using the random user generator api.
	 * 
	 * @param number Amount of Users to generate
	 * @return The list of generated Users
	 */
	@RequestMapping(path = "/generate/{number}", method = RequestMethod.GET)
	public List<User> getRandomUser(
			@PathVariable(value = "number")	Integer number
			) {
		
		return userService.generateRandom(number);
	}
	
}
