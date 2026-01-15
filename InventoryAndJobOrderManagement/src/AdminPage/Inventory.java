package AdminPage;

import java.util.Date;

public class Inventory {
    private String itemId;
    private String itemName;
    private String category;
    private String batchNo;
    private String status;
    private int quantity;
    private Date dateAdded;

    public Inventory(String itemId, String itemName, String category, String batchNo, String status, int quantity, Date dateAdded) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.batchNo = batchNo;
        this.status = status;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
