package edu.byu.pro.merge.domain;

import java.util.Date;
import java.io.Serializable;

/**
  *
  */
public class PersonQueue implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String personId;
	protected String subscriber;
	protected Date dateTimeUpdated;
	protected String updatedById;
	protected Integer returnCode;
	protected Integer statusCode;
	protected String netId;

	/** Default constructor */
	public PersonQueue() {
	}

	/**
	  * Get the current value of personId.
	  * @return the current value
	  */
	public String getPersonId() {
		return this.personId;
	}

	/**
	  * Set a new value for personId.
	  * @param personId the new value
	  */
	public void setPersonId(final String personId) {
		this.personId = personId;
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
	  * Get the current value of returnCode.
	  * @return the current value
	  */
	public Integer getReturnCode() {
		return this.returnCode;
	}

	/**
	  * Set a new value for returnCode.
	  * @param returnCode the new value
	  */
	public void setReturnCode(final Integer returnCode) {
		this.returnCode = returnCode;
	}

	/**
	  * Get the current value of statusCode.
	  * @return the current value
	  */
	public Integer getStatusCode() {
		return this.statusCode;
	}

	/**
	  * Set a new value for statusCode.
	  * @param statusCode the new value
	  */
	public void setStatusCode(final Integer statusCode) {
		this.statusCode = statusCode;
	}

	/**
	  * Get the current value of netId.
	  * @return the current value
	  */
	public String getNetId() {
		return this.netId;
	}

	/**
	  * Set a new value for netId.
	  * @param netId the new value
	  */
	public void setNetId(final String netId) {
		this.netId = netId;
	}

}
