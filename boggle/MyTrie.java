import java.util.HashMap;

public class MyTrie<T> {
    private static final int R = 26;
    private final HashMap<String, Node> prefixNodes;
    private Node root;

    public MyTrie() {
        prefixNodes = new HashMap<>();
    }

    public void put(String key, T val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, T val, int d) {
        if (x == null) x = new Node();

        if (d == key.length()) {
            x.val = val;
            return x;
        }

        int c = key.charAt(d) - 'A';
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    public boolean keysWithPrefix(String pre) {
        Node x = prefixNodes.get(pre);
        if (x != null) return true;
        if (!pre.isEmpty()) {
            x = prefixNodes.get(pre.substring(0, pre.length() - 1));
            if (x != null) {
                return existPrefix(x, pre, pre.length() - 1);
            }
        }
        return existPrefix(root, pre, 0);
    }

    private boolean existPrefix(Node x, String pre, int d) {
        if (x == null) return false;

        for (int i = d; i < pre.length(); i++) {
            x = x.next[pre.charAt(i) - 'A'];
            if (x == null) return false;
        }

        if (x.val != null) {
            prefixNodes.put(pre, x);
            return true;
        }

        for (int c = 0; c < R; c++) {
            if (x.next[c] != null) {
                prefixNodes.put(pre, x);
                return true;
            }
        }
        return false;
    }

    private static class Node {
        private Object val;
        private final Node[] next = new Node[R];
    }
}
