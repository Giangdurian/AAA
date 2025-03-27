import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuffixTree {
    private static class Node {
        int start, end;
        Map<Character, Node> children = new HashMap<>();
        Node suffixLink; // Dùng cho thuật toán Ukkonen
        int suffixIndex = -1; // Chỉ số hậu tố (cho nút lá)

        public Node(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private Node root = new Node(-1, -1);
    private String text;

    public SuffixTree(String text) {
        this.text = text;
        buildSuffixTree();
    }

    private void buildSuffixTree() {
        int n = text.length();
        for (int i = 0; i < n; i++) {
            insertSuffix(i);
        }
    }

    private void insertSuffix(int suffixStart) {
        Node current = root;
        int i = suffixStart;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (!current.children.containsKey(c)) {
                Node leaf = new Node(i, text.length());
                leaf.suffixIndex = suffixStart;
                current.children.put(c, leaf);
                break;
            }
            Node next = current.children.get(c);
            int j = next.start;
            while (j < next.end && i < text.length() && text.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j < next.end) {
                Node split = new Node(next.start, j);
                next.start = j;
                split.children.put(text.charAt(j), next);
                current.children.put(text.charAt(split.start), split);
                current = split;
            } else {
                current = next;
            }
        }
    }

    public boolean search(String pattern) {
        Node current = root;
        int i = 0;
        while (i < pattern.length()) {
            char c = pattern.charAt(i);
            if (!current.children.containsKey(c)) {
                return false;
            }
            Node next = current.children.get(c);
            int j = next.start;
            while (j < next.end && i < pattern.length() && text.charAt(j) == pattern.charAt(i)) {
                j++;
                i++;
            }
            if (i == pattern.length()) {
                return true;
            }
            if (j == next.end) {
                current = next;
            } else {
                return false;
            }
        }
        return false;
    }

    public List<Integer> findAllOccurrences(String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        Node node = findPatternNode(pattern);
        if (node != null) {
            collectLeafIndices(node, occurrences);
        }
        return occurrences;
    }

    private Node findPatternNode(String pattern) {
        Node current = root;
        int i = 0;
        while (i < pattern.length()) {
            char c = pattern.charAt(i);
            if (!current.children.containsKey(c)) {
                return null;
            }
            Node next = current.children.get(c);
            int j = next.start;
            while (j < next.end && i < pattern.length() && text.charAt(j) == pattern.charAt(i)) {
                j++;
                i++;
            }
            if (i == pattern.length()) {
                return next;
            }
            if (j == next.end) {
                current = next;
            } else {
                return null;
            }
        }
        return null;
    }

    private void collectLeafIndices(Node node, List<Integer> indices) {
        if (node.suffixIndex != -1) {
            indices.add(node.suffixIndex);
        }
        for (Node child : node.children.values()) {
            collectLeafIndices(child, indices);
        }
    }
}