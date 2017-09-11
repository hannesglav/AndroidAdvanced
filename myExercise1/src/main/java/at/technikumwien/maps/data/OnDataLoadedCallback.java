package at.technikumwien.maps.data;

/**
 * A callback which can be used when loading data.
 * onDataLoaded is invoked when data was successfully loaded,
 * onDataLoadError is invoked when there was an error loading data.
 * Both callbacks are called on the main thread.
 *
 * @param <T> The type of the data
 */
public interface OnDataLoadedCallback<T> {

    void onDataLoaded(T data);

    void onDataLoadError(Exception exception);

}
