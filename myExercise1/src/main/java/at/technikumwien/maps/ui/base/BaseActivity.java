package at.technikumwien.maps.ui.base;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.MyApplication;

/**
 * Created by FH on 08.10.2017.
 */

public abstract class BaseActivity<V extends MvpView,P extends MvpBasePresenter<V>> extends MvpActivity<V,P> {

    protected AppDependencyManager getAppDependencyManager() {
        return ((MyApplication) getApplicationContext()).getAppDependencyManager();
    }
}
