package gal.mosaee.galstock.controller;

import gal.mosaee.galstock.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = StockController.class)
class StockControllerIntTest {

    @Autowired
    private MockMvc mvc;

    //@MockBean
    @Autowired
    private ItemRepository itemsRepository;

    @Test
    void getAllItems_ShouldReturnArrayOfAllItems() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/stock/items");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("[{\"id\":1,\"name\":\"Glasses\",\"amount\":5,\"inventoryCode\":\"abcd\"},"+
                "{\"id\":2,\"name\":\"Doll\",\"amount\":2,\"inventoryCode\":\"abce\"},"+
                "{\"id\":3,\"name\":\"Bicycle\",\"amount\":3,\"inventoryCode\":\"abcf\"},"+
                "{\"id\":4,\"name\":\"Pants\",\"amount\":4,\"inventoryCode\":\"abcg\"},"+
                "{\"id\":5,\"name\":\"Picture\",\"amount\":1,\"inventoryCode\":\"abch\"},"+
                "{\"id\":6,\"name\":\"Mirror\",\"amount\":3,\"inventoryCode\":\"abci\"}]",
                result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),200);
    }

    @Test
    void getItemById_ShouldReturnItem() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/stock/items/1");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("{\"id\":1,\"name\":\"Glasses\",\"amount\":5,\"inventoryCode\":\"abcd\"}",
                result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),200);
    }

    @Test
    void getItemById_ShouldNotReturnItem() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/stock/items/200");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),404);
    }

    @Test
    void createItem_ShouldCreateNewItem() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/stock/items")
                .content("{\"name\": \"Shirt\",\"amount\": 2,\"inventoryCode\": \"abcj\"}")
                .contentType("application/json")
                .characterEncoding("utf-8")
                .accept("application/json");
        MvcResult result = mvc.perform(request).andReturn();
        String tempResult = result.getResponse().getContentAsString();
        int id = Integer.parseInt(tempResult.substring(tempResult.indexOf(":")+1,
                tempResult.indexOf(",")));
        assertEquals("{\"id\":"+id+",\"name\":\"Shirt\",\"amount\":2,\"inventoryCode\":\"abcj\"}",
                result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),201);
    }

    @Test
    void withdrawItem_ShouldNotDecreaseItemAmount_ThereAreNotEnough() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/stock/items/withdrawal/2/6");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),400);
    }

    @Test
    void withdrawItem_ShouldNotDecreaseItemAmount_AmountIsNegative() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/stock/items/withdrawal/2/-2");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),400);
    }

    @Test
    void withdrawItem_ShouldDecreaseItemAmount() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/stock/items/withdrawal/2/2");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("{\"id\":2,\"name\":\"Doll\",\"amount\":0,\"inventoryCode\":\"abce\"}",
                result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),200);
    }

    @Test
    void withdrawItem_ShouldNotIncreaseItemAmount_AmountIsNegative() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/stock/items/withdrawal/3/-2");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),400);
    }

    @Test
    void depositItem_ShouldIncreaseItemAmount() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/stock/items/deposit/3/2");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("{\"id\":3,\"name\":\"Bicycle\",\"amount\":5,\"inventoryCode\":\"abcf\"}",
                result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),200);
    }

    @Test
    void deleteItem_ShouldDeleteItem() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/stock/items/4");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("{\"id\":4,\"name\":\"Pants\",\"amount\":4,\"inventoryCode\":\"abcg\"}",
                result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),200);
    }

    @Test
    void deleteItem_ShouldNotDeleteItem() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/stock/items/200");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
        assertEquals(result.getResponse().getStatus(),404);
    }
}