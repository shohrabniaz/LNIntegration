
package com.bjit.project_structure.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class ActivitiesBean {

    @SerializedName("ProjectCode")
    @JsonProperty("ProjectCode")
    private String projectCode;
    
    @SerializedName("Description")
    @JsonProperty("Description")
    private String description;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
