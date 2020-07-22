
public class Outcast {
	private WordNet w ;
	public Outcast(WordNet wordnet) {
		if(wordnet == null)
			throw new IllegalArgumentException();
		this.w = wordnet;
	}
	public String outcast(String[] nouns) {
		if(nouns == null || nouns.length == 0)
			throw new IllegalArgumentException();
		int[] distances = new int[nouns.length];
		for(int i=0 ; i<nouns.length ; i++) {
			if(nouns[i] == null || !w.isNoun(nouns[i]))
				throw new IllegalArgumentException();
			else distances[i] = 0;
		}
		for(int i=0 ; i<nouns.length ; i++) {
			for(int j=i+1; j<nouns.length ; j++) {
				int k = w.distance(nouns[i], nouns[j]);
				distances[i] += k ;
				distances[j] += k ;
			}
		}
		int index = 0 ;
		int max = Integer.MIN_VALUE;
		for(int i=0 ; i<nouns.length ; i++)
			if(distances[i] > max) {
				max = distances[i];
				index = i;
			}
		return nouns[index];
	}
}
