package at.technikumwien.maps.ui.maps;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class MapsPresenter extends MvpBasePresenter<MapsView> {

    private final DrinkingFountainApi drinkingFountainApi;

    public MapsPresenter(AppDependencyManager manager) {
        drinkingFountainApi = manager.getDrinkingFountainApi();
    }

    public void loadDrinkingFountains() {
        drinkingFountainApi.getDrinkingFountains().enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if(response.isSuccessful()) {
                    if(isViewAttached()) {
                        getView().showDrinkingFountains(response.body().getDrinkingFountainList());
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
