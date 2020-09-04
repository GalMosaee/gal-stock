package gal.mosaee.galstock.controller;

import gal.mosaee.galstock.model.Item;
import gal.mosaee.galstock.repository.ItemRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/items")
    @ApiOperation(value = "Return all items",
                  notes = "Return all the items exist in the stock.")
    public List<Item> getAllItems() {
        return this.itemRepository.findAll();
    }

    @GetMapping("/items/{id}")
    @ApiOperation(value = "Return an item by Id",
            notes = "Return a specific item by its id.")
    public ResponseEntity<Item> getItemById(@ApiParam(value = "Item Id to retrieve.",required = true)
            @PathVariable(value = "id") Long itemId) {
        Optional<Item> item = this.itemRepository.findById(itemId);
        if (item.isPresent()){
            return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Id doesn't exist!");
    }

    @PostMapping("/items")
    @ApiOperation(value = "Add an item to stock",
            notes = "Add an item to stock.")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        if(item.getAmount()<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Amount must be non-negative number!");
        }
        Item createdItem = itemRepository.save(item);
        return new ResponseEntity<>(createdItem,HttpStatus.CREATED);
    }

    @PutMapping("/items/withdrawal/{id}/{amount}")
    @ApiOperation(value = "Reduce the amount of an item in stock",
            notes = "Reduce the amount of an item in stock.")
    public ResponseEntity<Item> withdrawItem(@ApiParam(value = "Item Id to withdraw.",required = true)
            @PathVariable(value = "id") long itemId,
            @ApiParam(value = "Amount to withdraw.\nMust be positive number",required = true)
            @PathVariable(value = "amount") int itemAmount){
        Optional<Item> item = this.itemRepository.findById(itemId);
        if(item.isPresent()){
            int calculatedAmount = item.get().getAmount()-itemAmount;
            if(itemAmount<1){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Amount must be positive number!");
            }
            if(calculatedAmount<0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "There are not enough items!");
            }
            Item updatedItem = item.get();
            updatedItem.setAmount(updatedItem.getAmount()-itemAmount);
            itemRepository.save(updatedItem);
            return new ResponseEntity<Item>(updatedItem, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Id doesn't exist!");
    }

    @PutMapping("/items/deposit/{id}/{amount}")
    @ApiOperation(value = "Increase the amount of an item in stock",
            notes = "Increase the amount of an item in stock.")
    public ResponseEntity<Item> depositItem(@ApiParam(value = "Item Id to deposit.",required = true)
            @PathVariable(value = "id") long itemId,
            @ApiParam(value = "Amount to deposit.\nMust be positive number",required = true)
            @PathVariable(value = "amount") int itemAmount){
        Optional<Item> item = this.itemRepository.findById(itemId);
        if(item.isPresent()){
            int calculatedAmount = item.get().getAmount()+itemAmount;
            if(itemAmount<1){
                if(itemAmount<1){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Amount must be positive number!");
                }
            }
            Item updatedItem = item.get();
            updatedItem.setAmount(calculatedAmount);
            itemRepository.save(updatedItem);
            return new ResponseEntity<Item>(updatedItem, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Id doesn't exist!");
    }

    @DeleteMapping("/items/{id}")
    @ApiOperation(value = "Delete an item from stock",
            notes = "Delete an item from stock.")
    public ResponseEntity<Item> deleteItem(@ApiParam(value = "Item Id to retrieve.",required = true)
            @PathVariable(value = "id") long itemId){
        Optional<Item> item = this.itemRepository.findById(itemId);
        if (item.isPresent()){
            itemRepository.delete(item.get());
            return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Id doesn't exist!");
    }

}
