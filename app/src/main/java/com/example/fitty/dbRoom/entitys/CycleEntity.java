package com.example.fitty.dbRoom.entitys;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;



@Entity(tableName = "cycles", indices = {@Index("id")}, primaryKeys = {"id"})
public class CycleEntity {

    @NonNull
    @ColumnInfo(name="id")
    public Integer id;
    @ColumnInfo(name="name")
    public String name;
    @ColumnInfo(name="detail")
    public String detail;
    @ColumnInfo(name="type")
    public String type;
    @ColumnInfo(name="order")
    public int order;
    @ColumnInfo(name="repetitions")
    public int repetitions;

    public CycleEntity(@NonNull Integer id, String name, String detail, String type, int order, int repetitions) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.type = type;
        this.order = order;
        this.repetitions = repetitions;
    }
}
