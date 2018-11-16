import errors.ErrorPrinter;
import errors.IncludeError;
import errors.VectorXError;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataParser {

    private final String file;

    private List<Double> X;
    private List<Double> Y;
    private double A;
    private double B;
    private int N;

    private double XX;

    public DataParser(String filename) {
        file = filename;
    }

    public void readFile(){

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file)).useDelimiter("\n");
            parse(scanner);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(!Validator.isOrdered(X)) try {
            throw new VectorXError();
        } catch (VectorXError vectorXError) {
            try {
                ErrorPrinter.print(vectorXError);
                System.exit(vectorXError.getCode());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (!Validator.isIncluded(X, XX)) try {
            throw new IncludeError();
        } catch (IncludeError includeError) {
            try {
                ErrorPrinter.print(includeError);
                System.exit(includeError.getCode());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(N != X.size() || N != Y.size()) {
            System.out.println("Количество точек, не совпадает с заявленным N ");
            System.exit(1);
        }
    }


    private void parse(Scanner scanner) {
        while (scanner.hasNext()){
            String source = scanner.next();
            if(source.contains("X:")){
                parseX(source);
            }
            if(source.contains("Y:")) {
                parseY(source);
            }
            if(source.contains("A:")){
                source = source.substring(source.indexOf(":") + 1);
                A = Double.parseDouble(source);
            }
            if(source.contains("B:")){
                source = source.substring(source.indexOf(":") + 1);
                B = Double.parseDouble(source);
            }
            if(source.contains("N:")){
                source = source.substring(source.indexOf(":") + 1);
                N = (int) Double.parseDouble(source);
            }
            if(source.contains("xx:")){
                source = source.substring(source.indexOf(":") + 1);
                XX = Double.parseDouble(source);
            }
        }
    }
    private void parseY(String source) {
        source = source.substring(source.indexOf(":") + 1);
        Scanner sourceScanner = new Scanner(source).useDelimiter(" ");
        Y = new ArrayList<>();
        while (sourceScanner.hasNext()) {
            Y.add(Double.parseDouble(sourceScanner.next()));
        }
    }

    private void parseX(String source){
        source = source.substring(source.indexOf(":") + 1);
        Scanner sourceScanner = new Scanner(source).useDelimiter(" ");
        X = new ArrayList<>();
        while (sourceScanner.hasNext()) {
            X.add(Double.parseDouble(sourceScanner.next()));
        }
    }


    public void print() {
        System.out.print("X: ");
        for (var a:X) {
            System.out.print(a + " ");
        }
        System.out.print("\n" + "Y: " );
        for (var a:Y) {
            System.out.print(a + " ");
        }
        System.out.print("\n" + "A: " + A + "\n");
        System.out.println("B: " + B);
        System.out.println("N: " + N);
        System.out.println("XX: " + XX);
    }

    public List<Double> getX() {
        return X;
    }
    public List<Double> getY() {
        return Y;
    }
    public double getA() {
        return A;
    }
    public double getB() {
        return B;
    }
    public int getN() {
        return N;
    }
    public double getXX() {
        return XX;
    }
}
