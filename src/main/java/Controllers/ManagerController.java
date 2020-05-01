package Controllers;

import Models.User;

import java.util.ArrayList;

public class ManagerController extends UserController{
    GlobalVariables userVariables;
    public ManagerController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public ArrayList<User> getAllUsers(){
        return User.getAllUsers();
    }



}
