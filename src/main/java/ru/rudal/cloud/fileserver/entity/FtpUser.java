package ru.rudal.cloud.fileserver.entity;

import lombok.*;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cfs_ftp_user")
public class FtpUser extends AuditTable implements org.apache.ftpserver.ftplet.User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "home_directory", nullable = false)
    private String homeDirectory;

    @Type(type = "yes_no")
    @Column(name = "enable_ftp", nullable = false)
    private boolean enableFtp;

    @Type(type = "yes_no")
    @Column(name = "write_permission", nullable = false)
    private boolean writePermission;

    @Column(name = "idle_time", nullable = false)
    private int idleTime;

    @Column(name = "upload_rate", nullable = false)
    private int uploadRate;

    @Column(name = "download_rate", nullable = false)
    private int downloadRate;

    @Column(name = "max_login_number", nullable = false)
    private int maxLoginNumber;

    @Column(name = "max_login_per_ip", nullable = false)
    private int maxLoginPerIp;

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return  user.getPassword();
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    @Override
    public List<? extends Authority> getAuthorities() {
        return null;
    }

    @Override
    public List<? extends Authority> getAuthorities(Class<? extends Authority> aClass) {
        return null;
    }

    @Override
    public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
        return authorizationRequest;
    }

    @Override
    public int getMaxIdleTime() {
        return idleTime;
    }

    @Override
    public boolean getEnabled() {
        return enableFtp;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FtpUser.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("user=" + user.getUsername())
                .add("homeDirectory='" + homeDirectory + "'")
                .add("enableFtp=" + enableFtp)
                .add("writePermission=" + writePermission)
                .add("idleTime=" + idleTime)
                .add("uploadRate=" + uploadRate)
                .add("downloadRate=" + downloadRate)
                .add("maxLoginNumber=" + maxLoginNumber)
                .add("maxLoginPerIp=" + maxLoginPerIp)
                .toString();
    }
}
