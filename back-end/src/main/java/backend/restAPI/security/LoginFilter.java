package backend.restAPI.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.repository.model.User;
import backend.service.general.UserService;

class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private final TokenAuthenticationService tokenAuthenticationService;
	private final UserService userDetailsService;

	protected LoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
			UserService userDetailsService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping));
		this.userDetailsService = userDetailsService;
		this.tokenAuthenticationService = tokenAuthenticationService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		/**
		 * Allow client to access response headers {USER-DETAILS, X-AUTH-TOKEN}
		 */
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Expose-Headers", "Content-Type, USER-DETAILS, X-AUTH-TOKEN");

		try {
			final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
					user.getUsername(), user.getPassword());
			return getAuthenticationManager().authenticate(loginToken);
		} catch (JsonMappingException e) {
			throw new InternalAuthenticationServiceException("Data received is not a valid User class");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {

		// Lookup the complete User object from the database and create an
		// Authentication for it
		final User authenticatedUser = userDetailsService.loadUserByUsername(authentication.getName());
		final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

		// Add the custom token as HTTP header to the response
		tokenAuthenticationService.addAuthentication(response, userAuthentication);

		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
}
