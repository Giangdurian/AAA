
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class SAP {

    private final Digraph sapDigraph;
    private final HashMap<HashSet<Integer>, int[]> cache;


    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Digraph is null");
        }
        sapDigraph = G;

        cache = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!validateVertice(v) || !validateVertice(w)) {
            throw new IllegalArgumentException("Invalid vertices");
        }

        HashSet<Integer> vKey = new HashSet<>();
        vKey.add(v);
        vKey.add(w);

        if(cache.containsKey(vKey)){
            return cache.get(vKey)[0];
        }

        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(sapDigraph, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(sapDigraph, w);
        int res = Integer.MAX_VALUE;
        int ancestor = -1;
        for(int i = 1; i <= sapDigraph.V(); i++){
            int dist1 = vBfs.distTo(i);
            int dist2 = wBfs.distTo(i);
            if(dist1 + dist2 <= res){
                res = dist1 + dist2;
                ancestor = i;
            }
        }

        if(res == Integer.MAX_VALUE){
            res = -1;
        }
        cache.put(vKey, new int[]{res, ancestor});
        return res;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        if (!validateVertice(v) || !validateVertice(w)) {
            throw new IllegalArgumentException("Invalid vertices");
        }

        length(v, w);
        HashSet<Integer> vKey = new HashSet<>();
        vKey.add(v);
        vKey.add(w);
        return cache.get(vKey)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[1];
    }


    private boolean validateVertice(int v) {
        return v >= 0 && v < sapDigraph.V();
    }

    private boolean validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            return false;
        }
        if(!vertices.iterator().hasNext()){
            return false;
        }
        for (int v : vertices) {
            if (!validateVertice(v)) {
                return false;
            }
        }
        return true;
    }

    public int[] sap(Iterable<Integer> v, Iterable<Integer> w){
        if(!validateVertices(v) || !validateVertices(w)){
            throw new IllegalArgumentException("Invalid vertices");
        }

        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(sapDigraph, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(sapDigraph, w);

        int res = Integer.MAX_VALUE;
        int ancestor = -1;

        for(int i = 0; i < sapDigraph.V(); i++){
            int sum = vBfs.distTo(i) + wBfs.distTo(i);
            if(sum <= res){
                sum = res;
                ancestor = i;
            }
        }

        if(res == Integer.MAX_VALUE){
            res = -1;
        }
        return new int[]{res, ancestor};
    }

    public static void main(String[] args) {
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}