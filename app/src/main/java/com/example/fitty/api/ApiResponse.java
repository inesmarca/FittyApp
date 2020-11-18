package com.example.fitty.api;

import com.example.fitty.api.models.Error;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ApiResponse<T> {
    //Maneja todas las respuetas, tanto error como data
    private T data;
    private Error error;

    public T getData() {
        return data;
    }

    public Error getError() {
        return error;
    }

    public ApiResponse(Response<T> response){
        if(response.isSuccessful()){
            data=response.body();
            return;
        }
        if(response.errorBody()==null){
            parseError("No hay body del error");
            return;
        }
        String errorMsg;
        try{
            errorMsg=response.errorBody().string();
        }catch (IOException exception){
            parseError(exception.getMessage());
            return;
        }
        if(errorMsg!=null && errorMsg.length()>=1){
            //Parseo
            Gson gson = new Gson();
            error= gson.fromJson(errorMsg,new TypeToken<Error>(){}.getType());
        }
    }
    public ApiResponse(Throwable t){
        parseError(t.getMessage());
    }

    private void parseError(String msg){

        List<String> errorDetails = new ArrayList<>();

        if(msg!=null){
            errorDetails.add(msg);
        }
        this.error = new Error(Error.LOCAL_UNEXPECTED_ERROR,"Error inesperado",errorDetails);

    }
}
