package edu.byu.auth.header;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/09/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/09/2013
 */
public enum AuthHeaders {

	ActorId("ACTOR_ID", 1),
	PrincipalId("PRINCIPAL_ID", 2),
	SmUser("SM_USER", 3)
	;

	private final String headerName;
	private final int order;

	private AuthHeaders(final String headerName, final int order) {
		this.headerName = headerName;
		this.order = order;
	}

	public String getHeaderName() {
		return headerName;
	}

	public int getOrder() {
		return order;
	}
}
