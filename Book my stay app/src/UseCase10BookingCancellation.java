import java.util.*;

// Represents a confirmed reservation
class Reservation {
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
class InventoryService {
    private Map<String, Integer> availability;

    public InventoryService(Map<String, Integer> availability) {
        this.availability = new HashMap<>(availability);
    }

    // Increment inventory (rollback)
    public void increment(String roomType) {
        availability.put(roomType, availability.getOrDefault(roomType, 0) + 1);
    }

    // Decrement inventory
    public boolean allocate(String roomType) {
        int count = availability.getOrDefault(roomType, 0);
        if (count > 0) {
            availability.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void printInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String roomType : availability.keySet()) {
            System.out.println(roomType + ": " + availability.get(roomType));
        }
    }
}

// Booking service with cancellation support
class BookingService {
    private Map<String, Reservation> activeReservations; // reservationID -> Reservation
    private Map<String, Set<String>> allocatedRooms;     // roomType -> Set of roomIDs
    private InventoryService inventory;
    private Stack<String> releasedRoomIDs;               // Tracks rollback order

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
        this.activeReservations = new HashMap<>();
        this.allocatedRooms = new HashMap<>();
        this.releasedRoomIDs = new Stack<>();
    }

    // Confirm a new booking
    public boolean confirmBooking(Reservation reservation) {
        String roomType = reservation.getRoomType();
        if (inventory.allocate(roomType)) {
            allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(reservation.getRoomID());
            activeReservations.put(reservation.getReservationID(), reservation);
            System.out.println("Booking confirmed: " + reservation);
            return true;
        } else {
            System.out.println("Booking failed: No availability for " + roomType);
            return false;
        }
    }

    // Cancel a booking safely
    public void cancelBooking(String reservationID) {
        if (!activeReservations.containsKey(reservationID)) {
            System.out.println("Cancellation failed: Reservation " + reservationID + " does not exist or is already cancelled.");
            return;
        }

        Reservation reservation = activeReservations.remove(reservationID);
        String roomType = reservation.getRoomType();
        String roomID = reservation.getRoomID();

        // Rollback: release room ID and restore inventory
        allocatedRooms.getOrDefault(roomType, new HashSet<>()).remove(roomID);
        releasedRoomIDs.push(roomID);  // Track released rooms
        inventory.increment(roomType);

        System.out.println("Booking cancelled: " + reservation);
    }

    // Print active reservations
    public void printActiveReservations() {
        System.out.println("\nActive Reservations:");
        if (activeReservations.isEmpty()) {
            System.out.println("None");
            return;
        }
        for (Reservation r : activeReservations.values()) {
            System.out.println(r);
        }
    }
}

// Main program simulating booking cancellation and inventory rollback
public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        // Initial inventory
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single", 2);
        initialInventory.put("Double", 1);

        InventoryService inventoryService = new InventoryService(initialInventory);
        BookingService bookingService = new BookingService(inventoryService);

        // Confirm bookings
        Reservation r1 = new Reservation("R-1001", "Alice", "Single", "S-101", 2);
        Reservation r2 = new Reservation("R-1002", "Bob", "Double", "D-201", 3);
        Reservation r3 = new Reservation("R-1003", "Charlie", "Single", "S-102", 1);

        bookingService.confirmBooking(r1);
        bookingService.confirmBooking(r2);
        bookingService.confirmBooking(r3);

        bookingService.printActiveReservations();
        inventoryService.printInventory();

        // Perform cancellations
        bookingService.cancelBooking("R-1002"); // Cancel Bob
        bookingService.cancelBooking("R-9999"); // Invalid cancellation
        bookingService.cancelBooking("R-1001"); // Cancel Alice

        bookingService.printActiveReservations();
        inventoryService.printInventory();
    }
}