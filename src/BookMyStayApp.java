import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {

    private Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }
}

class ReservationValidator {

    public void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inventory.getRoomAvailability().containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type");
        }

        if (inventory.getRoomAvailability().get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available");
        }
    }
}

class BookingRequestQueue {

    private Queue<String> queue = new LinkedList<>();

    public void addRequest(String request) {
        queue.offer(request);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Validation\n");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue queue = new BookingRequestQueue();

        try {

            System.out.print("Enter Guest Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Room Type (Single/Double/Suite): ");
            String type = scanner.nextLine();

            validator.validate(name, type, inventory);

            queue.addRequest(name + " - " + type);

            System.out.println("Booking request added successfully");

        } catch (InvalidBookingException e) {

            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}