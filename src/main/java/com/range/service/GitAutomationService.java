package com.range.service;

import com.range.exception.RepoInitException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.range.properties.RepositoryProperties;
import com.range.util.CommitGenerator;

import com.range.util.RepositoryInitializer;
import com.range.util.RepositoryPusher;

import java.io.File;
import java.io.IOException;

public class GitAutomationService {
    private static final Logger log = LoggerFactory.getLogger(GitAutomationService.class);
    private final RepositoryProperties properties;

    public GitAutomationService(RepositoryProperties repositoryProperties) {
        this.properties = repositoryProperties;
    }

    public void run() {
        File repoDir = new File(properties.repoPath());

        try {
            if (!isRepositoryPresent(repoDir)) {
                log.info("Repository not found locally. Starting clone process...");
                RepositoryInitializer.initProject(properties);
            } else {
                log.info("Repository already exists. Skipping clone, proceeding to automation...");
                try (org.eclipse.jgit.api.Git git = org.eclipse.jgit.api.Git.open(repoDir)) {
                    org.eclipse.jgit.lib.StoredConfig config = git.getRepository().getConfig();
                    config.setString("remote", "origin", "url", properties.repositoryURL());
                    config.save();
                    log.debug("Remote 'origin' URL verified/updated to use SSH");
                }
            }

            CommitGenerator commitGenerator = new CommitGenerator(repoDir);
            commitGenerator.generateCommits();

            RepositoryPusher.pushChanges(properties);

            log.info("Workflow completed successfully.");

        } catch (GitAPIException e) {
            log.error("Git operation failed: {}", e.getMessage());
            throw new RepoInitException("Git workflow failed", e);
        } catch (IOException e) {
            log.error("I/O error: {}", e.getMessage());
        }
    }

    private boolean isRepositoryPresent(File directory) {
        File gitDir = new File(directory, ".git");
        return directory.exists() && gitDir.exists();
    }
}