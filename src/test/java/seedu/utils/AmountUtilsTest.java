package seedu.utils;

import org.junit.jupiter.api.Test;
import seedu.exceptions.InvalidAmountFormatException;
import seedu.message.ErrorMessages;
import static org.junit.jupiter.api.Assertions.assertEquals;


class AmountUtilsTest {

    @Test
    void parseAmount_invalidAmountFormat_throwsException() {
        try {
            AmountUtils.parseAmount("test");
        } catch (InvalidAmountFormatException e) {
            assertEquals(ErrorMessages.INVALID_AMOUNT_FORMAT + "test", e.getMessage());
        }
    }

    @Test
    void parseAmount_negativeAmount_throwsException() {
        try {
            AmountUtils.parseAmount("-1");
        } catch (InvalidAmountFormatException e) {
            assertEquals(ErrorMessages.NEGATIVE_AMOUNT
                            + ": " + "-1", e.getMessage());
        }
    }

    @Test
    void parseAmount_validAmount_success() throws InvalidAmountFormatException {
        assertEquals(AmountUtils.parseAmount("10"), 10);
    }
}
