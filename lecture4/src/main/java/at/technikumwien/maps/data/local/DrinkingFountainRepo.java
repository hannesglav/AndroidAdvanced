package at.technikumwien.maps.data.local;

import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;

public interface DrinkingFountainRepo {

    void refreshList(OnOperationSuccessfulCallback callback, List<DrinkingFountain> drinkingFountains);
    void loadAll(OnDataLoadedCallback<List<DrinkingFountain>> callback);

}
