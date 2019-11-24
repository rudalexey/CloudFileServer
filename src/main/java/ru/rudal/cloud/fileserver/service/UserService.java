package ru.rudal.cloud.fileserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rudal.cloud.fileserver.entity.User;
import ru.rudal.cloud.fileserver.repository.UserRepository;

/**
 * @author Aleksey Rud
 */
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}
}
