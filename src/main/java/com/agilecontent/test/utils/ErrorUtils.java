package com.agilecontent.test.utils;

public class ErrorUtils {

	/**
	 * Enumeration of all the possible error strings
	 * 
	 * @author Carlos Melero
	 *
	 */
	public enum ErrorMsg {
		USERNAME_NOT_FOUND("Username '%s' does not exist."),
		NULL_USERNAME("Username is null and thus not valid."),
		EXISTING_USERNAME("Username '%s' is already present in the database."),
		DUPLICATED_VALUE("Username '%s' seems to be non-unique."),
		INCONSISTENT_DATA("JSON input and URI parameters are not consistent with each other"),
		PARSING_ERROR("The data obtained from the random generator seems to be not valid."),
		GENERATOR_ERROR("The random user generator is not working."),
		TOO_MANY_REQUESTS("%s");
		
		String msg;
		
		ErrorMsg(String msg) {
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}
	}
	
}
