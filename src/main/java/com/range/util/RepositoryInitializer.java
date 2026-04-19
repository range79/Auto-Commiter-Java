package com.range.util;

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
import com.range.properties.RepositoryProperties;

import java.io.File;

public class RepositoryInitializer {
    private static final Logger log = LoggerFactory.getLogger(RepositoryInitializer.class);

    public static void initProject(RepositoryProperties props) throws GitAPIException {
        File directory = new File(props.repoPath());

        TransportConfigCallback transportConfigCallback = getTransportConfigCallback(props.sshKeyPath());

        try (Git _ = Git.cloneRepository()
                .setURI(props.repositoryURL())
                .setDirectory(directory)
                .setTransportConfigCallback(transportConfigCallback)
                .call()) {

            log.info("Project cloned successfully to: {}", props.repoPath());
        }
    }

    private static TransportConfigCallback getTransportConfigCallback(String privateKeyPath) {
        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("PreferredAuthentications", "publickey");
                session.setConfig("server_host_key", "ssh-ed25519,rsa-sha2-512,rsa-sha2-256");
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