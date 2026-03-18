public class OrHrana {
    public Vrchol u;
    public Vrchol v;
    public int c_h;

    public OrHrana(Vrchol u, Vrchol v, int c_h) {
        this.v = v;
        this.u = u;
        this.c_h = c_h;
    }

    public OrHrana(Vrchol u, Vrchol v) {
        this.v = v;
        this.u = u;
        this.c_h = 1;
    }

    public boolean jeIncidentna(Vrchol v) {
        return this.u == v || this.v == v;
    }

    @Override
    public String toString() {
        return "(" +
            "u=" + u.cislo +
            ", v=" + v.cislo +
            ", c_h=" + c_h;
    }
}
