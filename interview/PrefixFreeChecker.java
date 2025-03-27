public class PrefixFreeChecker {
    private static class Node{
        boolean isEnd;
        Node[] next = new Node[2];
    }

    public boolean checkPrefixFree(String[] input){
        Node root = new Node();

        for(String s : input){
            Node node = root;
            for(int i = 0; i < s.length(); i++){
                int bit = s.charAt(i) - '0';

                if(node.isEnd)  return false;

                if(node.next[bit] == null){
                    node.next[bit] = new Node();
                }

                if(node.isEnd || node.next[0] != null || node.next[1] != null)  return false;

                node = node.next[bit];
            }
            node.isEnd = true;
        }

        return true;
    }
}
