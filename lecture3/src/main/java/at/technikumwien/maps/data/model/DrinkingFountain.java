package at.technikumwien.maps.data.model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class DrinkingFountain {

    @NonNull
    private String id;
    @NonNull
    private String name;
    private double lat;
    private double lng;

    public DrinkingFountain() { }

    public DrinkingFountain(@NonNull String id, @NonNull String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public LatLng getPosition() {
        return new LatLng(getLat(), getLng());
    }
}
