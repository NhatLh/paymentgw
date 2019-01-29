
package com.digitechlabs.paymentgw.paypal.callback;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaypalCallbackTask {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("event_version")
    @Expose
    private String eventVersion;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("resource")
    @Expose
    private Resource resource;
    @SerializedName("links")
    @Expose
    private List<Link_> links = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(String eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public List<Link_> getLinks() {
        return links;
    }

    public void setLinks(List<Link_> links) {
        this.links = links;
    }

}
