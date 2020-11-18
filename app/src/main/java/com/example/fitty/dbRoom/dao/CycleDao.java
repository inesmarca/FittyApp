package com.example.fitty.dbRoom.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitty.dbRoom.entitys.CycleEntity;

import java.util.List;
@Dao
public abstract class CycleDao {
    @Query("SELECT * FROM cycles")
    public abstract LiveData<List<CycleEntity>> findAll();

    @Query("SELECT * FROM cycles LIMIT :limit OFFSET :offset")
    public abstract LiveData<List<CycleEntity>> findAll(int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(CycleEntity... cycle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<CycleEntity> cycles);

    @Update
    public abstract void update(CycleEntity cycle);

    @Delete
    public abstract void delete(CycleEntity cycle);

    @Query("DELETE FROM cycles WHERE id = :id")
    public abstract void delete(int id);

    @Query("DELETE FROM cycles WHERE id IN (SELECT id FROM cycles LIMIT :limit OFFSET :offset)")
    public abstract void delete(int limit, int offset);

    @Query("DELETE FROM cycles")
    public abstract void deleteAll();

    @Query("SELECT * FROM cycles WHERE id = :id")
    public abstract LiveData<CycleEntity> findById(int id);
}
