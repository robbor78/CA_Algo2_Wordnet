/* Corner cases.  
 * All methods should throw a java.lang.NullPointerException if any argument is null. 
 * All methods should throw a java.lang.IndexOutOfBoundsException if any argument vertex is invalid—not between 0 and G.V() - 1.

Performance requirements.  
All methods (and the constructor) should take time at most proportional to E + V in the worst case,
where E and V are the number of edges and vertices in the digraph, respectively. 
Your data type should use space proportional to E + V. */
import edu.princeton.cs.algs4.Digraph;

public class SAP {

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null) {
			throw new java.lang.NullPointerException();
		}

		g = G;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		// * All methods should throw a java.lang.IndexOutOfBoundsException if
		// any argument vertex is invalid—not between 0 and G.V() - 1.

		return -1;
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		// * All methods should throw a java.lang.IndexOutOfBoundsException if
		// any argument vertex is invalid—not between 0 and G.V() - 1.

		return -1;
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) {
			throw new java.lang.NullPointerException();
		}
		return -1;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		return -1;
	}

	private Digraph g;

	// do unit testing of this class
	public static void main(String[] args) {

	}
}
