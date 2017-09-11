package at.technikumwien.maps;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainRepo;
import at.technikumwien.maps.ui.maps.MapsPresenter;
import at.technikumwien.maps.ui.maps.MapsView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class MapsPresenterUnitTest {

    @Mock AppDependencyManager dependencyManager;
    @Mock DrinkingFountainRepo drinkingFountainRepo;
    @Mock MapsView view;

    private MapsPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        doReturn(drinkingFountainRepo).when(dependencyManager).getDrinkingFountainRepo();

        presenter = new MapsPresenter(dependencyManager);
        presenter.attachView(view);
    }

    @Test
    public void loadDrinkingFountains_success() {
        final List<DrinkingFountain> drinkingFountainList = new ArrayList<>();

        // Define mock behavior
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback callback = invocationOnMock.getArgument(0);
                callback.onDataLoaded(drinkingFountainList);
                return null;
            }
        }).when(drinkingFountainRepo).loadDrinkingFountains(any(OnDataLoadedCallback.class));

        // Call method under test
        presenter.loadDrinkingFountains();

        // Verify correct behavior of object under test
        verify(view).showDrinkingFountains(eq(drinkingFountainList));
    }

    @Test
    public void loadDrinkingFountains_error() {
        final Exception exception = new Exception();

        // Define mock behavior
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback callback = invocationOnMock.getArgument(0);
                callback.onDataLoadError(exception);
                return null;
            }
        }).when(drinkingFountainRepo).loadDrinkingFountains(any(OnDataLoadedCallback.class));

        // Call method under test
        presenter.loadDrinkingFountains();

        // Verify correct behavior of object under test
        verify(view).showLoadingError(eq(exception));
    }


}
