package at.technikumwien.maps.util.managers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
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
import at.technikumwien.maps.service.SyncDrinkingFountainsJobService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class SyncManager {

    private static String TAG = SyncManager.class.getSimpleName();
    private static int SYNC_JOB_ID = 32561289;

    private final Context context;
    private final DrinkingFountainRepo drinkingFountainRepo;
    private final DrinkingFountainApi drinkingFountainApi;

    public SyncManager(AppDependencyManager manager) {
        context = manager.getAppContext();
        drinkingFountainApi = manager.getDrinkingFountainApi();
        drinkingFountainRepo = manager.getDrinkingFountainRepo();
    }

    public void schedulePeriodicSync() {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder(SYNC_JOB_ID, new ComponentName(context, SyncDrinkingFountainsJobService.class))
                .setPeriodic(7L * 24L * 60L * 60L * 1000L) // Weekly sync
                .setPersisted(true) // Don't forget to add RECEIVE_BOOT_COMPLETED permission!
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(true)
                .setRequiresDeviceIdle(true)
                .build();

        jobScheduler.schedule(jobInfo);

        Log.i(TAG, "SyncJobService scheduled for periodic execution");
    }

    public Cancelable syncDrinkingFountains(@NonNull final OnOperationSuccessfulCallback callback) {
        return syncDrinkingFountains(callback, null);
    }

    public Cancelable syncDrinkingFountains(@NonNull  final OnOperationSuccessfulCallback callback, final OnDataLoadedCallback<List<DrinkingFountain>> loadedCallback) {
        final Call<DrinkingFountainResponse> c = drinkingFountainApi.getDrinkingFountains();

        c.enqueue(new Callback<DrinkingFountainResponse>() {
            @Override
            public void onResponse(@NonNull Call<DrinkingFountainResponse> call, @NonNull Response<DrinkingFountainResponse> response) {
                if(response.isSuccessful()) {
                    List<DrinkingFountain> drinkingFountains = response.body().getDrinkingFountainList();
                    if(loadedCallback != null) { loadedCallback.onDataLoaded(drinkingFountains); }
                    drinkingFountainRepo.refreshList(callback, drinkingFountains);
                } else {
                    HttpException exception = new HttpException(response);
                    callback.onOperationError(exception);
                    if(loadedCallback != null) { loadedCallback.onDataLoadError(exception); }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DrinkingFountainResponse> call, @NonNull Throwable throwable) {
                callback.onOperationError(throwable);
                if(loadedCallback != null) { loadedCallback.onDataLoadError(throwable); }
            }
        });


        return new CallCancelable(c);
    }

    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> callback) {
        drinkingFountainRepo.loadAll(new OnDataLoadedCallback<List<DrinkingFountain>>() {
            @Override
            public void onDataLoaded(List<DrinkingFountain> data) {
                if(data.isEmpty()) {
                    Log.i(TAG, "loadDrinkingFountains(): No local data, starting sync");
                    syncDrinkingFountains(new NoOpOnOperationSuccessfulCallback(), callback);
                } else  {
                    Log.i(TAG, "loadDrinkingFountains(): Loaded local data");
                    callback.onDataLoaded(data);
                }
            }

            @Override
            public void onDataLoadError(Throwable throwable) {
                callback.onDataLoadError(throwable);
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
