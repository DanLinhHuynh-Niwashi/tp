# Developer Guide

## Table of Contents
1. [Acknowledgements](#acknowledgements)
2. [Notes](#notes)
3. [High-level Architecture](#high-level-architecture)
    - [User interaction](#user-interaction)
    - [UI layer](#ui-layer)
    - [Command handling layer](#command-handling-layer)
    - [Data layer](#data-layer)
    - [Persistent storage (Editable .json files)](#persistent-storage-editable-json-files)
    - [General system flow](#general-system-flow)
4. [Design & Implementation](#design--implementation)
    - [Category](#category)
    - [Transaction - Expense - Income](#transactionexpenseincome)
    - [TransactionList](#transactionlist)
    - [Command](#command)
    - [AddIncomeCommand](#addincomecommand)
    - [ViewHistoryCommand](#viewhistorycommand)
    - [Command Parser](#command-parser)
5. [Product Scope](#product-scope)
    - [Target User Profile](#target-user-profile)
    - [Value Proposition](#value-proposition)
5. [User Stories](#user-stories)
7. [Non-Functional Requirements](#non-functional-requirements)
8. [Glossary](#glossary)
9. [Instructions for Manual Testing](#instructions-for-manual-testing)

## Acknowledgements
- The `Parser` is adapted from [Dan Linh's iP](https://github.com/DanLinhHuynh-Niwashi/ip/tree/master/src/main/java/niwa/parser) code, with changes to get on well with the current project 
- The `Storage` uses external library Gson by Google from [Gson](https://github.com/google/gson.git), with changes to get on well with the current project

## Notes
- The Developer guide doesn't contain all the classes implemented in the final product, however, it contains the basic and important components, and some demonstration classes for providing an insight of how the product works.

## High-level Architecture
![Architecture](./diagrams/highArchitecture/high_level_architecture.png)

### User interaction:

- User - UI:
    - The user interacts with the **UI** component via the CLI. This is where commands are input by the user.
    - The **UI** displays the results of the commands processed by the system to the user.

### UI layer:

- Main - UI:
    - The **Main** class calls the **UI** layer to get user input and display messages. It's a central point that manages the flow of execution.
    - **UI** receives the user input and then sends it to the **Main** class for further processing.

### Command handling layer:
- **Main - Parser**:
    - The **Main** class sends the user input (command) to the **Parser**, which is responsible for parsing the string to get correct command object and extract arguments.

- **Parser - Command**:
    - The **Parser** identifies the appropriate **Command** class based on the input, returning a specific command object (e.g., `AddExpenseCommand`, `ViewTotalCommand`, etc.).

- **Main - Command**:
    - **Main** then invokes the `execute()` method on the appropriate **Command**. The **Command** is responsible for processing the business logic related to the user request.

### Data layer:

- **Command - TransactionList**:
    - **TransactionList** stores and manages all transactions (expenses and incomes). **Commands** interact with **TransactionList** to retrieve or modify transactions as needed (e.g., adding an expense, viewing a transaction).

- **TransactionList - Transaction**:
    - **TransactionList** contains individual **Transaction** objects. Each transaction represents a financial action (e.g., an expense or income), with details such as amount, description, date, or category. TransactionList provides methods to modify the list.

- **Command - CategoryList**:
    - **CategoryList** stores the categories used by transactions. **Commands** may interact with **CategoryList** to retrieve categories or modify them based on user input.

- **CategoryList - Category**:
    - **CategoryList** contains **Category** objects. Each category represents a classification for transactions, such as "Food," "Entertainment," etc. CategoryList provides methods to modify the list.
  
- **Command - BudgetTracker**:
    - **BudgetTracker** handles the budgeting logic, ensuring that the user’s spending is within their defined budget. **Commands** can query **BudgetTracker** to get the current budget status or modify it.

- **Command - Storage**:
    - The **Command** interacts with **Storage** after execution to save data. For example, if a command requires adding a new transaction, the **Command** will use **Storage** to save that transaction to the file system.

- **Main - Storage**:
    - The **Main** class utilizes **Storage** to read the data on initialization.

- **Storage - TransactionList, CategoryList, BudgetTracker**:
    - **Storage** is responsible for reading and saving data to and from persistent storage (files). It manages interactions with the `transactions.json`, `categories.json`, and `budgets.json` files.
    - **Storage** interacts with **TransactionList**, **CategoryList**, and **BudgetTracker** to load and save the relevant data.

### Persistent storage (Editable .json files):

- **transactions.json - Storage**:
    - **transactions.json** is the file where all transaction data is stored. **Storage** interacts with this file to read and save transactions data.

- **budgets.json - Storage**:
    - **budgets.json** is the file where all user's budget data is stored. **Storage** interacts with this file to read and save budgets data.

- **categories.json - Storage**:
    - **categories.json** is the file contains the list of categories used to classify expenses (a subclass of transaction). **Storage** interacts with this file to load and save categories data.

### General system flow
1. **User interaction**:
    - The **User** sends an input request to **UI**, which forwards this input (commandString) to **Main** for processing.

2. **Command parsing**:
    - **Main** splits `commandString` into `commandParts` and then calls **Parser** with the primary command identifier (`commandParts[0]`).
    - **Parser** checks if a matching **Command** is registered. If the command is not found, **Parser** returns `null`. If the command is found, **Parser** returns the appropriate **Command** instance to **Main**.
   
3. **Error handling (if Command is not found)**:
    - If no command matches, **Main** calls **UI** to print an error message.

4. **Executing the command (if found)**:
    - If the command exists, - **Main** then calls **Parser** with the secondary command argument string to extract arguments (`commandParts[1]`), **Parser** returns an `arguments` map.
    - **Main** sets these arguments on the **Command** and then executes the command.
    - The **Command** processes the request and returns a `result` list containing messages generated by the execution.

5. **Displaying the result**:
    - **Main** instructs **UI** to display the result to the **User**, providing feedback about the command execution.
 
    ![Flow](./diagrams/highArchitecture/user_flow.png)

## Design & implementation
### Category
The `Category` class encapsulates the name of a category and provides functionality for equality checks, hash code generation, and string representation. It serves as the foundational representation of a category.

![Category](./diagrams/category/category-class-diagram-2.png)
#### Class responsibilities
1. **Attribute Encapsulation**: Encapsulates the category name to prevent external modification.
2. **Equality Checks**: Implements equality based on the category name, allowing categories with the same name to be considered equal.
3. **String Representation**: Provides a `toString` method for convenient logging and debugging output.

#### Class attributes
1. **name**:String
   - Description:Represents the category name, set as a read-only attribute.

#### Class main Methods
1. **public Category(String name)**
   - **Parameters**:
     - **`name`**: The category name.
   - **Process**: Initializes the name attribute with the specified value.

2. **public String getName()**
   - **Returns**: The `name` of the Category object.
   - **Process**: Provides access to name attribute.

3. **public boolean equals(Object obj)**
   - **Parameters**:
     - **`obj`**: The object to compare for equality.
   - **Returns**: `true` if `obj` is a Category instance with the same name, `false` otherwise.
   - **Process**: Checks if `obj` is a Category instance and compares its name with the current `Category` object’s name.

   ![Category](./diagrams/category/category-class-diagram.png)
4. **public int hashCode()**
   - **Returns**: The hash code based on the name.
   - **Process**: Calculates the hash code for storing `Category` objects in hash-based collections.

5. **public String toString()**
   - **Returns**: The formatted description of the category
   - **Process**: Generates a string representation of the `Category` object, useful for logging and debugging.

### Transaction - Expense - Income
The `Transaction` class is an abstract class to provide the similar behavior for Income and Expense class. It serves as the foundational representation of a transaction.

The `Expense` class stores the data of an expense and gives string presentation function. It serves as the representation of an expense.

The `Income` class stores the data of an income and gives string presentation function. It serves as the representation of an income.

#### Class attributes
1. **description**: String
   - Description: Represents the transaction description.

2. **dateTimeString**: String
   - Description: Represents the date and time for the transaction. As we use Gsom library for parsing JSON data, we store this attribute as a String.

3. **amount**: Double
   - Description: Represents the amount of a transaction.

4. **category**: Category
   - Description: Private attribute for `Expense` class, to show the category of that expense.
   
#### Class main Methods
1. **public LocalDateTime getDate()**
   - **Returns**: The parsed LocalDateTime of the Transaction as date time is primarily stored as a String

2. **public String toString()**
   - **Returns**: The formatted description of the transaction
   - **Process**: Generates a string representation of the `Transaction` object. This method is overridden by inherited classes to get their representation string.

3. **public String getTransactionType()**
   - **Returns**: The type the transaction
   - **Process**: This method is overridden by inherited classes to get their own type string. Used by the Storage to parse specific inherited classes into JSON file
   
    ![Transaction](./diagrams/transactionclass/expense-income-class.png)


### TransactionList
The `TransactionList` class is responsible for storing user transactions of different types. It also provides various
operations that enable user to add, delete, search by (date/ category/ keywords).

![TransactionList](./diagrams/TransactionList/transactionlist-class-diagram.png)
    
#### Class Responsibilities

1. **Storage for transactions**: Keeps an ArrayList of `Transaction` objects.
2. **Amend transactions**: Adding or Deleting `Transaction` objects to or from the ArrayList.
3. **Search Transactions**: Search `Transaction` in the `TransactionList` based on multiple keywords, date range or `category` of `Transaction`.

#### Class attributes
1. **transactions: `ArrayList&lt;Transaction&gt;**
    - Description: A List of `Transaction` objects stored that supports List operations.
2. **InvertedIndex: `Map&lt;String, List&lt;Transaction&gt;&gt;**
    - Description: An inverted index implemented as a map that associates each unique keyword from transaction descriptions with a list of Transaction objects containing that keyword in their descriptions.

#### Class main methods

1. **addTransaction(transaction : Transaction) : void**
    - **Parameters**:
        - `transaction`: The `Transaction` object to be added to the `transactions` list.
    - **Process**:
        - Adds the `transaction` to the `transactions` list.
        - Updates the `invertedIndex` to include the new `transaction` for quick search.
        - Sorts the `transactions` list by date using a custom comparator.

2. **deleteTransaction(index : int) : Transaction**
    - **Parameters**:
        - `index`: The position of the `Transaction` object to be removed.
    - **Process**:
        - Removes the `Transaction` at the specified `index` from `transactions`.
        - Updates the `invertedIndex` to reflect the deletion.
        - Returns the removed `Transaction`.

3. **getTransactions() : ArrayList&lt;Transaction&gt;**
    - **Parameters**: None
    - **Process**:
        - Returns the complete list of `Transaction` objects in `transactions`.

4. **searchTransactionsByKeywords(keywords : List&lt;String&gt;) : List&lt;Transaction&gt;**
    - **Parameters**:
        - `keywords`: A list of keywords to search for within transaction descriptions.
    - **Process**:
        - Looks up each `keyword` in the `invertedIndex` to find matching transactions.
        - Aggregates and counts relevance for each match.
        - Sorts the results by relevance and returns the list of matched transactions.
5. **getExpensesByCategory(category : Category) : List&lt;Transaction&gt;**
    - **Parameters**:
        - `category`: The `Category` to filter expenses by.
    - **Process**:
        - Filters `transactions` to include only `Expense` objects with the specified `category`.
        - Returns the filtered list of expenses.
    ![SearchByCategory](./diagrams/TransactionList/transactionlist-Sequence-SearchByCategoty-diagram.png)

### Command
The `Command` class is an abstract class that provide a common behavior that other commands can share.

#### Class responsibilities
1. **Defining common structure**: It provides common fields that ensure consistency across all concrete command classes that extend Command.
2. **Defining common behavior**: It provides a template for commands by the abstract methods (e.g. `execute()`, etc.), the class enables developers to add new commands by simply extending the base class and implementing the specific behavior for each command.
3. **Defining some reusable methods**: It provides some common methods (e.g. `isArgumentsValid()`, `setArguments()`, `getArguments()`, etc.) across child classes.

#### Class attributes
1. **COMMAND_WORD**: `String`
    - Description: The keyword triggering the command. The inherit classes will override this static variable by their own values.
2. **COMMAND_GUIDE**: `String`
    - Description: Description of the command. The child classes will override this static variable by their own values.
3. **COMMAND_MANDATORY_KEYWORDS**: `String[]`
    - Description: Array storing mandatory argument keywords. The child classes will override this static variable by their own values.
4. **COMMAND_EXTRA_KEYWORDS**: `String[]`
    - Description: Array storing optional argument keywords. The child classes will override this static variable by their own values.
5. **arguments**: `Map<String, String>`
    - Description: A map storing argument strings along with its keyword.

#### Class main methods
1. **execute(): abstract void**
    - **Responsibility**: Act as a template for the child classes.

2. **isArgumentsValid(): boolean**
    - **Returns**: `true` if the `arguments` map contains all the mandatory keywords, `false` otherwise. Can be reused by the child classes.
      
### AddIncomeCommand
The `AddIncomeCommand` class inherits Command class, handles the logic for adding an income transaction to the `TransactionList` by parsing input arguments, creating a new `Income` instance, and updating the transaction list.
The other `AddExpenseCommand` has the similar logic, instead of one more checking step for category.

![AddIncomeCommand](./diagrams/addincomecommand/addincomecommand-class-diagram.png)

#### Class responsibilities

1. **Command parsing and validation**: The class validates required fields (e.g., amount) and parses optional fields (e.g., date).
2. **Transaction creation**: An `Income` transaction is instantiated and initialized with a description, amount, and date.
3. **Transaction storage**: Upon creation, the transaction is saved to the `TransactionList` and persisted using `Storage`.

#### Class attributes
1. **transactions**: `TransactionList`
    - Description: Stores the current list of all transactions.

#### Class main methods

1. **execute()**
    - **Returns**: `List<String>`
    - **Process**:
        - Validates the input arguments.
        - Parses `amount` and `date` fields.
        - Instantiates a new `Income` transaction and adds it to `TransactionList`.
        - Calls `Storage.saveTransaction()` to persist data.

    ![execute](./diagrams/addincomecommand/addincomecommand-class-diagram_001.png)

2. **createTransaction(double amount, String description, String date) : Transaction**
    - **Parameters**:
        - `amount`: Amount for the income.
        - `description`: Description for the income.
        - `date`: Date when the income was received.
    - **Returns**: A new `Income` instance.

### ViewHistoryCommand
The `ViewHistoryCommand` class inherits Command class, handles the logic for viewing the current `TransactionList` by parsing input arguments and display to the user accordingly.
The other `ViewExpenseCommand` and `ViewIncomeCommand` has the similar logic.

#### Class responsibilities

1. **Filtering transaction list**: The command filter the transaction list and find the transaction that meets the criteria
3. **Return filtered string list**: The command return the filtered list of transactions in string format for displaying.

#### Class attributes
1. **transactions**: `TransactionList`
    - Description: Stores the current list of all transactions.

#### Class main methods

1. **execute()**
    - **Returns**: `List<String>`
    - **Process**: As the arguments are optional, the command do a step-by-step filter for each criteria if it found the corresponding arguments
        - Create a copy `ArrayList<Transaction> temp` from `transactions` for filtering
        - Check the availability of the start date
           - If available: `temp` = `temp` filtered by start date

        - Check the availability of the end date
           - If available: `temp` = `temp` filtered by end date

        - Check if the start date is after the end date
           - If yes: throw new exception (as the start date should be before the end date)

        - Return a list of stringified filtered-transactions

    ![execute](./diagrams/ViewHistoryDiagram/viewhistory-flow-diagram.png)
   
### Command Parser
The `Parser` class is responsible for interpreting user commands and extracting the associated arguments. It facilitates interaction between the user and the underlying command execution logic. There is only one Command Parser living through a session.

#### Class responsibilities
1. **Command registration**: Maintain a mapping of command words to their corresponding `Command` objects. This command object will maintain throughout the session and every execution will be called through it.
2. **Command parsing**: Convert a command string entered by the user into a `Command` object.
3. **Argument extraction**: Extract and organize the arguments associated with a given command.
   
#### Class attributes
1. **commands: LinkedHashMap&lt;String, Command&gt;**
   - Description: Associates command words (as keys) with their corresponding Command objects (as values).
     
#### Class main methods
1. **registerCommands(Command command): void**
   - **Parameters**:
     - `command`: The `Command` object to be registered.
   - **Process**:
     - Retrieves the `COMMAND_WORD` field from the `Command` object
     - Adds the word and the command to the `commands` map.
   - **Diagram note**: In the following registration diagram, `helpCommand` stays alive and referenced throughout the session.
    
    ![register_command](./diagrams/parser/register-command-sequence.png)
    
2. **parseCommand(String commandPart): Command**
   - **Parameters**: 
     - `commandPart`: A string representing the command word entered by the user.
      
   - **Returns**: The corresponding `Command` object or `null` if the command is not found.
    
   - **Process**: 
     - Retrieves the associated `Command` object from the `commands` map, using the provided commandPart.

    ![parse_command](./diagrams/parser/parse-command-sequence.png)
  
3. **extractArguments(Command command, String argumentString): Map&lt;String, String&gt;**
   - **Parameters**: 
     - `command`: The `Command` object for which arguments are to be extracted.
     - `argumentString`: The string containing the arguments to be parsed.
      
   - **Returns**: A map of argument keys and their corresponding values.
    
   - **Process**: 
     - Initializes an empty map for arguments
     - Retrieves the expected argument keys from the command
     - Invokes `splitCommandRecursively` to populate the arguments map.
  
    ![extract_arguments](./diagrams/parser/extract-arguments-sequence.png)
  
4. **splitCommandRecursively(String argumentString, String[] keywords, Map&lt;String, String&gt; arguments, String prevKeyword): void**
   - **Parameters**: 
     - `argumentString`: The string containing the arguments to be split.
     - `keywords`: An array of expected keywords for argument extraction.
     - `arguments`: The map where extracted arguments will be stored.
     - `prevKeyword`: The keyword found in the previous recursive call.
      
   - **Description**: Extracts values associated with keywords and updates the arguments map accordingly.
    
   - **Process**:
     - Base case: No argument left to split: `argumentString.isEmpty()`
     - Find the first keyword in the list that appears in the argumentString
     - If found:
       - Attach the part before the keyword with the previously found keyword and put in to `arguments`.
       - Delete the keyword from the `keywords` list (to not be considered in the next call)
       - Pass the remaining `argumentString` after the keyword to the next recursive call
     - If not found (mean that the last keyword reached):
       - Attach the remaining part with the previously found keyword and put in to `arguments`.

     ![getArguments](./diagrams/parser/parse-arguments-activity.png)
   
## Product scope
### Target user profile
#### Demographics:
- Age: 18-25 years old
- Education: College or university students
- Income: Limited or variable income (part-time jobs, allowances, or scholarships)
  
#### Psychographics:
- Tech-Savvy: Comfortable using command-line interfaces and basic programming concepts.
- Motivated to create good spending habit: Aware of personal finance status and developing better money management habits.

### Value proposition
uNivUSaver offers a practical solution for students who want to take control of their finances, avoid over-spending and manage their saving goal.
- Customized budgeting tools: Helps users set up personalized budgets based on their income and expenses, allowing them to see where their money goes and how to optimize it.
- Habit formation: Encourages regular check-ins and tracking of spending habits, helps students develop a consistent routine for managing their finances.
- Limit tracking: Helps users to set specific monthly limit and monitor their progress to avoid over-spending.

## User Stories

| Version | As a...                              | I want to...                                           | So that I can...                                                |
|---------|--------------------------------------|--------------------------------------------------------|-----------------------------------------------------------------|
| v1.0   | new user                              | see usage instructions                                 | refer to them when I forget how to use the application          |
| v1.0   | student                               | input my expenses                                      | keep track of how much I spend daily                            |
| v1.0   | student with part-time job            | input my income into the budget                        | my budget reflects my earnings                                  |
| v1.0   | student                               | create categories for expenses                         | separate my expenses into the respective categories             |
| v1.0   | user                                  | delete any expense                                     | remove any spending that was input wrongly                      |
| v1.0   | user                                  | delete any income                                      | remove any income that was input wrongly                        |
| v1.0   | student with changing spending purposes| delete any category                                    | clean up unecessary categories                                  |
| v1.0   | student who wishes to manage my budget| view my transaction history                            | figure out my spending habits                                   |
| v1.0   | student                               | view my budget amount                                  | know the current state of my budget                             |
| v1.0   | student who wishes to manage spendings| view summary of my spending                            | have an idea of where my money goes in a period of time         |
| v1.0   | student who wishes to manage incomes  | view summary of my incomes                             | have an idea of where my sources of income in a period of time  |
| v1.0   | user                                  | view current category list                             | know the existing categories in my budget                       |
| v1.0   | student with various spending purposes| keep track of my spending in each category             | see how much I spend for each category                          |
| v2.0   | time-constrained student              | see fixed reminders to input daily expenses            | improve accuracy in recording and avoid missing any transactions|
| v2.0   | student that easily overspending      | see fixed reminders of today expenses                  | stop myself from overspending in the rest of the day            |
| v2.0   | student                               | find my expenses and incomes by keywords               | search for old transactions                                     |
| v2.0   | who often changes my mind             | categorize my expenses after creating                  | easily change my expense category                               |

## Non-Functional Requirements

* **Error prevention** - The system must minimize the possibility of user mistakes by implementing robust validation, clear feedback, and safeguards against incorrect actions.
* **Error recovery** - The system must provide a way for user to restart the session on error.
* **Compatibility** - The system must run on modern operating systems (Windows, macOS, Linux) and should be compatible with major terminal emulators and shells such as Bash and Command Prompt.
* **Feedback** - The system must provide real-time feedback for user actions.

## Glossary
* *Transaction* - A record of an income or expense that includes details such as the amount, description, date, and associated category.
* *Income* - Money received by the user. An income transaction is recorded to track the user's earnings.
* *Expense* - Money spent by the user. An expense transaction helps the user monitor their spending habits.
* *Category* - A classification used to group expenses (e.g., "Food", "Transportation", "Entertainment"). Each expense in the system is assigned to a category.
* *Budget* - A spending limit that the user set for each month, that the user can use to track if they're overspending.

## Instructions for manual testing
### Notes
* Note that the following instruction is just used for basic testing purpose
* The Expected output section is not the console output.

### Set up
* Ensure that you have Java 17 or above installed.
* Down the latest version of `uNivUSaver` from [here](https://github.com/AY2425S1-CS2113-W10-4/tp/releases).
* Copy the file to the folder you want the program to stay in.
* Open a command terminal, run the following:
```
cd Path/to/directory # Change directory into the folder you put the jar file in
java -jar uNivUSaver.jar # Start the program.
```
* Type `help` to view the command list and syntax.
* Refer to [User Guide](https://ay2425s1-cs2113-w10-4.github.io/tp/UserGuide.html) for detailed guide on command syntax.

---

### **1. Help command**
#### Test case:

**Prerequisites:** None

**Input:**  
- Input: `help`

**Expected output:**  
- A list of all available commands in the application is displayed, with syntax and description of their functionality.

---

### **2. Add expense command**
#### Test case 1: Valid input

**Prerequisites:** There is a category named Food in your category list

**Input:**  
- Input: `add-expense School fee a/ 1 d/ 2024-11-09 c/ Food`

**Expected output:**  
- The expense is successfully added with the description "School fee", with the amount of 1, the date 2024-11-09 0000, and the category Food.

#### Test case 2: Valid input without a category

**Prerequisites:** None

**Input:**  
- Input: `add-expense Amusement park a/ 1 d/ 2024-11-09`

**Expected output:**  
- The user is prompted to input a category since it's missing. After prompted, the expense is successfully added with the description "Amusement park", the amount of 1, the date 2024-11-09 0000, and the final choice of the category field.

#### Test case 3: Valid input without a date

**Prerequisites:** There is a category named Food in your category list

**Input:**  
- Input: `add-expense JustNow a/ 1 c/ Food`

**Expected output:**  
- The expense is successfully added with the description "JustNow", the amount of 1, the date is current time. 

---

### **3. Add income command**
#### Test case 1: Valid input

**Prerequisites:** None

**Input:**  
- Input: `add-income Monthly allowance a/ 1 d/ 2024-11-09`
 
**Expected output:**  
- The income is successfully added with the description "Monthly allowance", the amount of 1, and the date 2024-11-09 0000.

#### Test case 2: Valid input without description

**Prerequisites:** None

**Input:**  
- Input: `add-income a/ 1 d/ 2024-11-11`

**Expected output:**  
- The income is successfully added with the amount of 1 and the date 2024-11-11 0000. The description is missing but defaulted.

---

### **4. Add category command**
#### Test case 1: Valid input

**Prerequisites:** No category named 'FnB' is added to the list before

**Input:**  
- Input: `add-category FnB`
 
**Expected output:**  
- The category "FnB" is successfully added to the category list.

#### Test case 2: Duplicated category

**Prerequisites:** A category named 'Food' is added to the list before

**Input:**  
- Input: `add-category Food`

**Expected output:**  
- An error show that duplicated category found.

---

### **5. Add budget command**
#### Test case 1: Valid input

**Prerequisites:** None

**Note:** You may replace the month with the current month/ future month.

**Input:**  
- Input: `add-budget a/ 1000 m/ 2024-11`

**Expected output:**  
- The budget of 1000 for the month of 2024-11 is successfully set.

#### Test case 2: Past month

**Prerequisites:** None

**Input:**  
- Input: `add-budget a/ 200 m/ 2023-02`

**Expected output:**  
- An error message indicates that the budget can not be set for previous months.

---

### **6. Delete transaction command**
#### Test case 1: Valid input

**Prerequisites:** The list must contain at least 1 transaction.

**Input:**  
- Input: `delete-transaction i/ 1`

**Expected output:**  
- The transaction at index 1 is successfully deleted from the history.

---

### **7. Delete category command**
#### Test case 1: Valid input

**Prerequisites:** The category list must contain the category named 'Food'

**Input:**  
- Input: `delete-category Food`

**Expected output:**  
- The user might be prompted to re-categorize some expenses. If the user doesn't choose to cancel the command, category "Food" is successfully removed from the category list.

#### Test case 2: Non-existing category

**Prerequisites:** The category list must not contain the category named 'NonExist'

**Input:**  
- Input: `delete-category NonExist`

**Expected output:**  
- An error message show that the category doesn't exists.

---

### **8. Delete budget command**
#### Test case:

**Prerequisites:** The budget list must contain the budget on 2024-11

**Input:**  
- Input: `delete-budget m/ 2024-11`

**Expected output:**  
- The budget for month 2024-11 is deleted successfully

---

### **9. Categorize command**
#### Test case 1: Valid input

**Prerequisites:** The category list must contain the category named 'Others', and the transaction in index 1 must be an Expense

**Input:**  
- Input: `categorize i/ 1 c/ Others`

**Expected output:**  
- The expense at index 1 is successfully updated to the category "Food".

#### Test case 2: Non-existing category

**Prerequisites:** The category list must not contain the category named 'NonExist', and the transaction in index 1 must be an Expense

**Input:**  
- Input: `categorize i/ 1 c/ NonExist`

**Expected output:**  
- An error message show that the category doesn't exist.

---

### **10. View category command**
#### Test case:

**Prerequisites:** None

**Input:**  
- Input: `view-category`
 
**Expected output:**  
- The system displays the full list of categories available in the system.

---

### **11. View expense command**
#### Test case 1: With category

**Prerequisites:** The list should contain some expenses in the category "Others"

**Input:**  
- Input: `view-expense c/ Others`
 
**Expected output:**  
- The system displays all expenses in the "Others" category.

#### Test case 2: With date

**Prerequisites:** The list should contain some expenses 

**Input:**  
- Input: `view-expense f/ 2024-11-10 t/ 2024-11-20`  
 
**Expected output:**  
- The system displays all expenses from 2024-11-10 to 2024-11-20.

#### Test case 3: Without filter

**Prerequisites:** The list should contain some expenses 

**Input:**  
- Input: `view-expense`

**Expected output:**  
- The system displays all expenses.

---

### **12. View income command**
#### Test case 1: With date

**Prerequisites:** The list should contain some incomes 

**Input:**  
- Input: `view-income f/ 2024-11-10 t/ 2024-11-20`  
 
**Expected output:**  
- The system displays all incomes from 2024-11-10 to 2024-11-20.

#### Test case 2: Without filter

**Prerequisites:** The list should contain some incomes 

**Input:**  
- Input: `view-income`

**Expected output:**  
- The system displays all incomes.

---

### **13. History command**
#### Test case 1: With date

**Prerequisites:** The list should contain some expenses and incomes 

**Input:**  
- Input: `history f/ 2024-11-10 t/ 2024-11-20`  
 
**Expected output:**  
- The system displays all transactions from 2024-11-10 to 2024-11-20.

#### Test case 2: Without filter

**Prerequisites:** The list should contain some expenses and incomes 

**Input:**  
- Input: `history`

**Expected output:**  
- The system displays all transactions.

---

### **14. View total command**
#### Test case:

**Prerequisites:** The list should contain some expenses and incomes 

**Input:**  
- Input: `view-total`

**Expected output:**  
- The system displays the total amount of money currently in the account.

---

### **15. Search command**
#### Test case:

**Prerequisites:** The list should contain some expenses and incomes with valid descriptions  

**Input:**  
- Input: `search k/ Month Amuse`
 
**Expected output:**  
- The system displays all transactions with the keywords partially matching "Month" or "Amuse".

---

### **16. View budget command**
#### Test case 1: With specific month

**Prerequisites:** The system must contain the budget in "2024-11" and should have some transaction in that period 

**Input:**  
- Input: `view-budget m/ 2024-11`

**Expected output:**  
- The system displays the progress towards the budget for 2024-11.

#### Test case 2: With no month

**Prerequisites:** The system should have some budget

**Input:**  
- Input: `view-budget`

**Expected output:**  
- The system displays all the budgets.

---

### **17. Bye command**
#### Test case:
**Prerequisites:** None

**Input:**  
- Input: `bye`
 
**Expected output:**  
- The application exits peacefully.
