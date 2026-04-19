package com.range;

import com.range.config.RepositoryPropertiesFactory;
import com.range.service.GitAutomationService;
import com.range.properties.RepositoryProperties;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
        setupJSchLogger();
    }

    public static void main(String[] args) {
        System.setProperty("logback.color", "true");
        System.setProperty("jansi.force", "true");
        System.setProperty("jansi.mode", "force");
        System.setProperty("jansi.passthrough", "true");
        try {
            AnsiConsole.systemInstall();
        } catch (Throwable _) {
        }

        log.info("Auto-Commiter-Java is starting up...");
        
        try {
            RepositoryPropertiesFactory factory = new RepositoryPropertiesFactory();
            RepositoryProperties props = factory.readRepositoryProperties();
            GitAutomationService service = new GitAutomationService(props);
            service.run();
        } catch (Throwable e) {
            log.error("Application failed: {}", e.getMessage());
            System.exit(1);
        } finally {
            try {
                AnsiConsole.systemUninstall();
            } catch (Throwable _) {
            }
        }
    }

    private static void setupJSchLogger() {
        com.jcraft.jsch.JSch.setLogger(new com.jcraft.jsch.Logger() {
            @Override public boolean isEnabled(int level) { return true; }
            @Override public void log(int level, String message) {
                switch (level) {
                    case com.jcraft.jsch.Logger.DEBUG -> log.debug("SSH: {}", message);
                    case com.jcraft.jsch.Logger.INFO -> log.info("SSH: {}", message);
                    case com.jcraft.jsch.Logger.WARN -> log.warn("SSH: {}", message);
                    case com.jcraft.jsch.Logger.ERROR, com.jcraft.jsch.Logger.FATAL -> log.error("SSH: {}", message);
                    default -> log.trace("SSH: {}", message);
                }
            }
        });
    }
}