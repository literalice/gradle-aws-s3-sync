package com.monochromeroad.gradle.plugin.aws.s3

/**
 * @author Masatoshi Hayashi
 */
public enum ReportLevel {

    None(0),

    Actions(1),

    Differences(2),

    All(3);

    private Integer level;

    private ReportLevel(int level) {
        this.level = level
    }

    public Integer getLevel() {
        return level
    }

}
