package com.pierre.demineur;

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

    Case(){
        id = newId++;
        this.bombe = false;
        etat = FERMEE;
        nbBombes = 0;
    }

    public CaseEtats getEtat(){return  etat;}
    public void setEtat(CaseEtats e){etat = e;}
    public int getId(){return id;}
    public boolean getBombe(){return bombe;}
    public void setBombe(boolean b){bombe = b;}
    public int getNbBombes(){return nbBombes;}
    public void setNbBombes(int n){nbBombes = n;}
}
