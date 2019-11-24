package ru.rudal.cloud.fileserver.config.audit;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @author Aleksey Rud
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(getCurrentUserLogin().orElse("SYSTEM_USER"));
	}

	private static Optional<String> getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication())
				.map(authentication -> {
					if (authentication.getPrincipal() instanceof UserDetails) {
						UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
						return springSecurityUser.getUsername();
					} else if (authentication.getPrincipal() instanceof String) {
						return (String) authentication.getPrincipal();
					}
					return null;
				});
	}
}
