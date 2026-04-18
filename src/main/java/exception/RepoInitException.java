package exception;

import org.eclipse.jgit.api.errors.GitAPIException;

public class RepoInitException extends RuntimeException {
    public RepoInitException(String message, GitAPIException e) {
        super(message,e);
    }
    public RepoInitException(String message){super(message);}
}
