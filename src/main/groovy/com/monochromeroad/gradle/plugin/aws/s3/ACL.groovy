package com.monochromeroad.gradle.plugin.aws.s3

/**
 * Access Control List (ACL) for Amazon S3
 *
 * @author Masatoshi Hayashi
 */
public enum ACL {

    PublicRead("PUBLIC_READ"),

    PublicReadWrite("PUBLIC_READ_WRITE"),

    Private("PRIVATE");

    private string

    private ACL(String aclString) {
        this.string = aclString
    }

    String toString() {
        return string;
    }

}

