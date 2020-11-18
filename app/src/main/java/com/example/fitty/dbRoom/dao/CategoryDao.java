package com.example.fitty.dbRoom.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitty.dbRoom.entitys.CategoryEntity;

import java.util.List;

@Dao
public abstract class CategoryDao {
    @Query("SELECT * FROM categories")
    public abstract LiveData<List<CategoryEntity>> findAll();

    @Query("SELECT * FROM categories LIMIT :limit OFFSET :offset")
    public abstract LiveData<List<CategoryEntity>> findAll(int limit, int offset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(CategoryEntity... category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<CategoryEntity> categories);

    @Update
    public abstract void update(CategoryEntity category);

    @Delete
    public abstract void delete(CategoryEntity category);

    @Query("DELETE FROM categories WHERE id = :id")
    public abstract void delete(int id);

    @Query("DELETE FROM categories WHERE id IN (SELECT id FROM categories LIMIT :limit OFFSET :offset)")
    public abstract void delete(int limit, int offset);

    @Query("DELETE FROM categories")
    public abstract void deleteAll();

    @Query("SELECT * FROM categories WHERE id = :id")
    public abstract LiveData<CategoryEntity> findById(int id);
}
