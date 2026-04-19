package com.range;

import com.range.config.RepositoryPropertiesFactory;
import com.range.service.GitAutomationService;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
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
            RepositoryPropertiesFactory properties = new RepositoryPropertiesFactory();
            GitAutomationService service = new GitAutomationService(properties.readRepositoryProperties());
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
}