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
	private SAP sap;
	public WordNet(String synsets, String hypernyms) {
		if(synsets == null || hypernyms == null)
			throw new IllegalArgumentException();
		MySynset = new HashMap<Integer,String>();
		wordGraph = new Digraph(MySynset.size());
		SynsetParser(synsets);
		HypernymsParser(hypernyms);
		if(!isDAG())
			throw new IllegalArgumentException();
		sap = new SAP(wordGraph);
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
		int a=0,b=0;
		boolean fa=false,fb=false;
		for(Integer i : MySynset.keySet()) {
			if(MySynset.get(i).equals(nounA)) {
				a = i;
				fa = true;
			}
			else if(MySynset.get(i).equals(nounB)) {
				b = i;
				fb = true;
			}
			if(fb && fa )
				break;
		}
		return MySynset.get(sap.ancestor(a, b));
	}
	
	private boolean isDAG() {
		int count = 0 ;
		Set<Integer> s = MySynset.keySet();
		for(Integer i : s) {
			if(wordGraph.outdegree(i) == 0)
				{
				count++;
				}
		}
		if(count == 0 || count > 1)
			return false;
		return true;
	}
}
