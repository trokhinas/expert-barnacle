package errors;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintStream;

public class ErrorPrinter {
    private static final String filename = "D:\\Другое\\VM2\\src\\output\\Output";

    public static void print(SplineError error) throws FileNotFoundException {
        PrintStream printer = new PrintStream(filename);

        printer.println("Код ошибки: " + error.getCode());
        printer.println(error.getMessage());
        System.out.println("Код ошибки: " + error.getCode());
        System.out.println(error.getMessage());
    }
}