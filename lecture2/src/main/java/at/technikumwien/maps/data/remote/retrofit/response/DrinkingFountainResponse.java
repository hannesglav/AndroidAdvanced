package at.technikumwien.maps.data.remote.retrofit.response;


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


    public List<DrinkingFountain> toDrinkingFountainList() {
        List<DrinkingFountain> drinkingFountainList = new ArrayList<>(features.size());

        for(DrinkingFountainItem i : features) {
            DrinkingFountain drinkingFountain = DrinkingFountain.create(
                    i.id,
                    i.properties.NAME,
                    i.geometry.coordinates[1],
                    i.geometry.coordinates[0]
            );
            drinkingFountainList.add(drinkingFountain);
        }

        return drinkingFountainList;
    }
}
