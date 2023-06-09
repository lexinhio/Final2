import java.util.ArrayList;

public class UtilitairePaireChaineEntier {
    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int lts_tl=listePaires.size();
        if(listePaires.size()==0){
            return -1;
        }
        if (listePaires.get(lts_tl - 1).getChaine().compareTo(chaine) == -1) {
            return -1;
        } else {
            int inf = 0;
            int sup = lts_tl - 1;
            int m;
            while (inf < sup) {
                m = (inf + sup) / 2;
                if (listePaires.get(m).getChaine().compareTo(chaine) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            if (listePaires.get(sup).getChaine().compareTo(chaine) == 0) {
                return sup;
            } else {
                return -1;
            }
        }
    }
    public static int indicePourChaine_dep_cat(ArrayList<Depeche> listePaires, String categories) {
        if(listePaires.size()==0){
            return -1;
        }
        if (listePaires.get(listePaires.size() - 1).getCategorie().compareTo(categories) == -1) {
            return -1;
        } else {
            int inf = 0;
            int sup = listePaires.size() - 1;
            int m;
            while (inf < sup) {
                m = (inf + sup) / 2;
                if (listePaires.get(m).getCategorie().compareTo(categories) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            if (listePaires.get(sup).getCategorie().compareTo(categories) == 0) {
                return sup;
            } else {
                return -1;
            }
        }
    }
    public static int indicePourChaine_String(ArrayList<String> listePaires, String categories) {
        if(listePaires.size()==0){
            return -1;
        }
        if (listePaires.get(listePaires.size() - 1).compareTo(categories) == -1) {
            return -1;
        } else {
            int inf = 0;
            int sup = listePaires.size() - 1;
            int m;
            while (inf < sup) {
                m = (inf + sup) / 2;
                if (listePaires.get(m).compareTo(categories) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            if (listePaires.get(sup).compareTo(categories) == 0) {
                return sup;
            } else {
                return -1;
            }
        }
    }
    public static int nb_mot_liste(ArrayList<String> listePaires, String mot) {
        int  n_fois=0;
        if (listePaires.size()==0 || listePaires.get(listePaires.size()-1).compareTo(mot)<0 || listePaires.get(0).compareTo(mot)>0 ){
            return 0;
        } else {
            int i= indicePourChaine_String(listePaires,mot);
            if(i!=-1){
                while(i<listePaires.size() && listePaires.get(i).compareTo(mot)==0){
                    n_fois++;
                    i++;
                }}
            return n_fois;
        }
    }
    public static int indicePourChaineRec(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        if(listePaires.size()==0){
            return -1;
        }
        if (listePaires.get(listePaires.size() - 1).getChaine().compareTo(chaine)==-1 ) {
            return -1;
        } else {
            return indicePourChaineworker(listePaires,chaine,0,listePaires.size()-1);
        }
    }

    public static int indicePourChaineworker(ArrayList<PaireChaineEntier> listePaire,String chaine, int inf, int sup){
        if (inf == sup) {
            if (listePaire.get(sup).getChaine().compareTo(chaine) == 0) {
                return sup;
            } else {
                return -1;
            }
        } else {
            int m = (inf + sup) / 2;
            int paireRC;
            if (listePaire.get(m).getChaine().compareTo(chaine) <0) {
                paireRC = indicePourChaineworker(listePaire, chaine, m+1, sup);
            } else {
                paireRC = indicePourChaineworker(listePaire, chaine, inf, m);
            }
            return paireRC;
        }
    }

    public static int indicePourChaine_outille(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        if(listePaires.size()==0){
            return 0;
        }
        int nbcompt=1;
        if (listePaires.get(listePaires.size() - 1).getChaine().compareTo(chaine) == -1) {
            return nbcompt;
        } else {
            int inf = 0;
            int sup = listePaires.size() - 1;
            int m;
            while (inf < sup) {
                m = (inf + sup) / 2;
                nbcompt++;
                if (listePaires.get(m).getChaine().compareTo(chaine) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            nbcompt++;
            if (listePaires.get(sup).getChaine().compareTo(chaine) == 0) {
                return nbcompt;
            } else {
                return nbcompt;
            }
        }
    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        return listePaires.get(listePaires.size()-1).getChaine();
    }
    public static String chaineMax2(ArrayList<PaireChaineEntier> listePaires) {
        int lts_tl=listePaires.size();
        int indice=0;
        int j=0;
        while(indice<lts_tl){
            if(listePaires.get(indice).getEntier()>listePaires.get(j).getEntier()){
                j=indice;
            }
            indice++;
        }
        return listePaires.get(j).getChaine();
    }
    public static boolean veriftri_dep_cat(ArrayList<Depeche> depeche ){
        int i=0;
        while (i<depeche.size()-1 && depeche.get(i).getCategorie().compareTo(depeche.get(i+1).getCategorie())<=0){
            i++;
        }
        if (i==depeche.size()-1 && depeche.get(i-2).getCategorie().compareTo(depeche.get(i-1).getCategorie())<=0){
            return true;
        } else {
            return false;
        }
    }
    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        float moy=0;
        int indice=0;
        int lts_tl=listePaires.size();
        while(indice<lts_tl){
            moy=moy+listePaires.get(indice).getEntier();
            indice++;
        }
        return moy/lts_tl;
    }
    public static int entierPourChaine(ArrayList<PaireChaineEntier>
                                               listePaires, String chaine){
        int ind_val=indicePourChaine(listePaires,chaine);
        if(ind_val!=-1){
            return listePaires.get(ind_val).getEntier();
        } else {
            return 0;
        }
    }
    public static int entierPourChaine_outille(ArrayList<PaireChaineEntier>
                                                       listePaires, String chaine){
        int compt=indicePourChaine_outille(listePaires,chaine)+1;
        int ind_val=indicePourChaine(listePaires,chaine);
        if(ind_val!=-1){
            return compt;
        } else {
            return compt;
        }
    }
    public static void triFusion(ArrayList<PaireChaineEntier> vInt, int inf, int sup) {
        //{vInt[inf..sup] non vide} => {vInt[inf..sup] trié}
        if (inf < sup) {
            int m = (inf + sup) / 2;
            triFusion(vInt, inf, m);
            triFusion(vInt, m + 1, sup);
            fusionTabGTabD(vInt, inf, m, sup);
        }
    }

    public static void fusionTabGTabD(ArrayList<PaireChaineEntier> vInt,
                                      int inf, int m, int sup) {
//{ inf <= sup, m = (inf+sup)/2, vInt[inf..m] trié, vInt[m+1..sup] trié}
// => { VInt[inf..sup] trié }
        ArrayList<PaireChaineEntier> temp = new ArrayList<>();
        int index_1 = inf;
        int index_2 = m + 1;

        while (index_2 <= sup & index_1 <= m) {
            if (vInt.get(index_1).getChaine().compareTo(vInt.get(index_2).getChaine())<0) {
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
        for (int k=0;k<temp.size();k++){
            vInt.set(k+inf,temp.get(k));
        }
    }

    
/*    public static void triFusion_string(ArrayList<String> vInt, int inf, int sup) {
        //{vInt[inf..sup] non vide} => {vInt[inf..sup] trié}
        if (inf < sup) {
            int m = (inf + sup) / 2;
            triFusion_string(vInt, inf, m);
            triFusion_string(vInt, m + 1, sup);
            fusionTabGTabD_string(vInt, inf, m, sup);
        }
    }*/

 /*   public static void fusionTabGTabD_string(ArrayList<String> vInt,
                                             int inf, int m, int sup) {
//{ inf <= sup, m = (inf+sup)/2, vInt[inf..m] trié, vInt[m+1..sup] trié}
// => { VInt[inf..sup] trié }
        ArrayList<String> temp = new ArrayList<>();
        int index_1 = inf;
        int index_2 = m + 1;

        while (index_2 <= sup & index_1 <= m) {
            if (vInt.get(index_1).compareTo(vInt.get(index_2))<0) {
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
        for (int k=0;k<temp.size();k++){
            vInt.set(k+inf,temp.get(k));
        }
    }
*/
    public static void triFusion_cat(ArrayList<Categorie> vInt, int inf, int sup) {
        //{vInt[inf..sup] non vide} => {vInt[inf..sup] trié}
        if (inf < sup) {
            int m = (inf + sup) / 2;
            triFusion_cat(vInt, inf, m);
            triFusion_cat(vInt, m + 1, sup);
            fusionTabGTabD_cat(vInt, inf, m, sup);
        }
    }

    public static void fusionTabGTabD_cat(ArrayList<Categorie> vInt,
                                          int inf, int m, int sup) {
//{ inf <= sup, m = (inf+sup)/2, vInt[inf..m] trié, vInt[m+1..sup] trié}
// => { VInt[inf..sup] trié }
        ArrayList<Categorie> temp = new ArrayList<>();
        int index_1 = inf;
        int index_2 = m + 1;

        while (index_2 <= sup & index_1 <= m) {
            if (vInt.get(index_1).getNom().compareTo(vInt.get(index_2).getNom())<0) {
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
        for (int k=0;k<temp.size();k++){
            vInt.set(k+inf,temp.get(k));
        }
    }
 /*   public static void triFusion_ent(ArrayList<PaireChaineEntier> vInt, int inf, int sup) {
        //{vInt[inf..sup] non vide} => {vInt[inf..sup] trié}
        if (inf < sup) {
            int m = (inf + sup) / 2;
            triFusion_ent(vInt, inf, m);
            triFusion_ent(vInt, m + 1, sup);
            fusionTabGTabD_ent(vInt, inf, m, sup);
        }
    }
    public static int indicePourChaine2(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int indice=0;
        while(indice<listePaires.size()&& listePaires.get(indice).getChaine().compareTo(chaine)!=0 ){
            indice++;}
        if(indice<listePaires.size() &&  listePaires.get(indice).getChaine().compareTo(chaine)==0){
            return indice;
        }else {
            return 0;
        }
    }
*/
/*    public static void fusionTabGTabD_ent(ArrayList<PaireChaineEntier> vInt,
                                          int inf, int m, int sup) {
//{ inf <= sup, m = (inf+sup)/2, vInt[inf..m] trié, vInt[m+1..sup] trié}
// => { VInt[inf..sup] trié }
        ArrayList<PaireChaineEntier> temp = new ArrayList<>();
        int index_1 = inf;
        int index_2 = m + 1;

        while (index_2 <= sup & index_1 <= m) {
            if (vInt.get(index_1).getEntier()<(vInt.get(index_2).getEntier())) {
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
        for (int k=0;k<temp.size();k++){
            vInt.set(k+inf,temp.get(k));
        }
    }*/
}
