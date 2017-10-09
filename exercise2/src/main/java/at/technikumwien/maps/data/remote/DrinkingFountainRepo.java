package at.technikumwien.maps.data.remote;

import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;

public interface DrinkingFountainRepo {
    String BASE_URL = "https://data.wien.gv.at/daten/";
    String GET_DRINKING_FOUNTAINS_PATH = "geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:TRINKBRUNNENOGD&srsName=EPSG:4326&outputFormat=json";

    void loadDrinkingFountains(OnDataLoadedCallback<List<DrinkingFountain>> callback);
}
