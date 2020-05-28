package se.leovegas.wallet.service;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import se.leovegas.wallet.TestDataProvider;
import se.leovegas.wallet.exception.LowBalanceException;
import se.leovegas.wallet.model.Player;
import se.leovegas.wallet.model.Transaction;
import se.leovegas.wallet.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {
    
    @Mock
    private TransactionRepository mockTransRepo;
    
    @InjectMocks
    private TransactionService transactionService;
    
    @Test
    public void testGetBalance() throws NotFoundException {
        long playerId = 1L;
        List<Transaction> mockTransactions = TestDataProvider.getTransactions1(new Player(1));
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(mockTransactions);
        long balance = transactionService.getBalance(playerId);
        assertEquals(balance, mockTransactions.get(1).getBalance());
    }

    @Test(expected = NotFoundException.class)
    public void testGetBalanceNotFound() throws NotFoundException {
        long playerId = 10L;
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(new ArrayList<>());
        transactionService.getBalance(playerId);
    }

    @Test
    public void testCredit() throws Exception {
        long transactionId = 10L;
        long playerId = 1L;
        long amount = 5000;
        Transaction mockTransaction = TestDataProvider.makeMockTransaction(transactionId, playerId, amount, false);
        List<Transaction> mockTransactions = TestDataProvider.getTransactions1(new Player(playerId));
        when(mockTransRepo.findById(transactionId)).thenReturn(Optional.empty());
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(mockTransactions);
        when(mockTransRepo.save(mockTransaction)).thenReturn(mockTransaction);
        transactionService.credit(transactionId, playerId, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreditDuplicateTransactionId() throws Exception {
        long transactionId = 1L;
        long playerId = 1L;
        long amount = 5000;
        Transaction mockTransaction = TestDataProvider.makeMockTransaction(transactionId, playerId, amount, false);
        when(mockTransRepo.findById(transactionId)).thenReturn(Optional.of(mockTransaction));
        transactionService.credit(transactionId, playerId, amount);
    }

    @Test(expected = NotFoundException.class)
    public void testCreditNotFoundPlayerId() throws Exception {
        long transactionId = 10L;
        long playerId = 10L;
        long amount = 5000;
        when(mockTransRepo.findById(transactionId)).thenReturn(Optional.empty());
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(new ArrayList<>());
        transactionService.credit(transactionId, playerId, amount);
    }

    @Test
    public void testDebit() throws Exception {
        long transactionId = 10L;
        long playerId = 1L;
        long amount = 500;
        Transaction mockTransaction = TestDataProvider.makeMockTransaction(transactionId, playerId, amount, true);
        List<Transaction> mockTransactions = TestDataProvider.getTransactions1(new Player(playerId));
        when(mockTransRepo.findById(transactionId)).thenReturn(Optional.empty());
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(mockTransactions);
        when(mockTransRepo.save(mockTransaction)).thenReturn(mockTransaction);
        transactionService.debit(transactionId, playerId, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDebitDuplicateTransactionId() throws Exception {
        long transactionId = 1L;
        long playerId = 1L;
        long amount = 500;
        Transaction mockTransaction = TestDataProvider.makeMockTransaction(transactionId, playerId, amount, false);
        when(mockTransRepo.findById(transactionId)).thenReturn(Optional.of(mockTransaction));
        transactionService.debit(transactionId, playerId, amount);
    }

    @Test(expected = NotFoundException.class)
    public void testDebitNotFoundPlayerId() throws Exception {
        long transactionId = 10L;
        long playerId = 10L;
        long amount = 500;
        when(mockTransRepo.findById(transactionId)).thenReturn(Optional.empty());
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(new ArrayList<>());
        transactionService.debit(transactionId, playerId, amount);
    }

    @Test(expected = LowBalanceException.class)
    public void testDebitNotSufficientBalance() throws Exception {
        long transactionId = 10L;
        long playerId = 1L;
        long amount = 50000;
        Transaction mockTransaction = TestDataProvider.makeMockTransaction(transactionId, playerId, amount, true);
        List<Transaction> mockTransactions = TestDataProvider.getTransactions1(new Player(playerId));
        when(mockTransRepo.findById(transactionId)).thenReturn(Optional.empty());
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(mockTransactions);
        when(mockTransRepo.save(mockTransaction)).thenReturn(mockTransaction);
        transactionService.debit(transactionId, playerId, amount);
    }

    @Test
    public void testGetHistory() throws NotFoundException {
        long playerId = 1L;
        List<Transaction> mockTransactions = TestDataProvider.getTransactions1(new Player(1));
        when(mockTransRepo.findByPlayerId(playerId)).thenReturn(mockTransactions);
        List<Transaction> transactions = transactionService.getHistory(playerId);
        assertEquals(transactions.size(), mockTransactions.size());
    }
}
