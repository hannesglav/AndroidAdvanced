package at.technikumwien.maps.util;

import android.os.AsyncTask;

import at.technikumwien.maps.data.OnDataLoadedCallback;

public abstract class LoadDataAsyncTask<T> extends AsyncTask<Void, Void, T> {

    private final OnDataLoadedCallback<T> callback;

    private Throwable throwable = null;

    public LoadDataAsyncTask(OnDataLoadedCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    protected T doInBackground(Void... params) {
        try {
            return loadData();
        } catch(Throwable throwable) {
            this.throwable = throwable;
            return null;
        }
    }

    @Override
    protected void onPostExecute(T data) {
        if(throwable != null) {
            callback.onDataLoadError(throwable);
        } else {
            callback.onDataLoaded(data);
        }
    }

    public abstract T loadData() throws Throwable;
}
