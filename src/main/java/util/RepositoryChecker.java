package util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;

public class RepositoryChecker {
    public static boolean checkRepositoryExits(String url) {
        try {
            LsRemoteCommand ls = Git.lsRemoteRepository()
                    .setRemote(url)
                    .setHeads(true)
                    .setTags(true);

            ls.call();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}