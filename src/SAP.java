/* Corner cases.  
 * All methods should throw a java.lang.NullPointerException if any argument is null. 
 * All methods should throw a java.lang.IndexOutOfBoundsException if any argument vertex is invalid—not between 0 and G.V() - 1.

Performance requirements.  
All methods (and the constructor) should take time at most proportional to E + V in the worst case,
where E and V are the number of edges and vertices in the digraph, respectively. 
Your data type should use space proportional to E + V. */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

		cache = new Cache();

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
		if (cache.isInCache(vName, wName)) {
			r = cache.get(vName,wName);
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
						//System.out.println(r);
					}
				}

			}

			if (r != null) {
				cache.put(vName, wName, r);
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

	private Digraph g;
	private Cache cache;

	
	private class Cache {
		private HashMap<String, HashMap<String, Result>> cache;

		private Cache() {
			cache = new HashMap<>();
		}
		
		private boolean isInCache(String v, String w) {
			boolean isInCache = cache.containsKey(v) && cache.get(v).containsKey(w);			
//			if (isInCache) {
//				cache.get(v).get(w).ts = Calendar.getInstance().getTime();
//			}			
			return isInCache;
		}
		
		private Result get(String v, String w) {
			//cache.get(v).get(w).ts = Calendar.getInstance().getTime();
			return cache.get(v).get(w);	
		}
		
		private void put(String v, String w, Result r) {
			cache.put(v, new HashMap<String, Result>());
			cache.get(v).put(w, r);
		}
	}

	private class Result {
		private int length;
		private int ancestor;
		//private Date ts;

		Result(int length, int ancestor) {
			this.length = length;
			this.ancestor = ancestor;
			//ts = Calendar.getInstance().getTime();
		}
		
		@Override
		public String toString() {
			return length+" "+ancestor;
		}
	}

	// do unit testing of this class
	public static void main(String[] args) {
		test1();
		testCycle();
testDigraph1();
testDigraph3();
	}
	
	private static void test1() {
		System.out.println("test1");
		Digraph g = new Digraph(6);
		g.addEdge(1, 0);
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(4, 5);
		g.addEdge(5, 0);
		
testFromGraph(g,1,5,2,0);
		
	}
	
	private static void testCycle() {
		System.out.println("testCycle");
		Digraph g = new Digraph(6);
		g.addEdge(0, 1);
		g.addEdge(1,2);
		g.addEdge(2,3);
		g.addEdge(3,4);
		g.addEdge(4,5);
		g.addEdge(5,0);
		
		testFromGraph(g,1,1,0,1);
		testFromGraph(g,5,2,3,5);
	}
	
	private static void testDigraph1() {
		System.out.println("testDigraph1");
		String filename = "digraph1.txt";
		int[][] data = {{3,11,4,1}, {9,12,3,5}, {7,2,4,0}, {1,6,-1,-1}};
		testFromFile(filename,data);
	}
	
	
	private static void testDigraph3() {
		System.out.println("testDigraph3");
		String filename = "digraph3.txt";
		int[][] data = {{1,2,1,2},{1,7,-1,-1},{9,14,4,11}};
		testFromFile(filename,data);
	}
	
	private static void testFromFile(String filename, int[][] data) {
		String path = "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/1wordnet/wordnet/"+filename;
		for (int i=0; i<data.length; i++) {
			testFromFile(path,data[i][0],data[i][1],data[i][2],data[i][3]);
		}
	}
	
	private static void testFromFile(String filename, int v, int w, int expectedLength, int expectedAncestor) {
		In in = new In(filename);
	    Digraph G = new Digraph(in);
testFromGraph(G,v,w,expectedLength, expectedAncestor);
	}

	private static void testFromGraph(Digraph G, int v, int w, int expectedLength, int expectedAncestor) {
	    SAP sap = new SAP(G);
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        assert expectedLength == length;
	        assert expectedAncestor == ancestor;
	}
}
