package at.technikumwien.maps.ui.maps;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.MyApplication;

public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> {

    protected AppDependencyManager getAppDependencyManager() {
        return ((MyApplication) getApplicationContext()).getAppDependencyManager();
    }
}
