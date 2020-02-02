package github.com.sakamotodesu.file
/**
 * a file on local server
 */
class LocalCopyFile extends CopyFile {

    File localFile

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    BufferedInputStream getBufferedInputStream() {
        return new BufferedInputStream(new FileInputStream(localFile))
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    BufferedOutputStream getBufferedOutputStream() {
        return new BufferedOutputStream(new FileOutputStream(localFile))
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def isDirectory() {
        return localFile.isDirectory()
    }
    
    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def getPath() {
        return localFile.getAbsolutePath()
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def getParent() {
        return new LocalCopyFile(localFile: localFile.getParentFile())
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def String getName() {
        return localFile.getName()
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    def exists() {
        return localFile.exists()
    }

    /**
     *
     * @return {@inheritDoc}
     */
    @Override
    List<CopyFile> getFileList() {
        def copyFileList = new ArrayList()
        if (localFile.isDirectory()) {
            for (File file : localFile.listFiles()) {
                copyFileList.add(new LocalCopyFile(localFile: file))
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
        if (localFile.isDirectory()) {
            for (File file : localFile.listFiles()) {
                file.directory ? file.deleteDir() : file.delete()
            }
        }
    }

    /**
     * mkdirs
     */
    @Override
    void mkdirs() {
        if(!localFile.mkdirs()){
            throw new IOException('Failed to mkdirs ' + localFile)
        }
    }

    /**
     *
     * @return toString
     */
    @Override
    String toString() {
        return localFile.toString()
    }
}
