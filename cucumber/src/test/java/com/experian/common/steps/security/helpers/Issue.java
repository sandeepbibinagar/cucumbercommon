/**
 * Copyright (c) Experian, 2017. All rights reserved.
 */
package com.experian.common.steps.security.helpers;

import java.util.Set;

/**
 * Class for parsing Burp Suite Enterprise reported issue.
 * Created by c01266a on 8/31/2017.
 *
 */
public class Issue {

    private String confidence;
    private String scan_id;
    private String serial_number;
    private String severity;
    private String type_index;
    private String path;
    private String origin;

    private IssueDefinition issueDefinition;

    public Issue(String confidence, String scan_id, String serial_number, String severity, String type_index, String path,
                 String origin) {
        this.confidence = confidence;
        this.scan_id = scan_id;
        this.serial_number = serial_number;
        this.severity = severity;
        this.type_index = type_index;
        this.path = path;
        this.origin = origin;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getScanId() {
        return scan_id;
    }

    public void setScanId(String scan_id) {
        this.scan_id = scan_id;
    }

    public String getSerialNumber() {
        return serial_number;
    }

    public void setSerialNumber(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getTypeIndex() {
        return type_index;
    }

    public void setTypeIndex(String type_index) {
        this.type_index = type_index;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public IssueDefinition getIssueDefinition() {
        return issueDefinition;
    }

    public void setIssueDefinition (String type_index, Set<IssueDefinition> issuesDefinitions) {

        if (issuesDefinitions != null) {
            for (IssueDefinition issueDefinition : issuesDefinitions) {
                if (issueDefinition.getIssueTypeId().equals(type_index)) {
                    this.issueDefinition = issueDefinition;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Issue [confidence=" + confidence + ", scan_id=" + scan_id + ", serial_number=" + serial_number
                + ", severity=" + severity + ", type_index=" + type_index + ", path=" + path + ", origin=" + origin
                + ", issueDefinition=" + issueDefinition + "]";
    }
}
