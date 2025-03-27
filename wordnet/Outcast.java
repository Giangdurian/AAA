public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxTotalDistance = Integer.MIN_VALUE;
        String outcast = null;

        for (String nounA : nouns) {
            int totalDistance = 0;
            for (String nounB : nouns) {
                totalDistance += wordNet.distance(nounA, nounB);
            }
            if (totalDistance >= maxTotalDistance) {
                maxTotalDistance = totalDistance;
                outcast = nounA;
            }
        }
        return outcast;
    }

    public static void main(String[] args) {

    }
}