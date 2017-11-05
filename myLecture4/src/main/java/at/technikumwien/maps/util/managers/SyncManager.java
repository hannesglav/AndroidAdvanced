package at.technikumwien.maps.util.managers;

import android.util.Log;

import java.util.List;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.data.NoOpOnOperationSuccessfulCallback;
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

/**
 * Created by FH on 23.10.2017.
 */

public class SyncManager {

    private final DrinkingFountainApi drinkingFountainApi;
    private DrinkingFountainRepo drinkingFountainRepo;

    public SyncManager(AppDependencyManager manager) {
        drinkingFountainApi = manager.getDrinkingFountainApi();
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
    }

    public Cancelable syncDrinkingFountains(final OnOperationSuccessfulCallback callback,
                                            final OnDataLoadedCallback<List<DrinkingFountain>> dataLoadedCallback) {

        final Call<DrinkingFountainResponse> c = drinkingFountainApi.getDrinkingFountains();

        c.enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(Call<DrinkingFountainResponse> call, Response<DrinkingFountainResponse> response) {
                if (response.isSuccessful()){
                    List<DrinkingFountain> data = response.body().getDrinkingFountainList();
                    if (dataLoadedCallback!=null) {
                        dataLoadedCallback.onDataLoaded(data);
                    }
                    drinkingFountainRepo.refreshList(callback,data);

                } else {
                    if (dataLoadedCallback!=null) {
                        dataLoadedCallback.onDataLoadError(new HttpException(response));
                    }
                    callback.onOperationError(new HttpException(response));
                }
            }

            @Override
            public void onFailure(Call<DrinkingFountainResponse> call, Throwable throwable) {
                if (dataLoadedCallback!=null) {
                    dataLoadedCallback.onDataLoadError(throwable);
                }
                callback.onOperationError(throwable);
            }
        });

        return new CallCancelable(c);

    }



    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> dataLoadedCallback) {

        drinkingFountainRepo.loadAll(new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if (data.isEmpty()) {
                    Log.i("loadDrinkingFountains()","No DrinkingFountains loaded, SYNC starting");
                    syncDrinkingFountains(new NoOpOnOperationSuccessfulCallback(),dataLoadedCallback);
                } else {
                    Log.i("loadDrinkingFountains()","DrinkingFountains loaded");
                    dataLoadedCallback.onDataLoaded(data);
                }
            }

            @Override
            public void onDataLoadError(Throwable throwable) {
                dataLoadedCallback.onDataLoadError(throwable);
            }
        });

    }


    private static class CallCancelable implements Cancelable {
        private final Call<?> call;

        CallCancelable(Call<?> call) {
            this.call = call;
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public boolean isCanceled() {
            return call.isCanceled();
        }
    }
}
