/**
 * UseCase3InventorySetup
 *
 * This program demonstrates centralized room inventory management
 * using a HashMap for a Hotel Booking System.
 * Version 3.1 reflects the refactored inventory management approach.
 *
 * Author: ChatGPT
 * Version: 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Room class hierarchy
abstract class Room {
    protected String type;
    protected int beds;
    protected double size;
    protected double price;

    public Room(String type, int beds, double size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqm");
        System.out.println("Price per night: $" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 1, 15.0, 50.0); }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 2, 25.0, 90.0); }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite Room", 3, 50.0, 200.0); }
}

// Centralized inventory management
class RoomInventory {
    private final Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
    }

    // Initialize inventory for a room type
    public void addRoomType(Room room, int count) {
        availability.put(room.getType(), count);
    }

    // Get current availability for a room type
    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    // Update availability (increment or decrement)
    public void updateAvailability(String roomType, int change) {
        int current = availability.getOrDefault(roomType, 0);
        int updated = current + change;
        if (updated < 0) updated = 0; // prevent negative availability
        availability.put(roomType, updated);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}

// Application entry point
public class UseCase3InventorySetup {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("Hotel Booking System v3.1");
        System.out.println("Centralized Room Inventory Management");
        System.out.println("======================================\n");

        // Initialize rooms
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(singleRoom, 10);
        inventory.addRoomType(doubleRoom, 5);
        inventory.addRoomType(suiteRoom, 2);

        // Display current inventory
        inventory.displayInventory();

        // Example updates
        System.out.println("\nBooking 2 Single Rooms and 1 Suite Room...\n");
        inventory.updateAvailability(singleRoom.getType(), -2);
        inventory.updateAvailability(suiteRoom.getType(), -1);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nEnd of Inventory Check. Application terminates.");
    }
}