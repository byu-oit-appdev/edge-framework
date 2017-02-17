package edu.byu.pro.email;

import java.io.Serializable;

/**
 * Created by Wyatt Taylor on 1/10/17.
 */
public class ProEmailAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String personId;
	protected String emailAddress;
	protected String workEmailAddress;

	public ProEmailAddress() {
	}

	public ProEmailAddress(final String personId, final String emailAddress, final String workEmailAddress) {
		this.personId = personId;
		this.emailAddress = emailAddress;
		this.workEmailAddress = workEmailAddress;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getWorkEmailAddress() {
		return workEmailAddress;
	}

	public void setWorkEmailAddress(final String workEmailAddress) {
		this.workEmailAddress = workEmailAddress;
	}

	@Override
	public String toString() {
		return "ProEmailAddress{" +
				"personId='" + personId + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", workEmailAddress='" + workEmailAddress + '\'' +
				'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof ProEmailAddress)) return false;

		ProEmailAddress address = (ProEmailAddress) o;

		return personId.equals(address.personId);
	}

	@Override
	public int hashCode() {
		return personId.hashCode();
	}
}
