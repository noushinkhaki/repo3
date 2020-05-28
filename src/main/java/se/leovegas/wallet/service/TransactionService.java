package se.leovegas.wallet.service;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.leovegas.wallet.exception.LowBalanceException;
import se.leovegas.wallet.model.Player;
import se.leovegas.wallet.model.Transaction;
import se.leovegas.wallet.repository.TransactionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    public long getBalance(long playerId) throws NotFoundException {
        List<Transaction> transactions = transactionRepository.findByPlayerId(playerId);
        if (transactions.size() == 0) {
            LOGGER.error("Player Id is not valid: " + playerId);
            throw new NotFoundException("Player Id is not valid: " + playerId);
        }
        Date latestDate = transactions.stream().map(Transaction::getTransDate).max(Date::compareTo)
                .get();
        LOGGER.info("Latest date of the transaction: " + latestDate);
        Transaction transaction = transactions.stream().filter(trans -> trans.getTransDate()
                .equals(latestDate)).collect(Collectors.toList()).get(0);
        LOGGER.info("Balance is: " + transaction.getBalance());
        return transaction.getBalance();
    }

    public void debit(long transactionId, long playerId, long amount) throws Exception {
        Optional<Transaction> optional = transactionRepository.findById(transactionId);
        if (!optional.isEmpty()) {
            LOGGER.error("Operation failed! Transaction Id is not unique: " + transactionId);
            throw new IllegalArgumentException("Operation failed! Transaction Id is not unique: " + transactionId);
        }
        long balance = getBalance(playerId);
        if (balance-amount < 0) {
            LOGGER.error("Operation failed! No sufficient fund to debit, balance: " + balance);
            throw new LowBalanceException("Operation failed! No sufficient fund to debit, balance: " + balance);
        }
        Transaction transaction = makeTransactionObj(transactionId, playerId, amount, true);
        transaction.setBalance(balance-amount);
        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            LOGGER.error("Internal Server Error!");
            throw new Exception("Internal Server Error!");
        }
    }

    public void credit(long transactionId, long playerId, long amount) throws Exception {
        Optional<Transaction> optional = transactionRepository.findById(transactionId);
        if (!optional.isEmpty()) {
            LOGGER.error("Operation failed! Transaction Id is not unique: " + transactionId);
            throw new IllegalArgumentException("Operation failed! Transaction Id is not unique: " + transactionId);
        }
        Transaction transaction = makeTransactionObj(transactionId, playerId, amount, false);
        transaction.setBalance(getBalance(playerId)+amount);
        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            LOGGER.error("Internal Server Error!");
            throw new Exception("Internal Server Error!");
        }
    }

    public List<Transaction> getHistory(long playerId) throws NotFoundException {
        List<Transaction> transactions = transactionRepository.findByPlayerId(playerId);
        if (transactions.size() == 0) {
            LOGGER.error("No transaction was found for id: " + playerId);
            throw new NotFoundException("No transaction was found for id: " + playerId);
        }
        LOGGER.info("Transactions fetched");
        return transactions;
    }

    private Transaction makeTransactionObj(long transactionId, long playerId, long amount, boolean debit) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setTransDate(getTransDateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                .format(new Date())));
        if (debit) {
            transaction.setAmount(-1 * amount);
            transaction.setDescription("Removing " + amount);
        } else {
            transaction.setAmount(amount);
            transaction.setDescription("Adding " + amount);
        }
        transaction.setPlayer(new Player(playerId));
        return transaction;
    }

    private static Date getTransDateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
