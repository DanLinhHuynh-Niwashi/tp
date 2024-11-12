package seedu.utils;

import seedu.exceptions.InvalidDateFormatException;
import seedu.message.ErrorMessages;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.YearMonth;

/**
 * Utility class for handling date-time parsing.
 */
public class DateTimeUtils {
    // DateTimeFormatter for reading date-time strings in the specified format.
    private static final DateTimeFormatter DATETIME_READ_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter YEARMONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    // DateTimeFormatter for printing date-time strings in a user-friendly format.
    private static final DateTimeFormatter DATETIME_PRINT_FORMAT
            = DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd hh.mm a");

    /**
     * Parses a date-time string into a LocalDateTime object.
     *
     * @param datetime The date-time string to be parsed.
     * @return A LocalDateTime representing the parsed date and time.
     * @throws RuntimeException If the date-time format is invalid.
     */
    public static LocalDateTime parseDateTime(String datetime) throws InvalidDateFormatException {
        String[] datetimeParts = datetime.trim().split(" ", 2);

        // If only the date is provided, append time as "2359" (11:59 PM)
        if (datetimeParts.length == 1) {
            datetime += " 0000";
        }

        LocalDateTime result;
        try {
            result = LocalDateTime.parse(datetime, DATETIME_READ_FORMAT); // Parse the date-time string
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException(ErrorMessages.MESSAGE_INVALID_DATE_FORMAT);
        }

        return result;
    }

    /**
     * Parses a year-month string into a YearMonth object.
     *
     * @param yearMonthStr The year-month string to be parsed.
     * @return A YearMonth representing the parsed year and month.
     * @throws InvalidDateFormatException If the year-month format is invalid.
     */
    public static YearMonth parseYearMonth(String yearMonthStr) throws InvalidDateFormatException {
        try {
            return YearMonth.parse(yearMonthStr, YEARMONTH_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException(ErrorMessages.MESSAGE_INVALID_YEAR_MONTH_FORMAT);
        }
    }

    /**
     * Formats a LocalDateTime object into a user-friendly string.
     *
     * @param dateTime The LocalDateTime to format.
     * @return A formatted string representing the date and time.
     */
    public static String getDateTimeString(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_PRINT_FORMAT); // Format and return the date-time string
    }

    /**
     * Compares two LocalDateTime objects.
     *
     * @param dateTime1 The LocalDateTime to compare.
     * @param dateTime2 The LocalDateTime to compare.
     * @return false if dateTime1 is after dateTime2, true otherwise.
     */
    public static boolean compareDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }
}
