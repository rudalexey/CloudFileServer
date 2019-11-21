package ru.rudal.cloud.fileserver.service;

import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import ru.rudal.cloud.fileserver.repository.FtpUserRepository;

public class CustomDbUserManagerFactory implements UserManagerFactory {
    private final FtpUserRepository ftpUserRepository;

    public CustomDbUserManagerFactory(FtpUserRepository ftpUserRepository) {
        this.ftpUserRepository = ftpUserRepository;
    }

    @Override
    public UserManager createUserManager() {
        return new CustomDbUserManager(ftpUserRepository);
    }
}
