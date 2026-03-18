import java.util.ArrayList;

public class FloydAlgoritmus {
    private final DiGraf graf;
    private final int n;
    private final int[][] vzdialenosti;
    private final int[][] dalsiVrchol;
    private boolean vypocitane;

    public FloydAlgoritmus(DiGraf graf) {
        this.graf = graf;
        this.n = graf.vrcholy.size();
        this.vzdialenosti = new int[n + 1][n + 1];
        this.dalsiVrchol = new int[n + 1][n + 1];
        inicializuj();
        this.vypocitane = false;
    }

    public void najdiNajkratsieCesty() {
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                if (vzdialenosti[i][k] == DiGraf.NEKONECNO) {
                    continue;
                }
                for (int j = 1; j <= n; j++) {
                    if (vzdialenosti[k][j] == DiGraf.NEKONECNO) {
                        continue;
                    }
                    int kandidat = vzdialenosti[i][k] + vzdialenosti[k][j];
                    if (kandidat < vzdialenosti[i][j]) {
                        vzdialenosti[i][j] = kandidat;
                        dalsiVrchol[i][j] = dalsiVrchol[i][k];
                    }
                }
            }
        }
        vypocitane = true;
    }

    public int dajVzdialenost(int od, int doVrchu) {
        overRozsahVrchola(od);
        overRozsahVrchola(doVrchu);
        if (!vypocitane) {
            najdiNajkratsieCesty();
        }
        return vzdialenosti[od][doVrchu];
    }

    public ArrayList<Integer> najdiNajkratsiuCestu(int od, int doVrchu) {
        overRozsahVrchola(od);
        overRozsahVrchola(doVrchu);
        if (!vypocitane) {
            najdiNajkratsieCesty();
        }

        ArrayList<Integer> cesta = new ArrayList<>();
        if (dalsiVrchol[od][doVrchu] == 0) {
            return cesta;
        }

        int aktualny = od;
        cesta.add(aktualny);
        while (aktualny != doVrchu) {
            aktualny = dalsiVrchol[aktualny][doVrchu];
            if (aktualny == 0) {
                cesta.clear();
                return cesta;
            }
            cesta.add(aktualny);
        }

        return cesta;
    }

    public void vypisMaticuVzdialenosti() {
        if (!vypocitane) {
            najdiNajkratsieCesty();
        }

        System.out.println("Matica najkratsich vzdialenosti:");
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

    public void vypisNajkratsiuCestu(int od, int doVrchu) {
        ArrayList<Integer> cesta = najdiNajkratsiuCestu(od, doVrchu);
        if (cesta.isEmpty()) {
            System.out.println("Cesta z vrchola " + od + " do vrchola " + doVrchu + " neexistuje.");
            return;
        }

        System.out.print("Najkratsia cesta z " + od + " do " + doVrchu + ": ");
        for (int i = 0; i < cesta.size(); i++) {
            System.out.print(cesta.get(i));
            if (i < cesta.size() - 1) {
                System.out.print(" -> ");
            }
        }
        int dlzka = dajVzdialenost(od, doVrchu);
        System.out.println(" | dlzka = " + dlzka);
    }

    public boolean maZapornyCyklus() {
        if (!vypocitane) {
            najdiNajkratsieCesty();
        }
        for (int i = 1; i <= n; i++) {
            if (vzdialenosti[i][i] < 0) {
                return true;
            }
        }
        return false;
    }

    private void inicializuj() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                vzdialenosti[i][j] = DiGraf.NEKONECNO;
                dalsiVrchol[i][j] = 0;
            }
            vzdialenosti[i][i] = 0;
            dalsiVrchol[i][i] = i;
        }

        for (OrHrana h : graf.orHrany) {
            int u = h.u.cislo;
            int v = h.v.cislo;
            int c_h = h.c_h;
            if (c_h < vzdialenosti[u][v]) {
                vzdialenosti[u][v] = c_h;
                dalsiVrchol[u][v] = v;
            }
        }
    }

    private void overRozsahVrchola(int vrchol) {
        if (vrchol < 1 || vrchol > n) {
            throw new IllegalArgumentException("Vrchol " + vrchol + " je mimo rozsahu 1.." + n);
        }
    }
}
