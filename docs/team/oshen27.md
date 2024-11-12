# Shen Jiaming - Project Portfolio Page

## Project Overview: uNivUSaver

uNivUSaver is a CLI-based software designed to help students develop better money management habits, aiming to prevent users from running out of money before the end of the month. It is written in Java.

### Summary of Contributions

#### Code contributed
* **Code contributed**: [RepoSense link](https://nus-cs2113-ay2425s1.github.io/tp-dashboard/?search=Oshen27&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2024-09-20)

#### Enhancements implemented
* **New Feature**: Added `AddIncomeCommand` to allow users to add income records.
    * **What it does**: Enables users to add income details such as description, amount, and date, which are stored in the transaction list.
    * **Highlights**: This feature provides essential income tracking, which improves the budget management capabilities of the application.

* **New Feature**: Added `AddExpenseCommand` to allow users to track expenses.
    * **What it does**: Enables users to add expense entries with details like description, amount, date, and category.
    * **Highlights**: Expense tracking with categorization allows users to analyze their spending in different areas, giving a more comprehensive view of their financial habits.

* **New Feature**: Developed `AddBudgetCommand` to set a monthly budget.
    * **What it does**: Allows users to set a budget for a specific month, establishing a spending limit to track against.
    * **Highlights**: Ensures budgets can only be set for the current or future months. This feature integrates with `BudgetTracker` for a comprehensive budgeting experience.

* **New Feature**: Developed `DeleteBudgetCommand` to remove budgets.
    * **What it does**: Allows users to delete a budget for a specified month.
    * **Highlights**: Offers users flexibility in managing their budgets, ensuring they can update their budget tracking as their financial circumstances change.

* **New Feature**: Created `ViewBudgetCommand` to track budget progress or view all budgets.
    * **What it does**: Displays either a list of all budgets or the progress of a specific month's budget. Users can track their spending relative to their budget for any current or past month.
    * **Highlights**: This feature provides a high-level overview of monthly spending, indicating if the user is within their budget or overspending.

* **BudgetTracker Class**: Implemented a `BudgetTracker` class to manage monthly budgets.
  * **What it does**: Acts as the core budgeting component, providing methods to add, view, and delete budgets for specific months, and track budget progress.
  * **Highlights**: Validates inputs and ensures data consistency, supporting a robust budget management system. Integrated seamlessly with other budget-related commands for streamlined functionality.

* **New Feature**: Developed `ViewTotalCommand` to calculate the net total of income minus expenses.
    * **What it does**: Displays the user’s total income and total expenses, along with the net balance, allowing for an overview of budget standing.

* **Enhancements to existing features**:
  * Refactored existing command handling to improve code quality, ensuring adherence to the Single Responsibility Principle (SRP) for easier future development.
  * Integrated transaction tracking with the `BudgetTracker` class, enabling users to set and monitor monthly budgets.

#### Documentation
* **User Guide**:
  * Documented core features including `add-income`, `add-expense`, `add-budget`, `delete-budget`, `view-budget`, and `view-total` commands. Also provided examples and usage tips to improve user experience.
  * Updated the User Guide for version 2.1
* **Developer Guide**:
  * Added detailed explanations and UML diagrams for `AddIncomeCommand`, `AddExpenseCommand`, and `ViewTotalCommand`.

#### Contributions to team-based tasks

#### Community
* **Review/mentoring contributions**:
    * **Pull Request Reviews**: Provided feedback on team members’ pull requests, particularly for command parsing and budget management features.
    * **Mentorship**: Assisted team members in resolving issues related to command validation and argument parsing.
* **Contributions beyond the project team**:
  * **Forum responses**: Evidence of helping others e.g. responses you posted in our forum, bugs you reported in other team's products
  * **Helpful posts**: Evidence of technical leadership e.g. sharing useful information in the forum
