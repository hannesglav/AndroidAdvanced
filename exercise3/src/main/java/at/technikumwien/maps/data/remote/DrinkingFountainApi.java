package at.technikumwien.maps.data.remote;

import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DrinkingFountainApi {
    String BASE_URL = "https://data.wien.gv.at/daten/";

    @GET("geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:TRINKBRUNNENOGD&srsName=EPSG:4326&outputFormat=json")
    Call<DrinkingFountainResponse> getDrinkingFountains();
}
