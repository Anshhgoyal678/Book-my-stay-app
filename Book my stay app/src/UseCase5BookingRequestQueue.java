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

// Manages booking requests in arrival order (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Accept a new booking request
    public void submitRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request submitted: " + reservation.getGuestName());
    }

    // Peek at the next request without removing
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Process requests in FIFO order
    public Reservation processNextRequest() {
        return requestQueue.poll();
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }

    // Print all pending requests
    public void printPendingRequests() {
        if (requestQueue.isEmpty()) {
            System.out.println("No pending booking requests.");
            return;
        }
        System.out.println("Pending booking requests:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }
}

// Main program simulating booking request intake
public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submit booking requests
        bookingQueue.submitRequest(new Reservation("Alice", "Single", 2));
        bookingQueue.submitRequest(new Reservation("Bob", "Suite", 3));
        bookingQueue.submitRequest(new Reservation("Charlie", "Double", 1));

        // Print current pending requests
        bookingQueue.printPendingRequests();

        // Simulate processing requests in FIFO order
        System.out.println("\nProcessing booking requests (FIFO):");
        while (!bookingQueue.isEmpty()) {
            Reservation next = bookingQueue.processNextRequest();
            System.out.println("Processing: " + next);
        }

        // Confirm queue is empty
        bookingQueue.printPendingRequests();
    }
}