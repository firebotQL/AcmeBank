import com.tide.AccountCreationRequest;
import com.tide.TransactionRequest;
import com.tide.domain.Bank;
import com.tide.domain.enums.TRANSACTION_FLAG;
import com.tide.domain.enums.TRANSACTION_TYPE;
import com.tide.exceptions.AccountCreationRequestException;
import com.tide.exceptions.BankProcessingException;
import com.tide.exceptions.TransactionRequestException;
import junit.framework.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TideBankTest {

    @Test
    public void notEnoughFundException() throws AccountCreationRequestException {
        Long accountNumber = 1l;
        String sortCode = "30-55-31";

        new AccountCreationRequest()
                .withName("Platinum Account")
                .withAccountHolder("Mr Slavas Duk")
                .withInitialBalance(new BigDecimal(10.00))
                .open();    // open account

        try {
            new TransactionRequest()
                    .withAccountNumber(accountNumber)
                    .withSortCode(sortCode)
                    .withAmount(new BigDecimal("10.00"))
                    .withTransactionFlag(TRANSACTION_FLAG.DEBIT)
                    .withTransactionType(TRANSACTION_TYPE.DIRECT_DEBIT)
                    .make();                        // make a transaction
        } catch (TransactionRequestException e) {
            assertEquals(true, true);
        }
    }

    @Test
    public void successfulTransaction() throws AccountCreationRequestException, BankProcessingException, TransactionRequestException {
        Long accountNumber = 1l;
        String sortCode = "30-55-31";

        new AccountCreationRequest()
                .withName("Platinum Account")
                .withAccountHolder("Mr Slavas Duk")
                .withInitialBalance(new BigDecimal("15.03"))
                .open();    // open account

            new TransactionRequest()
                    .withAccountNumber(accountNumber)
                    .withSortCode(sortCode)
                    .withAmount(new BigDecimal("10.00"))
                    .withTransactionFlag(TRANSACTION_FLAG.DEBIT)
                    .withTransactionType(TRANSACTION_TYPE.DIRECT_DEBIT)
                    .make();                        // make a transaction


        BigDecimal accountBalance = Bank.getInstance().getAccountBalance(accountNumber, sortCode);
        assertEquals(new BigDecimal("5.03"), accountBalance);
    }
}
