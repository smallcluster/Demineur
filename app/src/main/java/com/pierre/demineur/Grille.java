package com.pierre.demineur;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.provider.Telephony;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import static com.pierre.demineur.CaseEtats.*;

/**
 * Plateau du jeu
 * Gère l'affichage, l'interaction avec l'utilisateur et le gameplay
 */
public class Grille extends View {

    // Dimension du plateaux (en nombre de cases)
    private int n = 0; // nb lignes
    private int m = 0; // nb colonnes

    // Tableau qui contient les cases
    private Case[][] cases;

    // Taille en pixels des cases
    private float tailleCase;
    // Décalage en pixel pour centrer les cases dans la vue
    private float xOffset, yOffset;

    // Pour obtenir la rotation de l'écran
    private Display display;

    // Permet de passer en mode "placer des drapeaux" au lieu de creuser les cases
    private boolean modeDrapeau = false;

    // Image du drapeau
    private Drawable imgDrapeau;

    // Pour communiquer le nombre de drapeaux restants ainsi que si l'on a gragné ou perdu
    // à l'activité parent
    private Partie partie;


    public Grille(Context context) {
        super(context);
    }

    public Grille(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModeDrapeau(boolean b){modeDrapeau = b;}

    public void init(Partie partie, float PBombe){
        // Référence à l'activité partie
        this.partie = partie;

        // On récup l'image du drapeau
        imgDrapeau = partie.getDrawable(R.drawable.flag);

        // Format adapté pour le 21/9 ème
        n = 20;
        m = (int) (n/2.33);

        // On ajoute les cases et on compte le nombre de bombes placées
        cases = new Case[n][m];
        int nbBombes = 0;
        for(int i=0; i < n; i++){
            for(int j=0; j<m; j++){
                Case c = new Case();
                // On alterne les couleurs pour créer un damier
                if( (i+j) % 2 == 0){
                    c.setCouleurOuvert(229, 194, 159);
                    c.setCouleurFermee(155, 210, 64);
                } else {
                    c.setCouleurOuvert(215, 184, 153);
                    c.setCouleurFermee(147, 202, 57);
                }
                // la case a une probabilité PBombe de d'être une bombe
                if(Math.random() < PBombe){
                    c.setBombe(true);
                    nbBombes++;
                }
                cases[i][j] = c;
            }
        }

        // Le nombre de drapeaux à placer = le nombre total de bombes
        partie.setDrapeauxRestants(nbBombes);

        // On détermine pour chaque case, combien de bombes il y a dans son voisinage
        for(int i=0; i < n; i++){
            for(int j=0; j<m; j++){
                Case c = cases[i][j];
                // Cases dans le carré 3x3 centré sur c
                for(int i2=-1; i2 < 2; i2++){
                    for (int j2=-1; j2 <2; j2++){
                        // On s'assure que l'on reste dans les limites de la grille
                        // et on ignore la cellule centrale (la case actuelle)
                        if(i+i2 >= 0 && i+i2 < n && j+j2 >=0 && j+j2 < m && !(j2 == 0 && i2==0)){
                            Case c2 = cases[i+i2][j+j2]; // Case voisine
                            if(c2.getBombe()) c.setNbBombes(c.getNbBombes()+1);
                        }
                    }
                }
            }
        }
        // Pour intérroger l'appareil sur sa rotation
        display = ((WindowManager) partie.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // On détermine dans quel sens le téléphone se trouve.
        int rotation = display.getRotation();

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
        for(int i=0; i < n; i++){
            for(int j=0; j < m; j++){
                // Positions de la case en fonction de la rotation de l'écran
                float x = Surface.ROTATION_0 == rotation ? j*tailleCase+xOffset : i*tailleCase+xOffset;
                float y = Surface.ROTATION_0 == rotation ? i*tailleCase+yOffset : j*tailleCase+yOffset;
                Case c = cases[i][j];
                c.setX(x);
                c.setY(y);
                c.setTaille(tailleCase);
                c.draw(canvas, paint, imgDrapeau);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);   // this super call is important !!!

        // Coordonnées du pointeur
        float mx = event.getX();
        float my = event.getY();

        // En mode landscape (rotation != 0) on prend la transposée de la grille
        // c'est à dire on inverse n et m
        int rotation = display.getRotation();
        int tm = Surface.ROTATION_0 != rotation ? n : m;
        int tn = Surface.ROTATION_0 != rotation ? m : n;

        // Le pointeur doit être dans la zone de dessin de la grille si non on ne fait rien
        // On prend en compte la transposée si besoin
        if(mx < xOffset || mx > xOffset+tailleCase*tm || my < yOffset || my > yOffset+tailleCase*tn)
            return true;

        // On recup les coordonnées de la case dans la grille en fonction de la rotation
        // En mode landscape (rotation != 0) on prend la transposée
        int j = (int) ((mx - xOffset) / tailleCase);
        int i = (int) ((my - yOffset) / tailleCase);
        // On permute i et j si besoin
        if(Surface.ROTATION_0 != rotation){ int tmp=i; i=j; j=tmp;}

        // On teste maintenant que le joueur a appuyé sur la case
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            // En mode drapeau on ajoute ou on retire des drapeaux
            if(modeDrapeau){
                int nbDrapeaux = partie.getDrapeauxRestants();
                // On ne peut que retirer des drapeaux quand le compteur est à 0
                if( nbDrapeaux == 0 && cases[i][j].getEtat() == DRAPEAU)
                    partie.setDrapeauxRestants(nbDrapeaux + cases[i][j].toggleDrapeau());
                else if(nbDrapeaux > 0)
                    partie.setDrapeauxRestants(nbDrapeaux + cases[i][j].toggleDrapeau());
            } else
                creuserCase(i,j);
            // On redessine le plateau
            invalidate();
        }
        return true;
    }

    private void creuserCase(int i, int j){
        Case c = cases[i][j];

        // On ne peut pas creuser les drapeau
        if(c.getEtat() == DRAPEAU)
            return;
        // C'est une bombe => fin de la partie
        if(c.getEtat() == FERMEE && c.getBombe()){
            c.setEtat(OUVERTE);
            return;
        }

        // On ouvre la case actuelle
        c.setEtat(OUVERTE);

        // Si il y a des bombes dans son voisinage, on ne creuse pas en automatique autour d'elle
        if(c.getNbBombes() > 0)
            return;

        // C'était une case vide, on creuse en automatique en croix
        if(i+1 < n) creuserAuto(i+1, j);
        if(i-1 >= 0) creuserAuto(i-1, j);
        if(j+1 < m) creuserAuto(i, j+1);
        if(j-1 >= 0) creuserAuto(i, j-1);
    }

    private void creuserAuto(int i, int j){
        Case c = cases[i][j];

        // Cas de base
        // 1 : Une bombe ne peut être creusée
        // 2 : Une case fermée avec des bombes dans son voisinage est creusée et on s'arrête
        // 3 : Une case vide ne peut être crusée
        // 4 : Un drapeau ne peut être creusé
        if(c.getBombe())
            return;
        else if((c.getEtat() == FERMEE && c.getNbBombes() > 0)){
            c.setEtat(OUVERTE);
            return;
        } else if(c.getEtat() == OUVERTE
                || c.getEtat() == DRAPEAU || (c.getEtat() == FERMEE && c.getBombe()))
            return;

        // Cas de récursion
        // 1 : Case fermée sans bombes dans son voisinage
        c.setEtat(OUVERTE);

        // On creuse en croix
        if(i+1 < n) creuserAuto(i+1, j);
        if(i-1 >= 0) creuserAuto(i-1, j);
        if(j+1 < m) creuserAuto(i, j+1);
        if(j-1 >= 0) creuserAuto(i, j-1);
    }
}