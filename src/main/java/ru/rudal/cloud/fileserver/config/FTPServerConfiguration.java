package ru.rudal.cloud.fileserver.config;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rudal.cloud.fileserver.repository.FtpUserRepository;

@Configuration
public class FTPServerConfiguration {
    private final FtpUserRepository ftpUserRepository;
    @Value("${file-server.ftp.enable}")
    boolean isFTPEnable;
	@Value("${file-server.ftp.port}")
	int ftpPort;
    private FtpServer ftpServer;

    public FTPServerConfiguration(FtpUserRepository ftpUserRepository) {
        this.ftpUserRepository = ftpUserRepository;
    }

    public FtpServer createServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(ftpPort);
        serverFactory.addListener("default", listenerFactory.createListener());
        CustomDbUserManagerFactory managerFactory = new CustomDbUserManagerFactory();
		/*managerFactory.setDataSource(dataSource);
		managerFactory
				.setSqlUserInsert("INSERT INTO FTP_USER (userid, userpassword, homedirectory, enableflag, writepermission, idletime, uploadrate, downloadrate, maxloginnumber, maxloginperip) VALUES ('{userid}', '{userpassword}', '{homedirectory}', {enableflag}, {writepermission}, {idletime}, {uploadrate}, {downloadrate}, {maxloginnumber}, {maxloginperip})");
		managerFactory
				.setSqlUserUpdate("UPDATE FTP_USER SET userpassword='{userpassword}',homedirectory='{homedirectory}',enableflag={enableflag},writepermission={writepermission},idletime={idletime},uploadrate={uploadrate},downloadrate={downloadrate},maxloginnumber={maxloginnumber}, maxloginperip={maxloginperip} WHERE userid='{userid}'");
		managerFactory
				.setSqlUserDelete("DELETE FROM FTP_USER WHERE userid = '{userid}'");
		managerFactory
				.setSqlUserSelect("SELECT * FROM FTP_USER WHERE userid = '{userid}'");
		managerFactory
				.setSqlUserSelectAll("SELECT userid FROM FTP_USER ORDER BY userid");
		managerFactory
				.setSqlUserAuthenticate("SELECT userid, userpassword FROM FTP_USER WHERE userid='{userid}'");
		managerFactory
				.setSqlUserAdmin("SELECT userid FROM FTP_USER WHERE userid='{userid}' AND userid='admin'");

		managerFactory.setPasswordEncryptor(new Md5PasswordEncryptor());*/
        UserManager createUserManager = managerFactory.createUserManager();
        serverFactory.setUserManager(createUserManager);

        NativeFileSystemFactory fileSystemFactory = new NativeFileSystemFactory();
        fileSystemFactory.setCreateHome(true);
        serverFactory.setFileSystem(fileSystemFactory);

        return serverFactory.createServer();
    }

    @Bean
    public FtpServer ftpServer() throws FtpException {
        ftpServer = createServer();
        if (isFTPEnable) ftpServer.start();
        return ftpServer;
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
            return ftpUserRepository.getFtpUserByUser_Username(s);
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

            UsernamePasswordAuthentication passwordAuthentication = (UsernamePasswordAuthentication) authentication;
            return ftpUserRepository.getFtpUserByUser_Username(passwordAuthentication.getUsername());
        }
    }

}
