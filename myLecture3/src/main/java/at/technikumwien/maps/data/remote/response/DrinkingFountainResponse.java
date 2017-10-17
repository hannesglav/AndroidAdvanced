package at.technikumwien.maps.data.remote.response;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

public class DrinkingFountainResponse {

    public List<DrinkingFountainItem> features;

    private static class DrinkingFountainItem {
        public String id;
        public Geometry geometry;
        public Properties properties;
    }

    private static class Geometry {
        public double[] coordinates;
    }

    private static class Properties {
        public String NAME;
    }

    public List<DrinkingFountain> getDrinkingFountainList() {
        List<DrinkingFountain> drinkingFountainList = new ArrayList<>(features.size());

        for(DrinkingFountainItem i : features) {
            try {
                drinkingFountainList.add(new DrinkingFountain(i.id, i.properties.NAME, i.geometry.coordinates[1], i.geometry.coordinates[0]));
            } catch(Exception e) {
                Log.e("DrinkingFountainResp", "Could not create Drinking Fountain from response", e);
            }
        }

        return drinkingFountainList;
    }
}
