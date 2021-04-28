package com.pierre.demineur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class PartieActivity extends AppCompatActivity {

    // La classe du "jeu" en quelque sorte
    private Grille grille;

    // Pour switcher entre poser des drapeaux ou creuser des cases
    private Switch sDrapeau;
    private ImageView imageViewFlagSwitch;

    // Textes
    private TextView texteDrapeaux, texteFin, texteScore, textHorloge;

    // Layout qui contient le screen de fin de partie
    private ConstraintLayout finPartieLayout;


    // Paramètres de difficulté
    private float PBombe = 0.1f; // Proportion de bombes
    private int n = 10;          // Nombre de lignes

    // Compte le temps écoulé depuis le début de la partie en seconde
    private Timer timer;
    private long timeInSec = 0;

    // Le niveau de difficulté actuel
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);


        // On recup le niveau de difficulté choisi par l'utilisateur
        Intent intent = getIntent();
        level = intent.getIntExtra("level", 0);

        // On ajuste la proportion de bombes et la quantité de lignes en fonction de la
        // difficulté
        switch (level){
            case 0: // FACILE
                PBombe = 0.1f;
                n = 10;
                break;
            case 1: // MOYEN
                PBombe = 0.13f;
                n = 15;
                break;
            case 2: // DIFFICILE
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

        // On écoute le changement d'état du switch (click + drag&release)
        sDrapeau.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleDrapeau(sDrapeau);
            }
        });

        // On initialise le jeu avec les paramètres de difficulté
        grille.init(this, PBombe, n);
        // On commence à compter le temps qui passe
        lancerTimer();

    }

    // Switch entre le mode "poser des drapeaux" ou le mode "creuser des cases"
    public void toggleDrapeau(View v){
        boolean modeDrapeau = sDrapeau.isChecked();
        grille.setModeDrapeau(modeDrapeau); // Passe l'info au jeu
        // Feedback graphique
        if(modeDrapeau)
            imageViewFlagSwitch.setImageDrawable(getDrawable(R.drawable.flag));
        else
            imageViewFlagSwitch.setImageDrawable(getDrawable(R.drawable.flag_disabled));
    }
    // Affiche le nombre de drapeaux restants
    public void setDrapeauxRestants(int v){ texteDrapeaux.setText(Integer.toString(v)); }

    // Quand on perd
    public void perdu(){
        // Sauvegarde des stats de la partie
        saveCurrentStat();
        // On affiche l'écran de fin
        finPartieLayout.setVisibility(View.VISIBLE);
        texteFin.setText("PERDU");
        texteScore.setText("Score : " + Integer.toString(grille.getScore())+" %");
        // On stoppe le timer
        stopperTimer();
    }

    public void gagne(){
        // Sauvegarde des stats de la partie
        saveCurrentStat();
        // On affiche l'écran de fin
        finPartieLayout.setVisibility(View.VISIBLE);
        texteFin.setText("GAGNÉ");
        texteScore.setText("Score : 100%");
        // On stoppe le timer
        stopperTimer();
    }

    // Sauvegarde les stats de la partie dans la liste des statistiques de l'activité MainActivity
    private void saveCurrentStat(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String score = Integer.toString(grille.getScore())+"%";
        String temps = String.format("%02d:%02d", timeInSec/60, timeInSec % 60);
        String difficulte = Integer.toString(level);
        String data = currentDate+";"+score+";"+temps+";"+difficulte;

        MainActivity.stats.add(0, data);
    }

    // Pour relancer une nouvelle partie avec la même difficulté
    public void rejouer(View v){
        // On cache l'écran de fin
        finPartieLayout.setVisibility(View.GONE);
        // On relance le timer
        lancerTimer();
        // On réunitialise l'UI
        sDrapeau.setChecked(false);
        grille.setModeDrapeau(false);
        imageViewFlagSwitch.setImageDrawable(getDrawable(R.drawable.flag_disabled));
        // On réinitialise le jeu
        grille.init(this, PBombe, n);
    }

    // Pour revenir au menu principal
    public void quitter(View v){
        super.onBackPressed();
    }

    // Lance/relance le timer
    public void lancerTimer(){
        // On stoppe le timer
        stopperTimer();
        // Temps écoulé = 0
        timeInSec = 0;
        // Actualise l'UI
        textHorloge.setText("00:00");
        // Instanciation d'un nouveau timer
        timer = new Timer();
        // Actualise l'affichage toute les secondes
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

    // Stoppe et détruit le Timer
    public void stopperTimer(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
