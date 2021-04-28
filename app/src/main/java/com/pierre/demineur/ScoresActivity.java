package com.pierre.demineur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {

    // Liste des statistiques
    private ArrayList<String> stats;
    // view avec des catégories pour afficher les stats sous forme de liste
    private ExpandableListView expandableListView;

    // List des catégories
    private List<String> listGroup;

    // listes des items par catégorie
    private HashMap<String, List<String>> listItem;

    // Gestionnaire pour gérer expandableListView
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        // Init ExpandableListView
        expandableListView = (ExpandableListView) findViewById(R.id.listScore);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();

        // On recup les stats depuis la vue principale
        Intent intent = getIntent();
        stats = intent.getStringArrayListExtra("array");

        // On ajoute les catégories
        listGroup.add("Facile");
        listGroup.add("Moyen");
        listGroup.add("Difficile");

        // On tri les stats par catégorie
        List<String> listFacile = new ArrayList<>();
        List<String> listMoyen = new ArrayList<>();
        List<String> listDifficile = new ArrayList<>();

        // voir la variable "stats" de MainActivity pour le format des données
        for (String s : stats){
            if(s.endsWith("0")) listFacile.add(s);
            else if(s.endsWith("1")) listMoyen.add(s);
            else if(s.endsWith("2")) listDifficile.add(s);
        }

        // On ajoute les données dans leurs catégories respectives.
        listItem.put(listGroup.get(0), listFacile);
        listItem.put(listGroup.get(1), listMoyen);
        listItem.put(listGroup.get(2), listDifficile);

        // Création du gestionnaire
        mainAdapter = new MainAdapter(this, listGroup, listItem);
        // On assigne le gestionnaire à la liste view
        expandableListView.setAdapter(mainAdapter);
    }
}