package com.monochromeroad.gradle.plugin.aws.s3

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.PathValidation
import org.jets3t.apps.synchronize.Synchronize
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.Jets3tProperties
import org.jets3t.service.Constants

/**
 * Main task class for the plugin
 *
 * @author Masatoshi Hayashi
 */
class S3Sync extends DefaultTask {

    def accessKey

    def secretKey

    def quiet

    def noProgress

    def force

    def keepFiles

    def noDelete

    def gzipEnabled

    def encryptionEnabled

    def moveEnabled

    def batchMode

    def configFile

    ACL acl = ACL.Private
    
    String action = 'UP'

    ReportLevel reportLevel = ReportLevel.All

    private String originalSourcePath

    private File sourceDir

    private String destination
    
    void from(sourcePath) {
        originalSourcePath = sourcePath
        sourceDir = project.file(sourcePath)
    }

    void into(destinationPath) {
        destination = destinationPath.toString()
    }

    @TaskAction
    def sync() {
        def awsCredentials = new AWSCredentials(accessKey, secretKey)
        def s3Service = new RestS3Service(awsCredentials)

        Jets3tProperties properties = loadProperties()

        boolean doAction = true;
        boolean isQuiet = quiet == true;
        boolean isNoProgress = noProgress == true;
        boolean isForce = force == true;
        boolean isKeepFiles = keepFiles == true;
        boolean isNoDelete = noDelete == true;
        boolean isGzipEnabled = gzipEnabled == true;
        boolean isEncryptionEnabled = encryptionEnabled == true;
        boolean isMoveEnabled = moveEnabled == true;
        boolean isBatchMode = batchMode == true;
        String aclString = acl.toString()

        Synchronize client = new Synchronize(
                s3Service, doAction, isQuiet, isNoProgress, isForce, isKeepFiles, isNoDelete,
                isMoveEnabled, isBatchMode, isGzipEnabled, isEncryptionEnabled,
                reportLevel.level, properties);

        if (action == 'UP'){
            def sources = sourceDir.listFiles()
            if (sources) {
                client.run(destination, sources,
                        action,
                        properties.getStringProperty("password", null), aclString,
                        "S3");
            } else {
                logger.warn("No files found in given source directory '${sourceDir}'.")
            }
        }
        else if (action == 'DOWN'){
            project.file(destination).mkdirs()
            def dest = [new File(destination)]
            client.run(originalSourcePath, dest,
                        action,
                        properties.getStringProperty("password", null), aclString,
                        "S3");
        }
        else{
            logger.error("unkown action '${action}'")
        }

    }

    Jets3tProperties loadProperties() {
        Jets3tProperties myProperties =
            Jets3tProperties.getInstance(Constants.JETS3T_PROPERTIES_FILENAME);

        // Read the Synchronize properties file from the classpath
        File synchronizeProperties = project.file(configFile, PathValidation.FILE)
        if (synchronizeProperties.canRead()) {
            synchronizeProperties.withInputStream {
                myProperties.loadAndReplaceProperties(it, configFile + " in the user config")
            }
        } else {
            throw new IllegalStateException("the config file cannot be read : " + synchronizeProperties.absolutePath)
        }
        return myProperties
    }
}
