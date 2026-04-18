package util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class RepositoryInitializer {

    public static void initProject(String url) throws GitAPIException {
        String currentPath = System.getProperty("user.dir");
        File directory = new File(currentPath);

        try (Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(directory)
                .call()) {

            System.out.println("Project Cloned Successfully: " + currentPath);
        }
    }
}