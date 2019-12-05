package br.com.cristiano.etanolougasolina.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

import br.com.cristiano.etanolougasolina.R;

public class SplashActivity extends AppCompatActivity {

    private int i;
    private Timer timer;
    private ProgressBar progressBar;
    private long periodo = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (i < 100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(i);
                        }
                    });

                    i++;
                }else{

                    timer.cancel();
                    Intent intent =new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 0, periodo);

    }
}
