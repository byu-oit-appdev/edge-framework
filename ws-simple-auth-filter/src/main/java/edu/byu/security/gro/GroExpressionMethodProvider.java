package edu.byu.security.gro;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import edu.byu.security.common.GrantedAuthorityComparator;
import edu.byu.security.common.SecurityExpressionMethodProvider;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 08/21/2013
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 08/21/2013
 */
public class GroExpressionMethodProvider implements SecurityExpressionMethodProvider {

	private static final Logger LOG = Logger.getLogger(GroExpressionMethodProvider.class);

	protected static final KeepOnlyGroStrings KEEP_ONLY_GRO_STRINGS = new KeepOnlyGroStrings();

	private final Map<String, Method> providedMethods;

	public GroExpressionMethodProvider() throws NoSuchMethodException {
		this.providedMethods = new HashMap<String, Method>();
		this.providedMethods.put("isMemberOfAllGroups", getClass().getMethod("isMemberOfAllGroups", Array.newInstance(String.class, 0).getClass()));
		this.providedMethods.put("isMemberOfAnyGroup", getClass().getMethod("isMemberOfAnyGroup", Array.newInstance(String.class, 0).getClass()));
		this.providedMethods.put("isMemberOfGroup", getClass().getMethod("isMemberOfGroup", String.class));
	}

	@Override
	public Map<String, Method> getMethods() {
		return providedMethods;
	}

	private static final Comparator<GrantedAuthority> GRANTED_AUTHORITY_COMPARATOR = new GrantedAuthorityComparator();

	public static boolean isMemberOfAllGroups(final String... groAuthStrings) {
		validateInput(groAuthStrings);
		final List<? extends GrantedAuthority> list = extractGroGrantedAuthoritiesFromAuthentication();
		if (list == null || list.isEmpty()) return false;
		for (final String gas : groAuthStrings) {
			try {
				if (! isMemberOfGroup(gas, list)) return false;
			} catch (final GroGrantedAuthorityParseException err) {
				return false;
			} catch (final IllegalArgumentException err) {
				return false;
			}
		}
		return true;
	}

	public static boolean isMemberOfAnyGroup(final String... groAuthStrings) {
		validateInput(groAuthStrings);
		final List<? extends GrantedAuthority> list = extractGroGrantedAuthoritiesFromAuthentication();
		if (list == null || list.isEmpty()) return false;
		for (final String gas : groAuthStrings) {
			try {
				if (isMemberOfGroup(gas, list)) return true;
			} catch (final GroGrantedAuthorityParseException ignore) {
			} catch (final IllegalArgumentException ignore) {
			}
		}
		return false;
	}

	public static boolean isMemberOfGroup(final String groAuthString) throws GroGrantedAuthorityParseException {
		validateInput(groAuthString);
		return isMemberOfGroup(groAuthString, extractGroGrantedAuthoritiesFromAuthentication());
	}

	public static List<? extends GrantedAuthority> extractGroGrantedAuthoritiesFromAuthentication(final Authentication auth) {
		if (auth == null) throw new IllegalStateException("No authentication in security context. Unable to examine granted authorities.");
		final Collection<? extends GrantedAuthority> authoritiesCollection = auth.getAuthorities();
		final List<? extends GrantedAuthority> list = new ArrayList<GrantedAuthority>(Collections2.filter(authoritiesCollection,
				KEEP_ONLY_GRO_STRINGS
		));
		Collections.sort(list, GRANTED_AUTHORITY_COMPARATOR);
		return list;
	}

	public static List<? extends GrantedAuthority> extractGroGrantedAuthoritiesFromAuthentication() {
		final SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) throw new IllegalStateException("No security context. Unable to process authentication.");
		return extractGroGrantedAuthoritiesFromAuthentication(context.getAuthentication());
	}

	public static boolean isMemberOfGroup(final String groAuthString, final Authentication authentication) throws GroGrantedAuthorityParseException {
		return isMemberOfGroup(groAuthString, extractGroGrantedAuthoritiesFromAuthentication(authentication));
	}

	public static boolean isMemberOfGroup(final String groAuthString, final List<? extends GrantedAuthority> granted) throws GroGrantedAuthorityParseException {
		if (groAuthString == null || groAuthString.length() == 0 || !GroGrantedAuthority.isGroGrantedAuthority(groAuthString))
			throw new IllegalArgumentException("The provided string (" + groAuthString + ") is not a valid GRO granted authority representation.");
		final GroGrantedAuthority gga = GroGrantedAuthority.parseAuthority(groAuthString);
		return granted.contains(gga);
	}

	private static void validateInput(final String... input) {
		if (input == null || input.length == 0) throw new IllegalArgumentException("No GRO group specified for user authorization.");
		for (final String s : input) {
			if (s.trim().length() > 0) return;
		}
		throw new IllegalArgumentException("No valid GRO group specified for user authorization.");
	}

	private static class KeepOnlyGroStrings implements Predicate<GrantedAuthority> {
		@Override
		public boolean apply(final GrantedAuthority input) {
			return GroGrantedAuthority.class.isAssignableFrom(input.getClass());
		}
	}
}
