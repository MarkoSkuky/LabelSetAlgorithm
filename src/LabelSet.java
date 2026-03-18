import java.util.ArrayList;

public class LabelSet {

    private final DiGraf graf;

    public LabelSet(DiGraf graf) {
        this.graf = graf;
    }

    //metoda algoritmu
    public void najdiNajkratsieCesty(Vrchol pociatocnyVrchol) {

        //vsetky znacky = nekonecno, start = 0
        inicializujZnacky(pociatocnyVrchol);

        // Pole pre natrvalo označené vrcholy.
        boolean[] jeNatrvaloOznaceny = new boolean[graf.vrcholy.size() + 1];
        int krok = 1;

        while (true) {
            System.out.println();
            // Epsilon = kandidáti (vrcholy s konečnou značkou, ktoré ešte nie sú natrvalo označené).
            System.out.println("KROK " + krok + ": epsilon = " + formatujEpsilon(jeNatrvaloOznaceny));

            // Vyber riadiaci vrchol r s najmenšou dočasnou značkou t.
            Vrchol r = vyberRiadiciVrchol(jeNatrvaloOznaceny);
            if (r == null) {
                // Ak v epsylone už nič nie je, algoritmus končí.
                System.out.println("V epsylone uz nie su kandidati. Koniec.");
                break;
            }

            System.out.println("r = " + r.cislo);
            // Riadiaci vrchol natrvalo oznacime lebo lepsiu cestu donho uz nenajdeme
            jeNatrvaloOznaceny[r.cislo] = true;

            boolean maOdchadzajuceHrany = false;
            for (OrHrana h : graf.orHrany) {
                // Filter: riešime len hrany, ktoré idú z riadiaceho vrcholu r.
                if (h.u != r) {
                    continue;
                }

                maOdchadzajuceHrany = true;

                Vrchol j = h.v;
                // t(i): cena do riadiaceho vrcholu r.
                int t_i = r.t;
                // c(h): cena hrany h.
                int c_h = h.c_h;
                // Kandidátna cena do vrcholu j cez riadiaci vrchol r.
                int kandidat = t_i + c_h;
                // Stará značka j pre výpis zmien.
                int staraZnacka = j.t;

                if (jeNatrvaloOznaceny[j.cislo]) {
                    // Do natrvalo označeného vrcholu už nezasahujeme.
                    System.out.println(
                        "h=(" + h.u.cislo + "," + h.v.cislo + "), c(h)=" + c_h +
                        ", t(i)=" + formatujZnacku(t_i) +
                        " -> bez zmeny (vrchol " + j.cislo + " je uz natrvalo oznaceny)"
                    );
                } else if (t_i != DiGraf.NEKONECNO && kandidat < j.t) {
                    // nasla sa lepsia cesta do j
                    j.t = kandidat;
                    j.x = r;
                    System.out.println(
                        "h=(" + h.u.cislo + "," + h.v.cislo + "), c(h)=" + c_h +
                        ", t(i)=" + formatujZnacku(t_i) +
                        " -> zmena: t(" + j.cislo + ") " +
                        formatujZnacku(staraZnacka) + " -> " + formatujZnacku(j.t) +
                        ", x(" + j.cislo + ")=" + r.cislo
                    );
                } else {
                    System.out.println(
                        "h=(" + h.u.cislo + "," + h.v.cislo + "), c(h)=" + c_h +
                        ", t(i)=" + formatujZnacku(t_i) +
                        " -> bez zmeny (kandidat = " +
                        formatujZnacku(kandidat) +
                        ", t(" + j.cislo + ") = " +
                        formatujZnacku(staraZnacka) + ")"
                    );
                }
            }

            //ak z riadiaceho vrcholu nie je kam ist
            if (!maOdchadzajuceHrany) {
                System.out.println("r nema ziadne odchadzajuce hrany.");
            }

            System.out.println("epsilon -> " + formatujEpsilon(jeNatrvaloOznaceny));

            //pocitame kroky pre pekny vypis
            krok++;
        }

        // Finálny výpis t(v)|x(v) po skončení algoritmu.
        vypisZnacky();
    }

    // Nastaví počiatočné značky pred spustením algoritmu.
    private void inicializujZnacky(Vrchol pociatocnyVrchol) {
        for (Vrchol v : graf.vrcholy) {
            v.t = DiGraf.NEKONECNO;
            v.x = null;
        }
        pociatocnyVrchol.t = 0;
    }

    // Nájde kandidáta s najmenšou dočasnou značkou.
    private Vrchol vyberRiadiciVrchol(boolean[] jeNatrvaloOznaceny) {
        //zatial null
        Vrchol r = null;
        int najlepsiaZnacka = DiGraf.NEKONECNO;


        for (Vrchol v : graf.vrcholy) {

            //ak este nieje oznaceny a zaroven sa donho vieme dostat
            if (!jeNatrvaloOznaceny[v.cislo] && v.t < najlepsiaZnacka) {
                najlepsiaZnacka = v.t;
                r = v;
            }
        }

        return r;
    }

    // Naformátuje množinu epsilon do textu, napr. {2, 4, 7}.
    private String formatujEpsilon(boolean[] jeNatrvaloOznaceny) {
        String epsilon = "{";

        boolean prvy = true;
        //kazdy vrchol prejdeme
        for (Vrchol v : graf.vrcholy) {
            //ak vrchol este nie je oznaceny a vieme sa donho dostat cize uz k nemu cesta nie je nekonecno:
            if (!jeNatrvaloOznaceny[v.cislo] && v.t != DiGraf.NEKONECNO) {
                if (!prvy) {
                    epsilon += ", ";
                }
                epsilon += v.cislo;
                prvy = false;
            }
        }
        epsilon += "}";
        return epsilon;
    }

    // Prevod internej značky na text: nekonečno -> INF, inak číslo.
    //znacka je sucet cien hran po ktorych sme sa dostali k vrecholu
    private String formatujZnacku(int znacka) {
        if (znacka == DiGraf.NEKONECNO) {
            return "INF";
        }
        return Integer.toString(znacka);
    }

    // Vypíše výsledné značky t(v)|x(v) pre všetky vrcholy.
    private void vypisZnacky() {
        System.out.println();
        System.out.println("Vysledne znacky t(v)|x(v):");
        for (Vrchol v : graf.vrcholy) {
            String t_v = formatujZnacku(v.t);
            String x_v;
            if (v.x == null) {
                x_v = "-";
            } else {
                x_v = Integer.toString(v.x.cislo);
            }
            System.out.println("vrchol " + v.cislo + ": " + t_v + "|" + x_v);
        }
    }
}
