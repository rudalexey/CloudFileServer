package ru.rudal.cloud.fileserver.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * @author Aleksey Rud
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cfs_user")
public class User extends AuditTable implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "login", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email",unique = true,nullable = false)
	private String email;

	@Column(name = "locked", nullable = false)
	@Getter(AccessLevel.NONE)
	@Type(type = "yes_no")
	private boolean locked;

	@Column(name = "enabled", nullable = false)
	@Type(type = "yes_no")
	private boolean enabled;

	@Column(name = "last_login", nullable = false, updatable = false)
	private LocalDateTime lastInLogin;

	@ManyToMany
	@JoinTable(
			name = "cfs_users_roles",
			joinColumns = @JoinColumn(
					name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
					name = "role_id", referencedColumnName = "id"))
	@Fetch(FetchMode.SUBSELECT)
	private Collection<Role> roles;

	@OneToOne(fetch = FetchType.LAZY,orphanRemoval = true, cascade = CascadeType.REMOVE,mappedBy = "user")
	private FtpUser ftpUser;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("username='" + username + "'")
				.add("password='" + password + "'")
				.add("email='" + email + "'")
				.add("locked=" + locked)
				.add("enabled=" + enabled)
				.add("lastInLogin=" + lastInLogin)
				.toString();
	}
}
