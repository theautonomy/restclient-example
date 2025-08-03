package com.example.restclientdemo.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpBinResponse {
    private String url;
    private Map<String, String> headers;
    private Map<String, Object> args;
    private String data;
    private Map<String, Object> json;
    private Map<String, Object> form;
    private String origin;

    @JsonProperty("user-agent")
    private String userAgent;

    // Constructors
    public HttpBinResponse() {}

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String, Object> getJson() {
        return json;
    }

    public void setJson(Map<String, Object> json) {
        this.json = json;
    }

    public Map<String, Object> getForm() {
        return form;
    }

    public void setForm(Map<String, Object> form) {
        this.form = form;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "HttpBinResponse{"
                + "url='"
                + url
                + '\''
                + ", headers="
                + headers
                + ", args="
                + args
                + ", data='"
                + data
                + '\''
                + ", json="
                + json
                + ", form="
                + form
                + ", origin='"
                + origin
                + '\''
                + ", userAgent='"
                + userAgent
                + '\''
                + '}';
    }
}
