package motorph.model;

public class User {

    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean authenticate(String inputPassword) {
        return password.equals(inputPassword);
    }

    public String getEmail() {
        return email;
    }
}
