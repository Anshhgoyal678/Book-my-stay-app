import java.util.*;

// Domain model for Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = new ArrayList<>(amenities);
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return Collections.unmodifiableList(amenities);
    }

    @Override
    public String toString() {
        return "Room Type: " + type + ", Price: $" + price + ", Amenities: " + amenities;
    }
}

// Inventory holding availability counts (read-only for search)
class Inventory {
    private Map<String, Integer> availability;

    public Inventory(Map<String, Integer> availability) {
        // Defensive copy to prevent external mutation
        this.availability = new HashMap<>(availability);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    // Returns a read-only snapshot of availability
    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availability);
    }
}

// Service responsible for read-only search
class SearchService {
    private Inventory inventory;
    private Map<String, Room> rooms;

    public SearchService(Inventory inventory, List<Room> roomList) {
        this.inventory = inventory;
        this.rooms = new HashMap<>();
        for (Room room : roomList) {
            rooms.put(room.getType(), room);
        }
    }

    // Search method returns only available rooms
    public List<Room> searchAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (String roomType : rooms.keySet()) {
            int count = inventory.getAvailability(roomType);
            if (count > 0) { // Defensive check
                availableRooms.add(rooms.get(roomType));
            }
        }
        return availableRooms;
    }
}

// Main program simulating guest search
public class UseCase4RoomSearch {
    public static void main(String[] args) {
        // Setup inventory
        Map<String, Integer> availabilityMap = new HashMap<>();
        availabilityMap.put("Single", 5);
        availabilityMap.put("Double", 0); // Not available
        availabilityMap.put("Suite", 2);

        Inventory inventory = new Inventory(availabilityMap);

        // Setup rooms
        List<Room> rooms = Arrays.asList(
                new Room("Single", 100.0, Arrays.asList("WiFi", "TV")),
                new Room("Double", 150.0, Arrays.asList("WiFi", "TV", "Mini Bar")),
                new Room("Suite", 300.0, Arrays.asList("WiFi", "TV", "Mini Bar", "Jacuzzi"))
        );

        // Initialize search service
        SearchService searchService = new SearchService(inventory, rooms);

        // Guest initiates room search
        System.out.println("Available Rooms:");
        List<Room> availableRooms = searchService.searchAvailableRooms();
        for (Room room : availableRooms) {
            System.out.println(room);
        }
    }
}