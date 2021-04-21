package com.pierre.demineur;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int PARTIE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void jouer(View v){
        Intent intent = new Intent(this, Partie.class);
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