import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private int[][] rgb2DArray;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("calling SeamCarver method with null argument");
        int W = picture.width();
        int H = picture.height();
        rgb2DArray = new int[W][H];
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                rgb2DArray[i][j] = picture.getRGB(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                 picture.setRGB(i, j, rgb2DArray[i][j]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return rgb2DArray.length;
    }

    // height of current picture
    public int height() {
        return rgb2DArray[0].length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkIndices(x, y);
        if (x == 0 || x == width()-1 || y == 0 || y == height()-1) return 1000.0;
        int uC = rgb2DArray[x][y - 1];
        int dC = rgb2DArray[x][y + 1];
        int lC = rgb2DArray[x - 1][y];
        int rC = rgb2DArray[x + 1][y];
        double dxSquared = singleSquaredEnergy(lC, rC);
        double dySquared = singleSquaredEnergy(uC, dC);
        return Math.sqrt(dxSquared + dySquared);
    }

    private double singleSquaredEnergy(int color1, int color2) {
        int dRed = ((color1 >> 16) & 0xFF) - ((color2 >> 16) & 0xFF);
        int dGreen = ((color1 >>  8) & 0xFF) - ((color2 >>  8) & 0xFF);
        int dBlue = ((color1 >>  0) & 0xFF) - ((color2 >>  0) & 0xFF);
        return dRed * dRed + dGreen *dGreen + dBlue * dBlue;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposeImage();
        int[] seam = findVerticalSeam();
        transposeImage();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int W = width();
        int H = height();
        double[][] E = new double[W][H];
        double[][] distTo = new double[W][H];
        int[][] pixelTo = new int[W][H];

        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                E[i][j] = energy(i, j);
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i < W; i++) {
            distTo[i][0] = E[i][0];
        }

        for (int j = 0; j < H - 1; j++) {
            for (int i = 0; i < W; i++) {
                if (distTo[i][j + 1] > distTo[i][j] + E[i][j + 1]) {
                    distTo[i][j + 1] = distTo[i][j] + E[i][j + 1];
                    pixelTo[i][j + 1] = i;
                }

                if (i > 0) {
                    if (distTo[i - 1][j + 1] > distTo[i][j] + E[i - 1][j + 1]) {
                        distTo[i - 1][j + 1] = distTo[i][j] + E[i - 1][j + 1];
                        pixelTo[i - 1][j + 1] = i;
                    }
                }

                if (i < width()-1) {
                    if (distTo[i + 1][j + 1] > distTo[i][j] + E[i + 1][j + 1]) {
                        distTo[i + 1][j + 1] = distTo[i][j] + E[i + 1][j + 1];
                        pixelTo[i + 1][j + 1] = i;
                    }
                }
            }
        }

        int minId = -1;
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < W; i++) {
            if (distTo[i][H - 1] < minDist) {
                minId = i;
                minDist = distTo[i][H - 1];
            }
        }

        int[] seam = new int[H];
        for (int j = H-1; j >= 0; j--) {
            seam[j] = minId;
            minId = pixelTo[minId][j];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transposeImage();
        removeVerticalSeam(seam);
        transposeImage();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        int W = width();
        int H = height();
        if (seam == null) throw new IllegalArgumentException("calling removeHorizontalSeam method with null argument");
        if (!isValidSeam(H, W, seam)) throw new IllegalArgumentException("calling removeHorizontalSeam method with a wrong array");
        if (W <= 1) throw new IllegalArgumentException("calling removeHorizontalSeam method with a wrong picture");
        int[][] removed = new int[W - 1][H];
        for (int i = 0; i < W - 1; i++) {
            for (int j = 0; j < H; j++) {
                if (i < seam[j]) removed[i][j] = rgb2DArray[i][j];
                else removed[i][j] = rgb2DArray[i + 1][j];
            }
        }
        rgb2DArray = removed;
    }

    private void transposeImage() {
        int W = width();
        int H = height();
        int[][] transposed = new int[H][W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                transposed[i][j] = rgb2DArray[j][i];
            }
        }
        rgb2DArray = transposed;
    }

    /**
     * check whether the input x, y are outside its prescribed range
     */
    private void checkIndices(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IllegalArgumentException();
    }

    /**
     * only work for vertical seam
     * check if the array is a valid seam
     * 1.wrong length 2.an entry is outside its prescribed range 3.two adjacent entries differ by more than one
     * @return if it is valid
     */
    private boolean isValidSeam(int length, int range, int[] seam) {
        if (seam.length != length) return false;
        for (int i: seam) {
            if (i < 0 || i >= range) return false;
        }
        for (int i = 0; i < length-1; i++) {
            if (seam[i] > seam[i+1] + 1 || seam[i] < seam[i+1] - 1) return false;
        }
        return true;
    }
    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture("6x5.png");
        SeamCarver sc = new SeamCarver(picture);
        int[] a = sc.findVerticalSeam();
        sc.removeVerticalSeam(a);
        /**
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }
         */
    }

}