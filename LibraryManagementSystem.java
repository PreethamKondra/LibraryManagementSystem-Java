import java.util.*;
import java.time.LocalDate;

class Item {
    private String id;
    private String title;
    private boolean isBorrowed;
    private LocalDate dueDate;

    public Item(String id, String title) {
        this.id = id;
        this.title = title;
        this.isBorrowed = false;
        this.dueDate = null;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isBorrowed() { return isBorrowed; }
    public LocalDate getDueDate() { return dueDate; }

    public void borrowItem() {
        this.isBorrowed = true;
        this.dueDate = LocalDate.now().plusDays(14);
    }

    public void returnItem() {
        this.isBorrowed = false;
        this.dueDate = null;
    }

    @Override
    public String toString() {
        return id + " - " + title + (isBorrowed ? " (Due: " + dueDate + ")" : " (Available)");
    }
}

class User {
    private String id;
    private String name;
    private List<Item> borrowedItems;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedItems = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Item> getBorrowedItems() { return borrowedItems; }

    public void borrowItem(Item item) {
        borrowedItems.add(item);
    }

    public void returnItem(Item item) {
        borrowedItems.remove(item);
    }

    @Override
    public String toString() {
        return id + " - " + name + " | Borrowed: " + borrowedItems.size();
    }
}

class Library {
    private Map<String, Item> items;
    private Map<String, User> users;

    public Library() {
        items = new HashMap<>();
        users = new HashMap<>();
    }

    public Map<String, Item> getItems() { return items; }
    public Map<String, User> getUsers() { return users; }

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void borrowItem(String userId, String itemId) {
        User user = users.get(userId);
        Item item = items.get(itemId);
        if (user != null && item != null && !item.isBorrowed()) {
            item.borrowItem();
            user.borrowItem(item);
            System.out.println("Item borrowed successfully.");
        } else {
            System.out.println("Borrowing failed.");
        }
    }

    public void returnItem(String userId, String itemId) {
        User user = users.get(userId);
        Item item = items.get(itemId);
        if (user != null && item != null && item.isBorrowed()) {
            item.returnItem();
            user.returnItem(item);
            System.out.println("Item returned successfully.");
        } else {
            System.out.println("Return failed.");
        }
    }
}

public class LibraryManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Library library = new Library();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add Item");
            System.out.println("2. Add User");
            System.out.println("3. Borrow Item");
            System.out.println("4. Return Item");
            System.out.println("5. Display Items");
            System.out.println("6. Display Users");
            System.out.println("7. Display User Transactions");
            System.out.println("8. Display Overdue Items");
            System.out.println("9. Exit");

            int choice = getIntInput("Enter choice: ");

            switch (choice) {
                case 1:
                    System.out.print("Enter Item ID: ");
                    String itemId = scanner.nextLine();
                    System.out.print("Enter Item Title: ");
                    String itemTitle = scanner.nextLine();
                    library.addItem(new Item(itemId, itemTitle));
                    break;

                case 2:
                    System.out.print("Enter User ID: ");
                    String userId = scanner.nextLine();
                    System.out.print("Enter User Name: ");
                    String userName = scanner.nextLine();
                    library.addUser(new User(userId, userName));
                    break;

                case 3:
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextLine();
                    System.out.print("Enter Item ID: ");
                    itemId = scanner.nextLine();
                    library.borrowItem(userId, itemId);
                    break;

                case 4:
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextLine();
                    System.out.print("Enter Item ID: ");
                    itemId = scanner.nextLine();
                    library.returnItem(userId, itemId);
                    break;

                case 5:
                    library.getItems().values().forEach(System.out::println);
                    break;

                case 6:
                    displayUsers();
                    break;

                case 7:
                    displayUserTransactions();
                    break;

                case 8:
                    displayOverdueItems();
                    break;

                case 9:
                    saveData();
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid number, try again.");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return val;
    }

    private static void displayUsers() {
        library.getUsers().values().forEach(System.out::println);
    }

    private static void displayUserTransactions() {
        library.getUsers().values().forEach(user -> {
            System.out.println(user);
            user.getBorrowedItems().forEach(System.out::println);
        });
    }

    private static void displayOverdueItems() {
        LocalDate today = LocalDate.now();
        library.getItems().values().stream()
                .filter(item -> item.isBorrowed() && item.getDueDate().isBefore(today))
                .forEach(System.out::println);
    }

    private static void saveData() {
        // This is just a placeholder - actual file saving not implemented
        System.out.println("Data saved (simulation).");
    }
}
