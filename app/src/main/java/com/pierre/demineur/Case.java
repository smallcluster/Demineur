package com.pierre.demineur;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import static com.pierre.demineur.CaseEtats.*;

public class Case {
    // Indique si la case est une bombe
    private boolean bombe;

    // Etat de la case (fermée, ouverte, drapeau)
    private CaseEtats etat;

    // Le nombre de cases voisines (carré de 3x3) contenant une bombe.
    private int nbBombes;

    // Couleurs de la case
    private int couleurFermee;
    private int couleurOuverte;

    // Position et taille en pixels
    private float x,y;
    private float taille;

    Case(){
        this.bombe = false;
        etat = FERMEE;
        nbBombes = 0;
    }

    public void draw(Canvas canvas, Paint paint, Drawable imgDrapeau, Drawable imgBombe){
        paint.setColor(etat == OUVERTE ? couleurOuverte : couleurFermee);
        canvas.drawRect(x, y, x+taille, y+taille, paint); // arrière plan de la case

        // Affiche la bombe
        if(etat == OUVERTE && bombe){
            drawImg(canvas, imgBombe, 0.80f);
        }
        // Affiche le nombe de bombes dans son voisinage
        else if(etat == OUVERTE && nbBombes > 0){
            paint.setTextSize(taille/2);
            paint.setTextAlign(Paint.Align.CENTER);
            if(nbBombes == 2) paint.setColor(Color.rgb(0, 200, 0));
            else if(nbBombes == 1) paint.setColor(Color.BLUE);
            else paint.setColor(Color.RED);
            canvas.drawText(Integer.toString(nbBombes), x+taille/2, y+taille/1.5f, paint);
        }
        // Affiche le drapeau
        else if(etat == DRAPEAU){
            drawImg(canvas, imgDrapeau, 0.85f);
        }
    }

    // Dessine une image au centre de la case, où un scale de 1 vaut taille, tout en conservant
    // le ratio
    private void drawImg(Canvas canvas, Drawable img, float scale){

        int imgWidth = img.getIntrinsicWidth();
        int imgHeight = img.getIntrinsicHeight();

        float targetWidth, targetHeight;

        // On conserve le ratio de l'image
        if(imgWidth < imgHeight){
            float ratio = (float) imgWidth/(float)imgHeight;
            targetWidth = taille*scale*ratio;
            targetHeight = taille*scale;
        } else {
            float ratio = (float )imgHeight/ (float) imgHeight;
            targetWidth = taille*scale;
            targetHeight = taille*scale*ratio;
        }

        // Décalage pour centrer l'image
        float xOffset = (taille-targetWidth) / 2.0f;
        float yOffset = (taille-targetHeight) / 2.0f;

        // Repositionnement et redimensionnement de l'image
        img.setBounds((int) (x+xOffset), (int) (y+yOffset), (int) (x+xOffset+targetWidth), (int)(y+yOffset+targetHeight));

        img.draw(canvas);
    }

    public void setX(float x){this.x = x;}
    public void setY(float y){this.y = y;}
    public void setTaille(float taille){this.taille = taille;}
    public void setCouleurFermee(int r, int g, int b){
        couleurFermee = Color.rgb(r,g,b);
    }
    public void setCouleurOuvert(int r, int g, int b){
        couleurOuverte= Color.rgb(r,g,b);
    }
    public CaseEtats getEtat(){return  etat;}
    public void setEtat(CaseEtats e){etat = e;}
    public boolean getBombe(){return bombe;}
    public void setBombe(boolean b){bombe = b;}
    public int getNbBombes(){return nbBombes;}
    public void setNbBombes(int n){nbBombes = n;}

    // Switch entre l'état drapeau et fermée si c'est possible
    // Retourne le cout de l'opération en nombre de drapeau
    public int toggleDrapeau(){
        if(etat == OUVERTE) return 0; // On ne fait rien, on ne change pas la quantitée de drapeau
        else {
            etat = etat == DRAPEAU ? FERMEE : DRAPEAU;
            // Si on place un drapeau, il faut diminuer le compteur de drapeau de 1
            // Si on retire un drapeau, il faut augmenter le compteur de drapeau de 1
            return etat == DRAPEAU ? -1 : 1;
        }
    }
}
