package at.technikumwien.maps.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import at.technikumwien.maps.data.model.DrinkingFountain;

@Dao
public interface DrinkingFountainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DrinkingFountain> drinkingFountains);

    @Query("DELETE FROM " + DrinkingFountain.TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + DrinkingFountain.TABLE_NAME)
    List<DrinkingFountain> loadAll();

    @Query("SELECT * FROM " + DrinkingFountain.TABLE_NAME)
    LiveData<List<DrinkingFountain>> loadAllWithChanges();

}
