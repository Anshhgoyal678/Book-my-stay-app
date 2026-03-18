import java.util.*;

// Custom exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Represents a reservation request
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

// Inventory service with validation
class InventoryService {
    private Map<String, Integer> availability;

    public InventoryService(Map<String, Integer> availability) {
        this.availability = new HashMap<>(availability);
    }

    // Validate room type exists
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!availability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check availability
    public boolean isAvailable(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        return availability.get(roomType) > 0;
    }

    // Allocate room with validation
    public void allocateRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        int count = availability.get(roomType);
        if (count <= 0) {
            throw new InvalidBookingException("No available rooms for type: " + roomType);
        }
        availability.put(roomType, count - 1);
    }

    public int getAvailability(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        return availability.get(roomType);
    }
}

// Validator for reservations
class BookingValidator {
    private InventoryService inventoryService;

    public BookingValidator(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void validateReservation(Reservation reservation) throws InvalidBookingException {
        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (reservation.getNumberOfNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be positive.");
        }
        if (!inventoryService.isAvailable(reservation.getRoomType())) {
            throw new InvalidBookingException("Room type unavailable: " + reservation.getRoomType());
        }
    }
}

// Main program demonstrating validation and error handling
public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        // Setup inventory
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single", 2);
        initialInventory.put("Double", 1);
        initialInventory.put("Suite", 1);

        InventoryService inventoryService = new InventoryService(initialInventory);
        BookingValidator validator = new BookingValidator(inventoryService);

        // Sample reservation requests
        List<Reservation> reservations = Arrays.asList(
                new Reservation("Alice", "Single", 2),
                new Reservation("Bob", "Penthouse", 3),   // Invalid room type
                new Reservation("Charlie", "Double", 0),  // Invalid nights
                new Reservation("", "Suite", 1),          // Empty guest name
                new Reservation("David", "Single", 1)
        );

        // Process reservations with validation and error handling
        for (Reservation r : reservations) {
            try {
                validator.validateReservation(r);
                inventoryService.allocateRoom(r.getRoomType());
                System.out.println("Reservation confirmed: " + r);
            } catch (InvalidBookingException e) {
                System.out.println("Failed to process reservation for " + r.getGuestName() + ": " + e.getMessage());
            }
        }

        // Print remaining inventory
        System.out.println("\nRemaining Inventory:");
        try {
            for (String roomType : Arrays.asList("Single", "Double", "Suite")) {
                System.out.println(roomType + ": " + inventoryService.getAvailability(roomType));
            }
        } catch (InvalidBookingException e) {
            System.out.println("Error checking inventory: " + e.getMessage());
        }
    }
}