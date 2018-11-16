package errors;

public class NLessThanThreeError extends SplineError{
    @Override
    public int getCode() {
        return 1;
    }

    @Override
    public String getMessage() {
        return "Значение N меньше 3!";
    }
}
