package seedu.utils;

import seedu.exceptions.InvalidAmountFormatException;
import seedu.message.ErrorMessages;

/**
 * Utility class for validating and parsing monetary amounts.
 */
public class AmountUtils {

    private static final double MAX_AMOUNT = 1000000000;

    /**
     * Parses the amount from a string to a double and provides detailed error messages.
     *
     * @param amountString The amount in string format to parse.
     * @return The parsed amount as a double.
     * @throws InvalidAmountFormatException if the amount is invalid.
     */
    public static double parseAmount(String amountString) throws InvalidAmountFormatException {
        double amount;

        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            throw new InvalidAmountFormatException(ErrorMessages.INVALID_AMOUNT_FORMAT + amountString);
        }

        if (amount < 0) {
            throw new InvalidAmountFormatException(ErrorMessages.NEGATIVE_AMOUNT + ": " + amountString);
        }

        if (amount == 0) {
            throw new InvalidAmountFormatException("Amount cannot be zero.");
        }

        if (amount > MAX_AMOUNT) {
            throw new InvalidAmountFormatException("Amount exceeds maximum limit of " + MAX_AMOUNT);
        }

        return amount;
    }
}
