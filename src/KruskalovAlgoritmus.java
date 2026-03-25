import java.util.ArrayList;
import java.util.Comparator;

public class KruskalovAlgoritmus {

    private final ArrayList<OrHrana> hrany;
    private final ArrayList<OrHrana> kostra;
    private final ArrayList<Vrchol> vrcholy;

    public KruskalovAlgoritmus(DiGraf graf) {
        this.hrany = graf.orHrany;
        this.vrcholy = graf.vrcholy;
        this.kostra = new ArrayList<>();
    }

    public void najdiNajlacnejsiuKostru() {
        this.hrany.sort(Comparator.comparingInt(h -> h.c_h));
        vyberHranyDoKostry();
    }

    public void najdiNajdrahsiuKostru() {
        this.hrany.sort(Comparator.comparingInt((OrHrana h) -> h.c_h).reversed());
        vyberHranyDoKostry();
    }

    private void vyberHranyDoKostry() {
        this.kostra.clear();

        // pole k(v) = komponent vrchola
        int maxCislo = 0;
        for (Vrchol v : this.vrcholy) {
            if (v.cislo > maxCislo) {
                maxCislo = v.cislo;
            }
        }

        int[] komponent = new int[maxCislo + 1];

        // kazdy vrchol ma svoj komponent na zaciatku
        for (Vrchol v : this.vrcholy) {
            komponent[v.cislo] = v.cislo;
        }

        // prechadzame vsetky hrany
        for (OrHrana hrana : this.hrany) {

            int k_u = komponent[hrana.u.cislo];
            int k_v = komponent[hrana.v.cislo];

            // ak niesu rovnake pridame do kostry
            if (k_u != k_v) {
                this.kostra.add(hrana);

                // aby sme mohli zmenit vacsiu na mensiu
                int novaZnacka = Math.min(k_u, k_v);
                int staraZnacka = Math.max(k_u, k_v);

                // prepisanie vacsej znacky
                for (Vrchol v : this.vrcholy) {
                    if (komponent[v.cislo] == staraZnacka) {
                        komponent[v.cislo] = novaZnacka;
                    }
                }
            }
        }
        vypis();
    }







    private void vypis() {
        System.out.print("kostra = {");
        System.out.print("{");

        for (int i = 0; i < this.vrcholy.size(); i++) {
            if (i == this.vrcholy.size() - 1) {
                System.out.print(this.vrcholy.get(i).cislo);
            } else {
                System.out.print(this.vrcholy.get(i).cislo + ", ");
            }
        }

        System.out.print("}, ");

        this.kostra.sort(Comparator.comparingInt(h -> h.u.cislo));

        for (int i = 0; i < this.kostra.size(); i++) {
            OrHrana h = this.kostra.get(i);

            if (i == this.kostra.size() - 1) {
                System.out.print("{" + h.u.cislo + "," + h.v.cislo + "}}");
            } else {
                System.out.print("{" + h.u.cislo + "," + h.v.cislo + "}, ");
            }
        }

        int cena = 0;
        for (OrHrana h : this.kostra) {
            cena += h.c_h;
        }

        System.out.printf("\ncena kostry = %d", cena);
    }
}