import java.util.*;

// Represents a guest's intent to book a room
class Reservation {
    private String guestName;
    private String roomType;
    private int numberOfNights;

    public Reservation(String guestName, String roomType, int numberOfNights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    @Override
    public String toString() {
        return "Reservation [Guest: " + guestName + ", Room Type: " + roomType +
                ", Nights: " + numberOfNights + "]";
    }
}

// Inventory service maintaining room availability
class InventoryService {
    private Map<String, Integer> availability;

    public InventoryService(Map<String, Integer> availability) {
        this.availability = new HashMap<>(availability); // defensive copy
    }

    // Check availability
    public boolean isAvailable(String roomType) {
        return availability.getOrDefault(roomType, 0) > 0;
    }

    // Decrement inventory after successful allocation
    public boolean allocateRoom(String roomType) {
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
}

// Service for allocating rooms and confirming reservations
class BookingService {
    private InventoryService inventoryService;
    private Queue<Reservation> bookingQueue;
    private Map<String, Set<String>> allocatedRooms; // roomType -> roomIDs

    public BookingService(InventoryService inventoryService, Queue<Reservation> bookingQueue) {
        this.inventoryService = inventoryService;
        this.bookingQueue = bookingQueue;
        this.allocatedRooms = new HashMap<>();
    }

    // Process all queued reservations
    public void processBookings() {
        while (!bookingQueue.isEmpty()) {
            Reservation reservation = bookingQueue.poll();
            String roomType = reservation.getRoomType();

            System.out.println("Processing reservation for " + reservation.getGuestName() + " (" + roomType + ")");

            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("  -> Room type unavailable. Reservation cannot be confirmed.");
                continue;
            }

            // Generate unique room ID
            String roomID = generateUniqueRoomID(roomType);

            // Allocate room and decrement inventory
            boolean allocated = inventoryService.allocateRoom(roomType);
            if (allocated) {
                allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomID);
                System.out.println("  -> Reservation confirmed. Assigned Room ID: " + roomID);
            } else {
                System.out.println("  -> Failed to allocate room due to inventory issue.");
            }
        }
    }

    // Generates a unique room ID for a room type
    private String generateUniqueRoomID(String roomType) {
        Set<String> existingIDs = allocatedRooms.getOrDefault(roomType, new HashSet<>());
        String roomID;
        do {
            roomID = roomType.substring(0, 1).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 8);
        } while (existingIDs.contains(roomID));
        return roomID;
    }

    // Display current allocation state
    public void printAllocatedRooms() {
        System.out.println("\nCurrent Allocated Rooms:");
        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + ": " + allocatedRooms.get(type));
        }
    }
}

// Main program simulating reservation confirmation and room allocation
public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        // Setup inventory
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single", 2);
        initialInventory.put("Double", 1);
        initialInventory.put("Suite", 1);

        InventoryService inventoryService = new InventoryService(initialInventory);

        // Setup booking queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single", 2));
        bookingQueue.add(new Reservation("Bob", "Suite", 3));
        bookingQueue.add(new Reservation("Charlie", "Single", 1));
        bookingQueue.add(new Reservation("David", "Double", 2));
        bookingQueue.add(new Reservation("Eve", "Single", 1)); // Should fail due to inventory

        // Initialize booking service
        BookingService bookingService = new BookingService(inventoryService, bookingQueue);

        // Process bookings
        bookingService.processBookings();

        // Display allocated rooms
        bookingService.printAllocatedRooms();
    }
}