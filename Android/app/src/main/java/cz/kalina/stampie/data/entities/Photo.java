package cz.kalina.stampie.data.entities;

import java.util.Calendar;

public class Photo extends BaseEntity {

    private String name;
    private String description;
    private String content;
    private long   dataLength;
    private double gpsPositionLat;
    private double gpsPositionLng;
    private User creator;
    private Calendar created;

    public Photo() {
    }

    public Photo(String name, String description, String content,long dataLength, double gpsPositionLat, double gpsPositionLng, User creator, Calendar created) {
        this.name = name;
        this.description = description;
        this.content = content;
        this.dataLength = dataLength;
        this.gpsPositionLat = gpsPositionLat;
        this.gpsPositionLng = gpsPositionLng;
        this.creator = creator;
        this.created = created;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDataLength() {
        return dataLength;
    }

    public void setDataLength(long dataLength) {
        this.dataLength = dataLength;
    }

    public double getGpsPositionLat() {
        return gpsPositionLat;
    }

    public void setGpsPositionLat(double gpsPositionLat) {
        this.gpsPositionLat = gpsPositionLat;
    }

    public double getGpsPositionLng() {
        return gpsPositionLng;
    }

    public void setGpsPositionLng(double gpsPositionLng) {
        this.gpsPositionLng = gpsPositionLng;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Photo [id=" + id + ", name=" + name + ", description=" + description + ", content=" + content + ", dataLength=" + dataLength +
                ", gpsPositionLat=" + gpsPositionLat + ", gpsPositionLng=" + gpsPositionLng + ", creator=" +
                ((creator != null) ? creator : "null") + ", created=" + ((created != null) ? created : "null") + "]";
    }
}
