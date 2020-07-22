import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
	private Map<Integer,String> MySynset;
	private Digraph wordGraph;
	private int root;
	public WordNet(String synsets, String hypernyms) {
		if(synsets == null || hypernyms == null)
			throw new IllegalArgumentException();
		MySynset = new HashMap<Integer,String>();
		wordGraph = new Digraph(MySynset.size());
		SynsetParser(synsets);
		HypernymsParser(hypernyms);
		if(!isDAG())
			throw new IllegalArgumentException();
	}
	private void SynsetParser (String synset) {
		In input  = new In(synset);
		while(input.hasNextLine()) {
			String[] s = input.readLine().split(",");
			MySynset.put(Integer.parseInt(s[0]),s[1]);
		}
	}
	private void HypernymsParser (String hypernym) {
		In input = new In(hypernym);
		while(input.hasNextLine()) {
			String[] s = input.readLine().split(",");
			int id  = Integer.parseInt(s[0]);
			for(int i=1 ; i < s.length ; i++) {
				wordGraph.addEdge(id,i);
			}
		}
	}
	public Iterable<String> nouns(){
		return MySynset.values();
	}
	public boolean isNoun(String word) {
		if(word == null)
			throw new IllegalArgumentException();
		return MySynset.containsValue(word);
	}
	public int distance(String nounA, String nounB) {
		if(nounA == null || nounB == null)
			throw new IllegalArgumentException();
		if(!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		Set<Integer> s = MySynset.keySet();
		Map<Integer,Integer> distances = new HashMap<Integer,Integer>();
		int index = 0;
		for(Integer i :  s)
			if(MySynset.get(i) == nounA)
				index = i;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(index);
		s.clear();
		distances.put(index, 0);
		while(!q.isEmpty()){
			int v = q.poll();
			if(!s.contains(v)) {
				s.add(v);
				if(MySynset.get(v).equals(nounB))
					return distances.get(v);
				for(Integer i: wordGraph.adj(v)) {
					q.add(i);
					distances.put(i,distances.get(v)+1);
				}
			}
		}
		return 0;
	}
	public String sap(String nounA, String nounB) {
		if(nounA == null || nounB == null)
			throw new IllegalArgumentException();
		if(!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		return null;
	}
	
	private boolean isDAG() {
		int count = 0 ;
		Set<Integer> s = MySynset.keySet();
		for(Integer i : s) {
			if(wordGraph.outdegree(i) == 0)
				{
				root = i ;
				count++;
				}
		}
		if(count == 0 || count > 1)
			return false;
		return true;
	}
	private static void print(Map<Integer,Integer> m,int v) {
		System.out.println(v);
	    for (Integer i: m.keySet())
	    	System.out.println(i + "-->" + m.get(i));
	    System.out.println("----");
	}
		public static void main(String[] args) {
		Digraph g = new Digraph(13);
		g.addEdge(0, 1);
		g.addEdge(0, 5);
		g.addEdge(5, 4);
		g.addEdge(4, 3);
		g.addEdge(4, 2);
		g.addEdge(3, 2);
		g.addEdge(2, 0);
		g.addEdge(6, 0);
		g.addEdge(6, 4);
		g.addEdge(6, 9);
		g.addEdge(6, 8);
		g.addEdge(8, 6);
		g.addEdge(7, 6);
		g.addEdge(7, 9);
		g.addEdge(9, 10);
		g.addEdge(9, 11);
		g.addEdge(10, 12);
		g.addEdge(12, 9);
		g.addEdge(11, 12);
	}
}
