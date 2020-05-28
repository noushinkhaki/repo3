package se.leovegas.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.leovegas.wallet.model.Player;
import se.leovegas.wallet.model.Transaction;
import se.leovegas.wallet.repository.PlayerRepository;
import se.leovegas.wallet.repository.TransactionRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    private void initDb() {
        Player player1 = DatabaseProvider.getPlayer("Mel", "Gibson", "31-08-1962",
                "+1576841922", "No. 22, Redwood 1234");
        Player createdPlayer1 = playerRepository.save(player1);
        List<Transaction> transactions1 = DatabaseProvider.getTransactions1(createdPlayer1);
        transactions1.stream().map(transaction -> transactionRepository.save(transaction)).collect(Collectors.toList());
        Player player2 = DatabaseProvider.getPlayer("Jennifer", "Aniston", "05-01-1975",
                "+1596847635", "No. 7, White 9090");
        Player createdPlayer2 = playerRepository.save(player2);
        List<Transaction> transactions2 = DatabaseProvider.getTransactions2(createdPlayer2);
        transactions2.stream().map(transaction -> transactionRepository.save(transaction)).collect(Collectors.toList());
    }
}
