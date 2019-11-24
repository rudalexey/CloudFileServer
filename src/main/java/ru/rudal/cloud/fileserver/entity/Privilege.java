package ru.rudal.cloud.fileserver.entity;

import lombok.*;

import javax.persistence.*;
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
@Table(name = "cfs_privilege")
public class Privilege {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name",nullable = false)
	private String name;

	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;

	@Override
	public String toString() {
		return new StringJoiner(", ", Privilege.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("name='" + name + "'")
				.toString();
	}
}
