package at.technikumwien.maps;

import android.content.Context;
import android.content.res.Resources;

import at.technikumwien.maps.data.remote.DownloadAsyncTaskDrinkingFountainRepo;
import at.technikumwien.maps.data.remote.DrinkingFountainRepo;
import at.technikumwien.maps.data.remote.MockDrinkingFountainRepo;
import at.technikumwien.maps.data.remote.RetrofitDrinkingFountainRepo;
import at.technikumwien.maps.data.remote.retrofit.DrinkingFountainApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppDependencyManager {

    private final Context appContext;
    private DrinkingFountainRepo drinkingFountainRepo;
    private DrinkingFountainApi drinkingFountainApi;

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
        if(drinkingFountainRepo == null) { drinkingFountainRepo = new RetrofitDrinkingFountainRepo(this); }
        return drinkingFountainRepo;
    }

    public DrinkingFountainApi getDrinkingFountainApi() {
        if(drinkingFountainApi == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

            if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(interceptor);
            }

            OkHttpClient okHttpClient = okHttpClientBuilder.build();

            drinkingFountainApi = new Retrofit.Builder()
                    .baseUrl(DrinkingFountainRepo.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(DrinkingFountainApi.class);
        }

        return drinkingFountainApi;
    }
}
