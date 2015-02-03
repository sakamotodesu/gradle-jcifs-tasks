package github.com.sakamotodesu

import jcifs.Config
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

/**
 * task for copying cifs file
 */
class JcifsTask extends DefaultTask {

    String from
    String into
    String lmCompatibility = 3
    String include = ""
    String exclude = ""

    @TaskAction
    def jcifsCopy() {
        Config.setProperty("jcifs.smb.lmCompatibility", lmCompatibility);
        if (from == null || from.isEmpty()) {
            throw new InvalidUserDataException("from is empty")
        }
        if (into == null || into.isEmpty()) {
            throw new InvalidUserDataException("into is empty")
        }

        def srcFile = CopyFileFactory.get(from)
        def dstDir = CopyFileFactory.get(into)
        if (!dstDir.isDirectory()) {
            throw new InvalidUserDataException(dstDir.getPath() + " is not Directory")
        }
        def dstFile = CopyFileFactory.get(dstDir, srcFile.getName())
        srcFile.copyTo(dstFile)
    }

}
