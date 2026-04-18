package config;

import properties.RepositoryProperties;

public class RepositoryPropertiesReader {
    public RepositoryProperties readRepositoryProperties(){
        String repoUrl= System.getenv("GITHUB_REPO_URL");
        return new RepositoryProperties(repoUrl);

    }
}
