package motorph.controller;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private static final Map<String, String> credentials = new HashMap<>();

    static {
        credentials.put("10001", "10001");
        credentials.put("10002", "10002");
        credentials.put("10003", "10003");
    }

    public static boolean authenticate(String empNo, String password) {
        return credentials.containsKey(empNo)
                && credentials.get(empNo).equals(password);
    }
}
