package com.agilecontent.test.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.agilecontent.test.models.GeneratedUser;
import com.agilecontent.test.models.GeneratedUserList;
import com.agilecontent.test.models.PagedList;
import com.agilecontent.test.models.User;
import com.agilecontent.test.utils.ErrorUtils;
import com.google.gson.Gson;

@Service
public class UserService {
	
	private static final int PAGESIZE = 10;

	/**
	 * In-memory list of users. Empty by default.
	 */
	private PagedList<User> users;

	/**
	 * Base uri for the random user generation api.
	 */
	private final String randomGeneratorUri = "https://randomuser.me/api/?results=";

	/**
	 * Empty constructor.
	 */
	public UserService() {
		this.users = new PagedList<User>(PAGESIZE);
	}

	/**
	 * Parameterized constructor.
	 * 
	 * @param users List of users to be pre-charged initially.
	 */
	public UserService(List<User> users) {
		this.users = new PagedList<User>(users, PAGESIZE);
	}

	/**
	 * Moves forward N pages and returns the contents of the page, then
	 * moves to the next one.
	 * @param offset The amount of offset to use to move the pages
	 * @return The page found
	 */
	public List<User> getCurrentPage() {
		return users.getPage();
	}

	/**
	 * Moves forward N pages and returns the contents of the page, then
	 * moves to the next one.
	 * @param offset The amount of offset to use to move the pages
	 * @return The page found
	 */
	public List<User> getNextPage(int offset) {
		int current = users.getCurrentPage();
		users.setPage(current+offset);
		return users.nextPage();
	}

	/**
	 * Moves backward N pages and returns the contents of the page, then
	 * moves to the previous one.
	 * @param offset The amount of offset to use to move the pages
	 * @return The page found
	 */
	public List<User> getPreviousPage(int offset) {
		int current = users.getCurrentPage();
		users.setPage(current-offset);
		return users.prevPage();
	}

	/**
	 * Return one user matched by the username
	 * 
	 * @param username The username to find the User by
	 * @return The User with the specified username, or null if not found
	 */
	public User getOne(String username) {
		return findUserInList(username);
	}

	/**
	 * Creates and stores one User in the in-memory list
	 * 
	 * @param user User to add to the list
	 * @return The very User we tried to add
	 */
	public User create(User user) {
		if (findUserInList(user.getUsername()) != null)
			throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.EXISTING_USERNAME.getMsg(), user.getUsername()));

		users.add(user);

		return user;
	}

	/**
	 * Changes the info of an existing user on the list
	 * 
	 * @param user The User to be modified, identified by their username, with all the new data to insert
	 * @return The modified User
	 */
	public User update(User user) {
		User updatedUser = findUserInList(user.getUsername());

		if (updatedUser == null)
			throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.USERNAME_NOT_FOUND.getMsg(), user.getUsername()));

		updatedUser.setName(user.getName());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setGender(user.getGender());
		updatedUser.setPicture(user.getPicture());

		return updatedUser;
	}

	/**
	 * Deletes the User identified by the specified username from the list
	 * 
	 * @param username The username of the User to delete
	 * @return The very User we have just deleted
	 */
	public User delete(String username) {
		User deletedUser = findUserInList(username);

		if (deletedUser == null)
			throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.USERNAME_NOT_FOUND.getMsg(), username));

		users.remove(deletedUser);

		return deletedUser;
	}

	/**
	 * Generates a random number of Users by calling the random user generator api
	 * 
	 * @param number Number of Users to generate
	 * @return The list of generated Users
	 */
	public List<User> generateRandom(Integer number) {

		List<User> addList = new ArrayList<User>();

		/*
		 * We loop the call to the random user generator so that we are sure to obtain exactly the amount
		 * of users requested. Multiple calls could be a consequence of:
		 *  - Very large amount of users requested: Every call to the api has a maximum of 5000 results
		 *  - Duplicated usernames: Usernames could be duplicated between requests to the api or even
		 *    within the same one (not sure about this part, but we check for that just in case) 
		 */
		while (addList.size() < number) {
			try {
				addList.addAll(generateRandomUsers(number - addList.size()));
			} catch (HttpServerErrorException e) {
				throw new RuntimeException(String.format(ErrorUtils.ErrorMsg.TOO_MANY_REQUESTS.getMsg()));
			}
		}

		users.addAll(addList);

		return addList;
	}

	/**
	 * Private sub-method to call the api as many times as needed, given that we can only request the random generator
	 * to give us up to 5000 Users each time, and usernames generated are not forced to be unique when called.
	 * 
	 * @param number The number of Users to be generated on this request
	 * @return The list of Users returned by the request
	 */
	private List<User> generateRandomUsers(Integer number) {

		String size = String.valueOf(number);

		/*
		 * Number correction so we're always making correct calls. "number" variable here
		 * will in theory never reach this point if it was less than 0, but we make sure anyway
		 */
		if (number < 1)
			size = "1";
		else if (number > 5000)
			size = "5000";

		String searchUri = randomGeneratorUri.concat(size);

		// We call the random generator service through RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		String jsonResult = restTemplate.getForObject( searchUri , String.class);

		// We map the result to a Json through Gson library (thanks Google)
		Gson gson = new Gson();
		GeneratedUserList list = gson.fromJson(jsonResult, GeneratedUserList.class);

		// A parsing error should not happen in theory, but it costs little to check
		if (list == null)
			throw new RuntimeException(String.format(ErrorUtils.ErrorMsg.GENERATOR_ERROR.getMsg()));

		// We discard the Users whose username is already present in our list
		List<User> resultList = new ArrayList<User>();
		for (GeneratedUser u : list.getUsers()) {
			if (findUserInList(u.getUsername()) == null)
				resultList.add(new User(u));
		}

		return resultList;
	}

	/**
	 * Auxiliary function to find and return a user in our list by only specifying its username.
	 * 
	 * @param username The username to identify the User by
	 * @return The found User; if not found, null is returned
	 */
	private User findUserInList(String username) {
		return users.stream().filter(u -> u.getUsername().equals(username)).collect(Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					if (list.size() < 1)
						return null;
					else if (list.size() > 1) {
						throw new IllegalArgumentException(String.format(ErrorUtils.ErrorMsg.USERNAME_NOT_FOUND.getMsg(), username));
					}

					return list.get(0);
				}
				));
	}

}
