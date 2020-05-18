package Repository;

import Models.Status;

public class SaveManager {
    private int userId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String password;
    private Status status;
    private static int lastId = 0;

    private SaveManager() {
    }
}
