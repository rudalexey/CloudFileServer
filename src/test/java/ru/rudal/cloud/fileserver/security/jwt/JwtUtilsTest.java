package ru.rudal.cloud.fileserver.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.rudal.cloud.fileserver.entity.Role;
import ru.rudal.cloud.fileserver.entity.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksey Rud
 */
@SpringBootTest
@AutoConfigureMockMvc
class JwtUtilsTest {

	@Autowired
	private JwtUtils jwtUtils;
    @Autowired


	private User getUser() {
		return User.builder()
				.username("admin")
				.enabled(true)
				.email("admin@localhost")
				.roles(Collections.singletonList(Role.builder().name("SUPERVISOR").build()))
				.password("admin")
				.build();
	}

	private Authentication getAuthentication(){
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(getUser(),"admin"));
		SecurityContextHolder.setContext(securityContext);
		return securityContext.getAuthentication();
	}
	@Test
	void generateJwtToken() {

//

		String token = jwtUtils.generateJwtToken(getAuthentication());
	}

	@Test
	void getUserNameFromJwtToken() {
	}

	@Test
	void validateJwtToken() {
	}


	@Test
	public void shouldGenerateAuthToken() throws Exception {


//		assertNotNull(token);
//		mvc.perform(MockMvcRequestBuilders.get("/test").header("Authorization", token)).andExpect(status().isOk());
	}
}
