/**
 * Copyright (c) Experian, 2017. All rights reserved.
 */
package com.experian.common.steps.security.helpers;

/**
 * Class for parsing Burp Suite Enterprise reported issue definition.
 * Created by c01266a on 8/31/2017.
 *
 */
public class IssueDefinition {

    private String issue_type_id;
    private String name;
    private String description;
    private String remediation;

    public IssueDefinition(String issue_type_id, String name, String description, String remediation) {
        this.issue_type_id = issue_type_id;
        this.name = name;
        this.description = description;
        this.remediation = remediation;
    }
    public String getIssueTypeId() {
        return issue_type_id;
    }
    public void setIssueTypeId(String issue_type_id) {
        this.issue_type_id = issue_type_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRemediation() {
        return remediation;
    }
    public void setRemediation(String remediation) {
        this.remediation = remediation;
    }

    @Override
    public String toString() {
        return "IssueDefinition [issue_type_id=" + issue_type_id + ", name=" + name + ", description=" + description
                + ", remediation=" + remediation + "]";
    }
}
