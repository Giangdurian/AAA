import java.util.HashSet;
import java.util.Set;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private static final int R = 26; // Số chữ cái A-Z
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},          {0, 1},
            {1, -1},  {1, 0}, {1, 1}
    };

    private Node root;
    private BoggleBoard board;
    private boolean[][] visited;
    private Set<String> validWords;

    private static class Node {
        String word;
        Node[] next = new Node[R];
    }

    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String word : dictionary) {
            if (word.length() >= 3) {
                put(word);
            }
        }
    }

    private void put(String word) {
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'A';
            if (current.next[c] == null) {
                current.next[c] = new Node();
            }
            current = current.next[c];
        }
        current.word = word;
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        validWords = new HashSet<>();
        visited = new boolean[board.rows()][board.cols()];

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(i, j, root);
            }
        }

        return validWords;
    }

    private void dfs(int row, int col, Node node) {
        // Kiểm tra giới hạn và ô đã thăm
        if (row < 0 || row >= board.rows() || col < 0 || col >= board.cols() || visited[row][col]) {
            return;
        }

        char letter = board.getLetter(row, col);
        int c = letter - 'A';
        Node nextNode = node.next[c];

        // Xử lý trường hợp 'Q' thành 'QU'
        if (letter == 'Q') {
            nextNode = (nextNode != null) ? nextNode.next['U' - 'A'] : null;
        }

        if (nextNode == null) {
            return;
        }

        visited[row][col] = true;

        // Nếu tìm thấy từ hợp lệ
        if (nextNode.word != null) {
            validWords.add(nextNode.word);
        }

        // Duyệt 8 hướng lân cận
        for (int[] dir : DIRECTIONS) {
            dfs(row + dir[0], col + dir[1], nextNode);
        }

        visited[row][col] = false;
    }

    public int scoreOf(String word) {
        if (!contains(word)) return 0;

        int length = word.length();
        if (length <= 2) return 0;
        if (length <= 4) return 1;
        if (length == 5) return 2;
        if (length == 6) return 3;
        if (length == 7) return 5;
        return 11;
    }

    private boolean contains(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'A';
            if (node.next[c] == null) return false;
            node = node.next[c];
        }
        return node.word != null;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}