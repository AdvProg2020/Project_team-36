package View;

public class ProductMenu extends Menu {//parentesh null e tuye har menu E ke budim ino  ke call knim badesh execute esh eturn mikne mire jaye habli

    private int productId;

    public ProductMenu( int productId) {
        super("ProductMenu", null);
        this.productId = productId;
    }

    @Override
    public void help() {

    }


}
