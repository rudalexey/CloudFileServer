package ru.rudal.cloud.fileserver.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rudal.cloud.fileserver.DefaultTestAnnotations;
import ru.rudal.cloud.fileserver.entity.User;
import ru.rudal.cloud.fileserver.security.errors.UserNotActivatedException;
import ru.rudal.cloud.fileserver.service.UserService;

import javax.transaction.Transactional;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Aleksey Rud
 */
@DefaultTestAnnotations
@Transactional
class CustomUserDetailsServiceTest {
	private static final String USER_ONE_LOGIN = "test-user-one";
	private static final String USER_ONE_EMAIL = "test-user-one@localhost";
	private static final String USER_TWO_LOGIN = "test-user-two";
	private static final String USER_TWO_EMAIL = "test-user-two@localhost";
	private static final String USER_THREE_LOGIN = "test-user-three";
	private static final String USER_THREE_EMAIL = "test-user-three@localhost";

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private UserService userService;

	@BeforeEach
	void init() {
		User userOne = new User();
		userOne.setUsername(USER_ONE_LOGIN);
		userOne.setPassword(passwordEncoder.encode(USER_ONE_LOGIN));
		userOne.setEnabled(true);
		userOne.setEmail(USER_ONE_EMAIL);
		userOne.setFirstName("userOne");
		userOne.setLastName("test");
		userOne.setLangKey(Locale.forLanguageTag("ru"));
		userService.save(userOne);

		User userTwo = new User();
		userTwo.setUsername(USER_TWO_LOGIN);
		userTwo.setPassword(passwordEncoder.encode(USER_TWO_LOGIN));
		userTwo.setEnabled(true);
		userTwo.setEmail(USER_TWO_EMAIL);
		userTwo.setFirstName("userTwo");
		userTwo.setLastName("test");
		userTwo.setLangKey(Locale.ENGLISH);
		userService.save(userTwo);

		User userThree = new User();
		userThree.setUsername(USER_THREE_LOGIN);
		userThree.setPassword(passwordEncoder.encode(USER_THREE_LOGIN));
		userThree.setEnabled(false);
		userThree.setEmail(USER_THREE_EMAIL);
		userThree.setFirstName("userThree");
		userThree.setLastName("test");
		userThree.setLangKey(Locale.forLanguageTag("ru"));
		userService.save(userThree);
	}

	@Test
	@Transactional
	public void assertThatUserCanBeFoundByLogin() {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(USER_ONE_LOGIN);
		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_LOGIN);
	}

	@Test
	@Transactional
	public void assertThatUserCanBeFoundByEmail() {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(USER_TWO_EMAIL);
		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo(USER_TWO_LOGIN);
	}

	@Test
	@Transactional
	public void assertThatUserCanNotBeFoundByLoginIgnoreCase() {
		Exception exception = assertThrows(UsernameNotFoundException.class, () ->
				customUserDetailsService.loadUserByUsername(USER_TWO_LOGIN.toUpperCase(Locale.ENGLISH))
		);
		assertNotNull(exception.getMessage());
	}

	@Test
	@Transactional
	public void assertThatEmailIsPrioritizedOverLogin() {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(USER_ONE_EMAIL);
		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_LOGIN);
	}

	@Test
	@Transactional
	public void assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
		Exception exception = assertThrows(UserNotActivatedException.class, () ->
				customUserDetailsService.loadUserByUsername(USER_THREE_LOGIN)
		);
		assertNotNull(exception.getMessage());

	}
}
