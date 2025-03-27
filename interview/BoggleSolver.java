import java.util.HashSet;

public class BoggleSolver {
    private static final int SIZE = 4;
    private static final int[][] DIRS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    private TrieNode root;

    private static class TrieNode{
        boolean isWord;
        TrieNode[] next = new TrieNode[26];
    }

    private void put(String s){
        TrieNode cur = root;
        for(int i = 0; i < s.length(); i++){
            int c = s.charAt(i) - 'A';

            if(cur.next[c] == null){
                cur.next[c] = new TrieNode();
            }
            cur = cur.next[c];
        }

        cur.isWord = true;
    }

    public BoggleSolver(String[] dictionary){
        for(String word : dictionary){
            put(word);
        }
    }

    public HashSet<String> findAllWords(char[][] board) {
        HashSet<String> result = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boolean[][] visited = new boolean[SIZE][SIZE];
                dfs(root, board, i, j, visited, "", result);
            }
        }
        return result;
    }

    private void dfs(TrieNode node, char[][] board, int i, int j, boolean[][] visited, String current, HashSet<String> res){
        if(i < 0 || i >= SIZE || j < 0 || j >= SIZE || visited[i][j])   return;

        char c = board[i][j];
        int idx = c - 'A';
        if(node.next[c] == null){
            return;
        }

        visited[i][j] = true;
        node = node.next[idx];
        String newWord = current + c;

        if(node.isWord && newWord.length() >= 3){
            res.add(newWord);
        }

        for(int[] dir : DIRS){
            int i1 = i + dir[0], j1 = j + dir[1];
            dfs(node, board, i1, j1, visited, newWord, res);
        }

        visited[i][j] = false;
    }
}
