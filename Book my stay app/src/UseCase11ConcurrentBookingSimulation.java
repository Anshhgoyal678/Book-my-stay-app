import java.util.*;
import java.util.concurrent.*;

// Represents a booking request
class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Thread-safe inventory service
class InventoryService {
    private final Map<String, Integer> availability;

    public InventoryService(Map<String, Integer> initialInventory) {
        this.availability = new HashMap<>(initialInventory);
    }

    // Thread-safe room allocation
    public synchronized boolean allocateRoom(String roomType) {
        int count = availability.getOrDefault(roomType, 0);
        if (count > 0) {
            availability.put(roomType, count - 1);
            return true;
        }
        return false;
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

// Thread-safe booking processor
class BookingProcessor {
    private final InventoryService inventoryService;
    private final List<String> confirmedBookings; // Records confirmed guests

    public BookingProcessor(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.confirmedBookings = Collections.synchronizedList(new ArrayList<>());
    }

    public void processBooking(ReservationRequest request) {
        boolean allocated = inventoryService.allocateRoom(request.getRoomType());
        if (allocated) {
            confirmedBookings.add(request.getGuestName() + " (" + request.getRoomType() + ")");
            System.out.println("Booking confirmed for " + request.getGuestName() + " [" + request.getRoomType() + "]");
        } else {
            System.out.println("Booking failed for " + request.getGuestName() + " [" + request.getRoomType() + "] - No availability");
        }
    }

    public void printConfirmedBookings() {
        System.out.println("\nConfirmed Bookings:");
        for (String s : confirmedBookings) {
            System.out.println(s);
        }
    }
}

// Runnable task representing a guest booking attempt
class BookingTask implements Runnable {
    private final ReservationRequest request;
    private final BookingProcessor processor;

    public BookingTask(ReservationRequest request, BookingProcessor processor) {
        this.request = request;
        this.processor = processor;
    }

    @Override
    public void run() {
        processor.processBooking(request);
    }
}

// Main simulation
public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {
        // Initial inventory
        Map<String, Integer> inventoryMap = new HashMap<>();
        inventoryMap.put("Single", 2);
        inventoryMap.put("Double", 1);

        InventoryService inventoryService = new InventoryService(inventoryMap);
        BookingProcessor processor = new BookingProcessor(inventoryService);

        // Simulate multiple guests submitting requests concurrently
        List<ReservationRequest> requests = Arrays.asList(
                new ReservationRequest("Alice", "Single"),
                new ReservationRequest("Bob", "Single"),
                new ReservationRequest("Charlie", "Single"),  // Should fail due to limited inventory
                new ReservationRequest("David", "Double"),
                new ReservationRequest("Eve", "Double")      // Should fail due to limited inventory
        );

        // Use thread pool for concurrent execution
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (ReservationRequest req : requests) {
            executor.execute(new BookingTask(req, processor));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        processor.printConfirmedBookings();
        inventoryService.printInventory();
    }
}