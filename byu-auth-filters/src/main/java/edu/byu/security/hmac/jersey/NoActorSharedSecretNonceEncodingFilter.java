package edu.byu.security.hmac.jersey;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import edu.byu.security.hmac.CredentialNotFoundException;
import edu.byu.security.hmac.CredentialResolver;
import edu.byu.security.hmac.ExpiredCredentialException;
import edu.byu.security.hmac.PersonIdCredentialResolutionRequest;
import edu.byu.security.hmac.ws.authentication.nonce.NonceHmacCredential;
import edu.byu.ws.authentication.dao.NonceDAO;
import edu.byu.ws.authentication.domain.SharedSecretCredential;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 07/15/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 07/15/2014
 */
public class NoActorSharedSecretNonceEncodingFilter extends BaseHeaderAuthenticationClientFilter implements InitializingBean {

	private final CredentialResolver<SharedSecretCredential, PersonIdCredentialResolutionRequest> credentialResolver;
	private final PersonIdCredentialResolutionRequest resolutionRequest;
	private final NonceDAO nonceDAO;

	public NoActorSharedSecretNonceEncodingFilter(
			final CredentialResolver<SharedSecretCredential, PersonIdCredentialResolutionRequest> credentialResolver,
			final PersonIdCredentialResolutionRequest resolutionRequest,
			final NonceDAO nonceDAO
	) {
		this.credentialResolver = credentialResolver;
		this.resolutionRequest = resolutionRequest;
		this.nonceDAO = nonceDAO;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(credentialResolver, "A CredentialResolver is required to use the SharedSecretNonceEncodingFilter.");
		Assert.notNull(resolutionRequest, "A PersonIdCredentialResolutionRequest is required to use the SharedSecretNonceEncodingFilter.");
		Assert.notNull(nonceDAO, "A NonceDAO is required to use the SharedSecretNonceEncodingFilter.");
	}

	@Override
	protected String generateAuthenticationHeader(final ClientRequest cr) throws ClientHandlerException {
		final SharedSecretCredential sharedSecretCredential = resolveCredential();
		return generateHeaderValue(sharedSecretCredential);
	}

	private SharedSecretCredential resolveCredential() {
		try {
			return credentialResolver.resolve(resolutionRequest);
		} catch (final CredentialNotFoundException e) {
			throw new ClientHandlerException(e);
		} catch (final ExpiredCredentialException e) {
			throw new ClientHandlerException(e);
		}
	}

	private String generateHeaderValue(final SharedSecretCredential sharedSecretCredential) {
		final NonceHmacCredential credential = new NonceHmacCredential(sharedSecretCredential);
		try {
			return credential.generateHeader(nonceDAO.getNewNonce(sharedSecretCredential.getWsId()));
		} catch (final UnsupportedEncodingException e) {
			throw new ClientHandlerException(e);
		}
	}
}
