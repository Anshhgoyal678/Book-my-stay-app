import java.io.*;
import java.util.*;

// Represents a confirmed reservation
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationID;
    private String guestName;
    private String roomType;
    private String roomID;
    private int numberOfNights;

    public Reservation(String reservationID, String guestName, String roomType, String roomID, int numberOfNights) {
        this.reservationID = reservationID;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomID = roomID;
        this.numberOfNights = numberOfNights;
    }

    public String getReservationID() {
        return reservationID;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomID() {
        return roomID;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    @Override
    public String toString() {
        return "Reservation [ID: " + reservationID + ", Guest: " + guestName +
                ", Room Type: " + roomType + ", Room ID: " + roomID +
                ", Nights: " + numberOfNights + "]";
    }
}

// Inventory service
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> availability;

    public InventoryService(Map<String, Integer> initialInventory) {
        this.availability = new HashMap<>(initialInventory);
    }

    public synchronized boolean allocate(String roomType) {
        int count = availability.getOrDefault(roomType, 0);
        if (count > 0) {
            availability.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public synchronized void increment(String roomType) {
        availability.put(roomType, availability.getOrDefault(roomType, 0) + 1);
    }

    public synchronized int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public synchronized void printInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String roomType : availability.keySet()) {
            System.out.println(roomType + ": " + availability.get(roomType));
        }
    }
}

// Booking history service
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedReservations);
    }

    public void printReservations() {
        System.out.println("\nConfirmed Reservations:");
        for (Reservation r : confirmedReservations) {
            System.out.println(r);
        }
    }
}

// Persistence service
class PersistenceService {
    private String fileName;

    public PersistenceService(String fileName) {
        this.fileName = fileName;
    }

    public void save(BookingHistory history, InventoryService inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(history);
            oos.writeObject(inventory);
            System.out.println("\nSystem state saved successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    public Object[] load() {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("\nNo persistence file found. Starting with empty state.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            BookingHistory history = (BookingHistory) ois.readObject();
            InventoryService inventory = (InventoryService) ois.readObject();
            System.out.println("\nSystem state loaded successfully from " + fileName);
            return new Object[]{history, inventory};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            return null;
        }
    }
}

// Main program simulating persistence and recovery
public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        String persistenceFile = "hotel_system_state.dat";
        PersistenceService persistenceService = new PersistenceService(persistenceFile);

        // Attempt to load previous state
        Object[] loaded = persistenceService.load();
        BookingHistory bookingHistory;
        InventoryService inventoryService;

        if (loaded != null) {
            bookingHistory = (BookingHistory) loaded[0];
            inventoryService = (InventoryService) loaded[1];
        } else {
            // Start fresh if no saved state
            Map<String, Integer> initialInventory = new HashMap<>();
            initialInventory.put("Single", 2);
            initialInventory.put("Double", 1);
            inventoryService = new InventoryService(initialInventory);
            bookingHistory = new BookingHistory();
        }

        // Simulate new bookings
        Reservation r1 = new Reservation("R-1001", "Alice", "Single", "S-101", 2);
        Reservation r2 = new Reservation("R-1002", "Bob", "Double", "D-201", 3);

        if (inventoryService.allocate(r1.getRoomType())) bookingHistory.addReservation(r1);
        if (inventoryService.allocate(r2.getRoomType())) bookingHistory.addReservation(r2);

        bookingHistory.printReservations();
        inventoryService.printInventory();

        // Save current state
        persistenceService.save(bookingHistory, inventoryService);

        // Simulate system restart
        System.out.println("\n--- Simulating system restart ---");
        Object[] recovered = persistenceService.load();
        if (recovered != null) {
            BookingHistory recoveredHistory = (BookingHistory) recovered[0];
            InventoryService recoveredInventory = (InventoryService) recovered[1];

            recoveredHistory.printReservations();
            recoveredInventory.printInventory();
        }
    }
}