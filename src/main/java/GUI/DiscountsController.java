package GUI;

import Models.Customer;
import Models.Discount;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;

import java.util.ArrayList;
import java.util.Date;

public class DiscountsController {
    @FXML
    private TableColumn<DiscountTable,Integer> id;
    @FXML
    private TableColumn<DiscountTable,Date> startTime;
    @FXML
    private TableColumn<DiscountTable,Date> endTime;
    @FXML
    private TableColumn<DiscountTable,Integer> remaining;
    @FXML
    private TableColumn<DiscountTable,Double> discountPercent;
    @FXML
    private TableColumn<DiscountTable,Long> discountLimit;

    @FXML
    private TableView<DiscountTable> allDiscounts;

    public void fill(){
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        remaining.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        discountLimit.setCellValueFactory(new PropertyValueFactory<>("discountLimit"));
        discountPercent.setCellValueFactory(new PropertyValueFactory<>("discountPercent"));

        ArrayList<DiscountTable> discountTables = new ArrayList<>();
        ((Customer)Constants.globalVariables.getLoggedInUser()).getAllDiscountsForCustomer().forEach(
                (key,value) -> discountTables.add(new DiscountTable(key,value))
        );

        allDiscounts.setItems(FXCollections.observableList(discountTables));
    }

    public class DiscountTable{
        private int id;
        private Date startTime;
        private Date endTime;
        private int remaining;
        private double discountPercent;
        private long discountLimit;

        public DiscountTable(Discount discount , int remaining) {
            this.id = discount.getId();
            this.startTime = discount.getStartTime();
            this.endTime = discount.getEndTime();
            this.remaining = remaining;
            this.discountPercent = discount.getDiscountPercent();
            this.discountLimit = discount.getDiscountLimit();
        }

        public int getId() {
            return id;
        }

        public Date getStartTime() {
            return startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public int getRemaining() {
            return remaining;
        }

        public double getDiscountPercent() {
            return discountPercent;
        }

        public long getDiscountLimit() {
            return discountLimit;
        }
    }
}

