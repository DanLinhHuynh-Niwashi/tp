# uNivUSaver User Guide
## Table of Contents
1. [Introduction](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#introduction)
2. [Quick Start](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#quick-start)
3. [Features](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#features)
   - [View command list: `help`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#view-command-list-help)
   - [Adding](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#adding)
     - [Add an expense: `add-expense`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#add-an-expense-add-expense)
     - [Add an income: `add-income`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#add-an-income-add-income)
     - [Add a category: `add-category`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#add-a-category-add-category)
     - [Add a budget: `add-budget`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#add-a-budget-add-budget)
   - [Deleting](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#deleting)
     - [Delete an expense/income: `delete-transaction`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#delete-an-expenseincome-delete-transaction)
     - [Delete a category: `delete-category`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#delete-a-category-delete-category)
     - [Delete a budget: `delete-budget`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#delete-a-budget-delete-budget)
   - [Updating](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#updating)
     - [Update an expense category: `categorize`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#update-an-expense-category-categorize)
   - [Viewing list](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#viewing-list)
     - [List all categories: `view-category`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#list-all-categories-view-category)
     - [View expenses: `view-expense`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#view-expenses-view-expense)
     - [View income: `view-income`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#view-income-view-income)
     - [View transaction history: `history`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#view-transaction-history-history)
     - [View budget: `view-budget`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#viewing-of-budget-view-budget)
   - [View total amount in account: `view-total`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#view-total-amount-in-account-view-total)
   - [Searching: `search`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#searching-search)
   - [Leave the app: `bye`](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#leave-the-app-bye)
4. [JSON Editing](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#json-editing)
5. [FAQs](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#faqs)
6. [Command Summary](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#command-summary)

---
## Introduction
uNivUSaver is a CLI-based software that helps students to develop a better habit of managing money, which will help you avoid using up your money before the end of month

## Quick Start
1. Ensure that you have Java 17 or above installed.
2. Download the latest version of `uNivUSaver` from [here](https://github.com/AY2425S1-CS2113-W10-4/tp/releases).
3. Copy the file to the folder you want the program to stay in.
4. Open a command terminal, run the following:
   ```
   cd Path/to/directory # Change directory into the folder you put the jar file in
   java -jar uNivUSaver.jar # Start the program.
   ```
5. Type `help` to view the command list and syntax.

## Features
> **&#9432;** **NOTES ON THE COMMAND FORMAT:**
>- Words in UPPER_CASE are the parameters to be supplied by the user.
>- Parameters in [] are optional (e.g. `[f/ DATE]`)
>- The keywords must be followed by **at least one space** before adding the associated parameter (e.g. `d/ DATE`)
>- Parameters can be in any order, except for the parameters without keyword (e.g. `DESCRIPTION`)
>- Extraneous parameters for commands that do not take in parameters (such as help, bye) will be ignored.
>- Empty commands will be ignored.
>- The date and time format for input is `yyyy-MM-dd [hhMM]`. Time is optional, the system will automatically take '0000' or '2359' for time.
>- The month format for input is `yyyy-MM-dd`
>- Description and category names cannot exceed 40 characters.
>- Please enter one command at a time
>- Expceted output: Note that the output may vary depending on your current transaction list, category list and current date

> **&#9432;** **NOTES ON THE TRANSACTION LIST:**
>- The transaction list will be sorted in time order.
>- Any updating action (e.g. deleting, re-categorizing...) will be performed on the whole transaction list, not the sole expense/ income list
>- The upper limit for each transaction is 1000000000 (1 billion)

> **&#9432;** **NOTES ON THE BUDGET:**
>- The upper limit for each budget is 1000000000 (1 billion)

> **&#9432;** **NOTES ON SAVE AND LOAD FUNCTIONALITY (FOR ADVANCED USERS)**
>- The Save & Load Functionality is implemented by serialising TransactionList, CategoryList and budgets and save as JSON files in the local storage
>- It is not advisable to edit JSON files directly as a corrupted JSON file may lead to unwanted effects
>- JSON Error Handling:
    >- Corruption within objects (eg. invalid key or value, incorrect syntax): The programme will skip these objects while loading the JSON file
>- Corruption of JSON file structure: The content will be cleared and reinitialised, previously stored objects will be lost
>- [JSON Guide](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html#json-editing)

### View command list: `help`
- View all available commands in the application.
- **Format:** `help`

---
### Adding
#### Add an expense: `add-expense`
- Add an amount of expense into the history.
- **Format:** `add-expense [DESCRIPTION] a/ AMOUNT [d/ DATE] [c/ CATEGORY]`
- **Tips:**
    - If the category is not entered, the program will prompt you to enter a category or to leave it empty
    - You may either fill in a category already in the category list or create a new category if it is not present in the list
    - If the date is not entered, the system will take the current day.
- **Examples:** Note that the follow-up question and the added notification will be printed on a line
  ```
  add-expense ChiCha a/ 17 d/ 2024-07-09 c/ FnB
  add-expense Amusement park a/ 52 d/ 2024-08-09
  add-expense a/ 6 d/ 2024-09-09 c/ FnB
  
  Example Output:
  add-expense a/ 6 d/ 2024-09-09 c/ FnB
  Category 'FnB' does not exist. Current categories:
  Category: Food
  Category: Entertainment
  Category: Transport
  Category: Utilities
  Category: Others
  Type 'yes' to create a new category, or enter an existing category name.
  Type 'no' to skip: 
  yes
  New category 'FnB' created.
  -------------------------------------
  Transaction added:
  Expense [amount=6.0, description=, date=2024-09-09 0000, category=FnB]
  Your current transaction list:
  1. Expense [amount=6.0, description=, date=2024-09-09 0000, category=FnB]
    -------------------------------------
  ```


#### Add an income: `add-income`
- Add an amount of income into the history.
- **Format:** `add-income [DESCRIPTION] a/ AMOUNT [d/ DATE]`
- **Tip:**
    - If the date is not entered, the system will take the current day.
- **Examples:**
  ```
  add-income Monthly allowance a/ 300 d/ 2024-09-19 1100
  add-income a/ 52 d/ 2024-09-19 1100
  
  Example Output:
    add-income a/ 52 d/ 2024-09-19 1100
    -------------------------------------
    Transaction added: Income [amount=52.0, description=, date=2024-09-19 1100]
    Your current transaction list: 
    1. Income [amount=52.0, description=, date=2024-09-19 1100]
    -------------------------------------
  ```


#### Add a category: `add-category`
- Add a category into the category list.
- **Format:** `add-category CATEGORY_NAME`
- **Tip:**
  - Category name must be unique. We do small checks on the duplicated categories, but can not deal with all the duplication cases (e.g. The app won't find "Wa" and "W a" is duplicated category)
- **Examples:**
  ```
  add-category FnB
  add-category Laundry
  
  Example Output: 
    add-category Laundry
    -------------------------------------
    Category added: Laundry
    -------------------------------------
  ```

#### Add a budget: `add-budget`
- Sets a monthly budget to track spending against.
- **Format:** `add-budget a/ AMOUNT m/ MONTH`
- **Tips:**
    - The month should be specified in the format `yyyy-MM` (e.g., `2024-11`).
    - The amount represents the budget limit for that month.
    - Budget can only be set for current or future months
- **Examples:**
  ```
  add-budget a/ 1000 m/ 2024-11 
  add-budget a/ 200 m/ 2025-02
  
  Example output:
  add-budget a/ 200 m/ 2025-02
  -------------------------------------
  Budget set: 200.0
  -------------------------------------
  ```

---
### Deleting
#### Delete an expense/income: `delete-transaction`
- Delete an expense or income from the history.
- **Format:** `delete-transaction i/ INDEX`
- **Example:**
  ```
  delete-transaction i/ 1
  
  Example output:
  delete-transaction i/ 1
  -------------------------------------
  Transaction deleted: 1. Income [amount=10.0, description=, date=2024-11-12 1051]
  Your current transaction list: 
  -------------------------------------
  ```

#### Delete a category: `delete-category`
- Delete a category from the category list.
- **Format:** `delete-category CATEGORY_NAME`
- **Tip:**
    - If there were some expenses categorized in the category, you will be prompted to choose a new category.
- **Example:**
  ```
  delete-category FnB
  
  Example output: 
  delete-category FnB
  -------------------------------------
  Category deleted: FnB
  -------------------------------------
  ```

#### Delete a budget: `delete-budget`
- Delete a budget from the budget list.
- **Format:** `delete-budget m/ MONTH`
- **Tips:**
    - The month should be specified in the format `yyyy-MM` (e.g., `2024-11`).
- **Example:**
  ```
  delete-budget m/ 2025-02
  
  Example output:
  delete-budget m/ 2025-02
  -------------------------------------
  Budget deleted: Budget for 2025-02 has been deleted.
  -------------------------------------
  ```

---
### Updating

#### Update an expense category: `categorize`
- Update the category field of an expense.
- **Format:** `categorize i/ INDEX c/ CATEGORY`
- - **Tip:**
- The category must be in the category list, and the transaction must be an expense.
- **Example:** Note that the output will be printed on a line
  ```
  categorize i/ 1 c/ Food
  
  Example output:
  categorize i/ 1 c/ Food
  -------------------------------------
  Transaction updated: 
  Expense [amount=1000.0, description=, date=2024-11-12 1058, category=Food]
  -------------------------------------
  ```
  
---
### Viewing list

#### List all categories: `view-category`
- View the category list.
- **Format:** `view-category`
- **Examples:**
  ```
  view-category 
  
  Example output:
  view-category
  ------------------------------------- 
  1. Food 
  2. Entertainment 
  3. Transport 
  4. Utilities 
  5. Others 
  -------------------------------------
  ```

#### View expenses: `view-expense`
- View the expenses with optional start/end time and category. If left blank, full history will be shown.
- **Format:** `view-expense [c/ CATEGORY] [f/ DATE] [t/ DATE]`
- **Examples:**
  ```
  view-expense c/ Food
  view-expense f/ 2024-09-16 t/ 2024-09-19
  
  Example output:
  view-expense c/ Food
  ------------------------------------- 
  1. Expense [amount=1000.0, description=, date=2024-11-12 1058, category=Food] 
  2. Expense [amount=200.0, description=fruits, date=2024-11-12 1102, category=Food] 
  3. Expense [amount=50.0, description=fruit pie, date=2024-11-12 1102, category=Food] 
  -------------------------------------
  ```

#### View income: `view-income`
- View the income in an optional start and end time. If start and end time are left blank, full history will be shown.
- **Format:** `view-income [f/ DATE] [t/ DATE]`
- **Example:** 
  ```
  view-income
  view-income f/ 2024-09-16 t/ 2024-09-19
  view-income f/ 2024-10-11
  
  Example output:
  view-income f/ 2024-10-11
  -------------------------------------
  4. Income [amount=200.0, description=, date=2024-11-12 1110] 
  -------------------------------------
  ```

#### Viewing of budget: `view-budget`
- View the list of budgets, or view the budget progress of a certain month
- **Format:** `view-budget [m/ MONTH]`
- **Tips:**
    - The month should be specified in the format `yyyy-MM` (e.g., `2024-11`).
    - Tracking of progress is only available for current or past months.
- **Example:** Note that the output for `view-budget m/ 2024-11` will be printed on a line
  ```
  view-budget 
  view-budget m/ 2024-11
  
  Example output:
  view-budget 
  -------------------------------------
  1. Budget for 2024-11: $120.00
  -------------------------------------
  
  view-budget m/ 2024-11
  ------------------------------------- 
  Warning! You've already exceeded your budget for 2024-11.
  Spent: $1000.00, Budget: $120.00. 
  -------------------------------------
  ```
  
#### View transaction history: `history`
- View transaction history with an optional start and end time. If left blank, full history will be shown.
- **Format:** `history [f/ DATE] [t/ DATE]`
- **Examples:**
  ```
  history f/ 2024-10-11 t/ 2024-12-01
  history
  
  Example output:
  history f/ 2024-10-11 t/ 2024-12-01
  -------------------------------------
  1. Expense [amount=1000.0, description=, date=2024-11-12 1058, category=Food] 
  2. Expense [amount=200.0, description=fruits, date=2024-11-12 1102, category=Food] 
  3. Expense [amount=50.0, description=fruit pie, date=2024-11-12 1102, category=Food] 
  4. Income [amount=200.0, description=, date=2024-11-12 1110] 
  -------------------------------------
  ```

---
### View total amount in account: `view-total`
- View your total income, total expenses and your net total in your account.
- **Format:** `view-total`
- **Example:**
  ```
  view-total
  
  Example output:
  view-total
  ------------------------------------- 
  Total Income: $0.00 
  Total Expenses: $1250.00 
  Net Total: $-1250.00 
  -------------------------------------
  ```

### Searching: `search`
- Search for transaction or transactions in the transactionList using one or multiple keywords. The command counts partial match, with case ignore.
- **Format:** `search k/ [keyword_1] [keyword_2] ... [keyword_n]`
- **Example:**
  ```
  search k/ fruit
  search k/ school fee
  
  Example output:
  search k/ fruit
  -------------------------------------
  Expense [amount=200.0, description=fruits, date=2024-11-12 1102, category=Food]
  Expense [amount=50.0, description=fruit pie, date=2024-11-12 1102, category=Food] 
  -------------------------------------
  ```

### Leave the app: `bye`
- Peacefully leave the application
- **Format:** `bye`


## JSON Editing ##
- JSON basic syntax:
    - data is in key/value pairs ```"key":"value"```
    - Data is separated by commas
    - Curly braces hold objects ```{"key":"value"}```
    - Square brackets hold arrays ```[{"key1":"value1"},{"key2":"value2"}]```
- TransactionList and CategoryList in the programme are stored as Array of Transaction object and Category object respectively, Budgets are stored as key/value pairs
- TransactionList Syntax:
```
[
    {
      "type": "expense",
      "category": {
         "name": ""
       },
      "amount": 52.0,
      "description": "Amusement park",
      "dateTimeString": "2024-08-09 0000"
    },
    {
    "type": "income",
    "amount": 300.0,
    "description": "Monthly allowance",
    "dateTimeString": "2024-09-19 1100"
    }
]
```
- CategoryList Syntax:
```
[
  {
    "name": "Food"
  },
  {
    "name": "Transport"
  },
  {
    "name": "Entertainment"
  },
  {
    "name": "FnB"
  }
]
```
- Budgets Syntax:
```
{
  "2024-11": 100.0,
  "2024-12": 200.0
}
```

## FAQs
**Q:** Why some of my data from my data file is missing?

**A:** Note that the app will do a brief scan of the data file and only allow valid data to be added into the list

**Q:** What is the difference between `view-total` and `view-budget`?

**A:** `view-total` shows the total amount of income, expenses, and the net total (income minus expenses) in the account overall.`view-budget` displays the progress toward a specific monthly budget, letting you know if you are within or exceeding the limit for that month.

**Q:** Why do I not see the newly added transaction at the end of the list?

**A:** Note that the list will be arranged in time order, with the end of the list presenting the latest transaction. You may see you transaction added in somewhere else in the list.


## Command Summary

### Command List

| **Command Word**     |                          **Syntax**                           |                     **Example**                     |
|----------------------|:-------------------------------------------------------------:|:---------------------------------------------------:|
| `help`               |                            `help`                             |                       `help`                        |
| `add-expense`        | `add-expense [DESCRIPTION] a/ AMOUNT [d/ DATE] [c/ CATEGORY]` |    `add-expense Food a/ 17 d/ 2024-07-09 c/ FnB`    |
| `add-income`         |        `add-income [DESCRIPTION] a/ AMOUNT [d/ DATE]`         | `add-income Monthly allowance a/ 300 d/ 2024-09-19` |
| `add-category`       |                    `add-category CATEGORY_NAME`               |                `add-category FnB`                   |
| `add-budget`         |                `add-budget a/ AMOUNT m/ MONTH`                |           `add-budget a/ 1000 m/ 2024-12`           |
| `delete-transaction` |                 `delete-transaction i/ INDEX`                 |              `delete-transaction i/ 1`              |
| `delete-category`    |                `delete-category CATEGORY_NAME`                |                `delete-category FnB`                |
| `delete-budget`      |                   `delete-budget m/ MONTH`                    |             `delete-budget m/ 2024-11`              |
| `categorize`         |               `categorize i/ INDEX c/ CATEGORY`               |              `categorize i/ 1 c/ Food`              |
| `view-category`      |                        `view-category`                        |                   `view-category`                   |
| `view-expense`       |       `view-expense [c/ CATEGORY] [f/ DATE] [t/ DATE]`        |   `view-expense c/ Food f/ 2024-09-16 t/ 2024-09-19`|
| `view-income`        |               `view-income [f/ DATE] [t/ DATE]`               |      `view-income f/ 2024-09-16 t/ 2024-09-19`      |
| `view-budget`        |                   `view-budget [m/ MONTH]`                    |       `view-budget` or `view-budget m/ 2024-11`     |
| `history`            |                 `history [f/ DATE] [t/ DATE]`                 |        `history f/ 2024-09-16 t/ 2024-09-19`        |
| `view-total`         |                         `view-total`                          |                    `view-total`                     |
| `search`             |      `search k/ [keyword_1] [keyword_2] ... [keyword_n]`      |             `search k/ school fee`                  |
| `bye`                |                             `bye`                             |                        `bye`                        |
           
