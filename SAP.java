import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private int[] edgeTo;
    private int[] distTo;
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.distTo = new int[G.V()];
        this.edgeTo = new int[G.V()];
        this.G = G;
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Queue<Integer> queue = new Queue<Integer>();
        int[] flaged = new int[this.G.V()];
        queue.enqueue(v);
        queue.enqueue(w);
        flaged[v] = v;
        flaged[w] = w;
        distTo[v] = 0;
        distTo[w] = 0;

        while (!queue.isEmpty()) {
            int current = queue.dequeue();

            for (int y : this.G.adj(current)) {
                if (flaged[y] == 0) {
                    flaged[y] = flaged[current];
                    distTo[y] = distTo[current] + 1;
                    edgeTo[y] = current;
                    queue.enqueue(y);
                }
                else if (flaged[y] != flaged[current]) {
                    return distTo[y] + distTo[current] + 1;
                }
            }
        }

        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Queue<Integer> queue = new Queue<Integer>();
        int[] flaged = new int[this.G.V()];
        queue.enqueue(v);
        queue.enqueue(w);
        flaged[v] = v;
        flaged[w] = w;
        distTo[v] = 0;
        distTo[w] = 0;

        while (!queue.isEmpty()) {
            int current = queue.dequeue();

            for (int y : this.G.adj(current)) {
                if (flaged[y] == 0) {
                    flaged[y] = flaged[current];
                    distTo[y] = distTo[current] + 1;
                    edgeTo[y] = current;
                    queue.enqueue(y);
                }
                else if (flaged[y] != flaged[current]) {
                    return y;
                }
            }
        }

        return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> queue = new Queue<Integer>();
        int[] flaged = new int[this.G.V()];

        for (int s : v) {
            queue.enqueue(s);
            flaged[s] = 1;
            distTo[s] = 0;
        }

        for (int s : w) {
            queue.enqueue(s);
            flaged[s] = 2;
            distTo[s] = 0;
        }


        while (!queue.isEmpty()) {
            int current = queue.dequeue();

            for (int y : this.G.adj(current)) {
                if (flaged[y] == 0) {
                    flaged[y] = flaged[current];
                    distTo[y] = distTo[current] + 1;
                    edgeTo[y] = current;
                    queue.enqueue(y);
                }
                else if (flaged[y] != flaged[current]) {
                    return distTo[y] + distTo[current] + 1;
                }
            }
        }

        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Queue<Integer> queue = new Queue<Integer>();
        int[] flaged = new int[this.G.V()];

        for (int s : v) {
            queue.enqueue(s);
            flaged[s] = 1;
            distTo[s] = 0;
        }

        for (int s : w) {
            queue.enqueue(s);
            flaged[s] = 2;
            distTo[s] = 0;
        }


        while (!queue.isEmpty()) {
            int current = queue.dequeue();

            for (int y : this.G.adj(current)) {
                if (flaged[y] == 0) {
                    flaged[y] = flaged[current];
                    distTo[y] = distTo[current] + 1;
                    edgeTo[y] = current;
                    queue.enqueue(y);
                }
                else if (flaged[y] != flaged[current]) {
                    return y;
                }
            }
        }

        return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        Queue<Integer> v = new Queue<Integer>();
        v.enqueue(7);
        v.enqueue(8);
        v.enqueue(9);
        v.enqueue(10);
        Queue<Integer> w = new Queue<Integer>();
        w.enqueue(13);
        w.enqueue(14);
        w.enqueue(0);
        w.enqueue(12);


        int ancestor = sap.ancestor(v, w);
        int length = sap.length(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

    }
}
