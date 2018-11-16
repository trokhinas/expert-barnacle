package errors;

public class VectorXError extends SplineError{
    @Override
    public int getCode() {
        return 2;
    }

    @Override
    public String getMessage() {
        return "Вектор Х не упорядочен!";
    }
}
