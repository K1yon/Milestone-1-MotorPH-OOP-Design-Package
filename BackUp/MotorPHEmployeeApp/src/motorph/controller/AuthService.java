package motorph.controller;

public interface AuthService {
    boolean authenticate(String employeeNumber, String password);
}