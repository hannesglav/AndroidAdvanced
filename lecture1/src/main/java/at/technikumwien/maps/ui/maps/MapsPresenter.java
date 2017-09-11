package at.technikumwien.maps.ui.maps;


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainRepo;

public class MapsPresenter extends MvpBasePresenter<MapsView> {

    private final DrinkingFountainRepo drinkingFountainRepo;

    public MapsPresenter(AppDependencyManager dependencyManager) {
        drinkingFountainRepo = dependencyManager.getDrinkingFountainRepo();
    }

    public void loadDrinkingFountains() {
        drinkingFountainRepo.loadDrinkingFountains(new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if(isViewAttached()) {
                    getView().showDrinkingFountains(data);
                }
            }

            @Override
            public void onDataLoadError(Exception exception) {
                if(isViewAttached()) {
                    getView().showLoadingError(exception);
                }
            }
        });
    }

}
