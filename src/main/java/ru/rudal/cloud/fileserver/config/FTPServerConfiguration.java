package ru.rudal.cloud.fileserver.config;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rudal.cloud.fileserver.repository.FtpUserRepository;
import ru.rudal.cloud.fileserver.service.CustomDbUserManagerFactory;

import javax.sql.DataSource;

@Configuration
public class FTPServerConfiguration {
    private final FtpUserRepository ftpUserRepository;
    private FtpServer ftpServer;
    @Value("${file-server.ftp.enable}")
    boolean isFTPEnable;
    public FTPServerConfiguration(FtpUserRepository ftpUserRepository) {
        this.ftpUserRepository = ftpUserRepository;
    }

    public FtpServer createServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(21);
        serverFactory.addListener("default", listenerFactory.createListener());
        CustomDbUserManagerFactory managerFactory = new CustomDbUserManagerFactory(ftpUserRepository);
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
//        FtpServerFactory serverFactory = new FtpServerFactory();
//        ftpServer = serverFactory.createServer();
        ftpServer = createServer();
        if(isFTPEnable)ftpServer.start();
        return ftpServer;
    }

 }
