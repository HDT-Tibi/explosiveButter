package backend.restAPI.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import backend.restAPI.configuration.CORSFilter;
import backend.service.general.UserService;

@EnableWebSecurity
@Configuration
@Order(1)
public class AuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userDetailsService;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	/**
	 *
	 */
	public AuthenticationSecurityConfig() {
		super(true);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().and().anonymous().and().servletApi().and().headers().cacheControl().and().and()
				.authorizeRequests()

				// allow anonymous resource requests
				.antMatchers("/").permitAll()

				// allow anonymous POSTs to register
				.antMatchers(HttpMethod.POST, "/api/register").permitAll()
				// allow anonymous POSTs to login
				.antMatchers(HttpMethod.POST, "/api/login").permitAll()

				// events
				.antMatchers(HttpMethod.GET, "/api/events/*/tests").hasRole("EVALUATOR")

				.antMatchers(HttpMethod.GET, "/api/exercises/accepted/*/image").hasAnyRole("ADMIN", "EMPLOYEE")

				// defined Admin only API area
				// .antMatchers("/admin/**").hasRole("ADMIN")

				// all other request need to be authenticated
				.anyRequest().hasRole("USER").and()

				// Used for CORS to set headders
				.addFilterBefore(new CORSFilter(), UsernamePasswordAuthenticationFilter.class)

				// custom JSON based authentication by POST of
				// {"username":"<name>","password":"<password>"}
				// which sets the token header upon authentication
				.addFilterBefore(new LoginFilter("/api/login", tokenAuthenticationService, userDetailsService,
						authenticationManager()), UsernamePasswordAuthenticationFilter.class)

				// custom Token based authentication based on the header
				// previously given to the client
				.addFilterBefore(new AuthenticationFilter(tokenAuthenticationService),
						UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected UserService userDetailsService() {
		return userDetailsService;
	}
}
