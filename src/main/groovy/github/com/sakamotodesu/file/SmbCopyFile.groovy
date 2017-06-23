package github.com.sakamotodesu.file

import jcifs.smb.SmbFile
import jcifs.smb.SmbFileInputStream
import jcifs.smb.SmbFileOutputStream

/**
 * a file on cifs server
 */
class SmbCopyFile extends CopyFile {

    SmbFile smbFile

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    BufferedInputStream getBufferedInputStream() {
        return new BufferedInputStream(new SmbFileInputStream(smbFile))
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    BufferedOutputStream getBufferedOutputStream() {
        return new BufferedOutputStream(new SmbFileOutputStream(smbFile))
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def isDirectory() {
        return smbFile.isDirectory()
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def getPath() {
        return smbFile.getPath()
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def getParent() {
        return new SmbCopyFile(smbFile: new SmbFile(smbFile.getParent()))
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def String getName() {
        return smbFile.getName()
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def exists() {
        return smbFile.exists()
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    List<CopyFile> getFileList() {
        def copyFileList = new ArrayList()
        if (smbFile.isDirectory()) {
            for (SmbFile file : smbFile.listFiles()) {
                copyFileList.add(new SmbCopyFile(smbFile: file))
            }
        } else {
            copyFileList.add(this)
        }
        return copyFileList
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def deleteDirectoryContents() {
        if (smbFile.isDirectory()) {
            for (SmbFile file : smbFile.listFiles()) {
                file.delete()
            }
        }
    }

    /**
     * mkdirs
     */
    @Override
    void mkdirs() {
        smbFile.mkdirs()
    }

    /**
     *
     * @return toString
     */
    @Override
    String toString() {
        return smbFile.toString()
    }
}
