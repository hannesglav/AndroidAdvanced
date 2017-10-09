package at.technikumwien.maps.util;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import at.technikumwien.maps.data.OnDataLoadedCallback;


public abstract class DownloadAsyncTask<Result> extends AsyncTask<Void, Void, Result> {

    private final String url;
    private final OnDataLoadedCallback<Result> callback;
    private Exception exception = null;

    public DownloadAsyncTask(String url, OnDataLoadedCallback<Result> callback) {
        this.url = url;
        this.callback = callback;
    }

    @Override
    protected Result doInBackground(Void... params) {
        try {
            URL javaUrl = new URL(url);
            String data = downloadUrl(javaUrl);
            return parseJson(data);
        } catch(Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if(exception != null) {
            callback.onDataLoadError(exception);
        } else {
            callback.onDataLoaded(result);
        }
    }

    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     */
    private String downloadUrl(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 30000ms.
            connection.setReadTimeout(30000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    // Converts the contents of an InputStream to a String.
    private String readStream(InputStream stream) throws IOException {
        try (Scanner scanner = new Scanner(stream, "UTF-8")) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    protected abstract Result parseJson(String json) throws JSONException;
}
