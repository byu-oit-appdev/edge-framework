package edu.byu.pro.email;

/**
 * Created by Wyatt Taylor on 1/10/17.
 */
public interface EmailAddressDao {

	/**
	 * @param personId
	 * @return result
	 */
	public ProEmailAddress getEmailAddressRec(String personId);

	/**
	 * @param personId
	 * @return result
	 */
	public String getBasicEmailAddress(String personId);

	/**
	 * @param personId
	 * @return result
	 */
	public String getWorkEmailAddress(String personId);

}
