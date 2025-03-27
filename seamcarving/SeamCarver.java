import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private static final double BORDER_ENERGY = 1000.0;
    private double[][] energy;
    private Picture picture;
    private int width;
    private int height;
    private Color[][] rgb;

    private void calcEnergy() {
        for (int j = 1; j < width - 1; j++) {
            for (int i = 1; i < height - 1; i++) {
                double deltaHsquared = Math.pow(rgb[j + 1][i].getRed() - rgb[j - 1][i].getRed(), 2)
                        + Math.pow(rgb[j + 1][i].getBlue() - rgb[j - 1][i].getBlue(), 2)
                        + Math.pow(rgb[j + 1][i].getGreen() - rgb[j - 1][i].getGreen(), 2);

                double deltaVsquared = Math.pow(rgb[j][i + 1].getRed() - rgb[j][i - 1].getRed(), 2)
                        + Math.pow(rgb[j][i + 1].getBlue() - rgb[j][i - 1].getBlue(), 2)
                        + Math.pow(rgb[j][i + 1].getGreen() - rgb[j][i - 1].getGreen(), 2);

                energy[j][i] = Math.sqrt(deltaHsquared + deltaVsquared);
            }
        }
    }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("constructor argument is null");
        this.picture = picture;
        width = picture.width();
        height = picture.height();

        rgb = new Color[width][height];
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                rgb[j][i] = picture.get(j, i);
            }
        }

        energy = new double[width][height];
        for (int i = 0; i < height; i++) {
            energy[0][i] = BORDER_ENERGY;
            energy[width - 1][i] = BORDER_ENERGY;
        }

        for (int j = 0; j < width; j++) {
            energy[j][0] = BORDER_ENERGY;
            energy[j][height - 1] = BORDER_ENERGY;
        }

        calcEnergy();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture); // Return a defensive copy
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException("invalid coordinates");
        }
        return energy[x][y];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int w = width();
        int h = height();
        if (h <= 1) return new int[w];

        int[] results = new int[w];
        double[][] dp = new double[w][h];
        int[][] trace = new int[w][h];

        for (int i = 0; i < h; i++) {
            dp[0][i] = BORDER_ENERGY;
        }

        for (int j = 1; j < w; j++) {
            for (int i = 0; i < h; i++) {
                double temp = dp[j - 1][i];
                if (i > 0) temp = Math.min(temp, dp[j - 1][i - 1]);
                if (i < h - 1) temp = Math.min(temp, dp[j - 1][i + 1]);

                dp[j][i] = energy[j][i] + temp;

                if (temp == dp[j - 1][i]) trace[j][i] = i;
                else if (i > 0 && temp == dp[j - 1][i - 1]) trace[j][i] = i - 1;
                else trace[j][i] = i + 1;
            }
        }

        double minVal = Double.POSITIVE_INFINITY;
        int i1 = 0;
        for (int i = 0; i < h; i++) {
            if (dp[w - 1][i] < minVal) {
                minVal = dp[w - 1][i];
                i1 = i;
            }
        }

        int idx = w - 1;
        while (idx >= 0) {
            results[idx] = i1;
            i1 = trace[idx--][i1];
        }
        return results;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int w = width();
        int h = height();
        if (w <= 1) return new int[h];

        int[] results = new int[h];
        double[][] dp = new double[w][h];
        int[][] trace = new int[w][h];

        for(int j = 0; j < w; j++){
            dp[j][0] = BORDER_ENERGY;
        }

        for(int i = 1; i < h; i++){
            for(int j = 0; j < w; j++){
                double tmp = dp[j][i - 1];
                trace[j][i] = j;
                if(j > 0 && dp[j - 1][i - 1] < tmp){
                    tmp = dp[j - 1][i - 1];
                    trace[j][i] = j - 1;
                }

                if(j < width - 1 && dp[j + 1][i - 1] < tmp){
                    tmp = dp[j + 1][i - 1];
                    trace[j][i] = j + 1;
                }
                dp[j][i] = energy[j][i] + tmp;
            }
        }

        double minVal = Double.MAX_VALUE;
        int pos = 0;
        for(int j = 0; j < w; j++){
            if(dp[j][h - 1] < minVal){
                minVal = dp[j][h - 1];
                pos = j;
            }
        }

        int idx = h - 1;
        while(idx >= 0){
            results[idx] = pos;
            pos = trace[pos][idx--];
        }
        return results;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width || height <= 1) {
            throw new IllegalArgumentException("Invalid horizontal seam");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height) {
                throw new IllegalArgumentException("Invalid horizontal seam value");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Invalid horizontal seam - not connected");
            }
        }

        Picture tmpPicture = new Picture(width, height - 1);
        Color tmpRgb[][] = new Color[width][height - 1];
        for(int j = 0; j < width; j++){
            for(int i = 0; i < seam[j]; i++){
                tmpPicture.set(j, i, rgb[j][i]);
                tmpRgb[j][i] = rgb[j][i];
            }

            for(int i = seam[j]; i < height - 1; i++){
                tmpPicture.set(j, i, rgb[j][i + 1]);
                tmpRgb[j][i] = rgb[j][i + 1];
            }
        }
        this.picture = tmpPicture;
        this.height = picture.height();
        this.width = picture.width();
        this.rgb = tmpRgb;
        this.energy = new double[width][height];
        initializeEnergy();
        calcEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height || width <= 1) {
            throw new IllegalArgumentException("Invalid vertical seam");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width) {
                throw new IllegalArgumentException("Invalid vertical seam value");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Invalid vertical seam - not connected");
            }
        }

        Picture tmpPicture = new Picture(width - 1, height);
        Color[][] tmpRgb = new Color[width - 1][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < seam[i]; j++) {
                tmpPicture.set(j, i, rgb[j][i]);
                tmpRgb[j][i] = rgb[j][i];
            }
            for(int j = seam[i]; j < width - 1; j++){
                tmpPicture.set(j, i, rgb[j + 1][i]);
                tmpRgb[j][i] = rgb[j + 1][i];
            }
        }

        this.picture = tmpPicture;
        this.width = picture.width();
        this.height = picture.height();
        this.rgb = tmpRgb;
        this.energy = new double[width][height];
        initializeEnergy();
        calcEnergy();
    }

    private void initializeEnergy() {
        for (int i = 0; i < height; i++) {
            energy[0][i] = BORDER_ENERGY;
            energy[width - 1][i] = BORDER_ENERGY;
        }

        for (int j = 0; j < width; j++) {
            energy[j][0] = BORDER_ENERGY;
            energy[j][height - 1] = BORDER_ENERGY;
        }
    }
}