package github.com.sakamotodesu

import jcifs.Config
import jcifs.smb.SmbFile
import jcifs.smb.SmbFileInputStream
import jcifs.smb.SmbFileOutputStream
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

/**
 * Created by sakamoto on 2015/01/30.
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

        if (from.startsWith("smb://") && into.startsWith("smb://")) {
            // remote to remote
            SmbFile srcSmbFile = new SmbFile(from)
            SmbFile dstSmbDir = new SmbFile(into)
            if (!dstSmbDir.isDirectory()) {
                throw new InvalidUserDataException(dstSmbDir.getPath() + " is not Directory")
            }
            SmbFile dstSmbFile = new SmbFile(dstSmbDir, srcSmbFile.getName())
            srcSmbFile.copyTo(dstSmbFile)
        } else if (from.startsWith("smb://") && !into.startsWith("smb://")) {
            //remote to local
            SmbFile srcSmbFile = new SmbFile(from)
            File dstLocalDir = new File(into)
            if (!dstLocalDir.isDirectory()) {
                throw new InvalidUserDataException(dstLocalDir.getPath() + " is not Directory")
            }
            File dstLocalFile = new File(dstLocalDir, srcSmbFile.getName())
            copyFile(new BufferedInputStream(new SmbFileInputStream(srcSmbFile)), new BufferedOutputStream(new FileOutputStream(dstLocalFile)))
        } else if (!from.startsWith("smb://") && into.startsWith("smb://")) {
            // local to remote
            File srcLocalFile = new File(from)
            SmbFile dstSmbDir = new SmbFile(into)
            if (!dstSmbDir.isDirectory()) {
                throw new InvalidUserDataException(dstSmbDir.getPath() + " is not Directory")
            }
            SmbFile dstSmbFile = new SmbFile(dstSmbDir,srcLocalFile.getName())
            copyFile(new BufferedInputStream(new FileInputStream(srcLocalFile)), new BufferedOutputStream(new SmbFileOutputStream(dstSmbFile)))
        } else {
            // local to local
            File srcLocalFile = new File(from)
            File dstLocalDir = new File(into)
            if (!dstLocalDir.isDirectory()) {
                throw new InvalidUserDataException(dstLocalDir.getPath() + " is not Directory")
            }
            File dstLocalFile = new File(dstLocalDir,srcLocalFile.getName())
            if (dstLocalFile.exists()) {
                dstLocalFile.createNewFile()
            }
            copyFile(new BufferedInputStream(new FileInputStream(srcLocalFile)), new BufferedOutputStream(new FileOutputStream(dstLocalFile)))
        }

    }

    static def copyFile(BufferedInputStream bi, BufferedOutputStream bo) {
        try {
            byte[] buf = new byte[512]
            int len
            while ((len = bi.read(buf)) != -1) {
                bo.write(buf, 0, len)
            }
        } finally {
            if (bi != null) {
                bi.close()
            }
            if (bo != null) {
                bo.close()
            }
        }

    }

}
