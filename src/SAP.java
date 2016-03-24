/* Corner cases.  
 * All methods should throw a java.lang.NullPointerException if any argument is null. 
 * All methods should throw a java.lang.IndexOutOfBoundsException if any argument vertex is invalid—not between 0 and G.V() - 1.

Performance requirements.  
All methods (and the constructor) should take time at most proportional to E + V in the worst case,
where E and V are the number of edges and vertices in the digraph, respectively. 
Your data type should use space proportional to E + V. */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

	// constructor takes a digraph (not necessarily a DAG)
	/*
	 * How can I make the data type SAP immutable? You can (and should) save the
	 * associated digraph in an instance variable. However, because our Digraph
	 * data type is mutable, you must first make a defensive copy by calling the
	 * copy constructor.
	 */
	public SAP(Digraph G) {
		if (G == null) {
			throw new java.lang.NullPointerException();
		}

		g = new Digraph(G);

		cache = new HashMap<>();

	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		// * All methods should throw a java.lang.IndexOutOfBoundsException if
		// any argument vertex is invalid—not between 0 and G.V() - 1.

		Result r = determineAncestor(Arrays.asList(v), Arrays.asList(w));

		int length = -1;
		if (r != null) {
			length = r.length;
		}

		return length;
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		// * All methods should throw a java.lang.IndexOutOfBoundsException if
		// any argument vertex is invalid—not between 0 and G.V() - 1.

		Result r = determineAncestor(Arrays.asList(v), Arrays.asList(w));

		int ancestor = -1;
		if (r != null) {
			ancestor = r.ancestor;
		}

		return ancestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		Result r = determineAncestor(v, w);

		int length = -1;
		if (r != null) {
			length = r.length;
		}

		return length;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		Result r = determineAncestor(v, w);

		int ancestor = -1;
		if (r != null) {
			ancestor = r.ancestor;
		}

		return ancestor;
	}

	private void checkValidVertex(int w) {
		if (w < 0 || w >= g.V())
			throw new java.lang.IndexOutOfBoundsException();

	}

	private void checkValidVertices(Iterable<Integer> v) {
		if (v == null) {
			throw new java.lang.NullPointerException();
		}
		v.forEach(x -> checkValidVertex(x));

	}

	private Result determineAncestor(Iterable<Integer> v, Iterable<Integer> w) {
		checkValidVertices(v);
		checkValidVertices(w);

		String vName = getKey(v);
		String wName = getKey(w);

		Result r = null;
		if (inCache(vName, wName)) {
			r = cache.get(vName).get(wName);
		} else {
			int bestAncestor = 0;
			int bestDistance = Integer.MAX_VALUE;

			BreadthFirstDirectedPaths vbfs = new BreadthFirstDirectedPaths(g, v);

			BreadthFirstDirectedPaths wbfs = new BreadthFirstDirectedPaths(g, w);

			boolean visited[] = new boolean[g.V()];
			Queue<Integer> stack = new LinkedList<Integer>();
			v.forEach(x -> stack.add(x));

			while (!stack.isEmpty()) {
				// for (int i = 0; i < g.V(); i++) {

				int i = stack.remove();
				if (visited[i]) {
					continue;
				}
				
				visited[i] = true;
				Iterable<Integer> adj = g.adj(i);
				adj.forEach(x-> {if(!visited[x]) {stack.add(x);}});

				if (vbfs.hasPathTo(i) && wbfs.hasPathTo(i)) {
					int dv = vbfs.distTo(i);
					int dw = wbfs.distTo(i);

					int newDistance = dv + dw;

					if (newDistance < bestDistance) {
						bestDistance = newDistance;
						bestAncestor = i;
						r = new Result(bestDistance, bestAncestor);
					}
				}

			}

			if (r != null) {
				cache.put(vName, new HashMap<String, Result>());
				cache.get(vName).put(wName, r);
			}
		}

		return r;

	}

	private String getKey(Iterable<Integer> v) {
		List<String> newList = new ArrayList<String>();
		for (Integer myInt : v) {
			newList.add(String.valueOf(myInt));
		}

		String vName = String.join(",", newList);
		return vName;
	}

	private boolean inCache(String v, String w) {
		return cache.containsKey(v) && cache.get(v).containsKey(w);
	}

	private Digraph g;

	private HashMap<String, HashMap<String, Result>> cache;

	private class Result {
		private int length;
		private int ancestor;

		Result(int length, int ancestor) {
			this.length = length;
			this.ancestor = ancestor;
		}
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
}
