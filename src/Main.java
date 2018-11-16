import errors.IncludeError;
import errors.NLessThanTwoError;
import errors.SplineError;
import errors.VectorXError;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {
        DataParser dp = new DataParser("C:\\Users\\toxat\\IdeaProjects\\VM2\\src\\input\\InputSquare");
        try {
            dp.readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SplineError e) {
            e.printStackTrace();
            System.exit(e.getCode());
        }

        dp.print();

        Interpolation interpolation = new Interpolation(dp);
        interpolation.calculate();
    }
}
