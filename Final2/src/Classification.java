import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Math.*;

public class Classification {
    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }

    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        int depeche_tl=depeches.size();
        int cat_tl=categories.size();
        int i = 0;
        try {
            FileWriter file = new FileWriter(nomFichier);
            int k = 0;
            ArrayList<PaireChaineEntier> lst_dep_par_cat = new ArrayList<>();
            ArrayList<PaireChaineEntier> lst_dep_par_cat_bon = new ArrayList<>();
            while (k <cat_tl) {
                PaireChaineEntier dep_par_cat = new PaireChaineEntier(categories.get(k).getNom(), 0);
                PaireChaineEntier dep_par_cat_bon = new PaireChaineEntier(categories.get(k).getNom(), 0);
                lst_dep_par_cat.add(dep_par_cat);
                lst_dep_par_cat_bon.add(dep_par_cat_bon);
                k++;
            }
            int ind_lst_cap ;
            while (i < depeche_tl) {
                int j = 0;
                ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
                while (j < cat_tl) {
                    PaireChaineEntier result_pp = new PaireChaineEntier(categories.get(j).getNom(), categories.get(j).score(depeches.get(i)));
                    resultat.add(result_pp);
                    j++;
                }
                ind_lst_cap = UtilitairePaireChaineEntier.indicePourChaine(lst_dep_par_cat, depeches.get(i).getCategorie());
                lst_dep_par_cat.get(ind_lst_cap).addEntier(1);
                if (UtilitairePaireChaineEntier.chaineMax2(resultat).compareTo(depeches.get(i).getCategorie()) == 0) {
                    lst_dep_par_cat_bon.get(ind_lst_cap).addEntier(1);
                }
                file.write(depeches.get(i).getId() + ":" + UtilitairePaireChaineEntier.chaineMax2(resultat) + "\n");
                i++;
            }
            float prct = 0;
            ArrayList<PaireChaineEntier> moy = new ArrayList<>();
            for (int f = 0; f < lst_dep_par_cat_bon.size(); f++) {
                prct = (float) (lst_dep_par_cat_bon.get(f).getEntier()) / (float) (lst_dep_par_cat.get(f).getEntier()) * 100;
                file.write(lst_dep_par_cat_bon.get(f).getChaine() + " " + Float.toString(Math.round(prct)) + "%" + "\n");
                moy.add(new PaireChaineEntier(lst_dep_par_cat_bon.get(f).getChaine(), (int) (prct)));
            }
            file.write("Moyenne : " + Float.toString(UtilitairePaireChaineEntier.moyenne(moy)) + "%");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        int depeche_tl=depeches.size();
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        int tl_mot=0;
        int i = 0;
        while (i < depeche_tl) {
            int j = 0;
            if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
                tl_mot=depeches.get(i).getMots().size();
                while (j < tl_mot) {
                    if (UtilitairePaireChaineEntier.indicePourChaine(resultat, depeches.get(i).getMots().get(j)) == -1) {
                        int l = 0;
                        String valeurAPlace = depeches.get(i).getMots().get(j);
                        while (l < resultat.size() && resultat.get(l).getChaine().compareTo(valeurAPlace) < 0) {
                            l++;
                        }
                        resultat.add(l, new PaireChaineEntier(depeches.get(i).getMots().get(j), 0));
                    }
                    j++;
                }
            }
            i++;
        }
        return resultat;
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        int dep_tl=depeches.size();
        int tl_mot=0;
        int i = 0;
        int val_ent_chaine = 0;
        while (i < dep_tl) {
            int j = 0;
            tl_mot=depeches.get(i).getMots().size();
            while (j <tl_mot ) {
                int ind_chaine = UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j));
                if (ind_chaine != -1) {
                    val_ent_chaine = dictionnaire.get(ind_chaine).getEntier();
                }
                if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
                    if (ind_chaine != -1) {
                        dictionnaire.get(ind_chaine).setEntier(val_ent_chaine + 1);
                    }
                } else {
                    if (ind_chaine != -1) {
                        dictionnaire.get(ind_chaine).setEntier(val_ent_chaine - 1);
                    }
                }
                j++;
            }
            i++;
        }
    }


    public static int calculScores_outille(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        int i = 0;
        int compt = 0;
        int val_ent_chaine = 0;
        while (i < depeches.size()) {
            int j = 0;
            while (j < depeches.get(i).getMots().size()) {
                int ind_chaine = UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j));
                compt = compt + UtilitairePaireChaineEntier.indicePourChaine_outille(dictionnaire, depeches.get(i).getMots().get(j));
                if (ind_chaine != -1) {
                    val_ent_chaine = dictionnaire.get(ind_chaine).getEntier();
                }
                compt++;
                if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
                    if (ind_chaine != -1) {
                        dictionnaire.get(ind_chaine).setEntier(val_ent_chaine + 1);
                    }
                } else {
                    if (ind_chaine != -1) {
                        dictionnaire.get(ind_chaine).setEntier(val_ent_chaine - 1);
                    }
                }
                j++;
            }
            i++;
        }
        return compt;
    }


    public static int poidsPourScore(int score) {
        if (score <= 0) {
            return 0;
        } else if (score <= 3) {
            return 1;
        } else if (score < 8) {
            return 2;
        } else {
            return 3;
        }
    }


    public static int poidsPourScore2(int score) {
        if (score < 10) {
            return 0;
        } else if (score <= 20) {
            return 1;
        } else if (score < 30) {
            return 2;
        } else {
            return 3;
        }
    }


    public static void triFusion_dep(ArrayList<Depeche> vInt, int inf, int sup) {
        //{vInt[inf..sup] non vide} => {vInt[inf..sup] trié}
        if (inf < sup) {
            int m = (inf + sup) / 2;
            triFusion_dep(vInt, inf, m);
            triFusion_dep(vInt, m + 1, sup);
            fusionTabGTabD_dep(vInt, inf, m, sup);
        }
    }

    public static void fusionTabGTabD_dep(ArrayList<Depeche> vInt,
                                          int inf, int m, int sup) {
        //{ inf <= sup, m = (inf+sup)/2, vInt[inf..m] trié, vInt[m+1..sup] trié}
        // => { VInt[inf..sup] trié }
        ArrayList<Depeche> temp = new ArrayList<>();
        int index_1 = inf;
        int index_2 = m + 1;

        while (index_2 <= sup & index_1 <= m) {
            if (vInt.get(index_1).getCategorie().compareTo(vInt.get(index_2).getCategorie()) < 0) {
                temp.add(vInt.get(index_1));
                index_1++;
            } else {
                temp.add(vInt.get(index_2));
                index_2++;
            }
        }
        while (index_1 <= m) {
            temp.add(vInt.get(index_1));
            index_1++;
        }
        while (index_2 <= sup) {
            temp.add(vInt.get(index_2));
            index_2++;
        }
        for (int k = 0; k < temp.size(); k++) {
            vInt.set(k + inf, temp.get(k));
        }
    }


    public static void calculScores3(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        int max = depeches.size() - 1;
        int min = 0;
        int moy = 0;
        while (max > min) {
            moy = (max + min) / 2;
            if (depeches.get(moy).getCategorie().compareTo(categorie) >= 0) {
                max = moy;
            } else {
                min = moy + 1;
            }
        }
        int tailleDepeche = depeches.size();
        int indice_ref = max;
        for (int i = 0; i < dictionnaire.size(); i++) {
            indice_ref = max;
            int nbDepechesCheck = 0;
            int nbOcccurencesMot = 0;
            int nbMot = 0;
            while (indice_ref< tailleDepeche && depeches.get(indice_ref).getCategorie().compareTo(categorie) == 0) {
                boolean pasFait = true;
                for (int j = 0; j < depeches.get(indice_ref).getMots().size(); j++) {
                    if (depeches.get(indice_ref).getMots().get(j).compareTo(dictionnaire.get(i).getChaine()) == 0) {
                        if (pasFait==true) {
                            nbDepechesCheck++;
                            pasFait = false;
                        }
                        nbOcccurencesMot++;
                    }
                    nbMot++;
                }
                indice_ref++;
            }
            dictionnaire.get(i).setEntier((int) ((float) nbOcccurencesMot / (float)nbMot * log(tailleDepeche / nbDepechesCheck) * pow(10, 4)));
        }
    }

    public static int calculScores3_Outille(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        int compt = 0;
        int max = depeches.size() - 1;
        int min = 0;
        int moy = 0;
        compt++;
        while (max > min) {
            compt++;
            moy = (max + min) / 2;
            if (depeches.get(moy).getCategorie().compareTo(categorie) >= 0) {
                max = moy;
            } else {
                min = moy + 1;
            }
        }
        int tailleDepeche = depeches.size();
        int indice_ref = max;
        for (int i = 0; i < dictionnaire.size(); i++) {
            indice_ref = max;
            int nbDepechesCheck = 0;
            int nbOcccurencesMot = 0;
            int nbMot = 0;
            compt++;
            while (indice_ref< tailleDepeche && depeches.get(indice_ref).getCategorie().compareTo(categorie) == 0) {
                compt++;
                boolean pasFait = true;
                for (int j = 0; j < depeches.get(indice_ref).getMots().size(); j++) {
                    compt++;
                    if (depeches.get(indice_ref).getMots().get(j).compareTo(dictionnaire.get(i).getChaine()) == 0) {
                        compt++;
                        if (pasFait==true) {
                            nbDepechesCheck++;
                            pasFait = false;
                        }
                        nbOcccurencesMot++;
                    }
                    nbMot++;
                }
                indice_ref++;
            }
            dictionnaire.get(i).setEntier((int) ((float) nbOcccurencesMot / (float)nbMot * log(tailleDepeche / nbDepechesCheck) * pow(10, 4)));
        }
        return compt;
    }


    public static void calculScores2(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        for (int i = 0; i < dictionnaire.size(); i++) {
            int nbCheck = 0;
            int j = 0;
            int compt = 0;
            int tot = 0;
            while (depeches.get(j).getCategorie().compareTo(categorie) != 0) {
                j++;
            }
            while (j < depeches.size() && depeches.get(j).getCategorie().compareTo(categorie) == 0) {
                int k = 0;
                boolean pasfait = true;
                while (k < depeches.get(j).getMots().size() && pasfait == true) {
                    if (depeches.get(j).getMots().get(k).compareTo(dictionnaire.get(i).getChaine()) == 0) {
                        nbCheck++;
                        compt++;
                    }
                    tot++;
                    k++;
                }
                j++;
            }
            dictionnaire.get(i).setEntier((int) ((float) compt / tot * log(depeches.size() / nbCheck) * pow(10, 4)));
        }
    }


    public static int calculScores2_outille(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        int compteur = 0;
        for (int i = 0; i < dictionnaire.size(); i++) {
            int nbCheck = 0;
            int j = 0;
            int compt = 0;
            int tot = 0;
            compteur++;
            while (depeches.get(j).getCategorie().compareTo(categorie) != 0) {
                compteur++;
                j++;
            }
            compteur++;
            while (j < depeches.size() && depeches.get(j).getCategorie().compareTo(categorie) == 0) {
                compteur++;
                int k = 0;
                boolean pasfait = true;
                compteur++;
                while (k < depeches.get(j).getMots().size() && pasfait == true) {
                    compteur= compteur+2;
                    if (depeches.get(j).getMots().get(k).compareTo(dictionnaire.get(i).getChaine()) == 0) {
                        nbCheck++;
                        compt++;
                    }
                    tot++;
                    k++;
                }
                j++;
            }
            dictionnaire.get(i).setEntier((int) ((float) compt / tot * log(depeches.size() / nbCheck) * pow(10, 4)));
        }
        return compteur;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier);
            ArrayList<PaireChaineEntier> initial = initDico(depeches, categorie);
            calculScores(depeches, categorie, initial);
            int init=initial.size();
            for (int i = 0; i < init; i++) {
                if (poidsPourScore(initial.get(i).getEntier()) != 0) {
                    file.write(initial.get(i).getChaine() + ":" + poidsPourScore(initial.get(i).getEntier()));

                    file.write("\n");
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generationLexique2(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier);
            ArrayList<PaireChaineEntier> initial = initDico(depeches, categorie);
            triFusion_dep(depeches,0,depeches.size()-1);
            int  r=0;
            calculScores2(depeches, categorie, initial);
            int init=initial.size();
            for (int i = 0; i < init; i++) {
                if (poidsPourScore2(initial.get(i).getEntier()) != 0) {
                    file.write(initial.get(i).getChaine() + ":" + poidsPourScore2(initial.get(i).getEntier()));

                    file.write("\n");
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generationLexique3(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        try {
            FileWriter file = new FileWriter(nomFichier);
            ArrayList<PaireChaineEntier> initial = initDico(depeches, categorie);
            triFusion_dep(depeches,0,depeches.size()-1);
            int  r=0;
            calculScores3(depeches, categorie, initial);
            int init=initial.size();
            for (int i = 0; i < init; i++) {
                if (poidsPourScore2(initial.get(i).getEntier()) != 0) {
                    file.write(initial.get(i).getChaine() + ":" + poidsPourScore2(initial.get(i).getEntier()));

                    file.write("\n");
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void Demo(){
        ArrayList<Depeche> test5 = lectureDepeches("./test.txt");
        ArrayList<Depeche> depeches5= lectureDepeches("./depeches.txt");

        //################################# METHODE 1 ###################################
        long start_Time = System.currentTimeMillis();
        ArrayList<Depeche> test = lectureDepeches("./test.txt");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");
        ArrayList<Categorie> lst_cat = new ArrayList<>();
        long end_Time = System.currentTimeMillis();
        long tempsCreatTab = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        Categorie ECONOMIE = new Categorie("ECONOMIE");
        Categorie SPORTS = new Categorie("SPORTS");
        Categorie CULTURE = new Categorie("CULTURE");
        Categorie POLITIQUE = new Categorie("POLITIQUE");
        Categorie ENVIRONNEMENT_SCIENCES = new Categorie("ENVIRONNEMENT-SCIENCES");

        lst_cat.add(ECONOMIE);
        lst_cat.add(SPORTS);
        lst_cat.add(CULTURE);
        lst_cat.add(POLITIQUE);
        lst_cat.add(ENVIRONNEMENT_SCIENCES);
        end_Time = System.currentTimeMillis();
        long tempsCreatCat = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        generationLexique(depeches, "ECONOMIE", "./EconomieAuto.txt");
        generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "./EnvironnementAuto.txt");
        generationLexique(depeches, "CULTURE", "./CultureAuto.txt");
        generationLexique(depeches, "POLITIQUE", "./PolitiqueAuto.txt");
        generationLexique(depeches, "SPORTS", "./SportsAuto.txt");
        end_Time = System.currentTimeMillis();
        long tempsGenLex = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        ENVIRONNEMENT_SCIENCES.initLexique("./EnvironnementAuto.txt");
        ECONOMIE.initLexique("./EconomieAuto.txt");
        SPORTS.initLexique("./SportsAuto.txt");
        CULTURE.initLexique("./CultureAuto.txt");
        POLITIQUE.initLexique("./PolitiqueAuto.txt");
        end_Time = System.currentTimeMillis();
        long tempsIniLex = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        int lst_cat_tl=lst_cat.size();
        UtilitairePaireChaineEntier.triFusion_cat(lst_cat, 0, lst_cat_tl - 1);
        classementDepeches(test,lst_cat,"./Fichier_Reponse1.txt");
        end_Time = System.currentTimeMillis();
        long tempsGenResul = end_Time-start_Time;
        long tempsTot = tempsCreatTab+tempsCreatCat+tempsGenLex+tempsIniLex+tempsGenResul;


        //################################# METHODE 2 ###################################

        start_Time = System.currentTimeMillis();
        ArrayList<Depeche> test2 = lectureDepeches("./test.txt");
        ArrayList<Depeche> depeches2 = lectureDepeches("./depeches.txt");
        ArrayList<Categorie> lst_cat2 = new ArrayList<>();
        end_Time = System.currentTimeMillis();
        long tempsCreatTab2 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        Categorie ECONOMIE2 = new Categorie("ECONOMIE");
        Categorie SPORTS2 = new Categorie("SPORTS");
        Categorie CULTURE2 = new Categorie("CULTURE");
        Categorie POLITIQUE2 = new Categorie("POLITIQUE");
        Categorie ENVIRONNEMENT_SCIENCES2 = new Categorie("ENVIRONNEMENT-SCIENCES");

        lst_cat2.add(ECONOMIE2);
        lst_cat2.add(SPORTS2);
        lst_cat2.add(CULTURE2);
        lst_cat2.add(POLITIQUE2);
        lst_cat2.add(ENVIRONNEMENT_SCIENCES2);
        end_Time = System.currentTimeMillis();
        long tempsCreatCat2 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        generationLexique2(depeches2, "ECONOMIE", "./EconomieAuto.txt");
        generationLexique2(depeches2, "ENVIRONNEMENT-SCIENCES", "./EnvironnementAuto.txt");
        generationLexique2(depeches2, "CULTURE", "./CultureAuto.txt");
        generationLexique2(depeches2, "POLITIQUE", "./PolitiqueAuto.txt");
        generationLexique2(depeches2, "SPORTS", "./SportsAuto.txt");
        end_Time = System.currentTimeMillis();
        long tempsGenLex2 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        ENVIRONNEMENT_SCIENCES2.initLexique("./EnvironnementAuto.txt");
        ECONOMIE2.initLexique("./EconomieAuto.txt");
        SPORTS2.initLexique("./SportsAuto.txt");
        CULTURE2.initLexique("./CultureAuto.txt");
        POLITIQUE2.initLexique("./PolitiqueAuto.txt");
        end_Time = System.currentTimeMillis();
        long tempsIniLex2 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        int lst_cat_tl2=lst_cat2.size();
        UtilitairePaireChaineEntier.triFusion_cat(lst_cat2, 0, lst_cat_tl2 - 1);
        classementDepeches(test2,lst_cat2,"./Fichier_Reponse2.txt");
        end_Time = System.currentTimeMillis();
        long tempsGenResul2 = end_Time-start_Time;
        long tempsTot2 = tempsCreatTab2+tempsCreatCat2+tempsGenLex2+tempsIniLex2+tempsGenResul2;

        //################################# METHODE 3 ###################################

        start_Time = System.currentTimeMillis();
        ArrayList<Depeche> test3 = lectureDepeches("./test.txt");
        ArrayList<Depeche> depeches3 = lectureDepeches("./depeches.txt");
        ArrayList<Categorie> lst_cat3 = new ArrayList<>();
        end_Time = System.currentTimeMillis();
        long tempsCreatTab3 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        Categorie ECONOMIE3 = new Categorie("ECONOMIE");
        Categorie SPORTS3 = new Categorie("SPORTS");
        Categorie CULTURE3 = new Categorie("CULTURE");
        Categorie POLITIQUE3 = new Categorie("POLITIQUE");
        Categorie ENVIRONNEMENT_SCIENCES3 = new Categorie("ENVIRONNEMENT-SCIENCES");

        lst_cat3.add(ECONOMIE3);
        lst_cat3.add(SPORTS3);
        lst_cat3.add(CULTURE3);
        lst_cat3.add(POLITIQUE3);
        lst_cat3.add(ENVIRONNEMENT_SCIENCES3);
        end_Time = System.currentTimeMillis();
        long tempsCreatCat3 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        generationLexique3(depeches3, "ECONOMIE", "./EconomieAuto.txt");
        generationLexique3(depeches3, "ENVIRONNEMENT-SCIENCES", "./EnvironnementAuto.txt");
        generationLexique3(depeches3, "CULTURE", "./CultureAuto.txt");
        generationLexique3(depeches3, "POLITIQUE", "./PolitiqueAuto.txt");
        generationLexique3(depeches3, "SPORTS", "./SportsAuto.txt");
        end_Time = System.currentTimeMillis();
        long tempsGenLex3 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        ENVIRONNEMENT_SCIENCES3.initLexique("./EnvironnementAuto.txt");
        ECONOMIE3.initLexique("./EconomieAuto.txt");
        SPORTS3.initLexique("./SportsAuto.txt");
        CULTURE3.initLexique("./CultureAuto.txt");
        POLITIQUE3.initLexique("./PolitiqueAuto.txt");
        end_Time = System.currentTimeMillis();
        long tempsIniLex3 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        int lst_cat_tl3=lst_cat3.size();
        UtilitairePaireChaineEntier.triFusion_cat(lst_cat3, 0, lst_cat_tl3 - 1);
        classementDepeches(test3,lst_cat3,"./Fichier_Reponse3.txt");
        end_Time = System.currentTimeMillis();
        long tempsGenResul3 = end_Time-start_Time;
        long tempsTot3 = tempsCreatTab3+tempsCreatCat3+tempsGenLex3+tempsIniLex3+tempsGenResul3;

        //################################# METHODE 4 ###################################

        start_Time = System.currentTimeMillis();
        ArrayList<Depeche> test4 = lectureDepeches("./test.txt");
        ArrayList<Categorie> lst_cat4 = new ArrayList<>();
        end_Time = System.currentTimeMillis();
        long tempsCreatTab4 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        Categorie ECONOMIE4 = new Categorie("ECONOMIE");
        Categorie SPORTS4 = new Categorie("SPORTS");
        Categorie CULTURE4 = new Categorie("CULTURE");
        Categorie POLITIQUE4 = new Categorie("POLITIQUE");
        Categorie ENVIRONNEMENT_SCIENCES4 = new Categorie("ENVIRONNEMENT-SCIENCES");

        lst_cat4.add(ECONOMIE4);
        lst_cat4.add(SPORTS4);
        lst_cat4.add(CULTURE4);
        lst_cat4.add(POLITIQUE4);
        lst_cat4.add(ENVIRONNEMENT_SCIENCES4);
        end_Time = System.currentTimeMillis();
        long tempsCreatCat4 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        ENVIRONNEMENT_SCIENCES4.initLexique("./environnement.txt");
        ECONOMIE4.initLexique("./Economie.txt");
        SPORTS4.initLexique("./sport.txt");
        CULTURE4.initLexique("./culture.txt");
        POLITIQUE4.initLexique("./Politique.txt");

        UtilitairePaireChaineEntier.triFusion(SPORTS4.getLexique(),0,SPORTS4.getLexique().size()-1);
        UtilitairePaireChaineEntier.triFusion(ENVIRONNEMENT_SCIENCES4.getLexique(),0,ENVIRONNEMENT_SCIENCES4.getLexique().size()-1);
        UtilitairePaireChaineEntier.triFusion(ECONOMIE4.getLexique(),0,ECONOMIE4.getLexique().size()-1);
        UtilitairePaireChaineEntier.triFusion(POLITIQUE4.getLexique(),0,POLITIQUE4.getLexique().size()-1);
        UtilitairePaireChaineEntier.triFusion(CULTURE4.getLexique(),0,CULTURE4.getLexique().size()-1);
        end_Time = System.currentTimeMillis();
        long tempsIniLex4 = end_Time-start_Time;

        start_Time = System.currentTimeMillis();
        int lst_cat_tl4=lst_cat.size();
        UtilitairePaireChaineEntier.triFusion_cat(lst_cat4, 0, lst_cat_tl4 - 1);
        classementDepeches(test,lst_cat4,"./Fichier_Reponse4.txt");
        end_Time = System.currentTimeMillis();
        long tempsGenResul4 = end_Time-start_Time;
        long tempsTot4 = tempsCreatTab4+tempsCreatCat4+tempsIniLex4+tempsGenResul4;

        try {
            FileWriter file = new FileWriter("./DemoResultat.txt");
            file.write("###########################  RESULTATS  #######################\n\n");
            file.write("===========================  Vitesse  ========================\n");
            file.write("------------------------   Résultat 1  -----------------------\n");
            file.write("Création des tableaux ---------------------------------  "+tempsCreatTab+" ms\n");
            file.write("Création et initialisation des catégories -------------  "+tempsCreatCat+" ms\n");
            file.write("Génération des lexiques -------------------------------  "+tempsGenLex+" ms\n");
            file.write("Initialisation des lexiques ---------------------------  "+tempsIniLex+" ms\n");
            file.write("Génération du résultat --------------------------------  "+tempsGenResul+" ms\n");
            file.write("--------------------   Temps TOTAL : "+tempsTot+" ms   -----------------\n\n\n");


            file.write("------------------------   Résultat 2  -----------------------\n");
            file.write("Création des tableaux ---------------------------------  "+tempsCreatTab2+" ms\n");
            file.write("Création et initialisation des catégories -------------  "+tempsCreatCat2+" ms\n");
            file.write("Génération des lexiques -------------------------------  "+tempsGenLex2+" ms\n");
            file.write("Initialisation des lexiques ---------------------------  "+tempsIniLex2+" ms\n");
            file.write("Génération du résultat --------------------------------  "+tempsGenResul2+" ms\n");
            file.write("--------------------   Temps TOTAL : "+tempsTot2+" ms   -----------------\n\n\n");

            file.write("------------------------   Résultat 3  -----------------------\n");
            file.write("Création des tableaux ---------------------------------  "+tempsCreatTab3+" ms\n");
            file.write("Création et initialisation des catégories -------------  "+tempsCreatCat3+" ms\n");
            file.write("Génération des lexiques -------------------------------  "+tempsGenLex3+" ms\n");
            file.write("Initialisation des lexiques ---------------------------  "+tempsIniLex3+" ms\n");
            file.write("Génération du résultat --------------------------------  "+tempsGenResul3+" ms\n");
            file.write("--------------------   Temps TOTAL : "+tempsTot3+" ms   -----------------\n\n\n");

            file.write("------------------------   Résultat 4  -----------------------\n");
            file.write("Création des tableaux ---------------------------------  "+tempsCreatTab4+" ms\n");
            file.write("Création et initialisation des catégories -------------  "+tempsCreatCat4+" ms\n");
            file.write("Initialisation des lexiques ---------------------------  "+tempsIniLex4+" ms\n");
            file.write("Génération du résultat --------------------------------  "+tempsGenResul4+" ms\n");
            file.write("--------------------   Temps TOTAL : "+tempsTot4+" ms   -----------------\n\n\n");

            file.write("===========================  Taux de réussite  ========================\n");
            file.write("------------------------   Résultat 1  -----------------------\n");
            FileInputStream file1 = new FileInputStream("./Fichier_Reponse1.txt");
            Scanner scanner = new Scanner(file1);
            for(int i = 0; i<500;i++){
                scanner.nextLine();
            }
            while (scanner.hasNextLine()){
                file.write(scanner.nextLine()+"\n");
            }
            file.write("------------------------   Résultat 2  -----------------------\n");
            FileInputStream file2 = new FileInputStream("./Fichier_Reponse2.txt");
            Scanner scanner2 = new Scanner(file2);
            for(int i = 0; i<500;i++){
                scanner2.nextLine();
            }
            while (scanner2.hasNextLine()){
                file.write(scanner2.nextLine()+"\n");
            }
            file.write("------------------------   Résultat 3  -----------------------\n");
            FileInputStream file3 = new FileInputStream("./Fichier_Reponse3.txt");
            Scanner scanner3 = new Scanner(file3);
            for(int i = 0; i<500;i++){
                scanner3.nextLine();
            }
            while (scanner3.hasNextLine()){
                file.write(scanner3.nextLine()+"\n");
            }
            file.write("------------------------   Résultat 4  -----------------------\n");
            FileInputStream file4 = new FileInputStream("./Fichier_Reponse4.txt");
            Scanner scanner4 = new Scanner(file4);
            for(int i = 0; i<500;i++){
                scanner4.nextLine();
            }
            while (scanner4.hasNextLine()){
                file.write(scanner4.nextLine()+"\n");
            }
            file.write("\n\n===========================  Cout algorithmique  ========================\n");
            file.write("------------------------   Résultat 1  -----------------------\n");
            int nbcomptsc=0,nbcomptscore=0;
            for (int i = 0; i < lst_cat.size(); i++) {
                nbcomptsc = nbcomptsc + calculScores_outille(depeches, lst_cat.get(i).getNom(), initDico(depeches, lst_cat.get(i).getNom()));
                for (int j = 0; j < depeches.size(); j++) {
                    nbcomptscore = nbcomptscore + lst_cat.get(i).score_outille(depeches.get(j));
                }
            }
            file.write("Nombre d'opérations calculScore --------------  "+nbcomptsc+" opérations\n");
            file.write("Nombre d'opérations score --------------------  "+nbcomptscore+" opérations\n");
            file.write("------------------------   Résultat 2  -----------------------\n");
            nbcomptsc = 0;
            nbcomptscore=0;
            for (int i = 0; i < lst_cat2.size(); i++) {
                nbcomptsc = nbcomptsc + calculScores2_outille(depeches2, lst_cat2.get(i).getNom(), initDico(depeches2, lst_cat2.get(i).getNom()));
                for (int j = 0; j < depeches2.size(); j++) {
                    nbcomptscore = nbcomptscore + lst_cat2.get(i).score_outille(depeches2.get(j));
                }
            }
            file.write("Nombre d'opérations calculScore --------------  "+nbcomptsc+" opérations\n");
            file.write("Nombre d'opérations score --------------------  "+nbcomptscore+" opérations\n");
            file.write("------------------------   Résultat 3  -----------------------\n");
            nbcomptsc = 0;
            nbcomptscore=0;
            for (int i = 0; i < lst_cat3.size(); i++) {
                nbcomptsc = nbcomptsc + calculScores3_Outille(depeches3, lst_cat3.get(i).getNom(), initDico(depeches3, lst_cat3.get(i).getNom()));
                for (int j = 0; j < depeches3.size(); j++) {
                    nbcomptscore = nbcomptscore + lst_cat3.get(i).score_outille(depeches3.get(j));
                }
            }
            file.write("Nombre d'opérations calculScore --------------  "+nbcomptsc+" opérations\n");
            file.write("Nombre d'opérations score --------------------  "+nbcomptscore+" opérations\n");



            file.close();

        } catch (IOException e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        Demo();
    }
}
