package edu.byu.dummy.rest.v1;

import edu.byu.auth.header.HeaderAuthResult;
import edu.byu.dummy.domain.MyResponse;
import edu.byu.security.userdetails.IdentityDetails;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/17/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/17/2013
 */
@Component
@Path("/v1")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
public class DummyController {

	private static final Logger LOG = Logger.getLogger(DummyController.class);

	@GET
	@Path("/test/{pathParam}")
	public Response dummyEndpoint(
			@PathParam("pathParam") final String pathParam,
			@HeaderParam("headParam") final String header
	) {
		return Response.ok(new MyResponse(header, pathParam, determineAuthenticatedPerson())).build();
	}

	@GET
	@Path("/test/secure/{pathParam}")
	public Response dummyEndpointSecure(
			@PathParam("pathParam") final String pathParam,
			@HeaderParam("headParam") final String header
	) {
		return Response.ok(new MyResponse(header, pathParam, determineAuthenticatedPerson())).build();
	}

	private String determineAuthenticatedPerson() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) return "";
		if (AnonymousAuthenticationToken.class.isAssignableFrom(auth.getClass())) return "Anonymous";
		if (! HeaderAuthResult.class.isAssignableFrom(auth.getClass())) return "Unknown authentication type";
		final IdentityDetails details = (IdentityDetails) auth.getPrincipal();
		if (details == null) return "Invalid identity";
		return details.getNetId();
	}

}
