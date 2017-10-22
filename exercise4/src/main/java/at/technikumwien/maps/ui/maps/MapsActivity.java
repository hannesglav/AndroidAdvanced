package at.technikumwien.maps.ui.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import at.technikumwien.maps.R;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.ui.base.BaseActivity;

public class MapsActivity extends BaseActivity<MapsView, MapsPresenter> implements OnMapReadyCallback, MapsView {

    private GoogleMap googleMap;

    private FrameLayout rootLayout;

    @NonNull
    @Override
    public MapsPresenter createPresenter() {
        return new MapsPresenter(getAppDependencyManager());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        rootLayout = findViewById(R.id.root_layout);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_refresh) {
            presenter.loadDrinkingFountains();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng viennaLatLng = new LatLng(48.239340, 16.377335);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viennaLatLng, 10));
        presenter.loadDrinkingFountains();
    }


    @Override
    public void showDrinkingFountains(List<DrinkingFountain> drinkingFountains) {
        if(googleMap != null) {
            googleMap.clear();

            for (DrinkingFountain df : drinkingFountains) {
                googleMap.addMarker(new MarkerOptions().position(df.getPosition()).title(df.getName()));
            }
        }
    }

    @Override
    public void showLoadingError(Throwable e) {
        Log.e("MapsActivity", "Could not load drinking fountains", e);
        Snackbar.make(rootLayout, R.string.snackbar_load_retry_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_load_retry_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.loadDrinkingFountains();
                    }
                }).show();
    }
}
