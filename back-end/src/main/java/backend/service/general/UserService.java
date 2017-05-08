package backend.service.general;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import backend.exceptions.CustomException;
import backend.repository.model.User;
import backend.repository.model.UserRole;
import backend.repository.repository.UserRepository;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public void registerValidateUser(User user) throws CustomException {
		this.validUser(user);
		userRepository.save(user);
	}

	private void validUser(User user) throws CustomException {
		if (user.getUsername().length() <= 4 || user.getUsername().length() > 30) {
			throw new CustomException("");
		}
		if (!validEmail(user.getEmail())) {
			throw new CustomException("");
		}
		if (user.getPassword().length() <= 4 || user.getPassword().length() >= 30) {
			throw new CustomException("");
		}
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new CustomException("");
		}

		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.grantRole(UserRole.EMPLOYEE);
		if (user.getUsername().equals("admin")) {
			user.grantRole(UserRole.ADMIN);
		}
	}

	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	private static boolean validEmail(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@Override
	public final User loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		detailsChecker.check(user);
		return user;
	}
}
