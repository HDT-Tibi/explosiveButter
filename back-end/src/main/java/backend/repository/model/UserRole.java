package backend.repository.model;

public enum UserRole {
	/**
	 * The admin has some higher responsibilities like editing users.
	 */
	ADMIN,

	/**
	 * Having this role means you are banned from the web site
	 */
	EVALUATOR,
	/**
	 * Having this role means you are an employee
	 */
	EMPLOYEE;

	/**
	 * @param user
	 * @return {@link UserAuthority}
	 */
	public UserAuthority asAuthorityFor(final User user) {
		final UserAuthority authority = new UserAuthority();
		authority.setAuthority("ROLE_" + toString());
		authority.setUser(user);
		return authority;
	}

	/**
	 * @param authority
	 * @return {@link UserRole}
	 */
	public static UserRole valueOf(final UserAuthority authority) {
		switch (authority.getAuthority()) {
		case "ROLE_ADMIN":
			return ADMIN;
		case "ROLE_EVALUATOR":
			return EVALUATOR;
		case "ROLE_EMPLOYEE":
			return EMPLOYEE;
		}
		throw new IllegalArgumentException("No role defined for authority: " + authority.getAuthority());
	}
}
