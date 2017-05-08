package backend.repository.model;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents the User object, witch is a model for a user that is
 * all ready register.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = { "username", "email" }))
public class User implements UserDetails {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public User() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_user")
	private Long idUser;

	@Column(unique = true)
	@NotNull
	private String username;

	@NotNull
	private String password;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@Transient
	private long expires;

	@NotNull
	private boolean accountExpired;

	@NotNull
	private boolean accountLocked;

	@NotNull
	private boolean credentialsExpired;

	@NotNull
	private boolean accountEnabled;

	@Transient
	private String newPassword;

	@NotNull
	private String email;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<UserAuthority> authorities;

	/**
	 * @param username
	 */
	public User(String username) {
		this.username = username;
	}

	/**
	 * @param username
	 * @param expires
	 */
	public User(String username, Date expires) {
		this.username = username;
		this.expires = expires.getTime();
	}

	/**
	 * @return {@link Long}
	 */
	public Long getId() {
		return idUser;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.idUser = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return {@link String}
	 */
	@JsonIgnore
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword
	 */
	@JsonProperty
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	@JsonIgnore
	public Set<UserAuthority> getAuthorities() {
		return authorities;
	}

	// Use Roles as external API
	/**
	 * @return {@link Set} of {@link UserRole}
	 */
	public Set<UserRole> getRoles() {
		Set<UserRole> roles = EnumSet.noneOf(UserRole.class);
		if (authorities != null) {
			for (UserAuthority authority : authorities) {
				roles.add(UserRole.valueOf(authority));
			}
		}
		return roles;
	}

	/**
	 * @param roles
	 */
	public void setRoles(Set<UserRole> roles) {
		for (UserRole role : roles) {
			grantRole(role);
		}
	}

	/**
	 * @param role
	 */
	public void grantRole(UserRole role) {
		if (authorities == null) {
			authorities = new HashSet<>();
		}
		authorities.add(role.asAuthorityFor(this));
	}

	/**
	 * @param role
	 */
	public void revokeRole(UserRole role) {
		if (authorities != null) {
			authorities.remove(role.asAuthorityFor(this));
		}
	}

	/**
	 * @param role
	 * @return {@link Boolean}
	 */
	public boolean hasRole(UserRole role) {
		return authorities.contains(role.asAuthorityFor(this));
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return !accountEnabled;
	}

	/**
	 * @return {@link Long}
	 */
	public long getExpires() {
		return expires;
	}

	/**
	 * @param expires
	 */
	public void setExpires(long expires) {
		this.expires = expires;
	}

	/**
	 *
	 * @return {@link String}
	 */
	public String getEmail() {
		return email;
	}

	/**
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getUsername();
	}

}
