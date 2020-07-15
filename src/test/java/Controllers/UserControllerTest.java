package Controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserControllerTest {


    @Test
    void editInfo() throws EntryController.InvalidUsernameException, EntryController.ManagerExistsException, EntryController.InvalidTypeException, UserController.NoFieldWithThisType {
        GlobalVariables globalVariables = new GlobalVariables();
        EntryController entryController = new EntryController(globalVariables);
        UserController userController = new UserController(globalVariables);

        entryController.setUsernameRegister("customer","Nazhixx");
        entryController.setFirstname("Nazanin");
        entryController.setLastname("Azarian");
        entryController.setPhoneNumber("09198923977");
        entryController.setEmail("badi.mojgan@gmail.com");
        entryController.setPassword("12345678");
        entryController.register();

        Assertions.assertEquals(true,entryController.isUserLoggedIn());
        Assertions.assertEquals("Nazhixx",globalVariables.getLoggedInUser().getUsername());
        Assertions.assertEquals("Nazanin",globalVariables.getLoggedInUser().getFirstname());
        Assertions.assertEquals("Azarian",globalVariables.getLoggedInUser().getLastname());
        Assertions.assertEquals("09198923977",globalVariables.getLoggedInUser().getPhoneNumber());
        Assertions.assertEquals("badi.mojgan@gmail.com",globalVariables.getLoggedInUser().getEmail());
        Assertions.assertEquals("12345678",globalVariables.getLoggedInUser().getPassword());

        userController.editInfo("firstname","Sayeh");
        userController.editInfo("lastname","Jarollahi");
        userController.editInfo("email","saye.jj@gmail.com");
        userController.editInfo("phone","09124637455");
        userController.editInfo("password","11111111");

        Assertions.assertEquals("Sayeh",globalVariables.getLoggedInUser().getFirstname());
        Assertions.assertEquals("Jarollahi",globalVariables.getLoggedInUser().getLastname());
        Assertions.assertEquals("09124637455",globalVariables.getLoggedInUser().getPhoneNumber());
        Assertions.assertEquals("saye.jj@gmail.com",globalVariables.getLoggedInUser().getEmail());
        Assertions.assertEquals("11111111",globalVariables.getLoggedInUser().getPassword());
    }
}