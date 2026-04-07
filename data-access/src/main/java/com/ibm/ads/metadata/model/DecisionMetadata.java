package com.ibm.ads.metadata.model;

import java.util.Map;

import ilog.rules.bom.annotations.NotVerbalized;

/**
 * Represents a decision with its metadata
 */
public class DecisionMetadata {
    private String id;
    private String name;
    private String description;
    private String version;
    private Map<String, Object> properties;

    @NotVerbalized
    public DecisionMetadata() {
    }

    @NotVerbalized
    public DecisionMetadata(String id, String name, String description, String version, Map<String, Object> properties) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.properties = properties;
    }

    @NotVerbalized
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotVerbalized
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotVerbalized
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotVerbalized
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @NotVerbalized
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    @NotVerbalized
    public String toString() {
        return "Decision{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", properties=" + properties +
                '}';
    }
}
