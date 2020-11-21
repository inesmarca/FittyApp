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
    @ColumnInfo(name="difficulty")
    public String difficulty;

    @NonNull
    @ColumnInfo(name="category_id")
    public Integer category_id;

    @ColumnInfo(name="category_name")
    public String category_name;

    @ColumnInfo(name="category_detail")
    public String category_detail;



    public RoutineEntity(@NonNull Integer id, String detail, Float avgRating, String name, Integer creatorId, String difficulty, Integer category_id, String category_name, String category_detail) {
        this.id = id;
        this.detail = detail;
        this.avgRating = avgRating;
        this.name = name;
        this.creatorId = creatorId;
        this.difficulty = difficulty;
        this.category_id= category_id;
        this.category_name=category_name;
        this.category_detail=category_detail;
    }
}