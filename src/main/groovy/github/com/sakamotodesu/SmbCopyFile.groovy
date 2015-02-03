package github.com.sakamotodesu

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
}
