package com.pierre.demineur;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private static final int PARTIE = 1;

    private RadioGroup levelSelector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        levelSelector = findViewById(R.id.levelSelector);

    }

    public void jouer(View v){
        Intent intent = new Intent(this, Partie.class);

        int radioID = levelSelector.getCheckedRadioButtonId();
        RadioButton radioLevel = findViewById(radioID);

        intent.putExtra("level", Integer.parseInt(radioLevel.getTag().toString()));
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
}