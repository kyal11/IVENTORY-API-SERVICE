package com.task.mapper;

import com.task.dto.item.ItemRes;
import com.task.dto.itemOwnerStock.ItemOwnerStockRes;
import com.task.entity.ItemOwnerStocks;
import com.task.entity.Items;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public ItemRes toItemRes(Items item) {
        ItemRes res = new ItemRes();
        res.setId(item.getId());
        res.setCodeProduct(item.getCodeProduct());
        res.setName(item.getName());
        res.setDescription(item.getDescription());
        res.setTotalQuantity(item.getTotalQuantity());
        res.setStatus(item.getStatus());
        res.setCreatedAt(item.getCreatedAt());
        res.setUpdatedAt(item.getUpdatedAt());
        return res;
    }

    public ItemOwnerStockRes itemOwnerStockRestoRes(ItemOwnerStocks stock) {
        ItemOwnerStockRes res = new ItemOwnerStockRes();
        res.setId(stock.getId());
        res.setItemId(stock.getItem().getId());
        res.setItemCode(stock.getItem().getCodeProduct());
        res.setItemName(stock.getItem().getName());
        res.setOwnerId(stock.getOwnerId());
        res.setQuantity(stock.getQuantity());
        return res;
    }
}
