package View;


import Controllers.ProductsController;
import Models.Field;
import Models.Product;
import Models.ProductField;


public class ProductMenu extends Menu {//parentesh null e tuye har menu E ke budim ino  ke call knim badesh execute esh eturn mikne mire jaye habli



    public ProductMenu(Menu parentMenu) {
        super("ProductMenu", parentMenu);
        subMenus.put("digest",new DigestMenu("Digest menu",this));
        subMenus.put("attributes", getAttributesMenu());
        subMenus.put("compare",getCompareMenu());
        subMenus.put("comment",new CommentMenu("Comment menu",this));

    }

    @Override
    public void help() {
        System.out.println("digest\n" +
                "attributes" +
                "compare\n" +
                "comments\n" +
                "login\n" +
                "logout\n" +
                "register\n" +
                "help");
    }

    private Menu getCompareMenu(){
        return new Menu("compare menu",this) {
            @Override
            public void help() {}

            @Override
            public void execute() {
                System.out.println("enter the productId you want to compare this with:");
                String input = scanner.nextLine().trim();
                if(!input.matches("\\d+"))
                    System.err.println("Invalid productId!");
                try {
                    Product product1 = productsController.getChosenProduct();
                    Product product2 = productsController.compare(Integer.parseInt(input));
                    printComparedProducts(product1,product2);
                } catch (ProductsController.NoProductWithId noProductWithId) {
                    System.err.println("There is no product with id!");
                } catch (ProductsController.NotInTheSameCategory notInTheSameCategory) {
                    System.out.println("Products are not in the same category!");;
                }
            }
        };
    }

    private void printComparedProducts(Product product1,Product product2){
        System.out.printf("%20s%20s%20s\n","fields",product1.getName(),product2.getName());
        System.out.printf("%20s%20s%20s\n","company",product1.getCompany(),product2.getCompany());
        System.out.printf("%20s%20s%20s\n","score",product1.getScore(),product2.getScore());
        System.out.printf("%20s%20s%20s\n","seen number",product1.getSeenNumber(),product2.getSeenNumber());
        for (Field field : product1.getFieldsOfCategory()) {
            System.out.printf("%20s%20s%20s",field.getName(),field.getQuantityString(),product2.getField(field.getName()).getQuantityString());
        }
    }

    private Menu getAttributesMenu(){
        return new Menu("Attributes",this) {
            @Override
            public void help() { }

            @Override
            public void execute() {
                Product product = productsController.getChosenProduct();
                System.out.println("Name: " + product.getName());
                System.out.println("Company: " + product.getCompany());
                System.out.println("Category: " + product.getCategory());
                System.out.println("Average score:" + product.getScore());
                for (Field field : product.getFieldsOfCategory()) {
                    System.out.println(field);
                }
                for (ProductField field : product.getProductFields()) {
                    System.out.println("****************");
                    System.out.println(field);
                }
            }
        };
    }

    @Override
    public void execute() {
        String input;
        while(!(input= scanner.nextLine().trim()).matches("back")){
            if(input.matches("loin|logout|register|help"))
                sideCommands(input);
            else{Menu menu=null;
                for (String regex : subMenus.keySet()) {
                    if(input.matches(regex)) {
                        menu = subMenus.get(regex);
                    }
                }
                if(menu==null){
                    System.err.println("invalid command!");
                }
                else
                    menu.execute();
            }
        }
        this.parentMenu.execute();
    }
}
