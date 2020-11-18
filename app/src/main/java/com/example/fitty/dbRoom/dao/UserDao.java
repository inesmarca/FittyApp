package com.example.fitty.dbRoom.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitty.dbRoom.entitys.RoutineEntity;
import com.example.fitty.dbRoom.entitys.UserEntity;

import java.util.List;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM users")
    public abstract LiveData<List<UserEntity>> findAll();

    @Query("SELECT * FROM users LIMIT :limit OFFSET :offset")
    public abstract LiveData<List<UserEntity>> findAll(int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(com.example.fitty.dbRoom.entitys.RoutineEntity... sport);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<RoutineEntity> sports);

    @Update
    public abstract void update(RoutineEntity sport);

    @Delete
    public abstract void delete(RoutineEntity sport);

    @Query("DELETE FROM routines WHERE id = :id")
    public abstract void delete(int id);

    @Query("DELETE FROM ROUTINES WHERE id IN (SELECT id FROM routines LIMIT :limit OFFSET :offset)")
    public abstract void delete(int limit, int offset);

    @Query("DELETE FROM routines")
    public abstract void deleteAll();

    @Query("SELECT * FROM routines WHERE id = :id")
    public abstract LiveData<RoutineEntity> findById(int id);
}
