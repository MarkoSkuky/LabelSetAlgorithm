import java.util.ArrayList;

public class LabelSet {
    private final DiGraf graf;
    private boolean vypocitane;
    private Vrchol poslednyPociatocnyVrchol;

    public LabelSet(DiGraf graf) {
        this.graf = graf;
        this.vypocitane = false;
        this.poslednyPociatocnyVrchol = null;
    }

    public void najdiNajkratsieCesty(Vrchol pociatocnyVrchol) {
        inicializujZnacky(pociatocnyVrchol);

        boolean[] jeNatrvaloOznaceny = new boolean[graf.vrcholy.size() + 1];
        int krok = 1;

        while (true) {
            System.out.println();
            System.out.println("KROK " + krok + ": epsilon = " + formatujEpsilon(jeNatrvaloOznaceny));

            Vrchol r = vyberRiadiciVrchol(jeNatrvaloOznaceny);
            if (r == null) {
                System.out.println("V epsylone uz nie su kandidati. Koniec.");
                break;
            }

            System.out.println("r = " + r.cislo);
            jeNatrvaloOznaceny[r.cislo] = true;

            boolean maOdchadzajuceHrany = false;
            for (OrHrana h : graf.orHrany) {
                if (h.u != r) {
                    continue;
                }

                maOdchadzajuceHrany = true;

                Vrchol j = h.v;
                int t_i = r.t;
                int c_h = h.c_h;
                int kandidat = t_i + c_h;
                int staraZnacka = j.t;

                if (jeNatrvaloOznaceny[j.cislo]) {
                    System.out.println(
                        "h=(" + h.u.cislo + "," + h.v.cislo + "), c(h)=" + c_h +
                        ", t(i)=" + formatujZnacku(t_i) +
                        " -> bez zmeny (vrchol " + j.cislo + " je uz natrvalo oznaceny)"
                    );
                } else if (t_i != DiGraf.NEKONECNO && kandidat < j.t) {
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

            if (!maOdchadzajuceHrany) {
                System.out.println("r nema ziadne odchadzajuce hrany.");
            }

            System.out.println("epsilon -> " + formatujEpsilon(jeNatrvaloOznaceny));

            krok++;
        }

        vypocitane = true;
        poslednyPociatocnyVrchol = pociatocnyVrchol;
        vypisZnacky();
    }

    public void najdiNajkratsieCesty(int pociatocnyVrcholCislo) {
        najdiNajkratsieCesty(graf.dajVrchol(pociatocnyVrcholCislo));
    }

    public void najdiNajkratsiuCestuDo(Vrchol pociatocnyVrchol, Vrchol ciel) {
        if (!vypocitane || poslednyPociatocnyVrchol != pociatocnyVrchol) {
            najdiNajkratsieCesty(pociatocnyVrchol);
        }
        ArrayList<Vrchol> cesta = zostrojCestuDo(ciel);
        if (cesta.isEmpty()) {
            System.out.println("Do vrchola " + ciel.cislo + " neexistuje cesta.");
            return;
        }

        System.out.print("Najkratsia cesta do vrchola " + ciel.cislo + " je: ");
        for (int i = 0; i < cesta.size(); i++) {
            System.out.print(cesta.get(i).cislo);
            if (i < cesta.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println(" | dlzka = " + ciel.t);
    }

    public void najdiNajkratsiuCestuDo(int pociatocnyVrcholCislo, int cielCislo) {
        najdiNajkratsiuCestuDo(graf.dajVrchol(pociatocnyVrcholCislo), graf.dajVrchol(cielCislo));
    }

    private void inicializujZnacky(Vrchol pociatocnyVrchol) {
        for (Vrchol v : graf.vrcholy) {
            v.t = DiGraf.NEKONECNO;
            v.x = null;
        }
        pociatocnyVrchol.t = 0;
        vypocitane = false;
        poslednyPociatocnyVrchol = null;
    }

    private Vrchol vyberRiadiciVrchol(boolean[] jeNatrvaloOznaceny) {
        Vrchol r = null;
        int najlepsiaZnacka = DiGraf.NEKONECNO;

        for (Vrchol v : graf.vrcholy) {
            if (!jeNatrvaloOznaceny[v.cislo] && v.t < najlepsiaZnacka) {
                najlepsiaZnacka = v.t;
                r = v;
            }
        }

        return r;
    }

    private String formatujEpsilon(boolean[] jeNatrvaloOznaceny) {
        StringBuilder epsilon = new StringBuilder();
        epsilon.append("{");

        boolean prvy = true;
        for (Vrchol v : graf.vrcholy) {
            if (!jeNatrvaloOznaceny[v.cislo] && v.t != DiGraf.NEKONECNO) {
                if (!prvy) {
                    epsilon.append(", ");
                }
                epsilon.append(v.cislo);
                prvy = false;
            }
        }

        epsilon.append("}");
        return epsilon.toString();
    }

    private String formatujZnacku(int znacka) {
        if (znacka == DiGraf.NEKONECNO) {
            return "INF";
        }
        return Integer.toString(znacka);
    }

    private ArrayList<Vrchol> zostrojCestuDo(Vrchol ciel) {
        ArrayList<Vrchol> cesta = new ArrayList<>();
        if (ciel.t == DiGraf.NEKONECNO) {
            return cesta;
        }

        for (Vrchol v = ciel; v != null; v = v.x) {
            cesta.add(0, v);
        }

        return cesta;
    }

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
