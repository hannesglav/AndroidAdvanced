package at.technikumwien.maps.ui.maps

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import at.technikumwien.maps.AppDependencyManager
import at.technikumwien.maps.R
import at.technikumwien.maps.data.model.DrinkingFountain
import at.technikumwien.maps.ui.base.BaseActivity
import at.technikumwien.maps.util.managers.SyncManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView

/**
 * Created by FH on 06.11.2017.
 */

interface MapsView : MvpView {

    fun showDrinkingFountains(drinkingFountains: List<DrinkingFountain>)
    fun showLoadingError(e:Throwable)
}

class MapsActivity : BaseActivity<MapsView, MapsPresenter>(), OnMapReadyCallback, MapsView {

    private var googleMap: GoogleMap? = null
    private lateinit var rootLayout: FrameLayout

    override fun createPresenter() = MapsPresenter(appDependencyManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        rootLayout =findViewById(R.id.root_layout)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap = p0
        val viennaLatLng = LatLng(48.239340, 16.377335);
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(viennaLatLng, 10F))
        presenter.loadDrinkingFountains()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
            R.id.menu_refresh -> {presenter.loadDrinkingFountains(); true}
            else -> super.onOptionsItemSelected(item)
        }

    override fun showDrinkingFountains(drinkingFountains: List<DrinkingFountain>) {
        googleMap?.let { googleMap ->
            googleMap.clear();
            drinkingFountains.forEach { df ->
                googleMap.addMarker(MarkerOptions().position(df.position).title(df.name)) }

        }

    }

    override fun showLoadingError(e: Throwable) {
        Log.e("MapsActivity", "Could not load drinking fountains", e);
        Snackbar.make(rootLayout, R.string.snackbar_load_retry_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_load_retry_action) { presenter.loadDrinkingFountains() }
                .show();
    }


}

class MapsPresenter constructor(manager: AppDependencyManager) : MvpBasePresenter<MapsView> {

    private lateinit var syncManager: SyncManager;

    private val liveData = manager.drinkingFountainRepo.loadAll();
    private val observer = Observer<List<DrinkingFountain>> { drinkingFountains ->

        if (isViewAttached) {
            drinkingFountains?.let {
                view.showDrinkingFountains(it);
            }
        }
    }

    // equivalent
    init {
        syncManager = manager.syncManager;
    }


}