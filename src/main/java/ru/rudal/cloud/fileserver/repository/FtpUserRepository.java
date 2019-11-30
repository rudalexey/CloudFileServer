package ru.rudal.cloud.fileserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rudal.cloud.fileserver.entity.FtpUser;

import java.util.Optional;


@Repository
public interface FtpUserRepository extends JpaRepository<FtpUser,Long> {

    Optional<FtpUser> getFtpUserByUser_Username(String user);
}
