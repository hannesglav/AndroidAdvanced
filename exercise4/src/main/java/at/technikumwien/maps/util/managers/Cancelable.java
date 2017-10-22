package at.technikumwien.maps.util.managers;

public interface Cancelable {

    void cancel();
    boolean isCanceled();
}
