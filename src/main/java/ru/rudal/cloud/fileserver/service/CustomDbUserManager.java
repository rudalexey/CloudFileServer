package ru.rudal.cloud.fileserver.service;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import ru.rudal.cloud.fileserver.repository.FtpUserRepository;


public class CustomDbUserManager extends AbstractUserManager {

    private final FtpUserRepository ftpUserRepository;

    public CustomDbUserManager(FtpUserRepository ftpUserRepository) {
        this.ftpUserRepository = ftpUserRepository;
    }

    @Override
    public User getUserByName(String s) throws FtpException {
        return ftpUserRepository.getFtpUserByUser_Username(s) ;
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        return new String[0];
    }

    @Override
    public void delete(String s) throws FtpException {
        ftpUserRepository.deleteById(Long.parseLong(s));
    }

    @Override
    public void save(User user) throws FtpException {
    }

    @Override
    public boolean doesExist(String s) throws FtpException {
        return false;
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {

        UsernamePasswordAuthentication passwordAuthentication= (UsernamePasswordAuthentication) authentication;
        return ftpUserRepository.getFtpUserByUser_Username(passwordAuthentication.getUsername()) ;
    }
}
