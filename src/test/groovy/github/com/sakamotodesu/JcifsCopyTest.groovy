package github.com.sakamotodesu

import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * JcifsTaskTest
 */
class JcifsCopyTest extends Specification {

    def "load the task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def task = project.task('test', type: JcifsCopy)
        task.doLast({
            from "C:\\Temp\\test.txt"
            into "D:\\Temp"
        })

        then:
        task instanceof JcifsCopy
    }


    def "copy local file to local directory"() {
        given:
        def project = ProjectBuilder.builder().build()
        def srcDir = prepareDir("srcDir")
        def srcFile = new File(srcDir, "test.txt")
        srcFile.createNewFile()
        def dstDir = prepareDir("dstDir")
        def dstFile = new File(dstDir, "test.txt")


        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
        })
        task.execute()

        then:
        dstFile.exists()
    }

    def "copy local file to local nest directory"() {
        given:
        def project = ProjectBuilder.builder().build()
        def srcDir = prepareDir("srcDir")
        def srcFile = new File(srcDir, "test.txt")
        srcFile.createNewFile()
        def dstDir = prepareDir("dstDir/dstDir/dstDir")
        def dstFile = new File(dstDir, "test.txt")


        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
        })
        task.execute()

        then:
        dstFile.exists()
    }

    def "copy local file to local file"() {
        given:
        def project = ProjectBuilder.builder().build()
        def srcDir = prepareDir("srcDir")
        def srcFile = new File(srcDir, "test.txt")
        srcFile.createNewFile()
        def dstDir = prepareDir("dstDir")
        def dstFile = new File(dstDir, "test.txt")

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcFile.getAbsolutePath()
            into dstFile.getAbsolutePath()
        })
        task.execute()

        then:
        new File(dstFile.getAbsolutePath(),srcFile.getName()).exists()
    }


    def srcDir
    def dstDir
    def srcTextFile
    def srcText2File
    def srcZipFile
    def dstTextFile
    def dstText2File
    def dstZipFile

    def setup() {
        srcDir = prepareDir("srcDir")
        dstDir = prepareDir("dstDir")
        srcTextFile = new File(srcDir, "text.txt")
        srcTextFile.createNewFile()
        srcText2File = new File(srcDir, "text2.txt")
        srcText2File.createNewFile()
        srcZipFile = new File(srcDir, "test.zip")
        srcZipFile.createNewFile()
        dstTextFile = new File(dstDir, srcTextFile.getName())
        dstText2File = new File(dstDir, srcText2File.getName())
        dstZipFile = new File(dstDir, srcZipFile.getName())
    }

    def prepareDir(String dir) {
        def target = new File(System.getProperty("java.io.tmpdir"), dir)
        if (target.exists() && !target.deleteDir()) {
            throw new IOException("Failed to deleteDir : " + target)
        }
        if (!target.mkdirs()) {
            throw new IOException("Failed to mkdirs : " + target)
        } 
        target
    }


    def "copy local file to local directory (include target)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcTextFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
            include '.*\\.txt$'
        })
        task.execute()

        then:
        dstTextFile.exists()
        !dstText2File.exists()
        !dstZipFile.exists()
    }

    def "copy local file to local directory (exclude target)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcTextFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
            exclude '.*\\.txt$'
        })
        task.execute()

        then:
        !dstTextFile.exists()
        !dstText2File.exists()
        !dstZipFile.exists()
    }

    def "copy local file to local directory (not exclude target)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcTextFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
            exclude '.*\\.gzip$'
        })
        task.execute()

        then:
        dstTextFile.exists()
        !dstText2File.exists()
        !dstZipFile.exists()
    }

    def "copy local directory to local directory"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcDir.getAbsolutePath()
            into dstDir.getAbsolutePath()
        })
        task.execute()

        then:
        dstTextFile.exists()
        dstText2File.exists()
        dstZipFile.exists()
    }

    def "copy local directory to local directory (include)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcDir.getAbsolutePath()
            into dstDir.getAbsolutePath()
            include '.*\\.txt$'
        })
        task.execute()

        then:
        dstTextFile.exists()
        dstText2File.exists()
        !dstZipFile.exists()
    }

    def "copy local directory to local directory (not include target)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcDir.getAbsolutePath()
            into dstDir.getAbsolutePath()
            include '.*\\.gzip$'
        })
        task.execute()

        then:
        !dstTextFile.exists()
        !dstText2File.exists()
        !dstZipFile.exists()
    }

    def "copy local directory to local directory (exclude)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcDir.getAbsolutePath()
            into dstDir.getAbsolutePath()
            exclude '.*\\.txt$'
        })
        task.execute()

        then:
        !dstTextFile.exists()
        !dstText2File.exists()
        dstZipFile.exists()
    }

    def "copy local directory to local directory (not exclude target)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcDir.getAbsolutePath()
            into dstDir.getAbsolutePath()
            exclude '.*\\.gzip$'
        })
        task.execute()

        then:
        dstTextFile.exists()
        dstText2File.exists()
        dstZipFile.exists()
    }

    def "copy local directory to local directory (include and exclude)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcDir.getAbsolutePath()
            into dstDir.getAbsolutePath()
            include '.*\\.zip$'
            exclude '.*\\.txt$'
        })
        task.execute()

        then:
        !dstTextFile.exists()
        !dstText2File.exists()
        dstZipFile.exists()
    }

    def "copy local file to local directory (not include target)"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcTextFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
            include '.*\\.gzip$'
        })
        task.execute()

        then:
        !dstTextFile.exists()
        !dstText2File.exists()
        !dstZipFile.exists()
    }

    def "from is empty"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def task = project.task('test', type: JcifsCopy, {
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
        def task = project.task('test', type: JcifsCopy, {
            from "C:\\Temp\\test.txt"
        })
        task.execute()

        then:
        def e = thrown(TaskExecutionException)
        e.getCause() instanceof InvalidUserDataException
    }

    def "empty dest dir before copy"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()
        def dstOldFile = new File(dstDir, "old.txt")
        dstOldFile.createNewFile()
        
        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcTextFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
            cleanBefore dstDir.getAbsolutePath()
        })
        task.execute()
        
        then:
        !dstOldFile.exists()
        dstTextFile.exists()
    }

    def "empty src dir after copy"() {
        given:
        def project = ProjectBuilder.builder().build()
        setup()

        when:
        def task = project.task('test', type: JcifsCopy, {
            from srcTextFile.getAbsolutePath()
            into dstDir.getAbsolutePath()
            cleanAfter srcDir.getAbsolutePath()
        })
        task.execute()

        then:
        srcDir.exists()
        !srcTextFile.exists()
        !srcText2File.exists()
        !srcZipFile.exists()
    }

}
