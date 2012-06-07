package com.monochromeroad.gradle.plugin.aws.s3

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

/**
 * @author Masatoshi Hayashi
 */
class S3SyncSpec extends spock.lang.Specification {

    def "Loads the synchronizer properties"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("sample")).build()
        def syncTask = project.task("sync", type: S3Sync) {
            configFile "synchronizer.properties"

            accessKey = "dummy key"
            secretKey = "dummy sec key"
        }

        when:
        def prop = syncTask.loadProperties()

        then:
        prop.getStringProperty("s3service.default-bucket-location", "default") == "Tokyo"
    }

}
