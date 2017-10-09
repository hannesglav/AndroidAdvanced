package at.technikumwien.maps.ui.maps;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

/**
 * Created by FH on 08.10.2017.
 */

public interface MapsView extends MvpView {
    void showDrinkingFountains(List<DrinkingFountain> drinkingFountains);
    void showLoadingError(Exception e);
}
