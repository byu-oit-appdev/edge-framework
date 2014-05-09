package edu.byu.pro.merge.domain;

import java.util.Date;
import java.io.Serializable;

/**
  *
  */
public class PersonMerge implements Serializable, Comparable<PersonMerge> {

	private static final long serialVersionUID = 1L;

	protected String subscriber;
	protected String oldPersonId;
	protected String newPersonId;
	protected Date dateTimeUpdated;
	protected String updatedById;
	protected String sourceInstitution;
	protected String sourceApplication;
	protected String sourceFunction;
	protected String oldByuId;
	protected String oldNetId;
	protected String oldSortName;
	protected String oldRestOfName;
	protected String oldSurname;
	protected String oldPreferredFirstName;
	protected String newByuId;
	protected String newNetId;
	protected String newSortName;
	protected String newRestOfName;
	protected String newSurname;
	protected String newPreferredFirstName;

	/** Default constructor */
	public PersonMerge() {
	}

	/**
	 * Get the current value of subscriber.
	 * @return the current value
	 */
	public String getSubscriber() {
		return this.subscriber;
	}

	/**
	 * Set a new value for subscriber.
	 * @param subscriber the new value
	 */
	public void setSubscriber(final String subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * Get the current value of oldPersonId.
	 * @return the current value
	 */
	public String getOldPersonId() {
		return this.oldPersonId;
	}

	/**
	 * Set a new value for oldPersonId.
	 * @param oldPersonId the new value
	 */
	public void setOldPersonId(final String oldPersonId) {
		this.oldPersonId = oldPersonId;
	}

	/**
	 * Get the current value of newPersonId.
	 * @return the current value
	 */
	public String getNewPersonId() {
		return this.newPersonId;
	}

	/**
	 * Set a new value for newPersonId.
	 * @param newPersonId the new value
	 */
	public void setNewPersonId(final String newPersonId) {
		this.newPersonId = newPersonId;
	}

	/**
	 * Get the current value of dateTimeUpdated.
	 * @return the current value
	 */
	public Date getDateTimeUpdated() {
		return this.dateTimeUpdated;
	}

	/**
	 * Set a new value for dateTimeUpdated.
	 * @param dateTimeUpdated the new value
	 */
	public void setDateTimeUpdated(final Date dateTimeUpdated) {
		this.dateTimeUpdated = dateTimeUpdated;
	}

	/**
	 * Get the current value of updatedById.
	 * @return the current value
	 */
	public String getUpdatedById() {
		return this.updatedById;
	}

	/**
	 * Set a new value for updatedById.
	 * @param updatedById the new value
	 */
	public void setUpdatedById(final String updatedById) {
		this.updatedById = updatedById;
	}

	/**
	 * Get the current value of sourceInstitution.
	 * @return the current value
	 */
	public String getSourceInstitution() {
		return this.sourceInstitution;
	}

	/**
	 * Set a new value for sourceInstitution.
	 * @param sourceInstitution the new value
	 */
	public void setSourceInstitution(final String sourceInstitution) {
		this.sourceInstitution = sourceInstitution;
	}

	/**
	 * Get the current value of sourceApplication.
	 * @return the current value
	 */
	public String getSourceApplication() {
		return this.sourceApplication;
	}

	/**
	 * Set a new value for sourceApplication.
	 * @param sourceApplication the new value
	 */
	public void setSourceApplication(final String sourceApplication) {
		this.sourceApplication = sourceApplication;
	}

	/**
	 * Get the current value of sourceFunction.
	 * @return the current value
	 */
	public String getSourceFunction() {
		return this.sourceFunction;
	}

	/**
	 * Set a new value for sourceFunction.
	 * @param sourceFunction the new value
	 */
	public void setSourceFunction(final String sourceFunction) {
		this.sourceFunction = sourceFunction;
	}

	/**
	 * Get the current value of oldByuId.
	 * @return the current value
	 */
	public String getOldByuId() {
		return this.oldByuId;
	}

	/**
	 * Set a new value for oldByuId.
	 * @param oldByuId the new value
	 */
	public void setOldByuId(final String oldByuId) {
		this.oldByuId = oldByuId;
	}

	/**
	 * Get the current value of oldNetId.
	 * @return the current value
	 */
	public String getOldNetId() {
		return this.oldNetId;
	}

	/**
	 * Set a new value for oldNetId.
	 * @param oldNetId the new value
	 */
	public void setOldNetId(final String oldNetId) {
		this.oldNetId = oldNetId;
	}

	/**
	 * Get the current value of oldSortName.
	 * @return the current value
	 */
	public String getOldSortName() {
		return this.oldSortName;
	}

	/**
	 * Set a new value for oldSortName.
	 * @param oldSortName the new value
	 */
	public void setOldSortName(final String oldSortName) {
		this.oldSortName = oldSortName;
	}

	/**
	 * Get the current value of oldRestOfName.
	 * @return the current value
	 */
	public String getOldRestOfName() {
		return this.oldRestOfName;
	}

	/**
	 * Set a new value for oldRestOfName.
	 * @param oldRestOfName the new value
	 */
	public void setOldRestOfName(final String oldRestOfName) {
		this.oldRestOfName = oldRestOfName;
	}

	/**
	 * Get the current value of oldSurname.
	 * @return the current value
	 */
	public String getOldSurname() {
		return this.oldSurname;
	}

	/**
	 * Set a new value for oldSurname.
	 * @param oldSurname the new value
	 */
	public void setOldSurname(final String oldSurname) {
		this.oldSurname = oldSurname;
	}

	/**
	 * Get the current value of oldPreferredFirstName.
	 * @return the current value
	 */
	public String getOldPreferredFirstName() {
		return this.oldPreferredFirstName;
	}

	/**
	 * Set a new value for oldPreferredFirstName.
	 * @param oldPreferredFirstName the new value
	 */
	public void setOldPreferredFirstName(final String oldPreferredFirstName) {
		this.oldPreferredFirstName = oldPreferredFirstName;
	}

	/**
	 * Get the current value of newByuId.
	 * @return the current value
	 */
	public String getNewByuId() {
		return this.newByuId;
	}

	/**
	 * Set a new value for newByuId.
	 * @param newByuId the new value
	 */
	public void setNewByuId(final String newByuId) {
		this.newByuId = newByuId;
	}

	/**
	 * Get the current value of newNetId.
	 * @return the current value
	 */
	public String getNewNetId() {
		return this.newNetId;
	}

	/**
	 * Set a new value for newNetId.
	 * @param newNetId the new value
	 */
	public void setNewNetId(final String newNetId) {
		this.newNetId = newNetId;
	}

	/**
	 * Get the current value of newSortName.
	 * @return the current value
	 */
	public String getNewSortName() {
		return this.newSortName;
	}

	/**
	 * Set a new value for newSortName.
	 * @param newSortName the new value
	 */
	public void setNewSortName(final String newSortName) {
		this.newSortName = newSortName;
	}

	/**
	 * Get the current value of newRestOfName.
	 * @return the current value
	 */
	public String getNewRestOfName() {
		return this.newRestOfName;
	}

	/**
	 * Set a new value for newRestOfName.
	 * @param newRestOfName the new value
	 */
	public void setNewRestOfName(final String newRestOfName) {
		this.newRestOfName = newRestOfName;
	}

	/**
	 * Get the current value of newSurname.
	 * @return the current value
	 */
	public String getNewSurname() {
		return this.newSurname;
	}

	/**
	 * Set a new value for newSurname.
	 * @param newSurname the new value
	 */
	public void setNewSurname(final String newSurname) {
		this.newSurname = newSurname;
	}

	/**
	 * Get the current value of newPreferredFirstName.
	 * @return the current value
	 */
	public String getNewPreferredFirstName() {
		return this.newPreferredFirstName;
	}

	/**
	 * Set a new value for newPreferredFirstName.
	 * @param newPreferredFirstName the new value
	 */
	public void setNewPreferredFirstName(final String newPreferredFirstName) {
		this.newPreferredFirstName = newPreferredFirstName;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PersonMerge that = (PersonMerge) o;

		if (!newPersonId.equals(that.newPersonId)) return false;
		if (!oldPersonId.equals(that.oldPersonId)) return false;
		if (!subscriber.equals(that.subscriber)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = subscriber.hashCode();
		result = 31 * result + oldPersonId.hashCode();
		result = 31 * result + newPersonId.hashCode();
		return result;
	}

	@Override
	public int compareTo(final PersonMerge o) {
		if (this.equals(o)) return 0;
		final int[] vals = new int[3];
		vals[0] = this.subscriber.compareTo(o.subscriber);
		vals[1] = this.oldPersonId.compareTo(o.oldPersonId);
		vals[2] = this.newPersonId.compareTo(o.newPersonId);
		return vals[0] != 0 ? vals[0] : vals[1] != 0 ? vals[1] : vals[2];
	}
}
