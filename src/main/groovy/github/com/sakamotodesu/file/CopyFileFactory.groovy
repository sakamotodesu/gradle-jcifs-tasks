package github.com.sakamotodesu.file

import jcifs.smb.SmbFile
import org.gradle.api.InvalidUserDataException

/**
 * factory for {@link github.com.sakamotodesu.file.CopyFile}
 */
class CopyFileFactory {

    def static CopyFile get(String filePath) {
        if (filePath.startsWith("smb://")) {
            new SmbCopyFile(smbFile: new SmbFile(filePath))
        } else {
            new LocalCopyFile(localFile: new File(filePath))
        }
    }

    def static CopyFile get(CopyFile parent, String filename) {
        if (parent instanceof SmbCopyFile) {
            new SmbCopyFile(smbFile: new SmbFile(parent.getSmbFile(), filename))
        } else if (parent instanceof LocalCopyFile) {
            new LocalCopyFile(localFile: new File(parent.getLocalFile(), filename))
        } else {
            throw new InvalidUserDataException("Invalid type : " + parent.class.name)
        }
    }
}
