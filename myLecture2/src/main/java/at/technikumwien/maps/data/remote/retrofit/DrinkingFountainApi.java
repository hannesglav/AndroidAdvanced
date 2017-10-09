package at.technikumwien.maps.data.remote.retrofit;

import at.technikumwien.maps.data.remote.DrinkingFountainRepo;
import at.technikumwien.maps.data.remote.retrofit.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by FH on 09.10.2017.
 * einfaches Interface von Retrofit
 * reicht eigentlich aus (= kann DrinkingFountainRepo ersetzen)
 */

public interface DrinkingFountainApi {

    @GET(DrinkingFountainRepo.GET_DRINKING_FOUNTAINS_PATH)
    Call<DrinkingFountainResponse> getDrinkingFountains();

}
