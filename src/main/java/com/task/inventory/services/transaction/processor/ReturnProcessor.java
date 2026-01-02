package com.task.inventory.services.transaction.processor;

import com.task.inventory.constant.ItemLogType;
import com.task.inventory.constant.LoanStatus;
import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.itemLoan.ReturnLoanReq;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import com.task.inventory.entity.*;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemMapper;
import com.task.inventory.mapper.ItemTransactionMapper;
import com.task.inventory.repository.*;
import com.task.inventory.security.SecurityUtils;
import com.task.inventory.services.ItemLoanService;
import com.task.inventory.services.ItemLogsService;
import com.task.inventory.services.ItemsService;
import com.task.inventory.utils.ObjectToJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReturnProcessor implements  TransactionProcessor{
    private final ItemLoanRepository itemLoanRepository;
    private final ItemsTransactionsRepository transactionsRepository;
    private final ItemOwnerStocksRepository itemOwnerStocksRepository;
    private final ItemTransactionMapper mapper;
    private final UsersRepository usersRepository;
    private final ItemsRepository itemsRepository;
    private final ItemsService itemsService;
    private final ItemLoanService itemLoanService;

    private final ItemMapper itemMapper;
    private final ObjectToJson objectToJson;
    private final ItemLogsService itemLogsService;

    @Override
    public TransactionType getType() {
        return TransactionType.RETURN;
    }

    @Override
    @Transactional
    public ItemTransactionRes process(CreateItemTransactionReq request) {

        if (request.getItemLoanId() == null) {
            throw new BadRequestException("Item loan ID is required for return");
        }
        ItemLoan loan = itemLoanRepository.findByIdAndStatus(request.getItemLoanId(), LoanStatus.BORROWED)
                .orElseThrow(() -> new NotFoundException("Item loan not found"));

        UUID itemId= loan.getItem().getId();
        UUID ownerId = loan.getOwner().getId();

        // Audit Item Log
        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("User with ID " + itemId + " not found"));
        String beforeState = objectToJson.toJson(itemMapper.toItemRes(item));

        ItemOwnerStocks ownerStock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(itemId, ownerId)
                .orElseThrow(() -> new BadRequestException("Owner does not own this item"));

        Users currentUser = usersRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemTransactions tx = new ItemTransactions();
        tx.setItem(loan.getItem());
        tx.setFromOwnerId(ownerId);
        tx.setToOwnerId(loan.getBorrower().getId());
        tx.setTransactionType(TransactionType.RETURN);
        tx.setQuantity(loan.getQuantity());
        tx.setPerformedBy(currentUser);
        tx.setNotes(request.getNotes());
        tx.setCreatedAt(LocalDateTime.now());
        tx.setUpdatedAt(LocalDateTime.now());

        ItemTransactions savedTx = transactionsRepository.save(tx);

        ReturnLoanReq returnLoanReq = new ReturnLoanReq();
        returnLoanReq.setLoanId(loan.getId());
        returnLoanReq.setReturnTransaction(savedTx);

        ItemLoan updatedLoan = itemLoanService.returnLoan(returnLoanReq);
        savedTx.setReturnLoan(updatedLoan);
        itemsService.updateItemStatus(loan.getItem().getId());

        // Audit Item Log
        Items itemAfterTransfer = itemsRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("User with ID " + itemId + " not found"));
        String afterState = objectToJson.toJson(itemMapper.toItemRes(itemAfterTransfer));
        itemLogsService.log(
                item.getId(),
                null,
                ItemLogType.RETURN_ITEM,
                "Transaction for return item",
                beforeState,
                afterState,
                SecurityUtils.getCurrentUserId()
        );
        return mapper.toItemTransactionRes(savedTx);
    }
}
