package online.rudal.cloud.fileserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDAVConfiguration {
    @Value("${document.root}")
    private String documentRoot;


}
