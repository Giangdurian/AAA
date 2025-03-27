import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ThreeWayQuickSort {
    private static final int CUTOFF = 10;

    private static void insertionSort(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    private static boolean less(String v, String w, int d) {
        return charAt(v, d) < charAt(w, d);
    }

    private static int charAt(String s, int d){
        if(d < s.length()){
            return s.charAt(d);
        }
        return -1;
    }

    private static void sort(String[] a){
        sort(a, 0, a.length - 1, 0);
    }

    public static void sort(String[] a, int lo, int hi, int d){





        if(hi <= lo)    return;
        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;

        while(i <= gt){
            int t = charAt(a[i], d);
            if(t < v)   exch(a, lt++, i++);
            else if(t > v)  exch(a, gt--, i);
            else    i++;
        }

        sort(a, lo, lt - 1, d);
        if(v >= 0)  sort(a, lt, gt, d + 1);
        sort(a, gt + 1, hi, d);
    }

    private static void exch(String[] a, int i, int j) {
        String tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args){
        try {
            File file = new File("input5.txt");
            Scanner scanner = new Scanner(file);
            StringBuilder sb = new StringBuilder();

            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append(" ");
            }

            String str = sb.toString().trim();
            str = str.replaceAll("[!?,]", "");
            String[] words = str.split("\\s+");
            String[] wordsCopy = words.clone();
            System.out.println(words.length);

            long startTime1 = System.nanoTime();
            sort(words);
            long endTime1 = System.nanoTime();
            long duration1 = (endTime1 - startTime1) / 1000000;


            long startTime2 = System.nanoTime();
            Arrays.sort(wordsCopy);
            long endTime2 = System.nanoTime();
            long duration2 = (endTime2 - startTime2) / 1000000;

            System.out.println("\nRunning time for 3-way quick sort: " + duration1 + " ms");
            System.out.println("\nRunning time for Java sort: " + duration2 + " ms");

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }
}