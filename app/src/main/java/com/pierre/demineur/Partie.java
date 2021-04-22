package com.pierre.demineur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class Partie extends AppCompatActivity {

    private Button[][] grid;
    private int buttonSize;
    private int nx, ny;
    private FrameLayout plateau;
    private int width, height;
    private Display display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);

        display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        plateau = findViewById(R.id.Plateau);

        ViewTreeObserver vto = plateau.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                plateau.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                placeButtons();
                updateGridLayout();
            }
        });
    }

    public void placeButtons(){
        nx = 10;
        ny = 18;
        grid = new Button[ny][nx];
        for (int i=0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                Button b = new Button(this);
                grid[i][j] = b;
                b.setText(Integer.toString(i*nx+j));
                b.setBackgroundColor(Color.BLUE);
                plateau.addView(b);
            }
        }
    }

    public void updateGridLayout(){
        int rotation = display.getRotation();
        if(rotation == Surface.ROTATION_0){
            width = plateau.getMeasuredWidth();
            height = plateau.getMeasuredHeight();
        } else {
            height = plateau.getMeasuredWidth();
            width = plateau.getMeasuredHeight();
        }
        buttonSize = Math.min(width/nx, height/ny);
        for (int i=0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                Button b = grid[i][j];
                b.setLayoutParams(new FrameLayout.LayoutParams(buttonSize, buttonSize));
                if(rotation == Surface.ROTATION_0){
                    b.setX(j*buttonSize);
                    b.setY(i*buttonSize);
                } else {
                    b.setY(j * buttonSize);
                    b.setX(i * buttonSize);
                }
            }
        }
        plateau.invalidate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        ViewTreeObserver vto = plateau.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                plateau.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateGridLayout();
            }
        });
        super.onConfigurationChanged(newConfig);
    }

}