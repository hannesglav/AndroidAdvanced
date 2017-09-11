package at.technikumwien.maps.data.remote;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;

public class MockDrinkingFountainRepo implements DrinkingFountainRepo {

    @Override
    public void loadDrinkingFountains(OnDataLoadedCallback<List<DrinkingFountain>> callback) {
        List<DrinkingFountain> drinkingFountains = new ArrayList<>();
        drinkingFountains.add(DrinkingFountain.create("1", "Trinkbrunnen", 48.2914334776787, 16.41516492809119));
        drinkingFountains.add(DrinkingFountain.create("2", "Auslaufbrunnen", 48.21316356486897, 16.45054908161278));
        callback.onDataLoaded(drinkingFountains);
    }
}
