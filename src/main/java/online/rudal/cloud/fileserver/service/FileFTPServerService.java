package online.rudal.cloud.fileserver.service;

import org.apache.ftpserver.FtpServer;
import org.springframework.stereotype.Service;

@Service
public class FileFTPServerService {
private final FtpServer ftpServe;
    public FileFTPServerService(FtpServer ftpServe) {

        this.ftpServe = ftpServe;
    }
}
