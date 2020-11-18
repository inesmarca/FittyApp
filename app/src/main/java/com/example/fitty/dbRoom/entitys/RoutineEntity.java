package com.example.fitty.dbRoom.entitys;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "routines", indices = {@Index("id")}, primaryKeys = {"id"})
public class RoutineEntity {

    @NonNull
    @ColumnInfo(name = "id")
    public Integer id;
    @ColumnInfo(name="detail")
    public String detail;

    @ColumnInfo(name="avgRating")
    public Float avgRating;
    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="creatorId")
    public Integer creatorId;

    @ColumnInfo(name="categoryId")
    public Integer categoryId;
    @ColumnInfo(name="difficulty")
    public String difficulty;



    public RoutineEntity(@NonNull Integer id, String detail, Float avgRating, String name, Integer creatorId, Integer categoryId, String difficulty) {
        this.id = id;
        this.detail = detail;
        this.avgRating = avgRating;
        this.name = name;
        this.creatorId = creatorId;
        this.categoryId = categoryId;
        this.difficulty = difficulty;
    }
}