import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        DiGraf g = DiGraf.nacitajSubor("src/grafCP.txt");
        g.printInfo();
        CasovePlanovanie casovePlanovanie = new CasovePlanovanie(g);
        casovePlanovanie.nastavTrvanie(1, 50);
        casovePlanovanie.nastavTrvanie(2, 30);
        casovePlanovanie.nastavTrvanie(3, 20);
        casovePlanovanie.nastavTrvanie(4, 10);
        casovePlanovanie.nastavTrvanie(5, 30);
        casovePlanovanie.nastavTrvanie(6, 70);
        casovePlanovanie.nastavTrvanie(7, 40);
        casovePlanovanie.vypocitajVsetko();
        casovePlanovanie.vypisVsetko();



//        int startCislo = 5;
//        int cielCislo = 3;
//
//        Vrchol start = g.dajVrchol(startCislo);
//        Vrchol ciel = g.dajVrchol(cielCislo);

//        LabelSet labelSet = new LabelSet(g);
//        labelSet.najdiNajkratsieCesty(start);

//        KruskalovAlgoritmus kruskalovAlgoritmus = new KruskalovAlgoritmus(g);
//        kruskalovAlgoritmus.najdiNajlacnejsiuKostru();
//        System.out.println();
//        kruskalovAlgoritmus.najdiNajdrahsiuKostru();

//        FloydAlgoritmus floydAlgoritmus = new FloydAlgoritmus(g);
//        floydAlgoritmus.najdiNajkratsieCesty();
//        floydAlgoritmus.vypisMaticuVzdialenosti();


    }
}
