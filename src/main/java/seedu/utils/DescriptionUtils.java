package seedu.utils;

import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.message.ErrorMessages;

public class DescriptionUtils {
    public static boolean isValidDescription(String description) {
        return (description == null || description.length() <= 50) ;
    }

    public static String parseDescription(String description) throws InvalidDescriptionFormatException {
        if (!isValidDescription(description)) {
            throw new InvalidDescriptionFormatException(ErrorMessages.INVALID_STRING_FORMAT + description);
        }
        return description;
    }
}
