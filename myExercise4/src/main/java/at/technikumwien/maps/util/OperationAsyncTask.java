package at.technikumwien.maps.util;

import android.os.AsyncTask;

import at.technikumwien.maps.data.OnOperationSuccessfulCallback;

public abstract class OperationAsyncTask extends AsyncTask<Void, Void, Throwable> {

    private final OnOperationSuccessfulCallback callback;

    public OperationAsyncTask(OnOperationSuccessfulCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Throwable doInBackground(Void... params) {
        try {
            doOperation();
            return null;
        } catch(Throwable throwable) {
            return throwable;
        }
    }

    @Override
    protected void onPostExecute(Throwable throwable) {
        if(throwable == null) {
            callback.onOperationSuccessful();
        } else {
            callback.onOperationError(throwable);
        }
    }

    public abstract void doOperation() throws Throwable;
}
