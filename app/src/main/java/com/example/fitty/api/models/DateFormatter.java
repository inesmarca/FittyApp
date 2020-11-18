package com.example.fitty.api.models;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/*Clase para hacer más fácil el manejo de las fechas, ya que la API las maneja como Long :)*/
public class DateFormatter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if(value==null)
            out.nullValue();
        else
            out.value(value.getTime());
    }
    @Override
    public Date read (JsonReader in) throws IOException{
        if(in!=null)
            return new Date(in.nextLong());
        return null;
    }
}
