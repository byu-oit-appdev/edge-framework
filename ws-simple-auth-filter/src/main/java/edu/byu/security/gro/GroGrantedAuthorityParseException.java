package edu.byu.security.gro;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 08/21/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 08/21/2013
 */
public class GroGrantedAuthorityParseException extends Throwable {

	private static final long serialVersionUID = 1L;

	/**
	 * Error message pattern String used to generate error message.
	 */
	private static final String MESSAGE = "The authority string [%s] is not a valid GroGrantedAuthority String representation.";

	/**
	 * Constructs the exception with the given invalid string inserted into the message.
	 *
	 * @param invalidGrantedAuthorityString an invalid GroGrantedAuthorityString representation
	 */
	public GroGrantedAuthorityParseException(final String invalidGrantedAuthorityString) {
		super(String.format(MESSAGE, invalidGrantedAuthorityString));
	}
}
