package ru.rudal.cloud.fileserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rudal.cloud.fileserver.entity.Privilege;

@Repository
public interface PrivilegeRerpository extends JpaRepository<Privilege,Long> {
}
