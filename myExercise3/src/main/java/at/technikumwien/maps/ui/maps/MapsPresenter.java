package at.technikumwien.maps.ui.maps;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.io.IOException;
import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.OnOperationSuccessfulCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MapsPresenter extends MvpBasePresenter<MapsView> {

    private final DrinkingFountainApi drinkingFountainApi;
    private final DrinkingFountainRepo drinkingFountainRepo;
    private boolean localRepoUsed;

    public MapsPresenter(AppDependencyManager manager) {
        drinkingFountainApi = manager.getDrinkingFountainApi();
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
    }

    public void updateCounter(String id) {
        drinkingFountainRepo.incrementCounter(new OnOperationSuccessfulCallback() {
            @Override
            public void onOperationSuccessful() {

            }

            @Override
            public void onOperationError(Throwable throwable) {

            }
        },id);
    }

    public void loadDrinkingFountains() {

        loadFromLocalStorage();
    }

    private void saveDataInRepo(List<DrinkingFountain> drinkingFountainList) {
        drinkingFountainRepo.refreshList(new OnOperationSuccessfulCallback() {
            @Override
            public void onOperationSuccessful() {
                if(isViewAttached()) {
                    getView().showLocalStorageSuccess();
                }
            }

            @Override
            public void onOperationError(Throwable throwable) {
                if(isViewAttached()) {
                    getView().showLoadingError(new IOException("No local storage", throwable));
                }
            }
        }, drinkingFountainList);

    }

    private void loadFromLocalStorage() {
        drinkingFountainRepo.loadAll(new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if (data.size()==0) {
                    loadFromRemote();
                }
                else {
                    if(isViewAttached()) {
                        getView().showDrinkingFountains(data);
                    }
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

    private void loadFromRemote() {
        drinkingFountainApi.getDrinkingFountains().enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if (response.isSuccessful()) {
                    if (isViewAttached()) {
                        getView().showDrinkingFountains(response.body().getDrinkingFountainList());
                    }
                    saveDataInRepo(response.body().getDrinkingFountainList());
                } else {
                    if (isViewAttached()) {
                        getView().showLoadingError(new HttpException(response));

                    }
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable throwable) {
                if (isViewAttached()) {
                    getView().showLoadingError(throwable);
                }
            }
        });
    }

}
