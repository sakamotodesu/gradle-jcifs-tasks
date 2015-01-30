package github.com.sakamotodesu

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
/**
 * Created by sakamoto on 2015/01/30.
 */
class JcifsTaskTest extends Specification {
    def "load the task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        def task = project.task('github.com.sakamotodesu.jcifs', type: github.com.sakamotodesu.JcifsTask)

        then:
        task instanceof github.com.sakamotodesu.JcifsTask
    }
}