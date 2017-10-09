package at.technikumwien.maps.data.remote.retrofit;


import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainRepo;
import at.technikumwien.maps.data.remote.retrofit.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DrinkingFountainApi {

    @GET(DrinkingFountainRepo.GET_DRINKING_FOUNTAINS_PATH)
    Call<DrinkingFountainResponse> getDrinkingFountains();

}
