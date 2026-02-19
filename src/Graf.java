import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graf {
    public ArrayList<Vrchol> vrcholy;
    public ArrayList<Hrana> hrany;

    int n; // pocet vrcholov grafu
    int m; // pocet hran grafu
    int H[][]; // pole udajov o hranach

    public Graf() {
        this.vrcholy = new ArrayList<>();
        this.hrany = new ArrayList<>();
    }

    public Graf(int pocetVrcholov) {
        this.vrcholy = new ArrayList<>();
        this.hrany = new ArrayList<>();
        this.n = pocetVrcholov;

        for (int i = 1; i <= pocetVrcholov; i++) {
            this.vrcholy.add(new Vrchol(i));
        }
    }

    public Graf(int paPocetVrcholov, int paPocetHran) {
        n = paPocetVrcholov;
        m = paPocetHran;

        H = new int[1 + m][3];

        vrcholy = new ArrayList<>();
        hrany = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            vrcholy.add(new Vrchol(i));
        }
    }

    public Vrchol dajVrchol(int v) {
        return this.vrcholy.get(v - 1);
    }

    public void pridajHranu(Vrchol u, Vrchol v) {
        this.hrany.add(new Hrana(u, v));
    }

    public void pridajHranu(int u, int v) {
        this.pridajHranu(dajVrchol(u), dajVrchol(v));
    }

    public void pridajHranu(int u, int v, int c) {
        this.pridajHranu(dajVrchol(u), dajVrchol(v));
        this.hrany.getLast().c = c;
    }

    /*
    Nacitanie grafu zo suboru
    */
    public static Graf nacitajSubor(String nazovSuboru)
        throws FileNotFoundException {

        Scanner s = new Scanner(new FileInputStream(nazovSuboru));

        int pocetVrcholov = 1;
        int pocetHran = 0;

        while (s.hasNext()) {
            int u = s.nextInt();
            int v = s.nextInt();
            s.nextInt(); // c

            pocetHran++;

            if (pocetVrcholov < u) pocetVrcholov = u;
            if (pocetVrcholov < v) pocetVrcholov = v;
        }
        s.close();

        // TU bola chyba – teraz je to správne
        Graf g = new Graf(pocetVrcholov, pocetHran);

        s = new Scanner(new FileInputStream(nazovSuboru));

        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            g.pridajHranu(u, v, c);

            g.H[j][0] = u;
            g.H[j][1] = v;
            g.H[j][2] = c;
        }

        s.close();
        return g;
    }

    public void printInfo() {
        System.out.println("Pocet vrcholov: " + n);
        System.out.println("Pocet hran: " + m);
    }

    public int stupenVrchola(Vrchol v) {
        int stupen = 0;
        for (Hrana h : hrany) {
            if (h.jeIncidentna(v)) {
                stupen++;
            }
        }
        return stupen;
    }

    public void printValencnuPostupnost() {
        for (Vrchol v : vrcholy) {
            System.out.print(stupenVrchola(v) + ", ");
        }
        System.out.println();
    }

    public void printSusedneHrany(Vrchol v) {
        ArrayList<Hrana> incidentne = new ArrayList<>();
        for (Hrana h : hrany) {
            if (h.jeIncidentna(v)) {
                incidentne.add(h);
            }
        }
        for (Hrana hrana : incidentne) {
            System.out.print("{" + hrana.u.cislo + ", " + hrana.v.cislo + "} ");
        }
    }
}