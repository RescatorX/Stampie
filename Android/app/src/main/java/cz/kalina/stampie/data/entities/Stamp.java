package cz.kalina.stampie.data.entities;

import java.util.Date;

import cz.kalina.stampie.utils.StampRecord;

public class Stamp extends BaseEntity {

    private String      name;
    private String      category;
    private String      county;
    private Date        published;
    private String      sellingPlace1;
    private String      sellingPlace1Web;
    private String      sellingPlace2;
    private String      sellingPlace2Web;
    private String      sellingPlace3;
    private String      sellingPlace3Web;
    private String      sellingPlace4;
    private String      sellingPlace4Web;
    private String      sellingPlace5;
    private String      sellingPlace5Web;
    private String      sellingPlace6;
    private String      sellingPlace6Web;
    private String      sellingPlace7;
    private String      sellingPlace7Web;
    private String      sellingPlace8;
    private String      sellingPlace8Web;
    private String      sellingPlace9;
    private String      sellingPlace9Web;
    private String      sellingPlace10;
    private String      sellingPlace10Web;
    private String      sellingPlace11;
    private String      sellingPlace11Web;
    private Double      gpsPositionLat;
    private Double      gpsPositionLng;

    public Stamp() {
    }

    public Stamp(String name, String category, String county, Date published, Double gpsPositionLat, Double gpsPositionLng) {
        this.name = name;
        this.category = category;
        this.county = county;
        this.published = published;
        this.gpsPositionLat = gpsPositionLat;
        this.gpsPositionLng = gpsPositionLng;
    }

    public Stamp(StampRecord stampRecord) {
        this.name = stampRecord.name;
        this.category = stampRecord.category;
        this.county = stampRecord.county;
        this.published = stampRecord.published;
        this.gpsPositionLat = stampRecord.gpsPositionLat;
        this.gpsPositionLng = stampRecord.gpsPositionLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
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

    @Override
    public String toString() {
        return "Stamp [id=" + id + ", name=" + name + ", category=" + category + ", county=" + county + ", published=" + published.toString() +
                ", gpsPositionLat=" + gpsPositionLat + ", gpsPositionLng=" + gpsPositionLng + "]";
    }
}
