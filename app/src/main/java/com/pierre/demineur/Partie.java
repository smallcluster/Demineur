package com.pierre.demineur;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class Partie extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);
        Grille grille = findViewById(R.id.grille);
        grille.setPBombe(0.2f);
    }



}