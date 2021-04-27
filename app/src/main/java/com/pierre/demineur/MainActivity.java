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

    private static final int PARTIE = 1;
    private static final int SCORES = 2;

    private RadioGroup levelSelector;

    // Format des données: "dd-mm-yyyy;score %;temps;difficulte"
    // avec score entre 0 et 100 entier, temps en format 00:00 et diffuculte vaut:
    // 0 pour facile, 1 pour moyen et 2 pour difficile
    // Static pour faciliter l'ajout de données depuis l'activitée PartieActivity
    public static ArrayList<String> stats = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        levelSelector = findViewById(R.id.levelSelector);
        try {
            chargerDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void jouer(View v){
        Intent intent = new Intent(this, PartieActivity.class);

        int radioID = levelSelector.getCheckedRadioButtonId();
        RadioButton radioLevel = findViewById(radioID);

        intent.putExtra("level", Integer.parseInt(radioLevel.getTag().toString()));
        startActivityForResult(intent, PARTIE);
    }

    public void voirScores(View v){
        Intent intent = new Intent(this, ScoresActivity.class);
        intent.putStringArrayListExtra("array", stats);
        startActivityForResult(intent, PARTIE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){

            case PARTIE:
                switch (resultCode){
                    case RESULT_OK:
                        break;
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sauvegarderDonnees() throws IOException {
        File file = new File(getFilesDir(), "stats.txt");
        if(file.exists()) file.delete();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            int length = stats.size();
            for(int i=0; i < length-1; i++){
                stream.write((stats.get(i)+"\n").getBytes());
            }
            stream.write((stats.get(length-1)).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            sauvegarderDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void chargerDonnees() throws IOException {
        File file = new File(getFilesDir(), "stats.txt");
        if(!file.exists()) return;
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
        String contents = new String(bytes);
        String[] data = contents.split("\n");
        for (int i=0; i < data.length; i++){
            stats.add(data[i]);
        }
    }
}