package com.example.assignement.services;

import com.example.assignement.domains.BankAccount;
import com.example.assignement.domains.Customer;
import com.example.assignement.domains.TransferHistory;
import com.example.assignement.exceptions.AccountNotFoundException;
import com.example.assignement.exceptions.InsufficientFundsException;
import com.example.assignement.repositories.BankAccountRepository;
import com.example.assignement.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public ResponseEntity<BankAccount> createAccount(Long customerId, BigDecimal initialDeposit) {
/*
  First requirement: Creation of new account
    */

        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {

            throw new InsufficientFundsException();// Exception in case value < 0
        }

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new AccountNotFoundException("Customer not found");

        } else {

            BankAccount account = new BankAccount();

            account.setCustomer(customer.get());
            account.setBalance(initialDeposit);

            BankAccount savedAccount = bankAccountRepository.save(account);

            return ResponseEntity.ok(savedAccount);
        }


    }

    /*
      Second requirement: the Transfer of amounts between any two accounts,
        */

    @Transactional
    public void transferAmount(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        BankAccount fromAccount = bankAccountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found" + fromAccountId));

        BankAccount toAccount = bankAccountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Customer not found" + toAccountId));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        TransferHistory transferHistory = new TransferHistory();
        transferHistory.setBankAccount(fromAccount);
        transferHistory.setAmount(amount);
        transferHistory.setReceiverTransferAccountId(toAccountId);

        fromAccount.getTransferHistories().add(transferHistory);
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);

    }

    /*
         third requirement: Function Retrieve balances for a given account.,
           */
    public BigDecimal getBalanceByAccount(Long accountId) {

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found" + accountId));

        return account.getBalance();
    }

    /*
         Four requirement: Function Retrieve transfer history for a given account.,
           */

    @Transactional
    public List<TransferHistory> getTransferHistoryByAccount(Long accountId) {

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found" + accountId));
        return account.getTransferHistories();
    }
}
