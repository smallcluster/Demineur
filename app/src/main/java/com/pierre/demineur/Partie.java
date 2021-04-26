package com.pierre.demineur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class Partie extends AppCompatActivity {

    private Grille grille;
    private Switch sDrapeau;
    private TextView texteDrapeaux, texteFin, texteScore;
    private ConstraintLayout finPartieLayout;
    private ImageView imageViewFlagSwitch;

    private float PBombe = 0.1f;
    private int n = 10;

    private Timer timer;
    private long timeInSec = 0;
    private TextView textHorloge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);


        Intent intent = getIntent();
        int level = intent.getIntExtra("level", 0);
        switch (level){
            case 0:
                PBombe = 0.1f;
                n = 10;
                break;
            case 1:
                PBombe = 0.13f;
                n = 15;
                break;
            case 2:
                PBombe = 0.15f;
                n = 20;
                break;
        }

        grille = findViewById(R.id.grille);
        sDrapeau = findViewById(R.id.switchDrapeau);
        texteDrapeaux = findViewById(R.id.textDrapeaux);
        imageViewFlagSwitch = findViewById(R.id.imageViewFlagSwitch);

        finPartieLayout = findViewById(R.id.finPArtie);
        texteFin = findViewById(R.id.texteFin);
        texteScore = findViewById(R.id.texteScore);
        finPartieLayout.setVisibility(View.GONE);

        textHorloge = findViewById(R.id.textHorloge);




        // On initialise la vue du jeu
        grille.init(this, PBombe, n);
        lancerTimer();

    }

    public void toggleDrapeau(View v){
        boolean modeDrapeau = sDrapeau.isChecked();
        grille.setModeDrapeau(modeDrapeau);
        if(modeDrapeau)
            imageViewFlagSwitch.setImageDrawable(getDrawable(R.drawable.flag));
        else
            imageViewFlagSwitch.setImageDrawable(getDrawable(R.drawable.flag_disabled));
    }
    public void setDrapeauxRestants(int v){ texteDrapeaux.setText(Integer.toString(v)); }

    public void perdu(){
        finPartieLayout.setVisibility(View.VISIBLE);
        texteFin.setText("PERDU");
        texteScore.setText("Score : " + Integer.toString(grille.getScore())+"%");
        stopperTimer();
    }

    public void gagne(){
        finPartieLayout.setVisibility(View.VISIBLE);
        texteFin.setText("GANGÉ");
        texteScore.setText("Score : 100%");
        stopperTimer();
    }

    public void rejouer(View v){
        finPartieLayout.setVisibility(View.GONE);
        lancerTimer();
        grille.init(this, PBombe, n);
    }

    public void quitter(View v){
        super.onBackPressed();
    }

    public void lancerTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        timeInSec = 0;
        textHorloge.setText("00:00");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String time = String.format("%02d:%02d", timeInSec/60, timeInSec % 60);
                        textHorloge.setText(time);
                        timeInSec++;
                    }
                });
            }
        }, 1000, 1000);
    }

    public void stopperTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
