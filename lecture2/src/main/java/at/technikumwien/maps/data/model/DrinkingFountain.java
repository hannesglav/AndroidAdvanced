package at.technikumwien.maps.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DrinkingFountain {

    public abstract String id();
    public abstract String name();
    public abstract double lat();
    public abstract double lng();

    public static DrinkingFountain create(String id, String name, double lat, double lng) {
        return new AutoValue_DrinkingFountain(id, name, lat, lng);
    }

    public LatLng position() {
        return new LatLng(lat(), lng());
    }
}
