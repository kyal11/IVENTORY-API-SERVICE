package com.task.inventory.mapper;

import com.task.inventory.dto.item.ItemRes;
import com.task.inventory.dto.itemOwnerStock.ItemOwnerStockRes;
import com.task.inventory.entity.ItemOwnerStocks;
import com.task.inventory.entity.Items;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

        List<ItemOwnerStockRes> ownerStockResList = item.getOwnerStocks().stream()
                .map(this::toItemOwnerStockRes)
                .collect(Collectors.toList());
        res.setItemOwnerStockResList(ownerStockResList);
        return res;
    }

    public ItemOwnerStockRes toItemOwnerStockRes(ItemOwnerStocks stock) {
        ItemOwnerStockRes res = new ItemOwnerStockRes();
        res.setId(stock.getId());
        res.setItemId(stock.getItem().getId());
        res.setItemCode(stock.getItem().getCodeProduct());
        res.setItemName(stock.getItem().getName());
        res.setOwnerId(stock.getOwnerId());
        res.setQuantity(stock.getQuantity());
        res.setBorrowedQuantity(stock.getBorrowedQuantity());
        res.setAvailableQuantity(stock.getAvailableQuantity());
        return res;
    }
    public ItemRes toItemResWithStocks(Items item, List<ItemOwnerStocks> stocks) {
        ItemRes res = toItemRes(item);
        res.setItemOwnerStockResList(
                stocks.stream()
                        .map(this::toItemOwnerStockRes)
                        .toList()
        );
        return res;
    }
}
