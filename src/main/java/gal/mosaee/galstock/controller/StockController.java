package gal.mosaee.galstock.controller;

import gal.mosaee.galstock.model.Item;
import gal.mosaee.galstock.repository.ItemRespository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private ItemRespository itemRespository;

    @GetMapping("/items")
    @ApiOperation(value = "Return all items",
                  notes = "Return all the items exist in the stock.")
    public List<Item> getAllItems() {
        return this.itemRespository.findAll();
    }

    @GetMapping("/items/{id}")
    @ApiOperation(value = "Return an item by Id",
            notes = "Return a specific item by its id.")
    public ResponseEntity<Item> getItemById(@ApiParam(value = "Item Id to retrieve.",required = true)
            @PathVariable(value = "id") Long itemId) {
        Optional<Item> item = this.itemRespository.findById(itemId);
        if (item.isPresent()){
            return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/items")
    @ApiOperation(value = "Add an item to stock",
            notes = "Add an item to stock.")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        if(item.getAmount()<0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Item createdItem = itemRespository.save(item);
        return new ResponseEntity<>(createdItem,HttpStatus.OK);
    }

    @PutMapping("/items/withdrawal/{id}/{amount}")
    @ApiOperation(value = "Reduce the amount of an item in stock",
            notes = "Reduce the amount of an item in stock.")
    public ResponseEntity<Item> withdrawItem(@ApiParam(value = "Item Id to withdraw.",required = true)
            @PathVariable(value = "id") long itemId,
            @ApiParam(value = "Amount to withdraw.\nMust be non-negative number",required = true)
            @PathVariable(value = "amount") int itemAmount){
        Optional<Item> item = this.itemRespository.findById(itemId);
        if(item.isPresent()){
            int calculatedAmount = item.get().getAmount()-itemAmount;
            if(itemAmount<1||calculatedAmount<0){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Item updatedItem = item.get();
            updatedItem.setAmount(updatedItem.getAmount()-itemAmount);
            itemRespository.save(updatedItem);
            return new ResponseEntity<Item>(updatedItem, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/items/deposit/{id}/{amount}")
    @ApiOperation(value = "Increase the amount of an item in stock",
            notes = "Increase the amount of an item in stock.")
    public ResponseEntity<Item> depositItem(@ApiParam(value = "Item Id to withdraw.",required = true)
            @PathVariable(value = "id") long itemId,
            @ApiParam(value = "Amount to deposit.\nMust be non-negative number",required = true)
            @PathVariable(value = "amount") int itemAmount){
        Optional<Item> item = this.itemRespository.findById(itemId);
        if(item.isPresent()){
            int calculatedAmount = item.get().getAmount()+itemAmount;
            if(itemAmount<1){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Item updatedItem = item.get();
            updatedItem.setAmount(calculatedAmount);
            itemRespository.save(updatedItem);
            return new ResponseEntity<Item>(updatedItem, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/items/{id}")
    @ApiOperation(value = "Delete an item from stock",
            notes = "Delete an item from stock.")
    public ResponseEntity<Item> deleteItem(@ApiParam(value = "Item Id to retrieve.",required = true)
            @PathVariable(value = "id") long itemId){
        Optional<Item> item = this.itemRespository.findById(itemId);
        if (item.isPresent()){
            itemRespository.delete(item.get());
            return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
