package ru.rudal.cloud.fileserver.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.filesystem.nativefs.impl.NativeFileSystemView;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.impl.DefaultConnectionConfig;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rudal.cloud.fileserver.entity.FtpUser;
import ru.rudal.cloud.fileserver.repository.FtpUserRepository;

import java.io.File;
import java.util.Optional;

@Configuration
public class FTPServerConfiguration {
    private final FtpUserRepository ftpUserRepository;
    private final PasswordEncoder encoder;
    @Value("${file-server.ftp.enable}")
    boolean isFTPEnable;
    @Value("${file-server.ftp.port}")
    int ftpPort;
    @Value("${file-server.ftp.directory.base-path}")
    private String baseFtpDir;
    @Value("${file-server.ftp.directory.case-insensitive}")
    private boolean caseInsensitive;

    public FTPServerConfiguration(FtpUserRepository ftpUserRepository, PasswordEncoder encoder) {
        this.ftpUserRepository = ftpUserRepository;
        this.encoder = encoder;
    }

    private FtpServer createServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(ftpPort);
        serverFactory.addListener("default", listenerFactory.createListener());
        CustomDbUserManagerFactory managerFactory = new CustomDbUserManagerFactory();
        UserManager createUserManager = managerFactory.createUserManager();
        serverFactory.setUserManager(createUserManager);
        FileSystemFactory fileSystemFactory = new CustomNativeFileSystemFactory();
        serverFactory.setFileSystem(fileSystemFactory);
        serverFactory.setConnectionConfig(connectionConfig());
        return serverFactory.createServer();
    }

    private ConnectionConfig connectionConfig() {
        return new DefaultConnectionConfig(false,
                500,
                10,
                10,
                3,
                0);
    }

    @Bean
    public FtpServer ftpServer() throws FtpException {
        FtpServer ftpServer = createServer();
        if (isFTPEnable) ftpServer.start();
        return ftpServer;
    }

    private class CustomNativeFileSystemFactory implements FileSystemFactory {
        Logger log = LoggerFactory.getLogger(this.getClass());
        @Override
        public FileSystemView createFileSystemView(User user) throws FtpException {
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (user) {
                // create home if does not exist
                String homeDirStr = baseFtpDir + File.separator + user.getName();
                File homeDir = new File(homeDirStr);
                if (homeDir.isFile()) {
                    log.warn("Not a directory :: " + homeDirStr);
                    throw new FtpException("Not a directory :: " + homeDirStr);
                }
                if ((!homeDir.exists()) && (!homeDir.mkdirs())) {
                    log.warn("Cannot create user home :: " + homeDirStr);
                    throw new FtpException("Cannot create user home :: "
                            + homeDirStr);
                }
                return new NativeFileSystemView(user,
                        caseInsensitive);
            }
        }
    }

    class CustomDbUserManagerFactory implements UserManagerFactory {
        @Override
        public UserManager createUserManager() {
            return new CustomDbUserManager();
        }
    }

    private class CustomDbUserManager extends AbstractUserManager {

        @Override
        public User getUserByName(String s) throws FtpException {
            return ftpUserRepository.getFtpUserByUser_Username(s).orElseThrow(AuthenticationFailedException::new);
        }

        @Override
        public String[] getAllUserNames() throws FtpException {
            throw new FtpException("not supported many user name");
        }

        @Override
        public void delete(String s) throws FtpException {
            throw new FtpException("not supported delete ftp user");
//            ftpUserRepository.deleteById(Long.parseLong(s));
        }

        @Override
        public void save(User user) throws FtpException {
            throw new FtpException("not supported save user");
        }

        @Override
        public boolean doesExist(String user) {
            return ftpUserRepository.getFtpUserByUser_Username(user).isPresent();
        }

        @Override
        public User authenticate(Authentication authentication) throws AuthenticationFailedException {
            UsernamePasswordAuthentication upauth = (UsernamePasswordAuthentication) authentication;
            if (upauth.getUsername() == null) {
                throw new AuthenticationFailedException("Authentication failed");
            }
            if (upauth.getPassword() == null) {
                throw new AuthenticationFailedException("Authentication failed");
            }
            Optional<FtpUser> ftpUser = ftpUserRepository.getFtpUserByUser_Username(upauth.getUsername());
            if (ftpUser.isPresent()) {
                FtpUser user = ftpUser.get();
                if (!user.getEnabled()) {
                    throw new AuthenticationFailedException(
                            "Authentication failed. User not active");
                }
                if (!user.getUser().isAccountNonLocked()) {
                    throw new AuthenticationFailedException(
                            "Authentication failed. User is locked");
                }
                if (encoder.matches(upauth.getPassword(), user.getPassword())) {
                    user.setHomeDirectory(user.getName());
                    return user;
                } else {
                    throw new AuthenticationFailedException(
                            "Authentication failed");
                }
            }
            throw new AuthenticationFailedException(
                    "Authentication failed");
        }
    }
}
