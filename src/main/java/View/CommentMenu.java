package View;

import Controllers.EntryController;
import Models.Comment;

import java.util.ArrayList;

public class CommentMenu extends Menu {

    public CommentMenu(String name, Menu parentMenu) {
        super(name, parentMenu);
    }

    @Override
    public void help() {
        System.out.println("Add comment\n" +
                "login\n" +
                "logout\n" +
                "help\n" +
                "register");
    }

    @Override
    public void execute() {
        ArrayList<Comment> allComments = productsController.getProductComments();
        if (allComments.isEmpty())
            System.out.println("there is no comment for this product yet!");
        else {
            for (Comment comment : allComments) {
                System.out.println("User: " + comment.getUser().getUsername());
                System.out.println(comment.getComment());
                System.out.println("*************");
            }
        }
        System.out.println("Score: " + productsController.getChosenProduct().getScore());
    }

    private Menu getAddCommentMenu() {
        return new Menu("add comment menu", this) {
            @Override
            public void help() {
                System.out.println("You are in comment menu");
            }

            @Override
            public void execute() {
                String title;
                String content;
                System.out.println("Enter title:");
                title = scanner.nextLine().trim();
                if (title.matches("register|login|logout|help")) {
                    sideCommands(title);
                    this.execute();
                } else if (title.matches("back"))
                    this.parentMenu.execute();
                System.out.println("Enter content:");
                content = scanner.nextLine().trim();
                if (content.matches("register|login|logout|help")) {
                    sideCommands(title);
                    this.execute();
                } else if (content.matches("back"))
                    this.parentMenu.execute();
                try {
                    productsController.addComment(title, content);
                } catch (EntryController.NotLoggedInException e) {
                    System.out.println("You have to login in order to put a comment!");
                }
            }
        };
    }
}







