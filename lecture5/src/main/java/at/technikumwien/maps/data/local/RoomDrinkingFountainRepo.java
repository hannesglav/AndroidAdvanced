package at.technikumwien.maps.data.local;

import android.arch.lifecycle.LiveData;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.util.LoadDataAsyncTask;
import at.technikumwien.maps.util.OperationAsyncTask;

public class RoomDrinkingFountainRepo implements DrinkingFountainRepo {

    private final DrinkingFountainDao drinkingFountainDao;

    public RoomDrinkingFountainRepo(AppDependencyManager manager) {
        this.drinkingFountainDao = manager.getAppDatabase().drinkingFountainDao();
    }

    @Override
    public void refreshList(final OnOperationSuccessfulCallback callback, final List<DrinkingFountain> drinkingFountains) {
        new OperationAsyncTask(callback) {
            @Override public void doOperation() throws Throwable {
                drinkingFountainDao.deleteAll();
                drinkingFountainDao.insert(drinkingFountains);
            }
        }.execute();
    }

    @Override
    public void loadAll(OnDataLoadedCallback<List<DrinkingFountain>> callback) {
        new LoadDataAsyncTask<List<DrinkingFountain>>(callback) {
            @Override public List<DrinkingFountain> loadData() throws Throwable {
                return drinkingFountainDao.loadAll();
            }
        }.execute();
    }

    @Override
    public LiveData<List<DrinkingFountain>> loadAll() {
        return drinkingFountainDao.loadAllLiveData();
    }
}
