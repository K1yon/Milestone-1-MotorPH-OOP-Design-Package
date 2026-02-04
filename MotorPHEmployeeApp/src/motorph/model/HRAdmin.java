package motorph.model;

public class HRAdmin extends User {

    public HRAdmin(String email, String password) {
        super(email, password);
    }

    public void manageEmployees() {
        System.out.println("Managing employee records...");
    }
}
