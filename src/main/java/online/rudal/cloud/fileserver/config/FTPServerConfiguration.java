package online.rudal.cloud.fileserver.config;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.DbUserManagerFactory;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FTPServerConfiguration {
    private final DataSource dataSource;
    private FtpServer ftpServer;
    @Value("${document.root}")
    private String documentRoot;
    @Value("${file-server.ftp.enable}")
    boolean isFTPEnable;
    public FTPServerConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public FtpServer createServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(21);
        serverFactory.addListener("default", listenerFactory.createListener());
        DbUserManagerFactory managerFactory = new DbUserManagerFactory();
        managerFactory.setDataSource(dataSource);
        managerFactory.setPasswordEncryptor(new Md5PasswordEncryptor());
        UserManager createUserManager = managerFactory.createUserManager();
        serverFactory.setUserManager(createUserManager);

        NativeFileSystemFactory fileSystemFactory = new NativeFileSystemFactory();
        fileSystemFactory.setCreateHome(true);
        serverFactory.setFileSystem(fileSystemFactory);

        return serverFactory.createServer();
    }

    @Bean
    public FtpServer ftpServer() throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ftpServer = serverFactory.createServer();
//        ftpServer = createServer();
        if(isFTPEnable)ftpServer.start();
        return ftpServer;
    }

 }
