package at.technikumwien.maps.data;

public final class NoOpOnOperationSuccessfulCallback implements OnOperationSuccessfulCallback {

    @Override
    public void onOperationSuccessful() { }

    @Override
    public void onOperationError(Throwable throwable) { }
}
