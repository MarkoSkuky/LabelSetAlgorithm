import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        DiGraf g = DiGraf.nacitajSubor("src/graf2.txt");
        g.printInfo();





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
