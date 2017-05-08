package backend.repository.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfigurator {

	/**
	 * @return {@link DataSource}
	 */
	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
		// @formatter:off
        .url("jdbc:mysql://localhost:3306/studentdorms")
        .driverClassName("com.mysql.jdbc.Driver")
        .username("root")
        .password("admin")
        .build();
    // @formatter:on
	}
}
