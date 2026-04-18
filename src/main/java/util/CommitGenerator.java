package util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CommitGenerator {

    private final Random random = new Random();

    private final String[] messages = {
            "random dev update",
            "auto developing flow",
            "system sync commit",
            "bot working progress",
            "temp fix commit",
            "engine update cycle",
            "development snapshot"
    };

    private final File repoDir;

    public CommitGenerator(File repoDir) {
        this.repoDir = repoDir;
    }

    public void generateCommits() throws IOException, GitAPIException {
        int commitNumber = random.nextInt(3) + 1;

        try (Git git = Git.open(repoDir)) {

            while (commitNumber > 0) {
                makeCommit(git);
                commitNumber--;
            }
        }
    }

    private void makeCommit(Git git) throws GitAPIException, IOException {
        modifyFile();
        
        String message = messages[random.nextInt(messages.length)];

        git.add().addFilepattern(".").call();

        git.commit()
                .setMessage(message)
                .call();
    }

    private void modifyFile() throws IOException {
        File activityFile = new File(repoDir, "activity.txt");
        java.nio.file.Files.writeString(
                activityFile.toPath(), 
                "Update at " + java.time.LocalDateTime.now() + " - " + java.util.UUID.randomUUID() + "\n", 
                java.nio.file.StandardOpenOption.CREATE, 
                java.nio.file.StandardOpenOption.APPEND
        );
    }
}