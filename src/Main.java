import errors.*;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {

        DataParser dp = new DataParser("D:\\Другое\\VM2\\src\\input\\NLessThanThreeError");
        dp.readFile();
        dp.print();

        Interpolation interpolation = new Interpolation(dp);
        interpolation.startAlgorithm();
    }
}
