package com.spawnai.middleware.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_model")
public class ModelConfiguration {

    @Id
    private String _id;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String emailId;

    private String username;
    private String modelName;
    private String token;
    private String lang;
    private String project;

    public ModelConfiguration() {

    }

    public ModelConfiguration(String _id, String emailId, String username, String modelName, String token, String lang, String project) {
        this._id = _id;
        this.emailId = emailId;
        this.username = username;
        this.modelName = modelName;
        this.token = token;
        this.lang = lang;
        this.project = project;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
