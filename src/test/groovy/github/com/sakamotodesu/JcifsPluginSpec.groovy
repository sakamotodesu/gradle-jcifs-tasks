package github.com.sakamotodesu

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class JcifsPluginSpec extends Specification {

    def "apply() should load the plugin"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'github.com.sakamotodesu.jcifs'
        }

        then:
        project.plugins.hasPlugin(JcifsPlugin)
    }

}
