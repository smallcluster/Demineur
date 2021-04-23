package com.pierre.demineur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Switch;
import android.widget.TextView;


public class Partie extends AppCompatActivity {

    private Grille grille;
    private Switch sDrapeau;
    private TextView texteDrapeaux, texteFin, texteScore;
    private ConstraintLayout finPartieLayout;

    private float PBombe = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);

        grille = findViewById(R.id.grille);
        sDrapeau = findViewById(R.id.switchDrapeau);
        texteDrapeaux = findViewById(R.id.textDrapeaux);

        finPartieLayout = findViewById(R.id.finPArtie);
        texteFin = findViewById(R.id.texteFin);
        texteScore = findViewById(R.id.texteScore);
        finPartieLayout.setVisibility(View.GONE);

        // On initialise la vue du jeu
        grille.init(this, PBombe);

    }



    public void toggleDrapeau(View v){
        grille.setModeDrapeau(sDrapeau.isChecked());
    }
    public void setDrapeauxRestants(int v){ texteDrapeaux.setText(Integer.toString(v)); }

    public void perdu(){
        finPartieLayout.setVisibility(View.VISIBLE);
        texteFin.setText("PERDU");
        texteScore.setText("Score : " + Integer.toString(grille.getScore())+"%");
    }

    public void gagne(){
        finPartieLayout.setVisibility(View.VISIBLE);
        texteFin.setText("GANGÉ");
        texteScore.setText("Score : 100%");
    }

    public void rejouer(View v){
        finPartieLayout.setVisibility(View.GONE);
        grille.init(this, PBombe);
    }

    public void quitter(View v){
        super.onBackPressed();
    }

    public void setDifficulte(int d){
        switch (d){
            case 0: PBombe = 0.1f; break;
            case 1: PBombe = 0.2f; break;
            case 2: PBombe = 0.3f; break;
        }
    }

}