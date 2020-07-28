package Client.Models;

import Client.GUI.Constants;
import Client.Network.Client;
import Models.Query;
import Models.Response;
import Models.Status;
import Repository.SaveComment;
import Repository.SaveProduct;
import Repository.SaveUser;
import com.google.gson.Gson;

public class Comment implements Pendable{
    private SaveComment saveComment;
    private String title;
    private String comment;
    private Enum<Status> ConfirmationStatus;
    private boolean hasBought;


    public Comment(SaveComment saveComment) {
        this.saveComment = saveComment;
        this.title = saveComment.getTitle();
        this.comment = saveComment.getComment();
        this.ConfirmationStatus = saveComment.getConfirmationStatus();
        this.hasBought = saveComment.isHasBought();
    }

    public SaveComment getSaveComment() {
        return saveComment;
    }

    public User getUser() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "User");
        query.getMethodInputs().put("id", "" + saveComment.getUserId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("User")) {
            Gson gson = new Gson();
            SaveUser saveUser = gson.fromJson(response.getData(), SaveUser.class);
            return User.generateUser(saveUser);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public Product getProduct() {
        Query query = new Query(Constants.globalVariables.getToken(), "GetById", "Product");
        query.getMethodInputs().put("id", "" + saveComment.getProductId());
        Response response = Client.process(query);
        if (response.getReturnType().equals("Product")) {
            Gson gson = new Gson();
            SaveProduct saveProduct = gson.fromJson(response.getData(), SaveProduct.class);
            return new Product(saveProduct);
        } else {
            System.out.println(response);
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public Enum<Status> getConfirmationStatus() {
        return ConfirmationStatus;
    }

    public boolean isHasBought() {
        return hasBought;
    }

    @Override
    public String getPendingRequestType() {
        return "comment";
    }

    @Override
    public String toString() {
        return "User : "+this.getUser().getUsername()+'\n'+
                "title: "+title+'\n'+
                "comment: "+comment+'\n'+
                "has bought: "+ (hasBought?"yes":"no");
    }

}
