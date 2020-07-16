package Controllers;

import Models.*;
import View.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EntryController extends UserController {
    UserAreaMenu userAreaMenu;

    public EntryController(GlobalVariables userVariables) {
        super(userVariables);
    }

    public void setUserAreaMenu(UserAreaMenu userAreaMenu) {
        this.userAreaMenu = userAreaMenu;
    }

    public void setPasswordLogin(String password) throws WrongPasswordException {
        if (!password.equals(userVariables.getLoggedInUser().getPassword())) {
            throw new WrongPasswordException("Wrong password!");
        }
        //  else{
        //       //TODO change for consule
        //userAreaMenu.newUserMenu(userVariables.getLoggedInUser().getType());
        //}
    }

    public void setUserNameLogin(String username) throws InvalidUsernameException {
        if (!User.isThereUsername(username) || User.getUserByUsername(username) == null) {
            throw new InvalidUsernameException("There is no user with this username");
        } else {
            userVariables.setLoggedInUser(User.getUserByUsername(username));

        }
    }

    public void setUsernameRegister(String type, String username) throws InvalidTypeException, InvalidUsernameException, ManagerExistsException {
        if (!type.matches("customer|manager|seller"))
            throw new InvalidTypeException("there is no user with this type");
        else if (User.isThereUsername(username))
            throw new InvalidUsernameException("there is a user with this username");
        else {
            createNewAccount(username, type);
        }
    }

    public void setCompany(String name) {
        ((Seller) userVariables.getLoggedInUser()).setCompanyName(name);
    }

    public void setCompanyInfo(String info) {
        ((Seller) userVariables.getLoggedInUser()).setCompanyInfo(info);
    }

    public void register() {
        User user = userVariables.getLoggedInUser();
        User.addUsername(user.getUsername());
        if (user instanceof Seller) {
            new Request((Seller) user, Status.TO_BE_ADDED);
            userVariables.setLoggedInUser(null);
            return;
        }
        User.addNewUser(user);
        if (user instanceof Manager)
            Manager.addNewManager((Manager) user);
        else if (user instanceof Customer)
            Customer.addNewCustomer((Customer) user);
        userVariables.setLoggedInUser(null);
    }

    public void logout() throws NotLoggedInException {
        if (this.userVariables.getLoggedInUser() != null)
            this.userVariables.setLoggedInUser(null);
        else
            throw new NotLoggedInException();

    }

    private void createNewAccount(String username, String type) throws ManagerExistsException {
        User newUser;
        if (type.matches("customer")) {
            newUser = new Customer(username);
        } else if (type.matches("seller")) {
            newUser = new Seller(username);

        } else {
            if (!Manager.canManagerRegister() && !(getLoggedInUser() instanceof Manager)) {
                throw new ManagerExistsException("There is a manager!You cannot register");
            } else {
                newUser = new Manager(username);
            }
        }
        userVariables.setLoggedInUser(newUser);
    }

    public boolean isUserLoggedIn() {
        if (userVariables.getLoggedInUser() != null)
            return true;
        return false;
    }

    public void setImage(String path) {
        userVariables.getLoggedInUser().setImageURL(path);
    }

    public Response processQuery(Query query) {
        return switch (query.getMethodName()) {
            case "setPasswordLogin" -> processSetPasswordLogin(query);
            case "setUserNameLogin" -> processSetUserNameLogin(query);
            case "setUsernameRegister" -> processSetUsernameRegister(query);
            case "setCompany" -> processSetCompany(query);
            case "setCompanyInfo" -> processSetCompanyInfo(query);
            case "register" -> processRegister(query);
            case "logout" -> processLogout(query);
            case "createNewAccount" -> processCreateNewAccount(query);
            case "isUserLoggedIn" -> processIsUserLoggedIn(query);
            case "setImage" -> processSetImage(query);
            default -> new Response("Error", "");
        };
    }

    private Response processSetImage(Query query) {
        String path = query.getMethodInputs().get("path");
        setImage(path);
        return new Response("void", "");
    }

    private Response processIsUserLoggedIn(Query query) {
        boolean result = isUserLoggedIn();
        Gson gson = new GsonBuilder().create();
        return new Response("boolean", gson.toJson(result));
    }

    private Response processCreateNewAccount(Query query) {
        String username = query.getMethodInputs().get("username");
        String type = query.getMethodInputs().get("type");
        try {
            createNewAccount(username, type);
            return new Response("void", "");
        } catch (ManagerExistsException e) {
            return new Response("ManagerExistsException", "");
        }
    }

    private Response processLogout(Query query) {
        try {
            logout();
            return new Response("void", "");
        } catch (NotLoggedInException e) {
            return new Response("NotLoggedInException", "");
        }
    }

    private Response processRegister(Query query) {
        register();
        return new Response("void", "");
    }

    private Response processSetCompanyInfo(Query query) {
        String info = query.getMethodInputs().get("info");
        setCompanyInfo(info);
        return new Response("void", "");
    }

    private Response processSetCompany(Query query) {
        String name = query.getMethodInputs().get("name");
        setCompany(name);
        return new Response("void", "");
    }

    private Response processSetUsernameRegister(Query query) {
        String type = query.getMethodInputs().get("type");
        String username = query.getMethodInputs().get("username");
        try {
            setUsernameRegister(type, username);
            return new Response("void", "");
        } catch (InvalidTypeException e) {
            return new Response("InvalidTypeException", "");
        } catch (InvalidUsernameException e) {
            return new Response("InvalidUsernameException", "");
        } catch (ManagerExistsException e) {
            return new Response("ManagerExistsException", "");
        }

    }

    private Response processSetUserNameLogin(Query query) {
        String username = query.getMethodInputs().get("username");
        try {
            setUserNameLogin(username);
            return new Response("void", "");
        } catch (InvalidUsernameException e) {
            return new Response("InvalidUsernameException", "");
        }
    }

    private Response processSetPasswordLogin(Query query) {
        String password = query.getMethodInputs().get("password");
        try {
            setPasswordLogin(password);
            return new Response("void", "");
        } catch (WrongPasswordException e) {
            return new Response("WrongPasswordException", "");
        }
    }

    public static class InvalidUsernameException extends Exception {
        public InvalidUsernameException(String message) {
            super(message);
        }
    }

    public static class InvalidTypeException extends Exception {
        public InvalidTypeException(String message) {
            super(message);
        }
    }

    public static class ManagerExistsException extends Exception {
        public ManagerExistsException(String message) {
            super(message);
        }
    }

    public static class WrongPasswordException extends Exception {
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    public static class NotLoggedInException extends Exception {
    }

}