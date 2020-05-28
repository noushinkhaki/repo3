package se.leovegas.wallet;

import se.leovegas.wallet.model.Player;
import se.leovegas.wallet.model.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataProvider {

    public static Player getPlayer(String firstName, String lastName, String birthDate, String phoneNumber,
                                   String address) {
        Player player = new Player();
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setBirthDate(getBirthDateFormat(birthDate));
        player.setPhoneNumber(phoneNumber);
        player.setAddress(address);
        return player;
    }

    private static Date getBirthDateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Transaction> getTransactions1(Player player) {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setTransDate(getTransDateFormat("15-04-2019 10:20:56"));
        transaction1.setAmount(1000);
        transaction1.setDescription("Award from Golden Globe");
        transaction1.setBalance(1000);
        transaction1.setPlayer(player);
        transactions.add(transaction1);
        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setTransDate(getTransDateFormat("15-04-2020 10:20:56"));
        transaction2.setAmount(1500);
        transaction2.setDescription("Gift");
        transaction2.setBalance(2500);
        transaction2.setPlayer(player);
        transactions.add(transaction2);
        return transactions;
    }

    public static List<Transaction> getTransactions2(Player player) {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId(3L);
        transaction1.setTransDate(getTransDateFormat("18-03-2018 11:20:50"));
        transaction1.setAmount(1500);
        transaction1.setDescription("Saving");
        transaction1.setBalance(1500);
        transaction1.setPlayer(player);
        transactions.add(transaction1);
        Transaction transaction2 = new Transaction();
        transaction2.setId(4L);
        transaction2.setTransDate(getTransDateFormat("21-02-2019 10:21:09"));
        transaction2.setAmount(-500);
        transaction2.setDescription("Bill");
        transaction2.setBalance(1000);
        transaction2.setPlayer(player);
        transactions.add(transaction2);
        return transactions;
    }

    public static Transaction makeMockTransaction(long transactionId, long playerId, long amount, boolean debit) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        if (debit) {
            transaction.setAmount(-1*amount);
            transaction.setDescription("Removing " + amount);
        } else {
            transaction.setAmount(amount);
            transaction.setDescription("Adding " + amount);
        }
        transaction.setPlayer(new Player(playerId));
        transaction.setTransDate(getTransDateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                .format(new Date())));
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
