package se.leovegas.wallet.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.leovegas.wallet.TestDataProvider;
import se.leovegas.wallet.model.Player;
import se.leovegas.wallet.model.Transaction;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionRepository mockTransactionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private Player createdPlayer1;
    private Player createdPlayer2;

    @Before
    public void setupDatabase() {
        Player player1 = TestDataProvider.getPlayer("Mel", "Gibson", "31-08-1962",
                "+1576841922", "No. 22, Redwood 1234");
        createdPlayer1 = playerRepository.save(player1);
        List<Transaction> transactions1 = TestDataProvider.getTransactions1(createdPlayer1);
        transactions1.stream().map(transaction -> transactionRepository.save(transaction)).collect(Collectors.toList());
        Player player2 = TestDataProvider.getPlayer("Jennifer", "Aniston", "05-01-1975",
                "+1596847635", "No. 7, White 9090");
        createdPlayer2 = playerRepository.save(player2);
        List<Transaction> transactions2 = TestDataProvider.getTransactions2(createdPlayer2);
        transactions2.stream().map(transaction -> transactionRepository.save(transaction)).collect(Collectors.toList());
    }

    @Test
    public void testFindByPlayerId() {
        List<Transaction> mockTransactions = TestDataProvider.getTransactions1(createdPlayer1);
        when(mockTransactionRepository.findByPlayerId(createdPlayer1.getId())).thenReturn(mockTransactions);
        List<Transaction> transactions = transactionRepository.findByPlayerId(createdPlayer1.getId());
        assertEquals(transactions.get(0).getBalance(), mockTransactions.get(0).getBalance());
        assertEquals(transactions.get(0).getTransDate(), mockTransactions.get(0).getTransDate());
        assertEquals(transactions.get(0).getPlayer().getId(), mockTransactions.get(0).getPlayer().getId());
        assertEquals(transactions.get(1).getBalance(), mockTransactions.get(1).getBalance());
        assertEquals(transactions.get(1).getTransDate(), mockTransactions.get(1).getTransDate());
        assertEquals(transactions.get(1).getPlayer().getId(), mockTransactions.get(1).getPlayer().getId());
    }
}
