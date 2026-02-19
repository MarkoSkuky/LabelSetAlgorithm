import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graf {
    public ArrayList<Vrchol> vrcholy;
    public ArrayList<Hrana> hrany;

    public Graf() {
        this.vrcholy = new ArrayList<>();
        this.hrany = new ArrayList<>();
    }

    public Graf(int pocetVrcholov) {
        this.vrcholy = new ArrayList<>();
        this.hrany = new ArrayList<>();
        for (int i = 1; i <= pocetVrcholov; i++) {
            this.vrcholy.add(new Vrchol(i));
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
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */


    int n; // pocet vrcholov grafu
    int m; // pocet hran grafu
    int H[][]; // pole udajov o hranach

    public Graf(int paPocetVrcholov, int paPocetHran) {
        n = paPocetVrcholov;
        m = paPocetHran;
        H = new int[1 + m][3];
    }

    /*
    Nacitanie grafu zo suboru:
    Na kazdom riadku su tri cisla, prve a druhe cislo su cisla vrcholov
    a tretie cislo je ohodnotenie hrany.
    Pocet vrcholov aj pocet hran sa urci automaticky. Pocet hran je rovny
    poctu riadkov v subore a pocet vrcholov je rovny najvacsiemu cislu
    vrcholu v udajoch o hranach.
    */
    public static Graf nacitajSubor(String nazovSuboru)
        throws FileNotFoundException {
        // otvorim subor a pripravim Scanner pre nacitavanie
        Scanner s = new Scanner(new FileInputStream(nazovSuboru));

        // najskor len zistim pocet vrcholov a pocet hran
        int pocetVrcholov = 1;
        int pocetHran = 0;
        // prejdem cely subor
        while(s.hasNext()) {
            // nacitam udaje o hrane
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            // nacital som hranu, zvysim ich pocet o 1
            pocetHran++;

            // skontrolujem, ci netreba zvysit pocet vrcholov
            if (pocetVrcholov < u) pocetVrcholov = u;
            if (pocetVrcholov < v) pocetVrcholov = v;
        }
        // ukoncim nacitavanie zo suboru
        s.close();

        // vytvorim objekt grafu s potrebnym poctom vrcholo v aj hran
        Graf g = new Graf(pocetVrcholov);

        // po druhy krat otvorim ten isty subor,
        // uz pozanm pocet vrcholov aj hran a mam alokovanu pamat
        s = new Scanner(new FileInputStream(nazovSuboru));

        // postune nacitam vsetky hrany
        // tentokrat si ich uz budem aj ukladat do pamate
        for (int j = 1; j <= pocetHran; j++) {
            int u = s.nextInt();
            int v = s.nextInt();
            int c = s.nextInt();

            g.pridajHranu(u, v ,c);

            g.H[j][0] = u;
            g.H[j][1] = v;
            g.H[j][2] = c;
        }

        return g;
    }

    public void printInfo() {
        System.out.println("Pocet vrcholov: " + n);
        System.out.println("Pocet hran: " + m);
    }

}


