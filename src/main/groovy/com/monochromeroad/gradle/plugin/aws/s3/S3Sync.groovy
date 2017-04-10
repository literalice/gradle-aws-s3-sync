package com.monochromeroad.gradle.plugin.aws.s3

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.PathValidation
import org.jets3t.apps.synchronize.Synchronize
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.Jets3tProperties
import org.jets3t.service.utils.Mimetypes

/**
 * Main task class for the plugin
 *
 * @author Masatoshi Hayashi
 */
class S3Sync extends DefaultTask {

    private static final DEFAULT_JETS3T_PROPERTIES_NAME = "default.jets3t.properties"

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

    Jets3tProperties jets3tProperties = createNewJets3tProperties(DEFAULT_JETS3T_PROPERTIES_NAME)

    ACL acl = ACL.Private
    
    String action = 'UP'

    ReportLevel reportLevel = ReportLevel.All

    private _configFile

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
                reportLevel.level, jets3tProperties);

        if (action == 'UP'){
            final useMD5 = jets3tProperties.getBoolProperty("filecomparer.use-md5-files", false);
            if (useMD5) {
                new File(jets3tProperties.getStringProperty("filecomparer.md5-files-root-dir", "")).mkdirs();
            }
            def sources = sourceDir.listFiles()
            if (sources) {
                client.run(destination, sources,
                        action,
                        jets3tProperties.getStringProperty("password", null), aclString,
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
                        jets3tProperties.getStringProperty("password", null), aclString,
                        "S3");
        }
        else{
            logger.error("unkown action '${action}'")
        }

    }

    def getConfigFile() { return _configFile }

    void configFile(configFile) {
        this._configFile = configFile

        if (!configFile) return

        this.jets3tProperties =
                loadJets3tProperties(project.file(configFile, PathValidation.FILE))
    }

    void mimeTypesFile(mimeFile) {
        project.file(mimeFile).withInputStream {
            Mimetypes.instance.loadAndReplaceMimetypes(it)
        }
    }

    private static Jets3tProperties loadJets3tProperties(File configFile) {
        String identifier = 'user-jets3t-props' + configFile.toString()

        Jets3tProperties newJets3tProperties = createNewJets3tProperties(identifier)

        if (configFile.canRead()) {
            configFile.withInputStream {
                newJets3tProperties.loadAndReplaceProperties(it, identifier)
            }
        } else {
            throw new IllegalStateException("the config file cannot be read : " + configFile.absolutePath)
        }

        return newJets3tProperties
    }

    private static Jets3tProperties createNewJets3tProperties(String identifier) {
        S3Sync.class.getResourceAsStream("/" + DEFAULT_JETS3T_PROPERTIES_NAME).withStream {
            return Jets3tProperties.getInstance(it, identifier)
        }
    }
}
