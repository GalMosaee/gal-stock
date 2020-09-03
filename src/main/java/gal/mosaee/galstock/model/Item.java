package gal.mosaee.galstock.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "items")
@ApiModel(description = "Details about the item")
public class Item {
    @ApiModelProperty(notes = "The unique Id of the item")
    private long id;
    @ApiModelProperty(notes = "The unique name of the item")
    private String name;
    @ApiModelProperty(notes = "The amount of the item")
    private int amount;
    @ApiModelProperty(notes = "The unique inventory code of the item")
    private String inventoryCode;

    public Item() {

    }

    public Item(String name, int amount, String inventoryCode) {
        this.name = name;
        this.amount = amount;
        this.inventoryCode = inventoryCode;
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="name",unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Column(name="inventory_code", unique = true, nullable = false)
    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", inventory_code='" + inventoryCode + '\'' +
                '}';
    }
}
