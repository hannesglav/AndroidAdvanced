package at.technikumwien.maps.ui.maps;


import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

public interface MapsView extends MvpView {
    void showDrinkingFountains(List<DrinkingFountain> drinkingFountains);
    void showLoadingError(Exception exception);
}
