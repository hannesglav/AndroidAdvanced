package at.technikumwien.maps.ui.base;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState;

import at.technikumwien.maps.AppDependencyManager;
import at.technikumwien.maps.MyApplication;

public abstract class BaseViewStateActivity<V extends MvpView, P extends MvpPresenter<V>, VS extends ViewState<V>> extends MvpViewStateActivity<V, P, VS> {

    protected AppDependencyManager getAppDependencyManager() {
        return ((MyApplication) getApplicationContext()).getAppDependencyManager();
    }

}
