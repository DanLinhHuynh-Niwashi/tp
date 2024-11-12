package seedu.message;

public class BudgetMessages {

    // Messages for the current month's progress
    public static final String WARNING_EXCEEDED_BUDGET =
            "Warning! You've already exceeded your budget for %s. Spent: $%.2f, Budget: $%.2f.";
    public static final String GREAT_JOB_ON_TRACK =
            "Great job! You're on track. Spent so far: $%.2f, " +
            "Projected spending for this month: $%.2f (within the budget of $%.2f).";
    public static final String CAUTION_PROJECTED_EXCEED =
            "Caution! You're on track to exceed the budget. Spent so far: $%.2f, " +
            "Projected spending for this month: $%.2f, Budget: $%.2f.";

    // Messages for past month's progress
    public static final String BUDGET_EXCEEDED_PAST =
            "Budget Exceeded! You spent $%.2f out of your budget of $%.2f for %s." +
            " Consider reviewing your expenses for better control in future months.";
    public static final String WELL_DONE_WITHIN_BUDGET =
            "Well done! You stayed within your budget for %s. Spent: $%.2f," +
            " Budget: $%.2f. Keep up the good work!";
}
