import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;

public class SAP {
	private Digraph graph;
	private int ShortestLength;
	private int Ancestor;
	public SAP(Digraph G) {
		    if(G == null)
		    	throw new IllegalArgumentException();
			this.graph = G;
			ShortestLength = -1;
			Ancestor = -1;
	}
	public int length(int v, int w){
		if(v >= graph.V() || v < 0 || w >= graph.V() || w < 0)
			throw new IllegalArgumentException();
		Set<Integer> a = new HashSet<Integer>();
		Set<Integer> b = new HashSet<Integer>();
		a.add(v);
		b.add(w);
		find(a,b);
		return ShortestLength;
	}
	public int ancestor(int v, int w) {
		if(v >= graph.V() || v < 0 || w >= graph.V() || w < 0)
			throw new IllegalArgumentException();
		Set<Integer> a = new HashSet<Integer>();
		a.add(v);
		Set<Integer> b = new HashSet<Integer>();
		b.add(w);
		find(a,b);
		return Ancestor;
	}
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if(v == null || w == null )
			throw new IllegalArgumentException();
		for(Integer i : v)
			if(i >= graph.V() || i < 0)
				throw new IllegalArgumentException();
		for(Integer i : w)
			if(i >= graph.V() || i < 0)
				throw new IllegalArgumentException();
		find(v,w);
		return ShortestLength;
	}
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if(v == null || w == null)
			throw new IllegalArgumentException();
		for(Integer i : v)
			if(i >= graph.V() || i < 0)
				throw new IllegalArgumentException();
		for(Integer i : w)
			if(i >= graph.V() || i < 0)
				throw new IllegalArgumentException();
		find(v,w);
		return Ancestor;
	}
	private void printMap(Map<Integer,Integer> m,int n) {
		System.out.println(n);
		for(Integer i: m.keySet())
			System.out.println(i + "-->" + m.get(i));
		System.out.println("-----");
	}
	private void find(Iterable<Integer> A,Iterable<Integer> B) {
		Map<Integer,Map<Integer,Integer>> a = new TreeMap<Integer,Map<Integer,Integer>>();
		Map<Integer,Map<Integer,Integer>> bMap = new TreeMap<Integer,Map<Integer,Integer>>();
		Set<Integer> visitedA = new HashSet<Integer>();
		Set<Integer> visitedB = new HashSet<Integer>();
		for(Integer i : A) { 
			a.put(i,new TreeMap<Integer,Integer>());
			a.get(i).put(i, 0);
		}
		for(Integer i : B) { 
			bMap.put(i,new TreeMap<Integer,Integer>());
			bMap.get(i).put(i, 0);
		}
		int counter = 0 ;
		boolean Fa = false , Fb = false ;
		while(true) {
			counter++;
			boolean flagA = false , flagB = false ;
			for(Integer i : a.keySet()) {
				for(Integer j: bMap.keySet()) {
					for(Integer k: bMap.get(j).keySet())
					if(a.get(i).containsKey(k)) {
						System.out.println("found");
						this.Ancestor = k;
						this.ShortestLength = a.get(i).get(k) + bMap.get(j).get(k) ;
						return;
					}
				}
			}
			if(!Fa) {
			for(Integer i : a.keySet()) {
				Set<Integer> s = new HashSet<Integer>(a.get(i).keySet());
				for (Integer j : s) {
					for(Integer k : graph.adj(j)) {
						if(!visitedA.contains(k)) {
							visitedA.add(k);
							a.get(i).put(k, counter);
							flagA = true;
						}
					}
				}
			}
			if(!flagA) 
				Fa = true;
			}
			if(!Fb) {
				for(Integer i : bMap.keySet()) {
					Set<Integer> s = new HashSet<Integer>(bMap.get(i).keySet());
					for (Integer j : s) {
						for(Integer k : graph.adj(j)) {
							if(!visitedB.contains(k)) {
								visitedB.add(k);
								bMap.get(i).put(k, counter);
								flagB = true;
							}
						}
					}
				}
				if(!flagB) 
					Fb = true;
			}
			if(Fb && Fa) {
				return;
			}
			for(Integer i: a.keySet()) {
				printMap(a.get(i),i);
				System.out.println("*****");
			}
			for(Integer i: bMap.keySet()) {
				printMap(bMap.get(i),i);
				System.out.println("*****");
			}
		}
	}
	public static void main(String[] args) {
		Digraph G = new Digraph(13);
		G.addEdge(6, 3);
		G.addEdge(7, 3);
		G.addEdge(3, 1);
		G.addEdge(4, 1);
		G.addEdge(5, 1);
		G.addEdge(1, 0);
		G.addEdge(2, 0);
		G.addEdge(10, 9);
		G.addEdge(11, 9);
		G.addEdge(9, 5);
		G.addEdge(8, 5);
	    SAP sap = new SAP(G);
	    Set<Integer> s1 = new HashSet<Integer>();
	    s1.add(6);
	    s1.add(4);
	    s1.add(10);
	    Set<Integer> s2 = new HashSet<Integer>();
	    //s2.add(7);
	    s2.add(8);
	    s2.add(2);
	    int a = sap.ancestor(s1, s2);
	    int l = sap.length(s1,s2);
	    System.out.println(a+"  "+l);
	}
}
