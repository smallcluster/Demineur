package com.pierre.demineur;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import static com.pierre.demineur.CaseEtats.*;

public class Case {
    // id d'instanciation
    public static int newId = 0;

    // id actuel de la case
    public final int id;

    // Indique si la case est une bombe
    private boolean bombe;

    // Etat de la case (fermée, ouverte, drapeau)
    private CaseEtats etat;

    // Le nombre de cases voisines (carré de 3x3) contenant une bombe.
    private int nbBombes;

    // Couleur de la case
    private int couleurFermee;
    private int couleurOuverte;

    // Position et taille en pixels
    private float x,y;
    private float taille;

    Case(){
        id = newId++;
        this.bombe = false;
        etat = FERMEE;
        nbBombes = 0;
    }

    public void draw(Canvas canvas, Paint paint, Drawable imgDrapeau){
        paint.setColor(etat == OUVERTE ? couleurOuverte : couleurFermee);
        canvas.drawRect(x, y, x+taille, y+taille, paint);
        if(etat == OUVERTE && bombe){
            paint.setColor(Color.BLACK);
            canvas.drawCircle(x+taille/2,y+taille/2,taille/4, paint);
        } else if(etat == OUVERTE && nbBombes > 0){
            paint.setTextSize(taille/2);
            paint.setTextAlign(Paint.Align.CENTER);
            if(nbBombes == 2) paint.setColor(Color.GREEN);
            else if(nbBombes == 1) paint.setColor(Color.BLUE);
            else paint.setColor(Color.RED);
            canvas.drawText(Integer.toString(nbBombes), x+taille/2, y+taille/1.5f, paint);
        } else if(etat == DRAPEAU){
            int width = imgDrapeau.getIntrinsicWidth();
            int height = imgDrapeau.getIntrinsicHeight();

            if(width < height){
                float scale = (float)width/ (float) height;
                imgDrapeau.setBounds((int)x, (int)y, (int) (x+taille*scale), (int)(y+taille));
            } else {
                float scale = (float )height/ (float) width;
                imgDrapeau.setBounds((int)x, (int)y, (int) (x+taille), (int)(y+taille*scale));
            }
            imgDrapeau.draw(canvas);
        }
    }

    public void setX(float x){this.x = x;}
    public void setY(float y){this.y = y;}
    public void setTaille(float taille){this.taille = taille;}

    public float getX(){return x;}
    public float getY(){return y;}
    public float getTaille(){return taille;}

    public int toggleDrapeau(){
        if(etat != OUVERTE)
            etat = etat == DRAPEAU ? FERMEE : DRAPEAU;
        return etat == DRAPEAU ? -1 : 1;
    }

    public void setCouleurFermee(int r, int g, int b){
        couleurFermee = Color.rgb(r,g,b);
    }
    public void setCouleurOuvert(int r, int g, int b){
        couleurOuverte= Color.rgb(r,g,b);
    }

    public CaseEtats getEtat(){return  etat;}
    public void setEtat(CaseEtats e){etat = e;}
    public int getId(){return id;}
    public boolean getBombe(){return bombe;}
    public void setBombe(boolean b){bombe = b;}
    public int getNbBombes(){return nbBombes;}
    public void setNbBombes(int n){nbBombes = n;}
}
