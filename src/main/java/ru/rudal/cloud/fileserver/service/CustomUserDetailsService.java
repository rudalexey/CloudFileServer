package ru.rudal.cloud.fileserver.service;

import lombok.RequiredArgsConstructor;
import ru.rudal.cloud.fileserver.entity.User;
import ru.rudal.cloud.fileserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Aleksey Rud
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
    @Transactional
	public UserDetails loadUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}


}
