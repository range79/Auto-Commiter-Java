package properties;
public record RepositoryProperties (
        String repositoryURL,
        String sshKeyPath,
        String repoPath
){

}