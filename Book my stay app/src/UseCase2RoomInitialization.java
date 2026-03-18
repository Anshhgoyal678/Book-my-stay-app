/**
 * UseCase2RoomInitialization
 *
 * This program demonstrates basic object modeling for a Hotel Booking System.
 * It defines an abstract Room class and concrete room types with static availability.
 * Version 2.1 reflects the refactored domain modeling introduction.
 *
 * Author: ChatGPT
 * Version: 2.1
 */
abstract class Room {
    protected String type;
    protected int beds;
    protected double size; // in square meters
    protected double price; // per night

    public Room(String type, int beds, double size, double price) {
        this.type = type;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    // Display room details
    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqm");
        System.out.println("Price per night: $" + price);
    }
}

// Concrete room classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 15.0, 50.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 25.0, 90.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 50.0, 200.0);
    }
}

// Application entry point
public class UseCase2RoomInitialization {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("Welcome to Hotel Booking System v2.1");
        System.out.println("Displaying Room Types & Availability");
        System.out.println("======================================\n");

        // Static availability for demonstration
        int singleRoomAvailable = 10;
        int doubleRoomAvailable = 5;
        int suiteRoomAvailable = 2;

        // Initialize room objects
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Display details and availability
        singleRoom.displayDetails();
        System.out.println("Available: " + singleRoomAvailable + "\n");

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleRoomAvailable + "\n");

        suiteRoom.displayDetails();
        System.out.println("Available: " + suiteRoomAvailable + "\n");

        System.out.println("End of Room Listing. Application terminates.");
    }
}