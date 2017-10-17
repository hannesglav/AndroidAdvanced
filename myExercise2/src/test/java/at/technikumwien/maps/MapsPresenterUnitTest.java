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

import static org.mockito.Mockito.any;
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
        final List<DrinkingFountain> drinkingFountains = new ArrayList<>();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocationOnMock.getArgument(0);
                callback.onDataLoaded(drinkingFountains);
                return null;
            }
        }).when(drinkingFountainRepo).loadDrinkingFountains(any(OnDataLoadedCallback.class));

        presenter.loadDrinkingFountains();

        verify(view).showDrinkingFountains(drinkingFountains);
    }

    @Test
    public void loadDrinkingFountains_error() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocationOnMock.getArgument(0);
                callback.onDataLoadError(new Exception());
                return null;
            }
        }).when(drinkingFountainRepo).loadDrinkingFountains(any(OnDataLoadedCallback.class));

        presenter.loadDrinkingFountains();

        verify(view).showLoadingError(any(Exception.class));
    }
}
