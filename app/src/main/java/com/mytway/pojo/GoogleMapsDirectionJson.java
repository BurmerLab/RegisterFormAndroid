package com.mytway.pojo;

public class GoogleMapsDirectionJson {

    public static final String    TAG_ID          = "id";
    public static final String    TAG_LAT         = "lat";
    public static final String    TAG_LNG         = "lng";
    public static final String    TAG_ROUTES      = "routes";
    public static final String    TAG_LEGS        = "legs";
    public static final String    TAG_STEPS       = "steps";
    public static final String    TAG_POLYLINE    = "polyline";
    public static final String    TAG_POINTS      = "points";
    public static final String    TAG_START       = "start_location";
    public static final String    TAG_END         = "end_location";
    public static final String    TAG_DISTANCE    = "distance";
    public static final String    TAG_DISTANCE_TEXT    = "text";
    public static final String    TAG_DISTANCE_VALUE    = "value";

    public static final String    TAG_DURATION    = "duration";
    public static final String    TAG_DURATION_TEXT    = "text";
    public static final String    TAG_DURATION_VALUE    = "value";

    private String id;
    private Double latitude;
    private Double longitude;
    private String routes;
    private Legs legs;
    private String steps;
    private String polyline;
    private String points;
    private String startLocation;
    private String endLocation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public Legs getLegs() {
        return legs;
    }

    public void setLegs(Legs legs) {
        this.legs = legs;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
}
