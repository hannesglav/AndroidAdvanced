package at.technikumwien.maps.ui.maps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.util.managers.SyncManager;

public class MapsPresenter extends MvpBasePresenter<MapsView> {

    private final DrinkingFountainApi drinkingFountainApi;
    private DrinkingFountainRepo drinkingFountainRepo;
    private SyncManager syncManager;

    private LiveData<List<DrinkingFountain>> liveData;
    private Observer<List<DrinkingFountain>> observer = new Observer<List<DrinkingFountain>>() {
        @Override
        public void onChanged(@Nullable List<DrinkingFountain> drinkingFountains) {
            Log.i("MapsPresenter", "Drinking fountain list changed, size="+drinkingFountains.size());
            if (isViewAttached()) {
                getView().showDrinkingFountains(drinkingFountains);
            }
        }
    };


    public MapsPresenter(AppDependencyManager manager) {
        drinkingFountainApi = manager.getDrinkingFountainApi();
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
        syncManager = manager.getSyncManager();
        liveData = manager.getDrinkingFountainRepo().loadAllWithChanges();
    }

    @Override
    public void attachView(MapsView view) {
        super.attachView(view);
        liveData.observeForever(observer);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        liveData.removeObserver(observer);
    }

    public void loadDrinkingFountains() {
        syncManager.loadDrinkingFountains(new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if(isViewAttached()) {
                    getView().showDrinkingFountains(data);
                }
            }

            @Override
            public void onDataLoadError(Throwable throwable) {
                if(isViewAttached()) {
                    getView().showLoadingError(throwable);
                }
            }
        });
    }
}
