package com.example.fitty;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitty.adapters.ExecuteCycleAdapter;
import com.example.fitty.databinding.ExecuteActivityBinding;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Exercise;
import com.example.fitty.models.Routine;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecuteActivity extends AppCompatActivity/* implements YouTubePlayer.OnInitializedListener*/ {

    ExecuteActivityBinding binding;

    ExecuteCycleAdapter adapter;

    AlertDialog dialogCuentaHasta3;

    ExecuteActivity activity;

    LocalTime elapsedTime;
    Timer timerElapsedTime;
    Boolean ejecutando = false;

    int currentCycleIdx, currentExerciseIdx;

    AlertDialog alertBackPressed = null;

    List<Cycle> fullCycles;

    Timer timerContarAtrasEjercicio;

    private int secondsRemaining;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ExecuteActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        /*YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtubePlayerFragment);

        youTubePlayerFragment.initialize(API_KEY, this);*/

        Routine routine = (Routine) getIntent().getExtras().getSerializable("routine");

        fullCycles = new ArrayList<>();
        for(Cycle cycle : routine.getCycles()) {
            if(cycle.getRepetitions() > 1) {
                for(int i = 1; i <= cycle.getRepetitions(); i++) {
                    Cycle aux = new Cycle(cycle.getId(), cycle.getName() + " " + i +"/"+cycle.getRepetitions(), cycle.getDetail(), cycle.getType(), cycle.getOrder(), cycle.getRepetitions());
                    aux.addExercise(cycle.getExercises());
                    fullCycles.add(aux);
                }
            } else {
                fullCycles.add(cycle);
            }
        }
        adapter = new ExecuteCycleAdapter(fullCycles, this);//routine.getCycles()

        binding.recyclerExecuteCycles.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerExecuteCycles.setAdapter(adapter);

        binding.btnPauseResume.setOnClickListener((view) -> {
            if(ejecutando == true) {
                ejecutando = false;
                pausar();
                binding.btnPauseResume.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            } else {
                ejecutando = true;
                resumir();
                binding.btnPauseResume.setImageResource(R.drawable.ic_baseline_pause_24);
            }
        });

        cuentaRegresiva();
    }

    private void cuentaRegresiva() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.preparate));
        builder.setMessage(" ");
        builder.setCancelable(false);
        dialogCuentaHasta3 = builder.create();
        dialogCuentaHasta3.show();

        Timer timer = new Timer();
        timer.schedule(new ContarHasta3(), 1000, 1000);

    }

    private void displayElapsedTime() {
        String minutes = String.valueOf(elapsedTime.getMinute());
        String seconds = elapsedTime.getSecond() >= 10 ? String.valueOf(elapsedTime.getSecond()) : "0"+String.valueOf(elapsedTime.getSecond());
        binding.txtElapsedTime.setText(minutes + ":" + seconds);
    }


    private void iniciar() {
        elapsedTime = LocalTime.of(0, 0, 0);
        displayElapsedTime();
        ejecutando = true;
        resumir();
        currentCycleIdx = -1;
        currentExerciseIdx = -1;
        siguienteEjercicio();
    }



    public void siguienteEjercicio() {
        if(currentCycleIdx == -1) {
            //Primer ejercicio
            currentCycleIdx = 0;
            currentExerciseIdx = 0;
        } else {
            currentExerciseIdx++;
        }
        if(currentCycleIdx < fullCycles.size()) {

            if(fullCycles.get(currentCycleIdx).getExercises() != null && currentExerciseIdx < fullCycles.get(currentCycleIdx).getExercises().size()) {
                asignarEjecucion(currentCycleIdx, currentExerciseIdx);
            } else {
                currentCycleIdx++;
                if(currentCycleIdx < fullCycles.size()) {
                    currentExerciseIdx = 0;
                    asignarEjecucion(currentCycleIdx, currentExerciseIdx);
                } else {
                    //Terminamos
                    terminarRutina();
                }

            }

        } else {
            //Terminamos
            terminarRutina();
        }
    }

    private void terminarRutina() {
        timerElapsedTime.cancel();
        binding.txtCabeceraCant.setText("");
        binding.txtCabeceraNombre.setText("");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.titulo_ejecucion_terminada);
        builder.setMessage(getString(R.string.mensaje_ejecucion_terminada, elapsedTime.getMinute(), elapsedTime.getSecond()));
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            super.onBackPressed();
        });

        alertBackPressed = builder.create();
        alertBackPressed.show();

    }

    private void asignarEjecucion(int cycleIdx, int exerciseIdx) {
        Exercise exercise = adapter.asignarEjecucion(cycleIdx, exerciseIdx);
        binding.txtCabeceraNombre.setText(exercise.getName());
        if(exercise.getDuration() > 0) {
            binding.txtCabeceraCant.setText(String.valueOf(exercise.getDuration()) + "s");
            //Al ser por tiempo debemos mostrar cuenta atras
            timerContarAtrasEjercicio = new Timer();
            timerContarAtrasEjercicio.schedule(new ContarAtrasEjercicio(exercise.getDuration()), 0, 1000);
        } else {
            timerContarAtrasEjercicio = null;
            binding.txtCabeceraCant.setText("x"+String.valueOf(exercise.getRepetitions()));
        }

    }

    private void pausar() {
        timerElapsedTime.cancel();
        if(timerContarAtrasEjercicio != null)
            timerContarAtrasEjercicio.cancel();
    }
    private void resumir() {
        timerElapsedTime = new Timer();
        timerElapsedTime.schedule(new SumarSegundo(), 1000, 1000);

        if(timerContarAtrasEjercicio != null) {
            timerContarAtrasEjercicio = new Timer();
            timerContarAtrasEjercicio.schedule(new ContarAtrasEjercicio(secondsRemaining), 1000, 1000);
        }
    }

    class SumarSegundo extends TimerTask {
        public void run() {
            elapsedTime = elapsedTime.plusSeconds(1);
            activity.runOnUiThread(() -> {
                displayElapsedTime();
            });
        }
    }

    class ContarHasta3 extends TimerTask {
        private int count = 5;
        public void run() {
            if(count == 0) {
                this.cancel();
                activity.runOnUiThread(() -> {
                    dialogCuentaHasta3.hide();
                    iniciar();
                });
            } else {
                dialogCuentaHasta3.setMessage(getString(R.string.comenzando, count--));
            }

        }
    }

    class ContarAtrasEjercicio extends TimerTask {
        private int totalTime;
        ContarAtrasEjercicio(int seconds) {
            secondsRemaining = seconds;
            totalTime = seconds;
            activity.runOnUiThread(() -> {
                binding.progressExecution.setProgress(0);
                binding.progressExecution.setVisibility(View.VISIBLE);
            });
        }
        public void run() {
            if(secondsRemaining == 0) {
                this.cancel();
                activity.runOnUiThread(() -> {
                    binding.progressExecution.setVisibility(View.GONE);
                    siguienteEjercicio();
                });
            } else {
                activity.runOnUiThread(() -> {
                    binding.txtCabeceraCant.setText(String.valueOf(secondsRemaining--) + "s");
                    binding.progressExecution.setProgress((int) (100 - (Double.valueOf(secondsRemaining)/totalTime)*100));
                });

            }

        }
    }


    @Override
    public void onBackPressed() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        ExecuteActivity.super.onBackPressed();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        alertBackPressed.hide();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.cancel_execution)).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener);

        alertBackPressed = builder.create();
        alertBackPressed.show();

    }












    // Youtube

    /*public static final String API_KEY = "AIzaSyAEsONprWWPgR9MQ3jFLMnOu1qgrYqA_8o";
    public static final String VIDEO_ID = "B08iLAtS3AQ";



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);


        if(!wasRestored) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {

        final int REQUEST_CODE = 1;

        if(error.isUserRecoverableError()) {
            error.getErrorDialog(this,REQUEST_CODE).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YoutubePlayer (%1$s)", error.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }*/



}
