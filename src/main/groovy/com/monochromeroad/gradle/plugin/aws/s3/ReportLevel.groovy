package com.monochromeroad.gradle.plugin.aws.s3

/**
 * Options for how much report information will be printed.
 *
 * @author Masatoshi Hayashi
 */
public enum ReportLevel {

    /** no report items will be printed (the summary will still be printed) */
    None(0),

    /** only actions are reported */
    Actions(1),

    /** differences & actions are reported */
    Differences(2),

    /** all items are reported */
    All(3);

    private Integer level;

    private ReportLevel(int level) {
        this.level = level
    }

    public Integer getLevel() {
        return level
    }

}
