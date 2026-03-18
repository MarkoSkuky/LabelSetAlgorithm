public class Vrchol {
    public int cislo;

    public int t;
    public Vrchol x;

    public Vrchol(int cislo) {
        this.cislo = cislo;
        this.t = DiGraf.NEKONECNO;
        this.x = null;
    }

    public String toString() {
        return "" + cislo;
    }
}
