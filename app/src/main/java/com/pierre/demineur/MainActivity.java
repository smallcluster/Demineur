package com.pierre.demineur;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup levelSelector;

    // Format des données: "dd-mm-yyyy;score %;temps;difficulte"
    // avec score entre 0 et 100 entier, temps en format 00:00 et difficulté vaut:
    // 0 pour facile, 1 pour moyen et 2 pour difficile
    // Static pour faciliter l'ajout de données depuis l'activité PartieActivity
    public static ArrayList<String> stats = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Radio group du niveau de difficulté
        levelSelector = findViewById(R.id.levelSelector);

        // On charge l'historique des parties en mémoire
        try {
            chargerDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lance l'activité du jeu avec le bon niveau de difficulté
    public void jouer(View v){
        Intent intent = new Intent(this, PartieActivity.class);

        // On recup la difficuté choisie
        int radioID = levelSelector.getCheckedRadioButtonId();
        RadioButton radioLevel = findViewById(radioID);
        int level = Integer.parseInt(radioLevel.getTag().toString());

        intent.putExtra("level", level);
        startActivity(intent); // On lance PartieActivity
    }

    // Lance l'activité de l'historique des scores
    public void voirScores(View v){
        Intent intent = new Intent(this, ScoresActivity.class);
        intent.putStringArrayListExtra("array", stats); // On lui donne les données
        startActivity(intent); // On lance ScoresActivity
    }

    // Sauvegarde l'historique des parties dans le stockage réservé à l'application
    public void sauvegarderDonnees() throws IOException {
        File file = new File(getFilesDir(), "stats.txt");
        // Si le fichier exste on le supprime
        if(file.exists()) file.delete();

        FileOutputStream stream = new FileOutputStream(file);
        try {
            // les stats sont stockés ligne par ligne
            int length = stats.size();
            for(int i=0; i < length-1; i++){
                stream.write((stats.get(i)+"\n").getBytes());
            }
            // Pas de retour à la ligne pour la dernière stat
            stream.write((stats.get(length-1)).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
    }

    @Override
    protected void onDestroy() {
        // On sauvegarde l'historique des parties quand l'application se ferme
        try {
            sauvegarderDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    // Charge l'historique des parties dans le stockage réservé à l'application
    public void chargerDonnees() throws IOException {
        File file = new File(getFilesDir(), "stats.txt");
        // Si il n'y a pas de sauvegarde on ne charge rien
        if(!file.exists()) return;

        // Lectures du flux de byte
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        // Conversion des bytes en un seul string
        String contents = new String(bytes);

        // Les stats sont stockées ligne par ligne, on découpe et on ajoute en mémoire
        String[] data = contents.split("\n");
        for (int i=0; i < data.length; i++){
            stats.add(data[i]);
        }
    }
}