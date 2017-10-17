package at.technikumwien.maps.data;

/**
 * A callback which can be used when executing an operation w/o returning data.
 * onOperationSuccessful is invoked when the operation was successful,
 * onOperationError is invoked when there was an error executing the operation.
 * Both callbacks are called on the main thread.
 */
public interface OnOperationSuccessfulCallback {

    void onOperationSuccessful();

    void onOperationError(Throwable throwable);

}
