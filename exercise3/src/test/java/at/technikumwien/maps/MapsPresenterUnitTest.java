package at.technikumwien.maps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.technikumwien.maps.data.OnDataLoadedCallback;
import at.technikumwien.maps.data.local.DrinkingFountainRepo;
import at.technikumwien.maps.data.model.DrinkingFountain;
import at.technikumwien.maps.data.remote.DrinkingFountainApi;
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse;
import at.technikumwien.maps.ui.maps.MapsPresenter;
import at.technikumwien.maps.ui.maps.MapsView;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MapsPresenterUnitTest {

    @Mock AppDependencyManager dependencyManager;
    @Mock DrinkingFountainRepo drinkingFountainRepo;
    @Mock DrinkingFountainApi drinkingFountainApi;

    @Mock MapsView view;
    private MapsPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doReturn(drinkingFountainRepo).when(dependencyManager).getDrinkingFountainRepo();
        doReturn(drinkingFountainApi).when(dependencyManager).getDrinkingFountainApi();

        presenter = new MapsPresenter(dependencyManager);
        presenter.attachView(view);
    }

    @Test
    public void loadDrinkingFountains_local_success() {
        final List<DrinkingFountain> drinkingFountains = new ArrayList<>();
        drinkingFountains.add(new DrinkingFountain("", "", 0, 0));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocationOnMock.getArgument(0);
                callback.onDataLoaded(drinkingFountains);
                return null;
            }
        }).when(drinkingFountainRepo).loadAll(any(OnDataLoadedCallback.class));

        presenter.loadDrinkingFountains();

        verify(view).showDrinkingFountains(drinkingFountains);
    }

    @Test
    public void loadDrinkingFountains_local_error() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocationOnMock.getArgument(0);
                callback.onDataLoadError(new Exception());
                return null;
            }
        }).when(drinkingFountainRepo).loadAll(any(OnDataLoadedCallback.class));

        presenter.loadDrinkingFountains();

        verify(view).showLoadingError(any(Exception.class));
    }

    @Test
    public void loadDrinkingFountains_remote_success() {
        final List<DrinkingFountain> drinkingFountains = new ArrayList<>();
        final List<DrinkingFountain> remoteDrinkingFountains = new ArrayList<>();
        remoteDrinkingFountains.add(new DrinkingFountain("", "", 0, 0));
        final DrinkingFountainResponse drinkingFountainResponse = mock(DrinkingFountainResponse.class);
        doReturn(remoteDrinkingFountains).when(drinkingFountainResponse).getDrinkingFountainList();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocationOnMock.getArgument(0);
                callback.onDataLoaded(drinkingFountains);
                return null;
            }
        }).when(drinkingFountainRepo).loadAll(any(OnDataLoadedCallback.class));

        final Call<DrinkingFountainResponse> call = mock(Call.class);
        doReturn(call).when(drinkingFountainApi).getDrinkingFountains();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Callback<DrinkingFountainResponse> callback = invocationOnMock.getArgument(0);
                callback.onResponse(call, Response.success(drinkingFountainResponse));
                return null;
            }
        }).when(call).enqueue(any(Callback.class));

        presenter.loadDrinkingFountains();

        verify(view).showDrinkingFountains(remoteDrinkingFountains);
    }

    @Test
    public void loadDrinkingFountains_remote_httpError() {
        final List<DrinkingFountain> drinkingFountains = new ArrayList<>();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocationOnMock.getArgument(0);
                callback.onDataLoaded(drinkingFountains);
                return null;
            }
        }).when(drinkingFountainRepo).loadAll(any(OnDataLoadedCallback.class));

        final Call<DrinkingFountainResponse> call = mock(Call.class);
        doReturn(call).when(drinkingFountainApi).getDrinkingFountains();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Callback<DrinkingFountainResponse> callback = invocationOnMock.getArgument(0);
                callback.onResponse(call, Response.<DrinkingFountainResponse>error(404, ResponseBody.create(MediaType.parse("nothing/nothing"), "")));
                return null;
            }
        }).when(call).enqueue(any(Callback.class));

        presenter.loadDrinkingFountains();

        verify(view).showLoadingError(any(HttpException.class));
    }

    @Test
    public void loadDrinkingFountains_remote_otherError() {
        final Throwable error = new IOException();
        final List<DrinkingFountain> drinkingFountains = new ArrayList<>();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OnDataLoadedCallback<List<DrinkingFountain>> callback = invocationOnMock.getArgument(0);
                callback.onDataLoaded(drinkingFountains);
                return null;
            }
        }).when(drinkingFountainRepo).loadAll(any(OnDataLoadedCallback.class));

        final Call<DrinkingFountainResponse> call = mock(Call.class);
        doReturn(call).when(drinkingFountainApi).getDrinkingFountains();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Callback<DrinkingFountainResponse> callback = invocationOnMock.getArgument(0);
                callback.onFailure(call, error);
                return null;
            }
        }).when(call).enqueue(any(Callback.class));

        presenter.loadDrinkingFountains();

        verify(view).showLoadingError(error);
    }
}
