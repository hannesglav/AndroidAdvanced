package at.technikumwien.maps.ui.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import at.technikumwien.maps.R;
import at.technikumwien.maps.data.model.DrinkingFountain;

public class MapsActivity extends BaseActivity<MapsView, MapsPresenter> implements MapsView, OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(48.239340, 16.377335),
                10
        );
        googleMap.moveCamera(cameraUpdate);

        presenter.loadDrinkingFountains();
    }

    @NonNull
    @Override
    public MapsPresenter createPresenter() {
        return new MapsPresenter(getAppDependencyManager());
    }

    @Override
    public void showDrinkingFountains(List<DrinkingFountain> drinkingFountains) {
        Log.i("MapsActivity", "Loaded " + drinkingFountains.size() + " fountains");

        googleMap.clear();

        for(DrinkingFountain df : drinkingFountains) {
            googleMap.addMarker(new MarkerOptions().position(df.position()).title(df.name()));
        }
    }

    @Override
    public void showLoadingError(Exception exception) {

    }


}
