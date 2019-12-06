package github.com.sakamotodesu

import github.com.sakamotodesu.file.CopyFileFactory
import jcifs.Config
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

/**
 * task for copying cifs file
 */
class JcifsCopy extends DefaultTask {

    String from
    String into
    String lmCompatibility = 3
    String include
    String exclude
    String cleanBefore = null
    String cleanAfter = null
    Boolean recursively = false

    @TaskAction
    def jcifsCopy() {
        Config.setProperty("jcifs.smb.lmCompatibility", lmCompatibility);
        if (from == null || from.isEmpty()) {
            throw new InvalidUserDataException("from is empty")
        }
        if (into == null || into.isEmpty()) {
            throw new InvalidUserDataException("into is empty")
        }

        if (cleanBefore != null) {
            def cleanBeforeDir = CopyFileFactory.get(cleanBefore)
            cleanBeforeDir.deleteDirectoryContents()
        }

        copyRecursively('')

        if (cleanAfter != null) {
            def cleanAfterDir = CopyFileFactory.get(cleanAfter)
            cleanAfterDir.deleteDirectoryContents()
        }
    }

    def copyRecursively(subPath) {
        def src = CopyFileFactory.get(from + subPath)
        def dst = CopyFileFactory.get(into + subPath)

        if (!dst.exists()) {
            dst.mkdirs()
        }

        src.getFileList().findAll {
            if (include == null || include.isEmpty() || (it.isDirectory() && recursively)) {
                true
            } else {
                it.getName().matches(include)
            }
        }.findAll {
            if (exclude == null || exclude.isEmpty()) {
                true
            } else {
                !it.getName().matches(exclude)
            }
        }.each {
            if (it.isDirectory()) {
                if (recursively) {
                    def sub = it.path.replaceFirst(from, '')
                    copyRecursively(sub + '/')
                }
            } else {
                def dstFile = CopyFileFactory.get(dst, it.name)
                it.copyTo(dstFile)
            }
        }
    }
}
