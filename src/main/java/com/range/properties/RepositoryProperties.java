package com.range.properties;
public record RepositoryProperties (
        String repositoryURL,
        String sshKeyPath,
        String repoPath
){

}