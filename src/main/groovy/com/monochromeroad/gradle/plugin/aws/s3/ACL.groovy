package com.monochromeroad.gradle.plugin.aws.s3

/**
 * Created with IntelliJ IDEA.
 * User: masatoshi
 * Date: 12/06/07
 * Time: 7:05
 * To change this template use File | Settings | File Templates.
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

