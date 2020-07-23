import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
	private Map<Integer,String> MySynset;
	private Map<String,Set<Integer>> nounToIds;
	private Digraph wordGraph;
	private SAP sap;
	public WordNet(String synsets, String hypernyms) {
		if(synsets == null || hypernyms == null)
			throw new IllegalArgumentException();
		MySynset = new HashMap<Integer,String>();
		nounToIds = new HashMap<String,Set<Integer>>();
		SynsetParser(synsets);
		wordGraph = new Digraph(MySynset.size());
		HypernymsParser(hypernyms);
		if(!isDAG())
			throw new IllegalArgumentException();
		sap = new SAP(wordGraph);
	}
	private void SynsetParser (String synset) {
		In input  = new In(synset);
		while(input.hasNextLine()) {
			String[] s = input.readLine().split(",");
			Integer id = Integer.valueOf(s[0]);
            String n = s[1];
			MySynset.put(id,n);
			String[] nouns = n.split(" ");
            for (String noun : nouns) {
                Set<Integer> ids = nounToIds.get(noun);
                if (null == ids) {
                    ids = new HashSet<>();
                }
                ids.add(id);
                nounToIds.put(noun, ids);
		}
	}
 }
	private void HypernymsParser (String hypernym) {
		In input = new In(hypernym);
		while(input.hasNextLine()) {
			String[] s = input.readLine().split(",");
			int id  = Integer.valueOf(s[0]);
			for(int i=1 ; i < s.length ; i++) {
				Integer k = Integer.valueOf(s[i]);
				wordGraph.addEdge(id,k);
			}
		}
	}
	public Iterable<String> nouns(){
		return nounToIds.keySet();
	}
	public boolean isNoun(String word) {
		if(word == null || "".equals(word))
			throw new IllegalArgumentException();
		return nounToIds.containsKey(word);
	}
	public int distance(String nounA, String nounB) {
		if(nounA == null || nounB == null)
			throw new IllegalArgumentException();
		if(!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		Set<Integer> a = nounToIds.get(nounA);
		Set<Integer> b = nounToIds.get(nounB);
		return sap.length(a, b);
	}
	public String sap(String nounA, String nounB) {
		if(nounA == null || nounB == null)
			throw new IllegalArgumentException();
		if(!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		Set<Integer> a = nounToIds.get(nounA);
		Set<Integer> b = nounToIds.get(nounB);
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
	public static void main(String[] args) {
		//only for testing
	}
}
