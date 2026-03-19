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

        // epsilony
        ArrayList<Vrchol> epsilon = new ArrayList<>();
        epsilon.add(pociatocnyVrchol);

        // Mnozina natrvalo oznacenych vrcholov.
        ArrayList<Vrchol> natrvaloOznacene = new ArrayList<>();
        int krok = 1;

        while (!epsilon.isEmpty()) {
            System.out.println();
            System.out.println("KROK " + krok + ": epsilon = " + formatujEpsilon(epsilon));

            // riadiaci podla epsilonu
            Vrchol r = vyberRiadiciVrchol(epsilon);

            System.out.println("r = " + r.cislo);
            // vyhodime z epsilonu, pridqme do oznacenych nech uz sa mnespustaju znova
            epsilon.remove(r);
            natrvaloOznacene.add(r);

            boolean maOdchadzajuceHrany = false;
            for (OrHrana h : graf.orHrany) {
                // odfiltrujeme hrany ktore su incidentne s riadiacim v
                if (h.u != r) {
                    continue;
                }

                maOdchadzajuceHrany = true;

                Vrchol j = h.v;
                // cena do r
                int t_i = r.t;
                // cena hrany h.
                int c_h = h.c_h;
                // Kandidátna cena do vrcholu j cez riadiaci vrchol r.
                int kandidat = t_i + c_h;
                // stara znacka
                int staraZnacka = j.t;

                if (natrvaloOznacene.contains(j)) {
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
                    if (!epsilon.contains(j)) {
                        epsilon.add(j);
                    }
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

            System.out.println("epsilon -> " + formatujEpsilon(epsilon));

            //pocitame kroky pre pekny vypis
            krok++;
        }

        System.out.println("V epsylone uz nie su kandidati. Koniec.");

        //   výpis t(v)|x(v)
        vypisZnacky();
    }

    // nastavime znacky na zaciatku
    private void inicializujZnacky(Vrchol pociatocnyVrchol) {
        for (Vrchol v : graf.vrcholy) {
            v.t = DiGraf.NEKONECNO;
        }
        pociatocnyVrchol.t = 0;
    }

    // vyberieme z epsilonu r podla najmensej znacky
    private Vrchol vyberRiadiciVrchol(ArrayList<Vrchol> epsilonVrcholy) {
        Vrchol r = epsilonVrcholy.get(0);
        int najlepsiaZnacka = DiGraf.NEKONECNO;

        for (Vrchol v : epsilonVrcholy) {
            if (v.t < najlepsiaZnacka) {
                najlepsiaZnacka = v.t;
                r = v;
            }
        }

        return r;
    }

    // Naformátuje množinu epsilon do textu, napr. {2, 4, 7}.
    // vyberieme cislo vrcholu v mnozine epsilonov
    private String formatujEpsilon(ArrayList<Vrchol> epsilonVrcholy) {
        String epsilon = "{";

        boolean prvy = true;
        for (Vrchol v : epsilonVrcholy) {
            if (!prvy) {
                epsilon += ", ";
            }
            epsilon += v.cislo;
            prvy = false;
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
