package com.example.fitty.api;

import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override  //magia de retrofit
    public CallAdapter<?,?> get(Type type, Annotation[] annotations, Retrofit retrofit){
      //validamos tipos de dato
        if(getRawType(type)!= LiveData.class)
            return null;
        //Ahora vemos que lo que este en el livedata sea un ApiResponse
        Type observableType = getParameterUpperBound(0, (ParameterizedType)type);
        Class<?> rawObservableType = getRawType(observableType);
        if(rawObservableType!= ApiResponse.class)
            throw new IllegalArgumentException("Necesito ApiResponse");
        if(!(observableType instanceof ParameterizedType))
            //Esto valida que se con generic
            throw new IllegalArgumentException("Api response tiene que ser usada de manera genérica");
        Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataCallAdapter<>(bodyType);


    }
}
