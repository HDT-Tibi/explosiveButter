package backend.repository.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.repository.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * @param username
	 * @return {@link User}
	 */
	User findByUsername(String username);

	/**
	 * @param email
	 * @return {@link User}
	 */
	User findByEmail(String email);

	@Override
	List<User> findAll();
	// ArrayList<User> findAll();

	User findByIdUser(Long id);

}
