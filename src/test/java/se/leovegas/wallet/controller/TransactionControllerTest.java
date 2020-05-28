package se.leovegas.wallet.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.leovegas.wallet.TestDataProvider;
import se.leovegas.wallet.model.Player;
import se.leovegas.wallet.model.Transaction;
import se.leovegas.wallet.service.TransactionService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Before
    public void init() {
        mockMvc = standaloneSetup(transactionController).build();
    }

    @Test
    public void testGetBalance() throws Exception {
        when(transactionService.getBalance(1L)).thenReturn(2500L);
        mockMvc.perform(get("/getBalance").param("playerId", "1")).andExpect(status().isOk());
    }

    @Test
    public void testCredit() throws Exception {
        mockMvc.perform(post("/credit").param("transactionId", "10")
                .param("playerId", "1")
                .param("amount", "5000")).andExpect(status().isOk());
    }

    @Test
    public void testDebit() throws Exception {
        mockMvc.perform(post("/debit").param("transactionId", "10")
                .param("playerId", "1")
                .param("amount", "500")).andExpect(status().isOk());
    }

    @Test
    public void testGetHistory() throws Exception {
        Player mockPlayer = new Player(1L);
        List<Transaction> mockTransactions = TestDataProvider.getTransactions1(mockPlayer);
        when(transactionService.getHistory(1L)).thenReturn(mockTransactions);
        mockMvc.perform(get("/getHistory").param("playerId", "1")).andExpect(status().isOk());
    }
}
