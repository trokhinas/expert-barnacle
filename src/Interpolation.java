import errors.ErrorPrinter;
import errors.NLessThanThreeError;
import errors.SplineError;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Interpolation {

    private final String output = "D:\\Другое\\VM2\\src\\output\\Output";
    private final int SUCCESS = 0;

    private List<Double> X;
    private List<Double> Y;
    private double A;
    private double B;
    private int N;
    private double XX;

    private List<Double> CC;

    List<Double> h;

    private List<Double> VectorA;
    private List<Double> VectorB;
    private List<Double> VectorC;
    private List<Double> VectorF;
    private List<Double> mu;
    private List<Double> nu;

    private List<Double> splineA;
    private List<Double> splineB;
    private List<Double> splineC;
    private List<Double> splineD;

    public Interpolation(List<Double> x, List<Double> y, double a, double b, double xx) {
        X = x;
        Y = y;    //вектора
        A = a;
        B = b;    //краевые условия
        N = x.size();
        XX = xx;
    }

    public Interpolation(DataParser dataParser) {
        this(dataParser.getX(),
                dataParser.getY(),
                dataParser.getA(),
                dataParser.getB(),
                dataParser.getXX());
    }

    public void startAlgorithm(){
        try {
            calculate();
        } catch (SplineError e) {

            try {ErrorPrinter.print(e);}
            catch (FileNotFoundException e1)
            {e1.printStackTrace();}

            System.exit(e.getCode());
        }
    }
    private void calculate() throws NLessThanThreeError {
        int n = N - 1;
        if (N < 3) {
            throw new NLessThanThreeError();
        }

        calculateH();   //считаем все h
        calculateMatrix();
        solveMatrix();
        calculateSplines();

        System.out.print("h: ");
        print(h);
        System.out.print("VectorA: ");
        print(VectorA);
        System.out.print("VectorC: ");
        print(VectorC);
        System.out.print("VectorB: ");
        print(VectorB);
        System.out.print("VectorF: ");
        print(VectorF);
        System.out.print("mu: ");
        print(mu);
        System.out.print("nu: ");
        print(nu);
        System.out.print("CC: ");
        print(CC);
        System.out.print("splineA: ");
        print(splineA);
        System.out.print("splineB: ");
        print(splineB);
        System.out.print("splineC: ");
        print(splineC);
        System.out.print("splineD: ");
        print(splineD);


        int i = 1;
        for( ; i < N ; i ++){
            if(XX <= X.get(i))
                break;
        }

        try {
            PrintStream ps = new PrintStream(output);
            double y = value(i);
            ps.println("Код ошибки " + SUCCESS + ".");
            ps.println("Значение второй производной в точке " + XX + ": " + y);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void calculateMatrix() {
        VectorC = new ArrayList<>(N);
        VectorA = new ArrayList<>(N - 1);
        VectorB = new ArrayList<>(N - 1);
        VectorF = new ArrayList<>(N);

        for (int i = 0; i < N; i++) {
            if (i == 0 || i == N - 1) {
                if (i == 0) {
                    VectorC.add(2d);
                    VectorB.add(1d);
                    double left = 6 / h(1);
                    double right = ((Y.get(1) - Y.get(0)) / h(1)) - A;
                    VectorF.add(left * right);
                }
                if (i == N - 1) {
                    VectorA.add(1d);
                    VectorC.add(2d);
                    double left = 6 / h(N - 2);
                    double right = B - ((Y.get(N - 1) - Y.get(N - 2)) / h(N - 2));
                    VectorF.add(left * right);
                }
            } else {
                VectorA.add(h(i));
                VectorC.add(2 * (h(i) + h(i + 1)));
                VectorB.add(h(i + 1));
                VectorF.add(F(i) * 6);
            }
        }
    }
    private void solveMatrix() {

        mu = new ArrayList<>(N - 1);
        nu = new ArrayList<>(N - 1);
        mu.add(-VectorB.get(0) / VectorC.get(0));
        nu.add(VectorF.get(0) / VectorC.get(0));
        for (int i = 1; i < N - 1; i++) {
            double Di = VectorC.get(i) + VectorA.get(i - 1) * mu.get(i - 1);
            mu.add((-VectorB.get(i) / Di));
            nu.add((VectorF.get(i) - VectorA.get(i - 1) * nu.get(i - 1)) / Di);
        }

        double[] c = new double[N];
        c[N - 1] = (VectorF.get(N - 1) - VectorA.get(N - 2) * nu.get(N - 2))
                / (VectorA.get(N - 2) * mu.get(N - 2) + VectorC.get(N - 1));
        for (int i = N - 2; i >= 0; i--) {
            c[i] = mu.get(i) * c[i + 1] + nu.get(i);
        }

        CC = new ArrayList<>();
        for (double aC : c) {
            CC.add(aC);
        }
    }
    private void calculateH() {

        h = new ArrayList<>(N - 1);

        for (int i = 1; i < N; i++) {
            h.add(X.get(i) - X.get(i - 1));
        }
    }
    private void calculateSplines(){
        calculateA();
        calculateD();
        calculateB();
        calculateC();
    }

    private void calculateC() {
        splineC = new ArrayList<>(N - 1);
        for(int i = 0 ; i < N - 1 ; i ++) {
            splineC.add(CC.get(i + 1));
        }
    }
    private void calculateB() {
        splineB = new ArrayList<>(N - 1);
        for (int i = 0 ; i < N - 1 ; i ++) {
            double first = h(i + 1) / 2 * CC.get(i + 1);
            double second = h(i + 1) * h(i + 1) / 6 * splineD.get(i);
            double third = (Y.get(i + 1) - Y.get(i)) / h(i + 1);
            splineB.add(first - second + third);
        }
    }
    private void calculateD() {
        splineD = new ArrayList<>(N - 1);
        for (int i = 0; i < N - 1; i++) {
            splineD.add((CC.get(i + 1) - CC.get(i)) / h(i + 1));
        }
    }
    private void calculateA() {
        splineA = new ArrayList<>(N - 1);
        for (int i = 0; i < N - 1; i++) {
            splineA.add(Y.get(i + 1));
        }
    }

    private double F(int i){
        if(i < 1 || i > N - 1)
            throw new IllegalArgumentException();

        double left = Y.get(i + 1) - Y.get(i);
        left /= h(i + 1);

        double right = Y.get(i) - Y.get(i - 1);
        right /= h(i);

        return left - right;
    }
    private double h(int index) {
        if (index >= 1)
            return h.get(index - 1);
        return 0;
    }
    private double value(int i){
        double y = splineC.get(i - 1)  +
                splineD.get(i - 1) * (XX - X.get(i));
        return y;
    }

    private void print(List a) {
        for (var b : a
                ) {
            System.out.print(b + " ");
        }
        System.out.print('\n');
    }
}
