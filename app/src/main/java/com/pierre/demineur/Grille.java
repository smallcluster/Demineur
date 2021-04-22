package com.pierre.demineur;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import static com.pierre.demineur.CaseEtats.*;

/**
 * Plateau du jeu
 * Gère l'affichage, l'interraction avec l'utilisateur et le gameplay
 */
public class Grille extends View {
    // Dimension du plateaux (en nombre de cases)
    private int n; // nb lignes
    private int m; // nb colonnes

    // Tableau qui contient les cases
    private Case[][] cases;

    // Probabilité pour qu'une case soit une bombe
    private float PBombe = 0.2f;


    private float tailleCase;
    private Display display;
    private int rotation;

    public Grille(Context context) {
        super(context);
        init(context);
    }

    public Grille(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setPBombe(float p){PBombe = p;}

    private void init(Context context){
        // Format adapté pour le 21/9 ème
        n = 20;
        m = (int) (n/2.33);

        cases = new Case[n][m];
        for(int i=0; i < n; i++){
            for(int j=0; j<m; j++){
                Case c = new Case();
                if(Math.random() < PBombe) c.setBombe(true);
                c.setEtat(OUVERTE);
                cases[i][j] = c;
            }
        }

        // Pour intéroger l'appareil sur sa rotation
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        // On détermine dans quel sens le téléphone se trouve.
        rotation = display.getRotation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float xOffset;
        float yOffset;

        // On détermine dans quel sens le téléphone se trouve.
        rotation = display.getRotation();

        // On lit les dimensions de la view qui sont déterminées par les contraintes de
        // l'activité Partie
        float width = getWidth();
        float height = getHeight();

        // Taille et offset pour centrer la grille en fonction la rotation de l'écran
        if(Surface.ROTATION_0 == rotation){
            tailleCase = Math.min(width/m, height/n);
            xOffset = (width - m* tailleCase)/2.0f;
            yOffset = (height - n* tailleCase)/2.0f;
        } else {
            tailleCase = Math.min(width/n, height/m);
            //tailleCase = height/m;
            xOffset = (width - n* tailleCase)/2.0f;
            yOffset = (height - m* tailleCase)/2.0f;
        }

        Paint paint = new Paint();
        paint.setTextSize(tailleCase/2);
        paint.setTextAlign(Paint.Align.CENTER);

        for(int i=0; i < n; i++){
            for(int j=0; j < m; j++){

                // Positions de la case en fonction de la rotation de l'écran
                float x = Surface.ROTATION_0 == rotation ? j*tailleCase+xOffset : i*tailleCase+xOffset;
                float y = Surface.ROTATION_0 == rotation ? i*tailleCase+yOffset : j*tailleCase+yOffset;

                Case c = cases[i][j];

                switch (c.getEtat()){
                    case FERMEE:
                        // On alterne les couleurs pour créer un damier
                        if( (i+j) % 2 == 0) paint.setColor(Color.rgb(155, 210, 64));
                        else paint.setColor(Color.rgb(147, 202, 57));
                        // On dessine la case
                        canvas.drawRect(x, y, x + tailleCase, y+ tailleCase, paint);
                        break;
                    case OUVERTE:
                        // On alterne les couleurs pour créer un damier
                        if( (i+j) % 2 == 0) paint.setColor(Color.rgb(229, 194, 159));
                        else paint.setColor(Color.rgb(215, 184, 153));
                        // On dessine la case
                        canvas.drawRect(x, y, x + tailleCase, y+ tailleCase, paint);

                        // Affiche la bombe si il y en a une
                        if(c.getBombe()){
                            paint.setColor(Color.BLACK);
                            canvas.drawCircle(x+tailleCase/2,y+tailleCase/2,tailleCase/4, paint);
                        }
                        // Sinon on affiche le nombre de bombes voisines
                        else {
                            int nbBombes = c.getNbBombes();
                            // Si il n'y a pas de bombes voisines on ne surcharge pas le joueur
                            // d' informations inutiles.
                            if(nbBombes>=0){
                                if(nbBombes >= 3) paint.setColor(Color.RED);
                                else if(nbBombes == 2) paint.setColor(Color.GREEN);
                                else if(nbBombes == 1) paint.setColor(Color.BLUE);
                                else paint.setColor(Color.CYAN);
                                canvas.drawText(Integer.toString(nbBombes), x+tailleCase/2, y+tailleCase/1.5f, paint);
                            }
                        }
                        break;
                }
            }
        }
        super.onDraw(canvas);
    }
}