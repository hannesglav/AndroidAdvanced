package at.technikumwien.maps.ui.maps;

import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.NoOpOnOperationSuccessfulCallback;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MapsPresenter extends MvpBasePresenter<MapsView> {

    private final DrinkingFountainRepo drinkingFountainRepo;
    private final DrinkingFountainApi drinkingFountainApi;

    public MapsPresenter(AppDependencyManager manager) {
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
        drinkingFountainApi = manager.getDrinkingFountainApi();
    }

    public void loadDrinkingFountains() {
        drinkingFountainRepo.loadAll(new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if(data.isEmpty()) {
                    refreshDrinkingFountains();
                } else if(isViewAttached()) {
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

    public void refreshDrinkingFountains() {
        drinkingFountainApi.getDrinkingFountains().enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if(response.isSuccessful()) {
                    List<DrinkingFountain> drinkingFountainList = response.body().getDrinkingFountainList();
                    drinkingFountainRepo.refreshList(new NoOpOnOperationSuccessfulCallback(), drinkingFountainList);

                    if(isViewAttached()) {
                        getView().showDrinkingFountains(drinkingFountainList);
                    }
                } else {
                    if(isViewAttached()) {
                        getView().showLoadingError(new HttpException(response));
                    }
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable throwable) {
                if(isViewAttached()) {
                    getView().showLoadingError(throwable);
                }
            }
        });
    }
}
