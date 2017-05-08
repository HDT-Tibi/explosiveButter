package backend.restAPI.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import backend.repository.model.User;
import backend.repository.model.UserRole;
import backend.repository.repository.UserRepository;

@EnableAutoConfiguration
@Configuration
@ComponentScan
public class InitializeAuthentication {

	/**
	 * @return {@link InitializingBean}
	 */
	@Bean
	public InitializingBean insertDefaultUsers() {
		return new InitializingBean() {

			@Autowired
			private UserRepository userRepository;

			@Override
			public void afterPropertiesSet() {
				addUser("admin", "admin");
				addUser("user", "user");
				addUser("victor", "victor");
				addUser("evaluator", "evaluator");
				addUser("tibi", "tibi");
			}

			private void addUser(String username, String password) {
				if (userRepository.findByUsername(username) != null) {
					return;
				}

				User user = new User();
				user.setUsername(username);
				user.setPassword(new BCryptPasswordEncoder().encode(password));
				user.setEmail("no@mail.me");
				user.setFirstName("first name");
				user.setLastName("last name");
				if (username.equals("admin")) {
					user.grantRole(UserRole.ADMIN);
				} else if (username.equals("tibi")) {
					user.grantRole(UserRole.ADMIN);
					user.grantRole(UserRole.EVALUATOR);
					user.grantRole(UserRole.EMPLOYEE);
				} else if (username.equals("victor")) {
					user.grantRole(UserRole.ADMIN);
					user.grantRole(UserRole.EVALUATOR);
					user.grantRole(UserRole.EMPLOYEE);
				} else if (username.equals("evaluator")) {
					user.grantRole(UserRole.EVALUATOR);
				} else {
					user.grantRole(UserRole.EMPLOYEE);
				}

				userRepository.save(user);
			}
		};
	}

	/**
	 * @return {@link Filter}
	 */
	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}
}
