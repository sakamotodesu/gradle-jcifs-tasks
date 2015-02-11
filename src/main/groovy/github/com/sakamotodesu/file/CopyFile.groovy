package github.com.sakamotodesu.file
/**
 * Abstract Copy File
 */
abstract class CopyFile {

    /**
     * copy self to destination file
     * @param dstFile destination file
     */
    def copyTo(CopyFile dstFile) {
        def bi = getBufferedInputStream()
        def bo = dstFile.getBufferedOutputStream()
        try {
            def buf = new byte[512]
            def len
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
    /**
     *
     * @return {@link BufferedInputStream}
     */
    abstract def BufferedInputStream getBufferedInputStream()

    /**
     *
     * @return {@link BufferedOutputStream}
     */
    abstract def BufferedOutputStream getBufferedOutputStream()

    /**
     *
     * @return true: directory, false: not directory
     */
    abstract def isDirectory()

    /**
     *
     * @return absolution file path
     */
    abstract def getPath()

    /**
     *
     * @return parent directory
     */
    abstract def getParent()

    /**
     *
     * @return filename
     */
    abstract def String getName()
    /**
     *
     * @return true: exists, false: not
     */
    abstract def exists()

    /**
     *
     * @return {@link CopyFile} list
     */
    abstract List<CopyFile> getFileList()

}
