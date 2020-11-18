package com.example.fitty.dbRoom.entitys;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import java.util.Date;

@Entity(tableName = "users", indices = {@Index("id")}, primaryKeys = {"id"})

public class UserEntity {
    @NonNull
    @ColumnInfo(name="id")

    public Integer id;
    @ColumnInfo(name="username")

    public String username;
    @ColumnInfo(name="fullName")

    public String fullName;
    @ColumnInfo(name="gender")
    public String gender;
    @ColumnInfo(name="birthdate")
    public Date birthdate;
    @ColumnInfo(name ="email")

    public String email;

    public UserEntity(int id, String username, String fullName, String gender, Date birthdate, String email) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.gender = gender;
        this.birthdate = birthdate;
        this.email = email;
    }
}
