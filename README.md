[![Build Status](https://travis-ci.org/literalice/gradle-aws-s3-sync.svg?branch=master)](https://travis-ci.org/literalice/gradle-aws-s3-sync)

This is the gradle task for synchronizing a local directory with a AWS S3 bucket and vice-versa.

This task depends on [JetS3t](http://jets3t.s3.amazonaws.com/index.html), a open-source Java toolkit for AWS.

## Situation - upload

This task assumes the case, for example, a web site would be published in a AWS S3 bucket.

## Usage - upload

    // Gradle Script
    buildscript {
        repositories {
            jcenter()
        }

        dependencies {
            classpath "com.monochromeroad.gradle-plugins:gradle-aws-s3-sync:0.8"
        }
    }

    import com.monochromeroad.gradle.plugin.aws.s3.S3Sync

    task deploy(type: S3Sync){
        description = "Deploys my site on a s3 bucket."

        accessKey awsAccessKey
        secretKey awsSecretKey

        configFile "synchronizer.properties"

        from "local-site"
        into "my.bucket.name/subdirectory-optional"
    }

## Situation - download

This task assumes the case, for example, a set of files has to be fetched from an S3 bucket to a local directory.

### Usage - download

    // Gradle Script
    buildscript {
        repositories {
            jcenter()
        }

        dependencies {
            classpath "com.monochromeroad.gradle-plugins:gradle-aws-s3-sync:0.5"
        }
    }

    import com.monochromeroad.gradle.plugin.aws.s3.S3Sync

    task deploy(type: S3Sync){
        description = "Downloads files from s3 bucket to a local directory"

        accessKey awsAccessKey
        secretKey awsSecretKey

        configFile "synchronizer.properties"
        
        // follows the jets3t conventions for action names
        action = 'DOWN'

        from "my.bucket.name/subdirectory-optional"
        from "local-site"
    }


# Options

<table>
    <tr>
        <th>Name <b>* required</b></th>
        <th>Description</th>
        <th>Default Value</th>
    </tr>
    <tr>
        <td><b>* accessKey()</b></td>
        <td>AWS Access Key</td>
        <td>-</td>
    </tr>
    <tr>
        <td><b>* secretKey()</b></td>
        <td>AWS Secret Key</td>
        <td>-</td>
    </tr>
    <tr>
        <td><b>* from()</b></td>
        <td>The local directory which would be synchronized with the S3 bucket</td>
        <td>-</td>
    </tr>
    <tr>
        <td><b>* into()</b></td>
        <td>The S3 bucket name which would be synchronized with the local directory. If needed, some sub directory could be added. (e.g. buc.ket/sub.d)</td>
        <td>-</td>
    </tr>
    <tr>
        <td>configFile()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/toolkit/configuration.html">JetS3t properties file</a> path. Interpreted relative to the project directory, as for project.file() method.</td>
        <td>-<br />See also: <a href="http://jets3t.s3.amazonaws.com/toolkit/configuration.html">JetS3t's Default</a></td>
    </tr>
    <tr>
        <td>quiet()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -q</td>
        <td>false</td>
    </tr>
    <tr>
        <td>noProgress()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -n</td>
        <td>false</td>
    </tr>
    <tr>
        <td>force()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -f</td>
        <td>false</td>
    </tr>
    <tr>
        <td>keepFiles()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -k</td>
        <td>false</td>
    </tr>
    <tr>
        <td>noDelete()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -d</td>
        <td>false</td>
    </tr>
    <tr>
        <td>moveEnabled()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -m</td>
        <td>false</td>
    </tr>
    <tr>
        <td>batchMode()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -b</td>
        <td>false</td>
    </tr>
    <tr>
        <td>gzipEnabled()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -g</td>
        <td>false</td>
    </tr>
    <tr>
        <td>encryptionEnabled()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> -c</td>
        <td>false</td>
    </tr>
    <tr>
        <td>acl()</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> --acl, from enum "ACL"</td>
        <td>com.monochromeroad.gradle.plugin.aws.s3.<b>ACL.Private</b></td>
    </tr>
    <tr>
        <td>reportLevel(ReportLevel)</td>
        <td><a href="http://jets3t.s3.amazonaws.com/applications/synchronize.html">JetS3t option</a> --reportlevel, from enum "ReportLevel"</td>
        <td>com.monochromeroad.gradle.plugin.aws.s3.<b>ReportLevel.All</b></td>
    </tr>
    <tr>
        <td>action()</td>
        <td>Either 'UP' for uploading into S3 or 'DOWN' for downloading from S3.</td>
        <td>UP</td>
    </tr>

</table>

