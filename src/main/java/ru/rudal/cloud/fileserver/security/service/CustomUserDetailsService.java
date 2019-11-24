package ru.rudal.cloud.fileserver.security.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rudal.cloud.fileserver.repository.UserRepository;
import ru.rudal.cloud.fileserver.security.errors.UserNotActivatedException;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

/**
 * @author Aleksey Rud
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(@NotNull String login) {
		if (new EmailValidator().isValid(login, null)) {
			return userRepository.findOneWithRolesByEmail(login.toLowerCase()).map((user) -> {
				if (!user.isEnabled())
					throw new UserNotActivatedException("User " + login.toLowerCase() + " was not activated");
				return user;
			}).orElseThrow(() -> new UsernameNotFoundException(login));
		} else {
			return userRepository.findOneWithRolesByUsername(login).map((user) -> {
				if (!user.isEnabled())
					throw new UserNotActivatedException("User " + login.toLowerCase() + " was not activated");
				return user;
			}).orElseThrow(() -> new UsernameNotFoundException(login));
		}
	}
}
