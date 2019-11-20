package ru.rudal.cloud.fileserver.repository;

import ru.rudal.cloud.fileserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Aleksey Rud
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
}
