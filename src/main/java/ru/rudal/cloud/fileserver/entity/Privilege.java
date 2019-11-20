package ru.rudal.cloud.fileserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Aleksey Rud
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Privilege {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;
}
