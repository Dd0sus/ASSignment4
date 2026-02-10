package menu;

import model.*;
import database.StaffDAO;
import exception.InvalidInputException;
import java.util.List;
import java.util.Scanner;

public class MenuManager implements Menu {

    private StaffDAO staffDAO;
    private Scanner scanner;

    public MenuManager() {
        this.staffDAO = new StaffDAO();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Grocery Store Staff Management (DB Version) ===");
        System.out.println("1. Add Cashier");
        System.out.println("2. Add Manager");
        System.out.println("3. Show All Staff");
        System.out.println("4. Demonstrate Polymorphism");
        System.out.println("5. Update Staff");
        System.out.println("6. Delete Staff");
        System.out.println("7. Search by Name");
        System.out.println("8. Search by Salary Range");
        System.out.println("9. Search by Smallest Salary");
        System.out.println("0. Exit");
    }

    @Override
    public void run() {
        int choice = -1;

        while (choice != 0) {
            displayMenu();
            System.out.print("Choose option: ");

            try {
                String input = scanner.nextLine();
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("That is not a number!");
                }

                switch (choice) {
                    case 1:
                        addCashier();
                        break;
                    case 2:
                        addManager();
                        break;
                    case 3:
                        showAllStaff();
                        break;
                    case 4:
                        demonstratePolymorphism();
                        break;
                    case 5:
                        updateStaff();
                        break;
                    case 6:
                        deleteStaff();
                        break;
                    case 7: searchByName(); break;
                    case 8: searchBySalary(); break;
                    case 9: searchBySmallestSalary(); break;
                    case 0: System.out.println("Exiting..."); break;
                    default: System.out.println("Invalid option.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void showAllStaff() {
        List<Staff> list = staffDAO.getAllStaff();
        if (list.isEmpty()) {
            System.out.println("Database is empty.");
        } else {
            for (Staff s : list) System.out.println(s);
        }
    }

    private void demonstratePolymorphism() {
        System.out.println("--- Polymorphism Demo ---");
        List<Staff> list = staffDAO.getAllStaff();
        for (Staff s : list) {
            s.work();
            if (s instanceof Promotable) {
                ((Promotable) s).promote();
            }
        }
    }

    private void addCashier() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Salary: ");
            double salary = Double.parseDouble(scanner.nextLine());
            System.out.print("Register Number: ");
            int reg = Integer.parseInt(scanner.nextLine());

            Cashier c = new Cashier(id, name, salary, reg);
            if (staffDAO.addCashier(c)) {
                System.out.println("Cashier added to Database!");
            } else {
                System.out.println("Failed to add (ID might exist).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Enter valid numbers!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void addManager() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Salary: ");
            double salary = Double.parseDouble(scanner.nextLine());
            System.out.print("Team Size: ");
            int size = Integer.parseInt(scanner.nextLine());

            Manager m = new Manager(id, name, salary, size);
            if (staffDAO.addManager(m)) {
                System.out.println("Manager added to Database!");
            } else {
                System.out.println("Failed to add (ID might exist).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Enter valid numbers!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private void updateStaff() {
        System.out.print("Enter Staff ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        Staff existing = staffDAO.getStaffById(id);
        if (existing == null) {
            System.out.println("Staff not found!");
            return;
        }

        System.out.println("Current: " + existing);
        System.out.print("New Name (Enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) existing.setName(name);

        System.out.print("New Salary (Enter to keep current): ");
        String salaryStr = scanner.nextLine();
        if (!salaryStr.trim().isEmpty()) existing.setSalary(Double.parseDouble(salaryStr));

        if (existing instanceof Cashier) {
            System.out.print("New Register (Enter to keep current): ");
            String reg = scanner.nextLine();
            if (!reg.trim().isEmpty()) ((Cashier) existing).setRegisterNumber(Integer.parseInt(reg));
        } else if (existing instanceof Manager) {
            System.out.print("New Team Size (Enter to keep current): ");
            String size = scanner.nextLine();
            if (!size.trim().isEmpty()) ((Manager) existing).setTeamSize(Integer.parseInt(size));
        }

        if (staffDAO.updateStaff(existing)) {
            System.out.println("Update successful!");
        } else {
            System.out.println("Update failed.");
        }
    }

    private void deleteStaff() {
        System.out.print("Enter Staff ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        Staff s = staffDAO.getStaffById(id);
        if (s == null) {
            System.out.println("Staff not found.");
            return;
        }

        System.out.println("Deleting: " + s.getName());
        System.out.print("Are you sure? (yes/any key): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            staffDAO.deleteStaff(id);
            System.out.println("Staff deleted.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void searchByName() {
        System.out.print("Enter name (partial allowed): ");
        String query = scanner.nextLine();
        List<Staff> results = staffDAO.searchByName(query);
        if (results.isEmpty()) System.out.println("No results.");
        for (Staff s : results) System.out.println(s);
    }

    private void searchBySalary() {
        System.out.print("Min Salary: ");
        double min = Double.parseDouble(scanner.nextLine());
        System.out.print("Max Salary: ");
        double max = Double.parseDouble(scanner.nextLine());

        List<Staff> results = staffDAO.searchBySalaryRange(min, max);
        if (results.isEmpty()) System.out.println("No results.");
        for (Staff s : results) System.out.println(s);
    }

    private void searchBySmallestSalary() {
        List<Staff> results = staffDAO.searchBySmallestSalary();
        if (results.isEmpty()) {
            System.out.println("No results.");
            return;
        }

        System.out.println("--- Staff with the Smallest Salary ---");
        for (Staff s : results) System.out.println(s);
    }
}