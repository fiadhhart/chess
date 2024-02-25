package service.exceptions;

public class UnauthorizedException  extends Exception{
    public UnauthorizedException(String message) {
        super(message);
    }
}