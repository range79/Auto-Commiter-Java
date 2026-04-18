package util;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import properties.RepositoryProperties;

import java.io.File;

public class RepositoryPusher {
    private static final Logger log = LoggerFactory.getLogger(RepositoryPusher.class);

    public static void pushChanges(RepositoryProperties props) throws GitAPIException {
        File directory = new File(props.repoPath());

        TransportConfigCallback transportConfigCallback = getTransportConfigCallback(props.sshKeyPath());

        try (Git git = Git.open(directory)) {
            git.push()
                    .setTransportConfigCallback(transportConfigCallback)
                    .setRemote("origin")
                    .call();

            log.info("Changes pushed successfully to: {}", props.repositoryURL());
        } catch (Exception e) {
            log.error("Failed to push changes: {}", e.getMessage());
            throw new RuntimeException("Push operation failed", e);
        }
    }

    private static TransportConfigCallback getTransportConfigCallback(String privateKeyPath) {
        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch defaultJSch = super.createDefaultJSch(fs);
                defaultJSch.addIdentity(privateKeyPath);
                return defaultJSch;
            }
        };

        return transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        };
    }
}