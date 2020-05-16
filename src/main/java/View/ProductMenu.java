package View;

import Models.Product;

public class ProductMenu extends Menu {//parentesh null e tuye har menu E ke budim ino  ke call knim badesh execute esh eturn mikne mire jaye habli
//TODO seen ro bayad ziad konim !
    private Product product;

    public ProductMenu(Product product) {
        super("ProductMenu", null);
        this.product = product;
    }

    @Override
    public void help() {

    }


}
