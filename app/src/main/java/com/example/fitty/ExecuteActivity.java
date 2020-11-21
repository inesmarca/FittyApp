package com.example.fitty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitty.adapters.ExecuteCycleAdapter;
import com.example.fitty.databinding.ExecuteActivityBinding;
import com.example.fitty.models.Cycle;
import com.example.fitty.models.Exercise;
import com.example.fitty.models.Routine;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecuteActivity extends YouTubeBaseActivity {

    ExecuteActivityBinding binding;

    ExecuteCycleAdapter adapter;

    AlertDialog dialogCuentaHasta3;

    ExecuteActivity activity;

    LocalTime elapsedTime;
    Timer timerElapsedTime;
    public Boolean ejecutando = false, modoSimple = false, enModoVideo = false;;

    int currentCycleIdx, currentExerciseIdx;

    AlertDialog alertBackPressed = null;

    List<Cycle> fullCycles;

    Timer timerContarAtrasEjercicio;

    private int secondsRemaining, totalTimeCurrentExercise;

    public static final String API_KEY = "AIzaSyAEsONprWWPgR9MQ3jFLMnOu1qgrYqA_8o";

    YouTubePlayer.OnInitializedListener onInitializedListener;

    YouTubePlayerFragment youTubePlayerFragment;
    YouTubePlayer routineYouTubePlayer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ExecuteActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtubePlayerFragment);


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
        adapter = new ExecuteCycleAdapter(fullCycles, this);

        binding.recyclerExecuteCycles.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerExecuteCycles.setAdapter(adapter);
        binding.btnPauseResume.setOnClickListener(btnPauseResumeListener);
        binding.btnPauseResumeVideo.setOnClickListener(btnPauseResumeListener);

        binding.btnSwitchMode.setOnClickListener(btnSwitchModeListener);
        binding.btnSwitchModeVideo.setOnClickListener(btnSwitchModeListener);

        binding.btnNext.setOnClickListener(btnNextListener);
        binding.btnNextVideo.setOnClickListener(btnNextListener);



        cuentaRegresiva();
    }

    View.OnClickListener btnNextListener = (view) -> {
        if(ejecutando) {
            marcarRealizado();
            siguienteEjercicio();
        }

    };

    private void marcarRealizado() {
        adapter.marcarRealizado(currentCycleIdx, currentExerciseIdx);
    }

    View.OnClickListener btnPauseResumeListener = (view) -> {
        if(ejecutando == true) {
            ejecutando = false;
            pausar();
            binding.btnPauseResume.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            binding.btnPauseResumeVideo.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            ejecutando = true;
            resumir();
            binding.btnPauseResume.setImageResource(R.drawable.ic_baseline_pause_24);
            binding.btnPauseResumeVideo.setImageResource(R.drawable.ic_baseline_pause_24);
        }
    };

    View.OnClickListener btnSwitchModeListener = (view) -> {
        if(modoSimple) {
            modoSimple = false;
            binding.recyclerExecuteCycles.setVisibility(View.VISIBLE);
            binding.vistaSimple.setVisibility(View.GONE);
            if(enModoVideo) {
                binding.txtCabeceraNombreVideo.setVisibility(View.VISIBLE);
                binding.txtCabeceraCantVideo.setVisibility(View.VISIBLE);
                binding.txtCabeceraNombreVideo.setText(binding.txtVistaSimpleNombreAhora.getText().toString());
                binding.txtCabeceraCantVideo.setText(binding.txtVistaSimpleCantAhora.getText().toString());
            } else {
                binding.txtCabeceraCant.setVisibility(View.VISIBLE);
                binding.txtCabeceraNombre.setVisibility(View.VISIBLE);
                binding.txtCabeceraCant.setText(binding.txtVistaSimpleCantAhora.getText().toString());
                binding.txtCabeceraNombre.setText(binding.txtVistaSimpleNombreAhora.getText().toString());
            }


        } else {
            modoSimple = true;
            binding.recyclerExecuteCycles.setVisibility(View.GONE);
            binding.vistaSimple.setVisibility(View.VISIBLE);
            if(enModoVideo) {
                binding.txtCabeceraCantVideo.setVisibility(View.GONE);
                binding.txtCabeceraNombreVideo.setVisibility(View.GONE);
            } else {
                binding.txtCabeceraCant.setVisibility(View.INVISIBLE);
                binding.txtCabeceraNombre.setVisibility(View.INVISIBLE);
            }


        }
    };



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
        binding.txtElapsedTime.setText(getString(R.string.display_hora, minutes, seconds));
        binding.txtElapsedTimeVideo.setText(getString(R.string.display_hora, minutes, seconds));
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

    public Exercise getSiguienteEjercicio() {
        int auxCycleIdx = currentCycleIdx, auxExerciseIdx = currentExerciseIdx;

        if(auxCycleIdx == -1) {
            //Primer ejercicio
            auxCycleIdx = 0;
            auxExerciseIdx = 0;
        } else {
            auxExerciseIdx++;
        }
        if(auxCycleIdx < fullCycles.size()) {

            if(fullCycles.get(auxCycleIdx).getExercises() != null && auxExerciseIdx < fullCycles.get(auxCycleIdx).getExercises().size()) {
                return adapter.getExercise(auxCycleIdx, auxExerciseIdx);
            } else {
                auxCycleIdx++;
                if(auxCycleIdx < fullCycles.size()) {
                    auxExerciseIdx = 0;
                    return adapter.getExercise(auxCycleIdx, auxExerciseIdx);
                } else {
                    return null;
                }

            }

        } else {
            return null;
        }
    }

    private void terminarRutina() {
        timerElapsedTime.cancel();
        binding.txtCabeceraCant.setText("");
        binding.txtCabeceraCantVideo.setText("");
        binding.txtCabeceraNombre.setText("");
        binding.txtCabeceraNombreVideo.setText("");
        binding.btnPauseResume.setVisibility(View.GONE);
        binding.btnPauseResumeVideo.setVisibility(View.GONE);
        binding.btnSwitchMode.setVisibility(View.GONE);
        binding.btnSwitchModeVideo.setVisibility(View.GONE);
        binding.vistaSimple.setVisibility(View.GONE);
        binding.btnNext.setVisibility(View.GONE);
        binding.btnNextVideo.setVisibility(View.GONE);
        if(routineYouTubePlayer.isPlaying())
            routineYouTubePlayer.pause();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.titulo_ejecucion_terminada);
        builder.setMessage(getString(R.string.mensaje_ejecucion_terminada, elapsedTime.getMinute(), elapsedTime.getSecond()));
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            cerrarActivity(true);
        });

        alertBackPressed = builder.create();
        alertBackPressed.show();
    }

    private void cerrarActivity(boolean rutinaTerminada) {
        Intent intent = new Intent().putExtra("status", rutinaTerminada);
        setResult(3, intent);
        finish();
    }

    private void asignarEjecucion(int cycleIdx, int exerciseIdx) {
        Exercise exercise = adapter.asignarEjecucion(cycleIdx, exerciseIdx);
        if(!modoSimple) {
            binding.txtCabeceraNombre.setText(exercise.getName());
            binding.txtCabeceraNombreVideo.setText(exercise.getName());
        }
        binding.txtVistaSimpleNombreAhora.setText(exercise.getName());


        binding.btnNext.setVisibility(View.VISIBLE);
        if(exercise.getDuration() > 0) {
            binding.btnNext.setVisibility(View.GONE);
            binding.btnNextVideo.setVisibility(View.GONE);
            if(!modoSimple) {
                binding.txtCabeceraCant.setText(String.valueOf(exercise.getDuration()) + "s");
                binding.txtCabeceraCantVideo.setText(String.valueOf(exercise.getDuration()) + "s");
            }

            binding.txtVistaSimpleCantAhora.setText(String.valueOf(exercise.getDuration()) + "s");
            //Al ser por tiempo debemos mostrar cuenta atras
            timerContarAtrasEjercicio = new Timer();
            timerContarAtrasEjercicio.schedule(new ContarAtrasEjercicio(exercise.getDuration()), 0, 1000);
        } else {
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.btnNextVideo.setVisibility(View.VISIBLE);
            timerContarAtrasEjercicio = null;
            if(!modoSimple) {
                binding.txtCabeceraCant.setText("x"+String.valueOf(exercise.getRepetitions()));
                binding.txtCabeceraCantVideo.setText("x"+String.valueOf(exercise.getRepetitions()));
            }
            binding.txtVistaSimpleCantAhora.setText("x"+String.valueOf(exercise.getRepetitions()));
        }

        Exercise nextExercise = getSiguienteEjercicio();
        if(nextExercise != null) {
            binding.txtVistaSimpleNombreLuego.setText(nextExercise.getName());
        } else {
            binding.txtVistaSimpleNombreLuego.setText("Fin");
        }

        //Tiene video?
        if(exercise.getUrlVideo() != null) {
            iniciarModoVideo();
            //binding.fondoNegro.setMinimumHeight(binding.youtubePlayerView.getHeight());
            onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                    routineYouTubePlayer = youTubePlayer;
                    routineYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);


                    if(!wasRestored) {
                        routineYouTubePlayer.loadVideo(exercise.getUrlVideo().getYouTubeId());
                        routineYouTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {
                                pausar();
                            }

                            @Override
                            public void onLoaded(String s) {
                                resumir();
                                routineYouTubePlayer.play();
                            }

                            @Override
                            public void onAdStarted() {
                                pausar();
                            }

                            @Override
                            public void onVideoStarted() {

                            }

                            @Override
                            public void onVideoEnded() {

                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {

                                Toast.makeText(activity, "Hubo un error al reproducir el video. " + errorReason.toString(), Toast.LENGTH_LONG).show();
                                youTubePlayerFragment.onDestroy();
                                cerrarModoVideo();

                            }
                        });
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    errorVideo(youTubeInitializationResult);
                }
            };
            youTubePlayerFragment.initialize(API_KEY, onInitializedListener);
        } else {
            youTubePlayerFragment.onDestroy();
            cerrarModoVideo();

        }

    }

    private void iniciarModoVideo() {
        enModoVideo = true;
        binding.youtubePlayerView.setVisibility(View.VISIBLE);
        binding.linearLayoutCabeceraVideo.setVisibility(View.VISIBLE);
        binding.imgNoVideo.setVisibility(View.GONE);
        binding.fondoNegro.setVisibility(View.GONE);
        binding.btnPauseResume.setVisibility(View.GONE);
        binding.btnSwitchMode.setVisibility(View.GONE);
        binding.txtElapsedTime.setVisibility(View.GONE);
        binding.txtCabeceraCant.setVisibility(View.GONE);
        binding.txtCabeceraNombre.setVisibility(View.GONE);
        binding.btnNext.setVisibility(View.GONE);
    }
    private void cerrarModoVideo() {
        enModoVideo = false;
        binding.youtubePlayerView.setVisibility(View.GONE);
        binding.linearLayoutCabeceraVideo.setVisibility(View.GONE);
        binding.imgNoVideo.setVisibility(View.VISIBLE);
        binding.fondoNegro.setVisibility(View.VISIBLE);
        binding.btnPauseResume.setVisibility(View.VISIBLE);
        binding.btnSwitchMode.setVisibility(View.VISIBLE);
        binding.txtElapsedTime.setVisibility(View.VISIBLE);
        binding.txtCabeceraCant.setVisibility(View.VISIBLE);
        binding.txtCabeceraNombre.setVisibility(View.VISIBLE);
    }

    private void errorVideo(YouTubeInitializationResult error) {
        final int REQUEST_CODE = 1;

        if(error.isUserRecoverableError()) {
            error.getErrorDialog(this,REQUEST_CODE).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YoutubePlayer (%1$s)", error.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void pausar() {
        timerElapsedTime.cancel();
        if(routineYouTubePlayer != null && routineYouTubePlayer.isPlaying())
            routineYouTubePlayer.pause();
        if(timerContarAtrasEjercicio != null)
            timerContarAtrasEjercicio.cancel();
    }
    private void resumir() {
        timerElapsedTime = new Timer();
        timerElapsedTime.schedule(new SumarSegundo(), 1000, 1000);
        if(routineYouTubePlayer != null && !routineYouTubePlayer.isPlaying())
            routineYouTubePlayer.play();
        if(timerContarAtrasEjercicio != null) {
            timerContarAtrasEjercicio = new Timer();
            timerContarAtrasEjercicio.schedule(new ContarAtrasEjercicio(), 1000, 1000);
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

        ContarAtrasEjercicio(int seconds) {
            secondsRemaining = seconds;
            totalTimeCurrentExercise = seconds;
            activity.runOnUiThread(() -> {
                binding.progressExecution.setProgress(0);
                binding.progressExecution.setVisibility(View.VISIBLE);
            });
        }

        ContarAtrasEjercicio() {
        }
        public void run() {
            if(secondsRemaining == 0) {
                this.cancel();
                activity.runOnUiThread(() -> {
                    binding.progressExecution.setVisibility(View.GONE);
                    marcarRealizado();
                    siguienteEjercicio();
                });
            } else {
                activity.runOnUiThread(() -> {
                    if(modoSimple) {
                        binding.txtVistaSimpleCantAhora.setText(String.valueOf(secondsRemaining) + "s");
                    } else {
                        binding.txtCabeceraCant.setText(String.valueOf(secondsRemaining) + "s");
                        binding.txtCabeceraCantVideo.setText(String.valueOf(secondsRemaining) + "s");
                    }

                    binding.progressExecution.setProgress((int) (100 - (Double.valueOf(secondsRemaining)/totalTimeCurrentExercise)*100));
                    secondsRemaining--;
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
                        cerrarActivity(false);
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




}
