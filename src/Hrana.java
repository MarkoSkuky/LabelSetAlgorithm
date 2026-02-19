public class Hrana {
    public Vrchol u;
    public Vrchol v;
    public int c;

    public Hrana(Vrchol u, Vrchol v, int c) {
        this.v = v;
        this.u = u;
        this.c = c;
    }

    public Hrana(Vrchol u, Vrchol v) {
        this.v = v;
        this.u = u;
        this.c = 1;
    }
}
