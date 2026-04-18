package util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryChecker {
    private static final Logger log = LoggerFactory.getLogger(RepositoryChecker.class);

    public static boolean checkRepositoryExits(String url) {
        try {
            LsRemoteCommand ls = Git.lsRemoteRepository()
                    .setRemote(url)
                    .setHeads(true)
                    .setTags(true);

            ls.call();
            return true;
        } catch (Exception e) {
            log.warn("Repository Not exits");
            return false;
        }
    }
}