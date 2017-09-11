package at.technikumwien.maps.data.remote;

import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;

public interface DrinkingFountainRepo {
    void loadDrinkingFountains(OnDataLoadedCallback<List<DrinkingFountain>> callback);
}
