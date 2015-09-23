package com.monochromeroad.gradle.plugin.aws.s3

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import spock.lang.Specification

/**
 * @author Masatoshi Hayashi
 */
class S3SyncSpec extends Specification {

    def "Loads the jets3t properties"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("sample")).build()
        def syncTask = project.task("sync", type: S3Sync) {
            configFile "jets3t.properties"

            accessKey = "dummy key"
            secretKey = "dummy sec key"
        }

        when:
        def prop = syncTask.jets3tProperties

        then:
        prop.getStringProperty("s3service.default-bucket-location", "default") == "Tokyo"
    }

    def "without a jets3t properties"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("sample")).build()
        def syncTask = project.task("syncWithDefaultProperties", type: S3Sync) {
            accessKey = "dummy key"
            secretKey = "dummy sec key"
        }

        when:
        def prop = syncTask.jets3tProperties

        then:
        prop.getStringProperty("s3service.default-bucket-location", "default") == "US"
    }
}
