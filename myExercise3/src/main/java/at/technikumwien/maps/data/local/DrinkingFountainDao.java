package at.technikumwien.maps.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

/**
 * Created by FH on 18.10.2017.
 */

@Dao
public interface DrinkingFountainDao {

    @Insert
    void insert(List<DrinkingFountain> drinkingFountains);

    @Query("DELETE FROM drinking_fountain")
    void deleteAll();

    @Query("SELECT * FROM drinking_fountain")
    List<DrinkingFountain> findAllDrinkingFountains();

    @Query("SELECT * FROM drinking_fountain WHERE id = :id")
    DrinkingFountain getDrinkingFountainById(String id);

    @Update
    void update(DrinkingFountain drinkingFountain);
}
