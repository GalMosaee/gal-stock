package gal.mosaee.galstock.controller;

import gal.mosaee.galstock.model.Item;
import gal.mosaee.galstock.repository.ItemRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class ItemController {
    @Autowired
    private ItemRespository itemRespository;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return this.itemRespository.findAll();
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable(value = "id") Long itemId) {
        Optional<Item> item = this.itemRespository.findById(itemId);
        if (item.isPresent()){
            return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        if(item.getAmount()<0){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Item createdItem = itemRespository.save(item);
        return new ResponseEntity<>(createdItem,HttpStatus.OK);
    }

    @PutMapping("/items/withdrawal/{id}/{amount}")
    public ResponseEntity<Item> withdrawalItem(@PathVariable(value = "id") long itemId,
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
    public ResponseEntity<Item> depositItem(@PathVariable(value = "id") long itemId,
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
    public ResponseEntity<Item> deleteItem(@PathVariable(value = "id") long itemId){
        Optional<Item> item = this.itemRespository.findById(itemId);
        if (item.isPresent()){
            itemRespository.delete(item.get());
            return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
