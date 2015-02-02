package github.com.sakamotodesu

import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * JcifsTaskTest
 */
class JcifsTaskTest extends Specification {

    def "load the task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def task = project.task('github.com.sakamotodesu.jcifs', type: JcifsTask)
        task.doLast({
            from "C:\\Temp\\test.txt"
            into "D:\\Temp"
        })

        then:
        task instanceof JcifsTask
    }

    def "copy local to local directory"() {
        given:
        def project = ProjectBuilder.builder().build()
        def srcDir = new File(System.getProperty("java.io.tmpdir"), "srcDir")
        prepareDir(srcDir)
        def srcFile = new File(srcDir, "test.txt")
        if (!srcFile.exists()) {
            srcFile.createNewFile()
        }
        srcFile.write("bonjour")
        def dstDir = new File(System.getProperty("java.io.tmpdir"), "dstDir")
        prepareDir(dstDir)
        def dstFile = new File(dstDir, "test.txt")
        if (dstFile.exists() && !dstFile.delete()) {
            throw new IOException("Failed to delete : " + dstFile)
        }

        when:
        def task = project.task('github.com.sakamotodesu.jcifs', type: JcifsTask, {
            from srcFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
        })
        task.execute()

        then:
        dstFile.exists()
    }

    def "copy local to local file"() {
        given:
        def project = ProjectBuilder.builder().build()
        def srcDir = new File(System.getProperty("java.io.tmpdir"), "srcDir")
        prepareDir(srcDir)
        def srcFile = new File(srcDir, "test.txt")
        if (!srcFile.exists()) {
            srcFile.createNewFile()
        }
        srcFile.write("bonjour")
        def dstDir = new File(System.getProperty("java.io.tmpdir"), "dstDir")
        prepareDir(dstDir)
        def dstFile = new File(dstDir, "test.txt")
        if (dstFile.exists() && !dstFile.delete()) {
            throw new IOException("Failed to delete : " + dstFile)
        }

        when:
        def task = project.task('github.com.sakamotodesu.jcifs', type: JcifsTask, {
            from srcFile.getAbsolutePath()
            into dstFile.getAbsolutePath()
        })
        task.execute()

        then:
        def e = thrown(TaskExecutionException)
        e.getCause() instanceof InvalidUserDataException
    }

    def prepareDir(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to mkdirs : " + dir)
        }
    }


    def "from is empty"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def task = project.task('github.com.sakamotodesu.jcifs', type: JcifsTask, {
            into "C:\\Temp\\test.txt"
        })
        task.execute()

        then:
        def e = thrown(TaskExecutionException)
        e.getCause() instanceof InvalidUserDataException
    }

    def "into is empty"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def task = project.task('github.com.sakamotodesu.jcifs', type: JcifsTask, {
            from "C:\\Temp\\test.txt"
        })
        task.execute()

        then:
        def e = thrown(TaskExecutionException)
        e.getCause() instanceof InvalidUserDataException
    }


}