package config;

import properties.RepositoryProperties;
import java.nio.file.Paths;
import java.util.Optional;

public class RepositoryPropertiesFactory {

    public RepositoryProperties readRepositoryProperties() {
        String repoUrl = getRequiredEnv("GITHUB_REPO_URL");
        String sshKeyPath = getRequiredEnv("GITHUB_SSH_KEY_PATH");

        String repoPath = Optional.ofNullable(System.getenv("LOCAL_REPO_PATH"))
                .orElseGet(() -> Paths.get(System.getProperty("user.dir"), "repo").toString());

        return new RepositoryProperties(
                repoUrl,
                sshKeyPath,
                repoPath
        );
    }

    private String getRequiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(String.format("Required environment variable '%s' is missing or empty", key));
        }
        return value;
    }
}