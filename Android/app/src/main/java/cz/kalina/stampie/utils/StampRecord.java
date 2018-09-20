package cz.kalina.stampie.utils;

import java.util.Date;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class StampRecord implements ClusterItem {

    public Integer      stampId;
    public String       name;
    public String       category;
    public String       county;
    public Date         published;
    public String       sellingPlace1;
    public String       sellingPlace1Web;
    public String       sellingPlace2;
    public String       sellingPlace2Web;
    public String       sellingPlace3;
    public String       sellingPlace3Web;
    public String       sellingPlace4;
    public String       sellingPlace4Web;
    public String       sellingPlace5;
    public String       sellingPlace5Web;
    public String       sellingPlace6;
    public String       sellingPlace6Web;
    public String       sellingPlace7;
    public String       sellingPlace7Web;
    public String       sellingPlace8;
    public String       sellingPlace8Web;
    public String       sellingPlace9;
    public String       sellingPlace9Web;
    public String       sellingPlace10;
    public String       sellingPlace10Web;
    public String       sellingPlace11;
    public String       sellingPlace11Web;
    public Double       gpsPositionLat;
    public Double       gpsPositionLng;

    public LatLng getPosition() {
        return new LatLng(gpsPositionLat, gpsPositionLng);
    }

    public String getTitle() {
        return this.name;
    }

    public String getSnippet() {
        return this.name;
    }
}
