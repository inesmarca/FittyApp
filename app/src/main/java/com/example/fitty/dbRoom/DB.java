package com.example.fitty.dbRoom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitty.dbRoom.dao.CategoryDao;
import com.example.fitty.dbRoom.dao.CycleDao;
import com.example.fitty.dbRoom.dao.RoutineDao;
import com.example.fitty.dbRoom.entitys.CategoryEntity;
import com.example.fitty.dbRoom.entitys.CycleEntity;
import com.example.fitty.dbRoom.entitys.RoutineEntity;


@Database(entities = {RoutineEntity.class, CategoryEntity.class, CycleEntity.class}, version = 1,exportSchema = false)
public abstract class DB extends RoomDatabase {

    public abstract RoutineDao routineDao();
    public abstract CategoryDao categoryDao();
    public abstract CycleDao cycleDao();
}

