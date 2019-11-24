package ru.rudal.cloud.fileserver.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import ru.rudal.cloud.fileserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import java.util.Optional;

/**
 * @author Aleksey Rud
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@EntityGraph(attributePaths = "roles")
	Optional<User> findOneWithRolesByUsername(String username);
	@EntityGraph(attributePaths = "roles")
	Optional<User> findOneWithRolesByEmail(@Email String email);

	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}
