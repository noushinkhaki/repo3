package se.leovegas.wallet.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.leovegas.wallet.exception.LowBalanceException;
import se.leovegas.wallet.model.Transaction;
import se.leovegas.wallet.service.TransactionService;

import java.util.List;

@RestController(value = "/wallet")
@Api(value = "Wallet", description = "Credit/Debit operations, Get Balance and History of Transactions")
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/getBalance")
    @ApiOperation(value = "Getting balance")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<String> getBalance(@RequestParam("playerId") long playerId) {
        long balance = 0;
        try {
            balance = transactionService.getBalance(playerId);
            LOGGER.info("Balance is: " + balance);
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(String.valueOf(balance), HttpStatus.OK);
    }

    @PostMapping(value = "/debit")
    @ApiOperation(value = "Removing fund")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    public ResponseEntity<String> debit(@RequestParam("transactionId") long transactionId,
                                        @RequestParam("playerId") long playerId,
                                        @RequestParam("amount") long amount) {
        try {
            transactionService.debit(transactionId, playerId, amount);
        } catch (IllegalArgumentException | LowBalanceException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Removing fund was done successfully!", HttpStatus.OK);
    }

    @PostMapping(value = "/credit")
    @ApiOperation(value = "Adding fund")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    public ResponseEntity<String> credit(@RequestParam("transactionId") long transactionId,
                                         @RequestParam("playerId") long playerId,
                                         @RequestParam("amount") long amount) {
        try {
            transactionService.credit(transactionId, playerId, amount);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Adding fund was done successfully!", HttpStatus.OK);
    }

    @GetMapping(value = "/getHistory")
    @ApiOperation(value = "Getting history of transactions")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<Object> getHistory(@RequestParam("playerId") long playerId) {
        List<Transaction> transactions;
        try {
            transactions = transactionService.getHistory(playerId);
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
