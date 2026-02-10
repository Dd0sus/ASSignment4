# Grocery Store Staff Management (Database Version)

A simple Java console application to manage grocery store staff (Cashiers and Managers) using a PostgreSQL database.  
It supports adding, viewing, updating, deleting, and searching staff records.

## Features
- Add Cashier (includes register number)
- Add Manager (includes team size)
- List all staff
- Update staff details
- Delete staff
- Search staff by name (partial match)
- Search staff by salary range
- Basic polymorphism demo (`work()` and `promote()` for managers)

## Tech
- Java 17
- Maven
- PostgreSQL
- JDBC

## Project Structure
- `src/main/java/model`  
  Domain classes: `Staff`, `Cashier`, `Manager`, `Promotable`
- `src/main/java/database`  
  Database layer: `DatabaseConnection`, `StaffDAO`
- `src/main/java/menu`  
  Console UI: `Menu`, `MenuManager`
- `src/main/java/Main.java`  
  Application entry point

## Requirements
- JDK 17 installed and configured
- PostgreSQL running
- Maven available (or use the Maven wrapper if configured)

## Database Setup

1. Create a PostgreSQL database (or use an existing one).
2. Create the `staff` table:
