import java.util.ArrayList;

public class CasovePlanovanie {
    private DiGraf diGraf;

    private int[] p; // trvania
    private int[] z; // najskor mozne zaciatky
    private int[] k; // najneskor nutne konce
    private int[] R; // rezervy

    private int T; // trvanie projektu
    private ArrayList<Integer> topologickePoradie;

    public CasovePlanovanie(DiGraf diGraf) {
        this.diGraf = diGraf;

        int n = diGraf.vrcholy.size();

        this.p = new int[n + 1];
        this.z = new int[n + 1];
        this.k = new int[n + 1];
        this.R = new int[n + 1];
        this.T = 0;
        this.topologickePoradie = new ArrayList<>();
    }

    public void nastavTrvanie(int vrchol, int trvanie) {
        p[vrchol] = trvanie;
    }

    private int dajTrvanie(int vrchol) {
        return p[vrchol];
    }

    private int dajZaciatok(int vrchol) {
        return z[vrchol];
    }

    private int dajKoniec(int vrchol) {
        return k[vrchol];
    }

    private int dajRezervu(int vrchol) {
        return R[vrchol];
    }

    private int dajTrvanieProjektu() {
        return T;
    }

    private ArrayList<Integer> dajTopologickePoradie() {
        return new ArrayList<>(topologickePoradie);
    }

    // vsetky vrcholy z ktorych sa vchadza do vrchola v
    public ArrayList<Integer> dajPredchodcov(int v) {
        ArrayList<Integer> predchodcovia = new ArrayList<>();

        for (OrHrana h : diGraf.orHrany) {
            if (h.v.cislo == v) {
                predchodcovia.add(h.u.cislo);
            }
        }

        return predchodcovia;
    }

    // vsetky vrcholy do ktorych sa vchadza z vrchola v
    public ArrayList<Integer> dajNaslednikov(int vrchol) {
        ArrayList<Integer> naslednici = new ArrayList<>();

        for (OrHrana h : diGraf.orHrany) {
            if (h.u.cislo == vrchol) {
                naslednici.add(h.v.cislo);
            }
        }

        return naslednici;
    }

    // topologicke usporiadanie vrcholov
    public ArrayList<Integer> topologickeUsporiadanie() {
        int pocetVrcholov = diGraf.vrcholy.size();
        int[] vstupneStupne = new int[pocetVrcholov + 1];

        // vypocet vstupnych stupnov
        for (OrHrana h : diGraf.orHrany) {
            vstupneStupne[h.v.cislo]++;
        }

        // vrcholy so vstupnym stupnom 0
        ArrayList<Integer> kandidati = new ArrayList<>();
        for (int i = 1; i <= pocetVrcholov; i++) {
            if (vstupneStupne[i] == 0) {
                kandidati.add(i);
            }
        }

        ArrayList<Integer> poradie = new ArrayList<>();
        int index = 0;

        while (index < kandidati.size()) {
            int u = kandidati.get(index);
            index++;

            poradie.add(u);


            //kazdemu naslednikovi zobereme jeden stupen
            //ak klesol na nulu ide rovno do kandidatov
            for (int naslednik : dajNaslednikov(u)) {
                vstupneStupne[naslednik]--;
                if (vstupneStupne[naslednik] == 0) {
                    kandidati.add(naslednik);
                }
            }
        }

        if (poradie.size() != pocetVrcholov) {
            throw new IllegalStateException("Graf nie je acyklicky.");
        }

        return poradie;
    }

    // vypocet najskor moznych zaciatkov z(v)
    public void vypocitajZ() {
        this.topologickePoradie = topologickeUsporiadanie();

        for (int v : topologickePoradie) {
            ArrayList<Integer> predchodcovia = dajPredchodcov(v);

            if (predchodcovia.isEmpty()) {
                this.z[v] = 0;
            } else {
                int max = 0;

                for (int u : predchodcovia) {
                    int kandidat = this.z[u] + this.p[u];
                    if (kandidat > max) {
                        max = kandidat;
                    }
                }

                this.z[v] = max;
            }
        }
    }

    // vypocet trvania projektu T = max{z(v)+p(v)}
    public void vypocitajT() {
        int n = diGraf.vrcholy.size();
        T = 0;

        for (int v = 1; v <= n; v++) {
            int casDokoncenia = z[v] + p[v];
            if (casDokoncenia > T) {
                T = casDokoncenia;
            }
        }
    }

    // vypocet najneskor nutnych koncov k(v)
    public void vypocitajK() {
        if (topologickePoradie.isEmpty()) {
            throw new IllegalStateException("Najprv treba vypocitat topologicke poradie a z(v).");
        }

        int n = diGraf.vrcholy.size();

        // inicializacia
        for (int v = 1; v <= n; v++) {
            if (dajNaslednikov(v).isEmpty()) {
                k[v] = T;
            } else {
                k[v] = DiGraf.NEKONECNO;
            }
        }

        // ideme odzadu cez topologicke poradie
        for (int i = topologickePoradie.size() - 1; i >= 0; i--) {
            int v = topologickePoradie.get(i);
            ArrayList<Integer> naslednici = dajNaslednikov(v);

            if (!naslednici.isEmpty()) {
                int minimum = DiGraf.NEKONECNO;

                for (int s : naslednici) {
                    int kandidat = k[s] - p[s];
                    if (kandidat < minimum) {
                        minimum = kandidat;
                    }
                }

                k[v] = minimum;
            }
        }
    }

    // vypocet rezerv R(v) = k(v) - z(v) - p(v)
    public void vypocitajR() {
        int n = diGraf.vrcholy.size();

        for (int v = 1; v <= n; v++) {
            R[v] = k[v] - z[v] - p[v];
        }
    }

    // hlavna metoda - spravi vsetko ako v exceli
    public void vypocitajVsetko() {
        vypocitajZ();
        vypocitajT();
        vypocitajK();
        vypocitajR();
    }

    public boolean jeKriticky(int vrchol) {
        return R[vrchol] == 0;
    }

    public ArrayList<Integer> dajKritickeVrcholy() {
        ArrayList<Integer> kriticke = new ArrayList<>();
        int n = diGraf.vrcholy.size();

        for (int v = 1; v <= n; v++) {
            if (R[v] == 0) {
                kriticke.add(v);
            }
        }

        return kriticke;
    }

    public void vypisTopologickePoradie() {
        System.out.println("Topologicke poradie:");
        for (int v : topologickePoradie) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

    public void vypisTrvania() {
        int n = diGraf.vrcholy.size();

        System.out.println("Trvania p(v):");
        for (int v = 1; v <= n; v++) {
            System.out.println("v=" + v + ", p(v)=" + p[v]);
        }
    }

    public void vypisTabulkuZ() {
        int n = diGraf.vrcholy.size();

        System.out.println("Najskor mozne zaciatky:");
        System.out.println("v\tp(v)\tz(v)\tz(v)+p(v)");

        for (int v : topologickePoradie) {
            System.out.println(v + "\t" + p[v] + "\t" + z[v] + "\t" + (z[v] + p[v]));
        }

        System.out.println("T = " + T);
    }

    public void vypisTabulkuK() {
        int n = diGraf.vrcholy.size();

        System.out.println("Najneskor nutne konce:");
        System.out.println("v\tp(v)\tk(v)-p(v)\tk(v)");

        for (int i = topologickePoradie.size() - 1; i >= 0; i--) {
            int v = topologickePoradie.get(i);
            System.out.println(v + "\t" + p[v] + "\t" + (k[v] - p[v]) + "\t" + k[v]);
        }
    }

    public void vypisTabulkuR() {
        int n = diGraf.vrcholy.size();

        System.out.println("Rezervy:");
        System.out.println("v\tp(v)\tz(v)\tk(v)\tR(v)");

        for (int v = 1; v <= n; v++) {
            System.out.println(v + "\t" + p[v] + "\t" + z[v] + "\t" + k[v] + "\t" + R[v]);
        }
    }

    public void vypisKritickeVrcholy() {
        ArrayList<Integer> kriticke = dajKritickeVrcholy();

        System.out.println("Kriticke vrcholy (R(v)=0):");
        for (int v : kriticke) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

    public void vypisVsetko() {
        vypisTopologickePoradie();
        System.out.println();

        vypisTabulkuZ();
        System.out.println();

        vypisTabulkuK();
        System.out.println();

        vypisTabulkuR();
        System.out.println();

        vypisKritickeVrcholy();
    }
}