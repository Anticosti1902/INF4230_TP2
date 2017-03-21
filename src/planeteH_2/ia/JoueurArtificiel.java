package planeteH_2.ia;

/*
 * Si vous utilisez Java, vous devez modifier ce fichier-ci.
 *
 * Vous pouvez ajouter d'autres classes sous le package planeteH_2.ia.
 *
 * Prénom Nom    (CODE00000001)
 * Prénom Nom    (CODE00000002)
 */

import static java.lang.Math.pow;
import planeteH_2.Grille;
import planeteH_2.Joueur;
import planeteH_2.Position;
import java.util.ArrayList;
import java.util.Random;


public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();

    /**
     * Voici la fonction à modifier.
     * Évidemment, vous pouvez ajouter d'autres fonctions dans JoueurArtificiel.
     * Vous pouvez aussi ajouter d'autres classes, mais elles doivent être
     * ajoutées dans le package planeteH_2.ia.
     * Vous ne pouvez pas modifier les fichiers directement dans planeteH_2., car ils seront écrasés.
     * 
     * @param grille Grille reçu (état courrant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */
    @Override
    public Position getProchainCoup(Grille grille, int delais) {
        int joueur = grille.nbLibre()%2;
        int evalJ1 = 0;
        int evalJ2 = 0;
        ArrayList<Integer> casesvides = new ArrayList<>();
        ArrayList<Integer> casesJoueesJ1 = new ArrayList<>();
        ArrayList<Integer> casesJoueesJ2 = new ArrayList<>();
        
        int nbcol = grille.getData()[0].length;
        for (int l = 0; l < grille.getData().length; l++) {
            for (int c = 0; c < nbcol; c++) {
                if (grille.getData()[l][c] == 0) {
                    casesvides.add(l * nbcol + c);
                }
            }
        }
        
        if (joueur == 1) {
            casesJoueesJ1 = getCasesJouées(grille, joueur);
            casesJoueesJ2 = getCasesJouées(grille, joueur + 1);
            evalJ1 = globalEval(casesJoueesJ1, casesvides, joueur, nbcol, grille.getSize());
            evalJ2 = globalEval(casesJoueesJ2, casesvides, joueur + 1, nbcol, grille.getSize());
        }
        System.out.println(evalJ1);
        int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }

    @Override
    public String getAuteurs() {
        return "Alexandre Fernandes-Bartolomeu (FERA19029301)";
    }
    
    public ArrayList<Integer> getCasesJouées(Grille grille, int joueur)
    {
        ArrayList<Integer> casesPleines = new ArrayList<>();
        int nbcol = grille.getData()[0].length;
         for(int l=0;l<grille.getData().length;l++)
            for(int c=0;c<nbcol;c++)
            {
                 if(grille.getData()[l][c]==joueur)
                    casesPleines.add(l*nbcol+c);
            }
         return casesPleines;
    }
    
    public ArrayList<Integer> getCasesVides(Grille grille)
    {
        ArrayList<Integer> casesPleines = new ArrayList<>();
        int nbcol = grille.getData()[0].length;
         for(int l=0;l<grille.getData().length;l++)
            for(int c=0;c<nbcol;c++)
            {
                 if(grille.getData()[l][c]==0)
                    casesPleines.add(l*nbcol+c);
            }
         return casesPleines;
    }
 
    public int evalLigne(ArrayList<Integer> caseJouees, ArrayList<Integer> casesVides, int joueur, int nbcol, int totalSize) {
        int nbSuite = 0;
        int eval = 1;
        int lastPlay = 0;
        ArrayList<Integer> casesCorrectes = new ArrayList<>();
        casesCorrectes.addAll(casesVides);
        casesCorrectes.addAll(caseJouees);
        for (int i = 0; i < caseJouees.size(); i++) {

            if (caseJouees.get(i) - lastPlay == 1 && caseJouees.get(i) % nbcol != 0) {
                nbSuite++;
            } else if (caseJouees.get(i) == totalSize) {
                eval += nbSuite * 10;
            } else {
                if (nbSuite == 6) {
                    eval += 0;
                } else {
                    if (is5PossibleLigne(casesCorrectes, nbSuite, caseJouees.get(i), nbcol)) {
                        eval += pow(10, nbSuite - 1);
                    }
                }
                nbSuite = 1;
            }
            lastPlay = caseJouees.get(i);
        }
        return eval;
    }

    public int evalColonne(ArrayList<Integer> caseJouees, int joueur, int nbcol) {
        int nbSuite = 1;
        int eval = 0;
        int i = 0;
        while (!caseJouees.isEmpty()) {
            if (caseJouees.contains(caseJouees.get(i) + nbcol)) {
                nbSuite++;
                int tmp =  i;
                i = caseJouees.indexOf(caseJouees.get(i) + nbcol) - 1;
                caseJouees.remove(tmp);
            } else {
                if (nbSuite == 6) {
                    eval += 0;
                } else if (nbSuite !=1){
                    eval += pow(10, nbSuite - 1);
                }
                caseJouees.remove(i);
                nbSuite = 1;
                i = 0;
            }
        }
        return eval;
    }

    public int globalEval(ArrayList<Integer> caseJouees,ArrayList<Integer> casesvides, int joueur, int nbCol, int totalSize)
    {
        int eval = evalLigne(caseJouees, casesvides, joueur, nbCol, totalSize);
        //int eval = evalColonne(caseJouees, joueur, nbCol);
        return eval;
    }
    
    public boolean is5PossibleLigne(ArrayList<Integer> casesVides, int nbSuite, int pos, int nbCol)
    {
        switch (nbSuite) {
            case 1:
                if (casesVides.contains(pos - 1) && casesVides.contains(pos - 2) && casesVides.contains(pos - 3) && casesVides.contains(pos - 4) && pos % nbCol > 3
                        || casesVides.contains(pos - 1) && casesVides.contains(pos - 2) && casesVides.contains(pos - 3) && casesVides.contains(pos + 1) && pos % nbCol > 2 && pos % nbCol < nbCol
                        || casesVides.contains(pos - 1) && casesVides.contains(pos - 2) && casesVides.contains(pos + 1) && casesVides.contains(pos + 2) && pos % nbCol > 1 && pos % nbCol < nbCol - 1
                        || casesVides.contains(pos - 1) && casesVides.contains(pos + 1) && casesVides.contains(pos + 2) && casesVides.contains(pos + 3) && pos % nbCol > 0 && pos % nbCol < nbCol - 2
                        || casesVides.contains(pos + 1) && casesVides.contains(pos + 2) && casesVides.contains(pos + 3) && casesVides.contains(pos + 4) && pos % nbCol < nbCol - 3) {
                    return true;
                }
                break;
            case 2:
                if (casesVides.contains(pos - 2) && casesVides.contains(pos - 3) && casesVides.contains(pos - 4) && pos % nbCol > 2
                        || casesVides.contains(pos - 2) && casesVides.contains(pos - 3) && casesVides.contains(pos + 1) && nbCol > 1 && pos % nbCol < nbCol
                        || casesVides.contains(pos - 2) && casesVides.contains(pos + 1) && casesVides.contains(pos + 2) && nbCol > 0 && pos % nbCol < nbCol - 1
                        || casesVides.contains(pos + 1) && casesVides.contains(pos + 2) && casesVides.contains(pos + 3) && pos % nbCol < nbCol - 2)  {
                    return true;
                }
                break;
            case 3:
                if (casesVides.contains(pos - 3) && casesVides.contains(pos - 4) && pos % nbCol > 1
                        || casesVides.contains(pos - 3) && casesVides.contains(pos + 1) && pos % nbCol > 0 && pos % nbCol < nbCol
                        || casesVides.contains(pos + 1) && casesVides.contains(pos + 2) && pos % nbCol < nbCol - 1) {
                    return true;
                }
                break;
            case 4:
                if (casesVides.contains(pos - 4) && pos % nbCol > 0
                        || casesVides.contains(pos + 1) && pos % nbCol < nbCol - 0) {
                    return true;
                }
                break;
        }
        return false;
    }
    
    /*public int meilleurChoix (ArrayList<Integer> caseJoueesJ1,ArrayList<Integer> caseJoueesJ2, ArrayList<Integer> casesVides, int nbcol) 
    {
         ArrayList<Integer> caseJoueesTmpJ1 = new ArrayList<>();
         ArrayList<Integer> caseVidesTmp = new ArrayList<>();
         int evalTotal = -1000000;
         int meilleurePlace = -1;
         for(int place : casesVides)
         {
             caseVidesTmp.addAll(casesVides);
             caseJoueesTmpJ1.addAll(caseJoueesJ1);
             caseVidesTmp.remove(place);
             caseJoueesTmpJ1.add(place);
             int evalJ1 = globalEval(caseJoueesTmpJ1,caseVidesTmp,1,nbcol,place);
             int evalJ2 = globalEval(caseJoueesTmp,caseVidesTmp,1,nbcol,place);
             if(evalJ1 - evalJ2 > evalTotal)
             {
                 evalTotal = evalJ1 - evalJ2;
                 meilleurePlace = place;
             }
         }
        return meilleurePlace;
    }*/

    /*public int elagageLigne(ArrayList<Integer> caseJouees, ArrayList<Integer> casesVides, int nbSuite, int pos, int nbCol){

        int plafond = 10000;
        int lastPlay = 0;
        for(int place : caseJouees)
        {
            if (caseJouees.get(place) - lastPlay == 1 && caseJouees.get(place) % nbCol != 0) {
                nbSuite++;
            } 
            else{
                
            }
            lastPlay = caseJouees.get(place);
        }
        return 0;
    }*/

}
