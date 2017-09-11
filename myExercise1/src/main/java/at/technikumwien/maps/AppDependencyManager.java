package at.technikumwien.maps;

import android.content.Context;
import android.content.res.Resources;

import at.technikumwien.maps.data.remote.DrinkingFountainRepo;
import at.technikumwien.maps.data.remote.MockDrinkingFountainRepo;

public class AppDependencyManager {

    private final Context appContext;
    private DrinkingFountainRepo drinkingFountainRepo;

    public AppDependencyManager(Context appContext) {
        this.appContext = appContext;
    }

    public Context getAppContext() {
        return appContext;
    }

    public Resources getResources() {
        return appContext.getResources();
    }

    public DrinkingFountainRepo getDrinkingFountainRepo() {
        if(drinkingFountainRepo == null) { drinkingFountainRepo = new MockDrinkingFountainRepo(); }
        return drinkingFountainRepo;
    }
}
