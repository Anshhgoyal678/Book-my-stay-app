import java.util.*;

// Represents a confirmed reservation
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

// Maintains a chronological list of confirmed reservations
class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add confirmed reservation to history
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Added to booking history: " + reservation.getReservationID());
    }

    // Retrieve all reservations in chronological order
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedBookings);
    }
}

// Service for generating reports from booking history
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Print all bookings
    public void printAllBookings() {
        System.out.println("\nAll Confirmed Bookings:");
        for (Reservation r : bookingHistory.getAllReservations()) {
            System.out.println(r);
        }
    }

    // Print summary report (count by room type)
    public void printSummaryReport() {
        System.out.println("\nBooking Summary Report:");
        Map<String, Integer> roomCounts = new HashMap<>();
        for (Reservation r : bookingHistory.getAllReservations()) {
            roomCounts.put(r.getRoomType(), roomCounts.getOrDefault(r.getRoomType(), 0) + 1);
        }
        for (String roomType : roomCounts.keySet()) {
            System.out.println(roomType + ": " + roomCounts.get(roomType) + " booking(s)");
        }
    }
}

// Main program simulating booking history and reporting
public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("R-1001", "Alice", "Single", 2);
        Reservation r2 = new Reservation("R-1002", "Bob", "Suite", 3);
        Reservation r3 = new Reservation("R-1003", "Charlie", "Single", 1);
        Reservation r4 = new Reservation("R-1004", "David", "Double", 2);

        // Add confirmed bookings to history
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);
        bookingHistory.addReservation(r4);

        // Admin requests reports
        reportService.printAllBookings();
        reportService.printSummaryReport();
    }
}