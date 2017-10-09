package at.technikumwien.maps.data.remote;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.retrofit.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.retrofit.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by FH on 09.10.2017.
 * umständliches mappen von einem callback zu einem anderen
 * wenn nur retrofti verwendet wird nicht nötig
 */

public class RetrofitDrinkingFountainRepo implements DrinkingFountainRepo {

    private final DrinkingFountainApi drinkingFountainApi;

    public RetrofitDrinkingFountainRepo(AppDependencyManager manager) {
        drinkingFountainApi = manager.getDrinkingFountainApi();
    }


    @Override
    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> callback) {
        drinkingFountainApi.getDrinkingFountains().enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataLoaded(response.body().toDrinkingFountainList());
                } else {
                    callback.onDataLoadError(new HttpException(response));
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable throwable) {
                callback.onDataLoadError(throwable);
            }
        });
    }
}
