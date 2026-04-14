import java.util.ArrayList;

public class CasovePlanovanie {
    private DiGraf diGraf;
    private int[] p; //trvania
    private int[] z; //najskor mozne zaciatky
    private int[] k; //najneskor nutne konce
    private int[] R; //rezervy

    public CasovePlanovanie(DiGraf diGraf) {
        this.diGraf = diGraf;

        int n = diGraf.vrcholy.size();

        this.p = new int[n + 1];
        this.z = new int[n + 1];
        this.k = new int[n + 1];
        this.R = new int[n + 1];
    }

    public void nastavTrvanie(int vrchol, int trvanie) {
        p[vrchol] = trvanie;
    }

    //vsetky vrcholy z ktorych sa vchadza do vrchola v
    public ArrayList<Integer> dajPredchodcov(int v) {
        ArrayList<Integer> prechodcovia = new ArrayList<>();
        for (OrHrana h : diGraf.orHrany) {
            if (h.v.cislo == v) {
                prechodcovia.add(h.u.cislo);
            }
        }
        return prechodcovia;
    }

    //vsetky vrcholy do ktorych sa vchadza z vrchola v
    public ArrayList<Integer> dajNaslednikov(int vrchol) {
        ArrayList<Integer> naslednici = new ArrayList<>();

        for (OrHrana h : diGraf.orHrany) {
            if (h.u.cislo == vrchol) {
                naslednici.add(h.v.cislo);
            }
        }
        return naslednici;
    }



    public ArrayList<Integer> topologickeUsporiadanie() {
        int n = diGraf.vrcholy.size();

        int[] vstupneStupne = new int[n + 1];

        for (OrHrana h : diGraf.orHrany) {
            vstupneStupne[h.v.cislo]++;
        }

        ArrayList<Integer> kandidati = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
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

            for (int naslednik : dajNaslednikov(u)) {
                vstupneStupne[naslednik]--;

                if (vstupneStupne[naslednik] == 0) {
                    kandidati.add(naslednik);
                }
            }
        }

        if (poradie.size() != n) {
            throw new IllegalStateException("Graf nie je acyklicky.");
        }

        return poradie;
    }


}
