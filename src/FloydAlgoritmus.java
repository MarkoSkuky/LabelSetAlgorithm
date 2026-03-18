import java.util.ArrayList;

public class FloydAlgoritmus {
    private final DiGraf graf;

    // Počet vrcholov digrafu.
    private final int n;

    // vzdialenosti[i][j] predstavuje aktuálnu dĺžku najkratšej známej cesty z vrchola i do vrchola j.
    private final int[][] vzdialenosti;


    // Konštruktor pripraví matice a inicializuje ich podľa hrán digrafu.
    public FloydAlgoritmus(DiGraf graf) {
        this.graf = graf;
        this.n = graf.vrcholy.size();
        this.vzdialenosti = new int[n + 1][n + 1];
        inicializuj();
    }

    // Floydov algoritmus:
    // hľadá najkratšie cesty medzi všetkými dvojicami vrcholov.
    // V každom kroku skúša, či sa cesta i -> j nezlepší cez medzivrchol k.
    public void najdiNajkratsieCesty() {
        inicializuj();

        // k je aktuálny medzivrchol.
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) { ///riadok
                for (int j = 1; j <= n; j++) { ///stlpec
                    int kandidat = vzdialenosti[i][k] + vzdialenosti[k][j];

                    if (kandidat < vzdialenosti[i][j]) {
                        // Našli sme kratšiu cestu z i do j cez k.
                        vzdialenosti[i][j] = kandidat;
                    }
                }
            }
        }
    }

    // Vypíše maticu dĺžok najkratších ciest v tabuľkovom tvare.
    public void vypisMaticuVzdialenosti() {
        najdiNajkratsieCesty();

        System.out.println("Matica dĺžok najkratších ciest:");
        System.out.print("      ");
        for (int j = 1; j <= n; j++) {
            System.out.printf("%6d", j);
        }
        System.out.println();

        for (int i = 1; i <= n; i++) {
            System.out.printf("%6d", i);
            for (int j = 1; j <= n; j++) {
                if (vzdialenosti[i][j] >= DiGraf.NEKONECNO) {
                    System.out.printf("%6s", "INF");
                } else {
                    System.out.printf("%6d", vzdialenosti[i][j]);
                }
            }
            System.out.println();
        }
    }

    // Inicializácia matíc:
    // - na diagonále je 0
    // - mimo diagonály je inf
    // - a este orientovane hrany
    private void inicializuj() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                vzdialenosti[i][j] = DiGraf.NEKONECNO;
            }

            // Dĺžka cesty z vrchola do samého seba je 0.
            vzdialenosti[i][i] = 0;
        }

        for (OrHrana h : graf.orHrany) {
            int u = h.u.cislo;
            int v = h.v.cislo;
            int c_h = h.c_h;

            // Ak medzi rovnakou dvojicou vrcholov existuje viac orientovaných hrán,
            // ponecháme si najlacnejšiu.
            if (c_h < vzdialenosti[u][v]) {
                vzdialenosti[u][v] = c_h;
            }
        }
    }
}