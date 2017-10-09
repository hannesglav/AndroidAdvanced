package at.technikumwien.maps.data.remote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.util.DownloadAsyncTask;

public class AsyncTaskJsonObjectDrinkingFountainRepo implements DrinkingFountainRepo {

    @Override
    public void loadDrinkingFountains(final OnDataLoadedCallback<List<DrinkingFountain>> callback) {
        new DownloadAsyncTask<List<DrinkingFountain>>(BASE_URL + GET_DRINKING_FOUNTAINS_PATH, callback) {

            @Override
            protected List<DrinkingFountain> parseJson(String json) throws JSONException {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray features = jsonObject.getJSONArray("features");
                List<DrinkingFountain> drinkingFountains = new ArrayList<>(features.length());

                for(int i=0; i<features.length(); i++) {
                    JSONObject drinkingFountainItem = features.getJSONObject(i);
                    JSONObject geometry = drinkingFountainItem.getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    JSONObject properties = drinkingFountainItem.getJSONObject("properties");
                    String id = drinkingFountainItem.getString("id");
                    String name = properties.getString("NAME");
                    double lat = coordinates.getDouble(1);
                    double lng = coordinates.getDouble(0);
                    drinkingFountains.add(DrinkingFountain.create(id, name, lat, lng));
                }

                return drinkingFountains;
            }

        }.execute();
    }
}
