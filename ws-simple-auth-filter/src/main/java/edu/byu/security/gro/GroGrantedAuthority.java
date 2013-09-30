package edu.byu.security.gro;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public final class GroGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 2l;

	public static final String GRO_AUTHORITY_PREFIX = "GRO_";

	private static final ConcurrentMap<String, GroGrantedAuthority> GRANTED_AUTHORITY_CACHE = new ConcurrentHashMap<String, GroGrantedAuthority>(16, .999999f);

	public static boolean isGroGrantedAuthority(final String authorityString) {
		return isValidString(authorityString) && isGroAuthorityString(authorityString.trim());
	}

	private static boolean isValidString(final String s) {
		return s != null && s.replaceAll("\\s", "").length() > 0;
	}

	private static boolean isGroAuthorityString(final String s) {
		return s.startsWith(GRO_AUTHORITY_PREFIX);
	}

	private static GroGrantedAuthority doParseAuthority(final String s) {
		return new GroGrantedAuthority(s.substring(GRO_AUTHORITY_PREFIX.length()));
	}

	public static GroGrantedAuthority parseAuthority(final String input) throws GroGrantedAuthorityParseException {
		if (! isGroGrantedAuthority(input)) throw new GroGrantedAuthorityParseException(input);
		final String authorityString = input.replaceAll("\\s", "");
		if (GRANTED_AUTHORITY_CACHE.containsKey(authorityString)) return GRANTED_AUTHORITY_CACHE.get(authorityString);
		final GroGrantedAuthority gga = doParseAuthority(authorityString);
		if (gga == null || !gga.isValid()) throw new GroGrantedAuthorityParseException(input);
		GRANTED_AUTHORITY_CACHE.putIfAbsent(authorityString, gga);
		return gga;
	}

	private String authority = null;
	private String personId;
	private String groupName;

	GroGrantedAuthority() {

	}

	public GroGrantedAuthority(final String groupName) {
		Assert.hasText(groupName, "A 'groupName' is required to create a GroGrantedAuthority.");
		this.groupName = groupName;
		createAuthorityString();
	}

	String getPersonId() {
		return personId;
	}

	void setPersonId(String personId) {
		this.personId = personId;
	}

	String getGroupName() {
		return groupName;
	}

	void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String getAuthority() {
		if (authority == null) {
			createAuthorityString();
		}
		return authority;
	}

	private void createAuthorityString() {
		if (groupName == null) {
			throw new IllegalStateException("A groupName is required to create a GroGrantedAuthority.");
		}
		authority = GRO_AUTHORITY_PREFIX + this.groupName.toUpperCase();
	}

//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//
//		GroGrantedAuthority that = (GroGrantedAuthority) o;
//
//		if (!groupName.equals(that.groupName)) return false;
//		if (!personId.equals(that.personId)) return false;
//
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		int result = personId.hashCode();
//		result = 31 * result + groupName.hashCode();
//		return result;
//	}


	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GroGrantedAuthority that = (GroGrantedAuthority) o;

		if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) return false;
		if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = personId != null ? personId.hashCode() : 0;
		result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return getAuthority();
	}

	private boolean isValid() {
		return isValidString(this.getGroupName());
	}
}
