package at.technikumwien.maps.data.remote.retrofit.response;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

/**
 * Created by FH on 09.10.2017.
 * Variablen werden durch Retrofit auf JSON gemappt
 */

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

        List<DrinkingFountain> drinkingFountainList = new ArrayList<DrinkingFountain>(features.size());

        for (DrinkingFountainItem current : features) {
            drinkingFountainList.add(DrinkingFountain.create(
                    current.id,
                    current.properties.NAME,
                    current.geometry.coordinates[1],
                    current.geometry.coordinates[0]
                    )
            );
        }

        return drinkingFountainList;
    }
}
