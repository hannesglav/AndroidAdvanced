package at.technikumwien.maps.data.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

@Dao
public interface DrinkingFountainDao {

    @Insert
    void insert(List<DrinkingFountain> drinkingFountains);

    @Query("DELETE FROM drinking_fountain")
    void deleteAll();

    @Query("SELECT * FROM drinking_fountain")
    List<DrinkingFountain> findAllDrinkingFountains();
}
