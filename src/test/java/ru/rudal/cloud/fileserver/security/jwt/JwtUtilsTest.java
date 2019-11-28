package ru.rudal.cloud.fileserver.security.jwt;

import io.github.netmikey.logunit.api.LogCapturer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.rudal.cloud.fileserver.DefaultTestAnnotations;
import ru.rudal.cloud.fileserver.entity.Role;
import ru.rudal.cloud.fileserver.entity.User;

import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aleksey Rud
 */
@DefaultTestAnnotations
class JwtUtilsTest {
    @RegisterExtension
    LogCapturer logs = LogCapturer.create().captureForType(JwtUtils.class, Level.ERROR);
    @Autowired
    private JwtUtils jwtUtils;
    private static String TEST_TOKEN_NOT_VALID_KEY = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0X2FkbWluIiwiaWF0IjoxNTc0NjY0NjczLCJleHAiOjE1NzQ3NTEwNzN9.yX_RnQkyj29MA4_rwloFd-DDqejPEgJCRjakaOOhdyELV4ay2G2dWahPWYJz4Hi30REigKM3z8PyPCDlMLFO5g";
    private static String TEST_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0X2p3dF90b2tlbl91c2VyIiwiaWF0IjoxNTc0NjU1NzIzLCJleHAiOjE1NzQ3NDIxMjN9.14R-RLNCrbIOw_OqYXYKb01kwJ_Mmk6D5tAMiWo4b7z_AdKlQuikuEv3MNHBXkK03oms_Z_UAh1Ahlne44jFng";
    private static String TEST_TOKEN_NOT_VALID_EX = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNTc0NjYyNjQxLCJleHAiOjE1NzQ1NzYyNDF9.FORc9DTSw6Neol3Tq0tbxwpKP0FvWtgJ-pN_Oiw6kdt9eP_zCQvAL_OCfgHL0a4F5SpeBPMNGge2Wcf5KAfGJA";
    private static String TEST_TOKEN_NOT_SUP = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU3NDY2NTIzOCwiZXhwIjoxNTc0NzUxNjM4fQ.";
    private User user;
    private Authentication authentication;

    @BeforeEach
    void init() {
        user = User.builder()
                .username("test_admin")
                .firstName("admin")
                .lastName("test")
                .langKey(Locale.ENGLISH)
                .enabled(true)
                .email("test_admin@localhost")
                .roles(Collections.singletonList(Role.builder().name("SUPERVISOR").build()))
                .password("test_admin")
                .build();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword()));
        SecurityContextHolder.setContext(securityContext);
        authentication = securityContext.getAuthentication();
    }

    @Test
    void getUserNameFromJwtToken() {
        String resultUser = jwtUtils.getUserNameFromJwtToken(TEST_TOKEN);
        assertEquals(resultUser, "test_jwt_token_user");
    }

    @Test
    void validateJwtToken() {
        boolean tokenValid = jwtUtils.validateJwtToken(TEST_TOKEN);
        assertTrue(tokenValid);
        // test save in log SignatureException
        tokenValid = jwtUtils.validateJwtToken(TEST_TOKEN_NOT_VALID_KEY);
        assertFalse(tokenValid);
        logs.assertContains("Invalid JWT signature: ");
        // test save in log MalformedJwtException
        tokenValid = jwtUtils.validateJwtToken("THIS IS STRING IS NOT TOKEN");
        assertFalse(tokenValid);
        logs.assertContains("Invalid JWT token:");
        // test save in log ExpiredJwtException
        tokenValid = jwtUtils.validateJwtToken(TEST_TOKEN_NOT_VALID_EX);
        assertFalse(tokenValid);
        logs.assertContains("JWT token is expired:");
        // test save in log UnsupportedJwtException
        tokenValid = jwtUtils.validateJwtToken(TEST_TOKEN_NOT_SUP);
        assertFalse(tokenValid);
        logs.assertContains("JWT token is unsupported: ");
        // test save in log IllegalArgumentException
        tokenValid = jwtUtils.validateJwtToken("");
        assertFalse(tokenValid);
        logs.assertContains("JWT claims string is empty: ");
    }

    @Test
    void generateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);
        assertEquals(jwtUtils.getUserNameFromJwtToken(token), user.getUsername());
        assertTrue(jwtUtils.validateJwtToken(token));
    }

}
