import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class Item {
    private String title;
    private String isbn;
    private ItemType type;

    public Item(String title, String isbn, ItemType type) {
        this.title = title;
        this.isbn = isbn;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", ISBN: " + isbn + ", Type: " + type;
    }
}

enum ItemType {
    BOOK,
    DVD
}

class Reader {
    private String name;
    private int id;
    private List<Item> borrowedItems = new ArrayList<>();

    public Reader(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Item> getBorrowedItems() {
        return borrowedItems;
    }

    public void borrowItem(Item item) {
        borrowedItems.add(item);
    }

    public void returnItem(Item item) {
        borrowedItems.remove(item);
    }

    @Override
    public String toString() {
        return "Reader Name: " + name + ", Reader ID: " + id;
    }
}

class Library {
    private List<Item> items = new ArrayList<>();
    private Map<Integer, Reader> readers = new HashMap<>();
    private int readerIdCounter = 1;

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItemByIsbn(String isbn) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getIsbn().equals(isbn)) {
                iterator.remove();
            }
        }
    }

    public int registerReader(String name) {
        Reader reader = new Reader(name, readerIdCounter);
        readers.put(readerIdCounter, reader);
        readerIdCounter++;
        return reader.getId();
    }

    public void lendItem(int readerId, String isbn) {
        Reader reader = readers.get(readerId);
        if (reader == null) {
            System.out.println("Reader not found.");
            return;
        }

        Item item = findItemByIsbn(isbn);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        reader.borrowItem(item);
        removeItemByIsbn(isbn);
    }

    public void returnItem(int readerId, String isbn) {
        Reader reader = readers.get(readerId);
        if (reader == null) {
            System.out.println("Reader not found.");
            return;
        }

        Item item = findItemByIsbn(isbn);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        reader.returnItem(item);
        addItem(item);
    }

    public void showAvailableItems() {
        items.forEach(System.out::println);
    }

    public void showBorrowedItems() {
        for (Reader reader : readers.values()) {
            List<Item> borrowedItems = reader.getBorrowedItems();
            for (Item item : borrowedItems) {
                System.out.println("Borrowed by " + reader.getName() + ": " + item);
            }
        }
    }

    private Item findItemByIsbn(String isbn) {
        return items.stream()
                .filter(item -> item.getIsbn().equalsIgnoreCase(isbn))
                .findFirst()
                .orElse(null);
    }
}

public class LibraryManagement {
    public static void main(String[] args) {
        Library library = new Library();

        library.addItem(new Item("Book A", "ISBN1", ItemType.BOOK));
        library.addItem(new Item("Book B", "ISBN2", ItemType.BOOK));
        library.addItem(new Item("DVD F", "ISBN3", ItemType.DVD));

        System.out.println("All Items in Library:");
        library.showAvailableItems();

        int readerId = library.registerReader("Max");
        System.out.println("\nRegistered Reader with ID: " + readerId);

        int readerId2 = library.registerReader("Milana");
        System.out.println("Registered Reader with ID: " + readerId2);

        library.lendItem(readerId, "ISBN1");
        library.lendItem(readerId, "ISBN2");
        library.lendItem(readerId2, "ISBN3");

        System.out.println("\nAvailable Items in Library after lending:");
        library.showAvailableItems();

        System.out.println("\nBorrowed Items:");
        library.showBorrowedItems();

        library.returnItem(readerId, "ISBN1");

        System.out.println("\nAvailable Items in Library after returning:");
        library.showAvailableItems();

        System.out.println("\nBorrowed Items:");
        library.showBorrowedItems();
    }
}