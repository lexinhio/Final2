import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
public class Categorie {
    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique= new ArrayList<>(); //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }


    public String getNom() {
        return nom;
    }


    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                int entier = Integer.parseInt(ligne.substring(ligne.length() - 1));
                ligne=ligne.substring(0,ligne.length() - 2);
                PaireChaineEntier retenu = new PaireChaineEntier(ligne, entier);
                lexique.add(retenu);
            }
            scanner.close();
        } catch(IOException e) {
            e.printStackTrace();

        }
    }



    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        int mot_tl=d.getMots().size();
        int indice=0;
        int score=0;
        while(indice<mot_tl){
            score=score+ UtilitairePaireChaineEntier.entierPourChaine(lexique,d.getMots().get(indice));
            indice++;
        }
        return score;
    }
    public int score_outille(Depeche d) {
        int indice=0;
        int score=0;
        int compt=1;
        while(indice<d.getMots().size()){
            score=score+ UtilitairePaireChaineEntier.entierPourChaine(lexique,d.getMots().get(indice));
            compt=compt+UtilitairePaireChaineEntier.entierPourChaine_outille(lexique,d.getMots().get(indice));
            indice++;
        }
        return compt;
    }
}
