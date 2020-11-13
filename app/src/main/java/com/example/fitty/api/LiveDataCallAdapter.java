package com.example.fitty.api;

import androidx.lifecycle.LiveData;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<ApiResponse<T>>> {

    private final Type responseType;

    LiveDataCallAdapter(Type type){
        this.responseType=type;
    }

    @Override
    public Type responseType() {
        return responseType;
    }
    @Override
    public LiveData<ApiResponse<T>> adapt(Call<T> call){
        return new LiveData<ApiResponse<T>>() {
            final AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                //Esto se habilita cuando alguien se suscribe al evento
                //Cuando alguien se suscribe llamamos al api
                super.onActive();

                if (started.compareAndSet(false, true)) {
                    //La primera vez q alguien se suscriba va a entrar aca, dsp ya no
                    call.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                            postValue(new ApiResponse<>(response));
                        }

                        @Override
                        public void onFailure(@NotNull Call<T> call, Throwable t) {
                            postValue(new ApiResponse<>(t));
                        }
                    }) ;//Solo vamos a hacer esto una vez
                }
            }

        };
    }
}

