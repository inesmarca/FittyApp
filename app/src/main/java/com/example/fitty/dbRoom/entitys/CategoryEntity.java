package com.example.fitty.dbRoom.entitys;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "categories", indices = {@Index("id")}, primaryKeys = {"id"})

public class CategoryEntity {
    @NonNull
    @ColumnInfo(name="id")
    public Integer id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="detail")
    public String detail;

    public CategoryEntity(@NonNull Integer id, String name, String detail) {
        this.id = id;
        this.name = name;
        this.detail = detail;
    }
}
