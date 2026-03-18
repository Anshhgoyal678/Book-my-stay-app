import java.util.*;

// Represents a reservation with a unique room assignment
class Reservation {
    private String reservationID;
    private String guestName;
    private String roomType;
    private int numberOfNights;

    public Reservation(String reservationID, String guestName, String roomType, int numberOfNights) {
        this.reservationID = reservationID;
        this.guestName = guestName;
        this.roomType = roomType;
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

    public int getNumberOfNights() {
        return numberOfNights;
    }

    @Override
    public String toString() {
        return "Reservation [ID: " + reservationID + ", Guest: " + guestName +
                ", Room Type: " + roomType + ", Nights: " + numberOfNights + "]";
    }
}

// Represents an optional add-on service
class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " ($" + cost + ")";
    }
}

// Manages add-on services for reservations
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add one or more services to a reservation
    public void addServices(String reservationID, List<AddOnService> services) {
        reservationServices.computeIfAbsent(reservationID, k -> new ArrayList<>()).addAll(services);
        System.out.println("Added " + services.size() + " service(s) to reservation " + reservationID);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationID) {
        return reservationServices.getOrDefault(reservationID, Collections.emptyList());
    }

    // Calculate total additional cost for a reservation
    public double calculateTotalCost(String reservationID) {
        return getServices(reservationID).stream().mapToDouble(AddOnService::getCost).sum();
    }

    // Print all services for all reservations
    public void printAllServices() {
        System.out.println("\nAdd-On Services for Reservations:");
        for (String reservationID : reservationServices.keySet()) {
            List<AddOnService> services = reservationServices.get(reservationID);
            System.out.println("Reservation " + reservationID + ": " + services +
                    " | Total Additional Cost: $" + calculateTotalCost(reservationID));
        }
    }
}

// Main program simulating add-on service selection
public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        // Sample reservations
        Reservation r1 = new Reservation("R-1001", "Alice", "Single", 2);
        Reservation r2 = new Reservation("R-1002", "Bob", "Suite", 3);

        // Sample add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 20.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 50.0);
        AddOnService spa = new AddOnService("Spa Access", 80.0);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guests select services
        serviceManager.addServices(r1.getReservationID(), Arrays.asList(breakfast, airportPickup));
        serviceManager.addServices(r2.getReservationID(), Arrays.asList(spa, breakfast));

        // Print selected services and total additional cost
        serviceManager.printAllServices();
    }
}