package errors;

public class IncludeError extends SplineError{
    @Override
    public int getCode() {
        return 3;
    }

    @Override
    public String getMessage() {
        return "Значение ХХ не включено в вектор";
    }
}
