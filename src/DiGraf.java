import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DiGraf {
    public static final int NEKONECNO = 99999;

    public ArrayList<Vrchol> vrcholy;
    public ArrayList<OrHrana> orHrany;

    int n;
    int m;
    int H[][];

    public DiGraf() {
        this.vrcholy = new ArrayList<>();
        this.orHrany = new ArrayList<>();
    }

    public DiGraf(int pocetVrcholov) {
        this.vrcholy = new ArrayList<>();
        this.orHrany = new ArrayList<>();
        this.n = pocetVrcholov;

        for (int i = 1; i <= pocetVrcholov; i++) {
            this.vrcholy.add(new Vrchol(i));
        }
    }

    public DiGraf(int paPocetVrcholov, int paPocetHran) {
        n = paPocetVrcholov;
        m = paPocetHran;

        H = new int[1 + m][3];

        vrcholy = new ArrayList<>();
        orHrany = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            vrcholy.add(new Vrchol(i));
        }
    }

    public void zakladny(Vrchol u) {
        for (Vrchol v : vrcholy) {
            v.t = NEKONECNO;
            v.x = null;
        }
        u.t = 0;

        boolean urobitKolo = true;
        while(urobitKolo) {
            urobitKolo = false;
            System.out.println("zacinam kolo");
            for (OrHrana h : orHrany) {
                Vrchol r = h.u;
                Vrchol j = h.v;
                int t_i = r.t;
                int c_h = h.c_h;

                System.out.println("kontrolujem orientovanu hranu " + h + " t(i) = " + t_i + " c(h) = " + c_h + " t(j) = " + j.t);
                if (j.t > t_i + c_h) {
                    j.t = t_i + c_h;
                    j.x = r;
                    urobitKolo = true;
                    System.out.println("nasiel som zlepsenie, upravujem znacky: t(j) =" + j.t + " x(j) =" + j.x);
                }
            }
        }

        System.out.println("Vysledok");
        for (Vrchol v : vrcholy) {
            System.out.println("vrchol " + v +": " + v.t + "|" + v.x);
        }
    }

    public Vrchol dajVrchol(int v) {
        return this.vrcholy.get(v - 1);
    }

    public void pridajHranu(Vrchol u, Vrchol v) {
        this.orHrany.add(new OrHrana(u, v));
    }

    public void pridajHranu(int u, int v) {
        this.pridajHranu(dajVrchol(u), dajVrchol(v));
    }

    public void pridajHranu(int u, int v, int c_h) {
        this.pridajHranu(dajVrchol(u), dajVrchol(v));
        this.orHrany.getLast().c_h = c_h;
    }

    
    public static DiGraf nacitajSubor(String nazovSuboru)
        throws FileNotFoundException {

        Scanner s = new Scanner(new FileInputStream(nazovSuboru));

        int pocetVrcholov = 1;
        int pocetHran = 0;

        while (s.hasNext()) {
            int u = s.nextInt();
            int v = s.nextInt();
            s.nextInt();

            pocetHran++;

            if (pocetVrcholov < u) pocetVrcholov = u;
            if (pocetVrcholov < v) pocetVrcholov = v;
        }
        s.close();

        DiGraf g = new DiGraf(pocetVrcholov, pocetHran);

        s = new Scanner(new FileInputStream(nazovSuboru));

        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c_h = s.nextInt();

            g.pridajHranu(u, v, c_h);

            g.H[j][0] = u;
            g.H[j][1] = v;
            g.H[j][2] = c_h;
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
        for (OrHrana h : orHrany) {
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
        ArrayList<OrHrana> incidentne = new ArrayList<>();
        for (OrHrana h : orHrany) {
            if (h.jeIncidentna(v)) {
                incidentne.add(h);
            }
        }
        for (OrHrana hrana : incidentne) {
            System.out.print("{" + hrana.u.cislo + ", " + hrana.v.cislo + "} ");
        }
    }
}
