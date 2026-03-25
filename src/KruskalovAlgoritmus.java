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

    public void najlacnejsiaKostra() {
        this.hrany.sort(Comparator.comparingInt(a -> a.c_h));
        nastavZnacku();
    }

    public void najdrahsiaKostra() {
        this.hrany.sort(Comparator.comparingInt((OrHrana a) -> a.c_h).reversed());
        nastavZnacku();
    }

    private void nastavZnacku() {
        this.kostra.clear();

        int maxCislo = 0;
        for (Vrchol v : this.vrcholy) {
            if (v.cislo > maxCislo) {
                maxCislo = v.cislo;
            }
        }

        int[] znacky = new int[maxCislo + 1];
        for (Vrchol v : this.vrcholy) {
            znacky[v.cislo] = v.cislo;
        }

        for (OrHrana h : this.hrany) {
            int znackaU = znacky[h.u.cislo];
            int znackaV = znacky[h.v.cislo];

            if (znackaU != znackaV) {
                this.kostra.add(h);
                int staraZnacka = znackaU;
                int novaZnacka = znackaV;

                for (Vrchol v : this.vrcholy) {
                    if (znacky[v.cislo] == staraZnacka) {
                        znacky[v.cislo] = novaZnacka;
                    }
                }
            }
        }

        vypis();
    }

    private void vypis() {
        System.out.print("Vysledok : {");
        System.out.print("{");
        for (int i = 0; i < this.vrcholy.size(); i++) {
            if (i == this.vrcholy.size() - 1) {
                System.out.print(this.vrcholy.get(i).cislo);
            } else {
                System.out.print(this.vrcholy.get(i).cislo + ", ");
            }
        }
        System.out.print("}, ");

        this.kostra.sort(Comparator.comparingInt(a -> a.u.cislo));
        for (int i = 0; i < this.kostra.size(); i++) {
            if (i == this.kostra.size() - 1) {
                System.out.print("{" + this.kostra.get(i).u.cislo + "," + this.kostra.get(i).v.cislo + "}}");
            } else {
                System.out.print("{" + this.kostra.get(i).u.cislo + "," + this.kostra.get(i).v.cislo + "}, ");
            }
        }

        int cena = 0;
        for (int i = 0; i < this.kostra.size(); i++) {
            cena += this.kostra.get(i).c_h;
        }

        System.out.printf("\nCena : %d", cena);
    }
}
