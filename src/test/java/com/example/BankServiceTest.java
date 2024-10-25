package com.example;

import com.example.assignement.domains.BankAccount;
import com.example.assignement.domains.Customer;
import com.example.assignement.domains.TransferHistory;
import com.example.assignement.exceptions.AccountNotFoundException;
import com.example.assignement.exceptions.CustomerNotFoundException;
import com.example.assignement.exceptions.InsufficientFundsException;
import com.example.assignement.repositories.BankAccountRepository;
import com.example.assignement.repositories.CustomerRepository;
import com.example.assignement.services.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BankServiceTest {
    @Mock
    private BankService bankService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bankService = new BankService(bankAccountRepository, customerRepository);

    }

    /*
    3 use cases of createAccount( customerId, initialDeposit):
    -   success : CreatedAccount_WhenValidInput
    -   failure:  Exception when CustomerNotFound
    -   failure:  Exception when InitialDeposit Negative

     */
    @Test
    void createAccount_ReturnCreatedAccount_WhenValidInput() {
        Long customerId = 1L;
        BigDecimal initialDeposit = BigDecimal.valueOf(1000);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("saida");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<BankAccount> response = bankService.createAccount(customerId, initialDeposit);

        assertNotNull(response, "The created account should not be null");
        assertEquals(initialDeposit, response.getBody().getBalance());
        assertEquals(customer, response.getBody().getCustomer());
        verify(bankAccountRepository).save(any(BankAccount.class));
    }

    @Test
    void createAccount_ThrowException_WhenCustomerNotFound() {
        Long customerId = 2L;
        BigDecimal initialDeposit = BigDecimal.valueOf(1000);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            bankService.createAccount(customerId, initialDeposit);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void createAccount_ThrowException_WhenInitialDepositIsNegative() {
        Long customerId = 1L;
        BigDecimal initialDeposit = BigDecimal.valueOf(-100);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("gareb");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            bankService.createAccount(customerId, initialDeposit);
        });

        assertNotNull(exception);
    }
    /*
       4 scenarios of transferAmount(fromAccountId, toAccountId, amount):

       -    Success:transfer with correct parameters
       -    Failure:Exception when fromAccount is NotFound
       -    Failure:Exception when toAccount is NotFound
       -    Failure: Exception when amount is heigher than balance of fromAccount
     */

    @Test
    void transferAmount_UpdateBalances_WhenValidTransfer() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal transferAmount = BigDecimal.valueOf(300);

        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(fromAccountId);
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        fromAccount.setTransferHistories(new ArrayList<>());

        BankAccount toAccount = new BankAccount();
        toAccount.setId(toAccountId);
        toAccount.setBalance(BigDecimal.valueOf(500));

        when(bankAccountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        bankService.transferAmount(fromAccountId, toAccountId, transferAmount);


        assertEquals(BigDecimal.valueOf(700), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(800), toAccount.getBalance());

        assertEquals(1, fromAccount.getTransferHistories().size());
        TransferHistory transferHistory = fromAccount.getTransferHistories().get(0);
        assertEquals(fromAccount, transferHistory.getBankAccount());
        assertEquals(transferAmount, transferHistory.getAmount());
        assertEquals(toAccountId, transferHistory.getReceiverTransferAccountId());
    }

    @Test
    void transferAmount_ThrowException_WhenFromAccountNotFound() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal transferAmount = BigDecimal.valueOf(300);

        when(bankAccountRepository.findById(fromAccountId)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            bankService.transferAmount(fromAccountId, toAccountId, transferAmount);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void transferAmount_ThrowException_WhenToAccountNotFound() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal transferAmount = BigDecimal.valueOf(300);

        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(fromAccountId);
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        when(bankAccountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.findById(toAccountId)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            bankService.transferAmount(fromAccountId, toAccountId, transferAmount);
        });

        assertEquals("Account not found", exception.getMessage());
    }


    @Test
    void transferAmount_ThrowException_WhenInsufficientFunds() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal transferAmount = BigDecimal.valueOf(2000); // tranfer Value heigher than balance

        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(fromAccountId);
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        BankAccount toAccount = new BankAccount();
        toAccount.setId(toAccountId);
        toAccount.setBalance(BigDecimal.valueOf(500));

        when(bankAccountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            bankService.transferAmount(fromAccountId, toAccountId, transferAmount);
        });

        assertNotNull(exception);
    }

    /*
        2 scenarios for  getBalanceByAccount(Long accountId)
   -    Success: when account exist.
   -    Failure: Exception Acount not exists
     */

    @Test
    void getBalanceByAccount_ReturnBalance_WhenAccountExists() {
        Long accountId = 1L;
        BigDecimal balance = BigDecimal.valueOf(500);
        BankAccount account = new BankAccount();
        account.setId(accountId);
        account.setBalance(balance);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal result = bankService.getBalanceByAccount(accountId);

        assertEquals(balance, result);
    }

    @Test
    void getBalanceByAccount_ThrowException_WhenAccountNotFound() {
        Long accountId = 1L;

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            bankService.getBalanceByAccount(accountId);
        });

        assertEquals("Account not found", exception.getMessage());
    }

    /*
          2 scenarios of getTransferHistoryByAccount(Long accountId)
          -   success: exists transfer for a given account
          -   failure: Exception when acount not exists
     */
    @Test
    void getTransferHistoryByAccount_ReturnTransferHistories_WhenAccountExists() {
        Long accountId = 1L;
        BankAccount account = new BankAccount();
        account.setId(accountId);

        List<TransferHistory> transferHistories = new ArrayList<>();
        transferHistories.add(new TransferHistory());
        account.setTransferHistories(transferHistories);

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        List<TransferHistory> result = bankService.getTransferHistoryByAccount(accountId);

        assertEquals(transferHistories, result);
    }

    @Test
    void getTransferHistoryByAccount_ThrowException_WhenAccountNotFound() {
        Long accountId = 1L;

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            bankService.getTransferHistoryByAccount(accountId);
        });

        assertEquals("Account not found", exception.getMessage());
    }
}
