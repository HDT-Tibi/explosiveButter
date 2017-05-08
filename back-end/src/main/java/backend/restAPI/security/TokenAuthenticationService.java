package backend.restAPI.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import backend.repository.model.User;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	private static final String USER_DETAILS = "USER-DETAILS";
	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

	private final TokenHandler tokenHandler;

	/**
	 * @param secret
	 */
	@Autowired
	public TokenAuthenticationService(@Value("${token.secret}") String secret) {
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}

	/**
	 * @param response
	 * @param authentication
	 * @throws JsonProcessingException
	 */
	public void addAuthentication(HttpServletResponse response, UserAuthentication authentication)
			throws JsonProcessingException {
		final User user = authentication.getDetails();
		user.setExpires(System.currentTimeMillis() + TEN_DAYS);
		response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
		ObjectMapper mapper = new ObjectMapper();
		response.addHeader(USER_DETAILS, mapper.writeValueAsString(user));
	}

	/**
	 * @param request
	 * @return {@link Authentication}
	 */
	public Authentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(AUTH_HEADER_NAME);
		if (token != null) {
			final User user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				return new UserAuthentication(user);
			}
		}
		return null;
	}
}
