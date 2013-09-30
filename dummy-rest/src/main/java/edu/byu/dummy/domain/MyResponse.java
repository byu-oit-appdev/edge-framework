package edu.byu.dummy.domain;

import javax.xml.bind.annotation.*;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/17/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/17/2013
 */
@XmlType(name = "response", propOrder = {
		"pathParam", "header", "auth"
})
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class MyResponse {

	@XmlElement(name = "header", required = false)
	protected String header;

	@XmlElement(name = "pathParam", required = false)
	protected String pathParam;

	@XmlElement(name = "auth", required = false)
	protected String auth;

	public MyResponse() {
	}

	public MyResponse(final String header, final String pathParam, final String auth) {
		this.header = header;
		this.pathParam = pathParam;
		this.auth = auth;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(final String header) {
		this.header = header;
	}

	public String getPathParam() {
		return pathParam;
	}

	public void setPathParam(final String pathParam) {
		this.pathParam = pathParam;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(final String auth) {
		this.auth = auth;
	}
}
