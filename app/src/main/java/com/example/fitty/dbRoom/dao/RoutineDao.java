package com.example.fitty.dbRoom.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitty.dbRoom.entitys.RoutineEntity;

import java.util.List;
@Dao
public abstract  class RoutineDao {
    @Query("SELECT * FROM routines")
    public abstract LiveData<List<RoutineEntity>> findAll();

    @Query("SELECT * FROM routines LIMIT :limit OFFSET :offset")
    public abstract LiveData<List<RoutineEntity>> findAll(int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RoutineEntity... routines);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<RoutineEntity> routines);

    @Update
    public abstract void update(RoutineEntity routine);

    @Delete
    public abstract void delete(RoutineEntity routines);

    @Query("DELETE FROM routines WHERE id = :id")
    public abstract void delete(int id);

    @Query("DELETE FROM ROUTINES WHERE id IN (SELECT id FROM routines LIMIT :limit OFFSET :offset)")
    public abstract void delete(int limit, int offset);

    @Query("DELETE FROM routines")
    public abstract void deleteAll();

    @Query("SELECT * FROM routines WHERE id = :id")
    public abstract LiveData<RoutineEntity> findById(int id);
}
