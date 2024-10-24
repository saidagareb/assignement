package com.example.assignement.controllers;

import com.example.assignement.domains.BankAccount;
import com.example.assignement.domains.TransferHistory;
import com.example.assignement.services.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController

@CrossOrigin(origins = "http://localhost:4200")
public class BankController {

    @Autowired
    private BankService bankService;

    @Operation(summary = "Create a new bank account for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
    })

    @PostMapping("/accounts/{customerId}/{initialDeposit}")
    public ResponseEntity<BankAccount> createAccount(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "Initial deposit amount") @PathVariable BigDecimal initialDeposit) {

        return bankService.createAccount(customerId, initialDeposit);

    }

    @Operation(summary = "Transfer balance between accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds or invalid accounts")
    })

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Parameter(description = "ID of the source account") @RequestParam Long fromAccountId,
                                         @Parameter(description = "ID of the target account") @RequestParam Long toAccountId,
                                         @Parameter(description = "Amount to transfer") @RequestParam BigDecimal amount) {
        bankService.transferAmount(fromAccountId, toAccountId, amount);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Retrieve balance of an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get account balance successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid accounts")
    })
    @GetMapping("/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBalance(@Parameter(description = "ID of the account") @PathVariable Long accountId) {
        BigDecimal balance = bankService.getBalanceByAccount(accountId);
        return ResponseEntity.ok(balance);

    }

    @Operation(summary = "Retrieve transfer history of an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get History transfer successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid accounts")
    })
    @GetMapping("/transfer-history/{accountId}")
    public ResponseEntity<List<TransferHistory>> getTransferHistory(@Parameter(description = "ID of the account") @PathVariable Long accountId) {
        List<TransferHistory> history = bankService.getTransferHistoryByAccount(accountId);
        return ResponseEntity.ok(history);
    }
}
