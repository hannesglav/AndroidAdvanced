package at.technikumwien.maps;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import at.technikumwien.maps.data.local.AppDatabase;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.local.RoomDrinkingFountainRepo;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.util.managers.SyncManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppDependencyManager {

    private final Context appContext;
    private DrinkingFountainRepo drinkingFountainRepo;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private DrinkingFountainApi drinkingFountainApi;
    private AppDatabase appDatabase;
    private SyncManager syncManager;

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
        if(drinkingFountainRepo == null) { drinkingFountainRepo = new RoomDrinkingFountainRepo(this); }
        return drinkingFountainRepo;
    }

    public Gson getGson() {
        if(gson == null) { gson = new Gson(); }
        return gson;
    }

    public OkHttpClient getOkHttpClient() {
        if(okHttpClient == null) { okHttpClient = new OkHttpClient(); }
        return okHttpClient;
    }

    public DrinkingFountainApi getDrinkingFountainApi() {
        if(drinkingFountainApi == null) {
            OkHttpClient.Builder httpClientBuilder = getOkHttpClient().newBuilder();

            // Enable logging on debug builds
            if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClientBuilder.addInterceptor(loggingInterceptor);
            }

            drinkingFountainApi = new Retrofit.Builder()
                    .baseUrl(DrinkingFountainApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .callFactory(httpClientBuilder.build())
                    .build().create(DrinkingFountainApi.class);
        }

        return drinkingFountainApi;
    }

    public AppDatabase getAppDatabase() {
        if(appDatabase == null) { appDatabase = Room.databaseBuilder(appContext, AppDatabase.class, "mapsdb").build(); }
        return appDatabase;
    }

    public SyncManager getSyncManager() {
        if(syncManager == null) { syncManager = new SyncManager(this); }
        return syncManager;
    }
}
