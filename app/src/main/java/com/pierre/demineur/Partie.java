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

    Grille grille;
    Switch sDrapeau;
    TextView textDrapeaux;
    int drapeauxRestants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);
        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        grille = findViewById(R.id.grille);
        sDrapeau = findViewById(R.id.switchDrapeau);
        textDrapeaux = findViewById(R.id.textDrapeaux);

        // On initialise la vue du jeu
        grille.init(this, 0.2f);
    }

    public void toggleDrapeau(View v){
        grille.setModeDrapeau(sDrapeau.isChecked());
    }

    public int getDrapeauxRestants(){return drapeauxRestants;}
    public void setDrapeauxRestants(int v){
        drapeauxRestants = v;
        textDrapeaux.setText(Integer.toString(drapeauxRestants));
    }

}