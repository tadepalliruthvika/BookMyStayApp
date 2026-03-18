import java.util.*;

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
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

    public void updateAvailability(String type, int count) {
        availability.put(type, count);
    }
}

class RoomAllocationService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

    public RoomAllocationService() {
        assignedRoomsByType.put("Single", new HashSet<>());
        assignedRoomsByType.put("Double", new HashSet<>());
        assignedRoomsByType.put("Suite", new HashSet<>());
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String type = reservation.getRoomType();
        int available = inventory.getRoomAvailability().get(type);

        if (available <= 0) {
            System.out.println("No rooms available for " + type);
            return;
        }

        String roomId = generateRoomId(type);

        allocatedRoomIds.add(roomId);
        assignedRoomsByType.get(type).add(roomId);

        inventory.updateAvailability(type, available - 1);

        System.out.println("Booking Confirmed");
        System.out.println("Guest: " + reservation.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Room ID: " + roomId + "\n");
    }

    private String generateRoomId(String type) {
        return type.substring(0, 1).toUpperCase() + (allocatedRoomIds.size() + 1);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        Queue<Reservation> queue = new LinkedList<>();

        queue.add(new Reservation("Abhi", "Single"));
        queue.add(new Reservation("Subha", "Double"));
        queue.add(new Reservation("Vannathi", "Suite"));

        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        while (!queue.isEmpty()) {
            Reservation r = queue.poll();
            service.allocateRoom(r, inventory);
        }
    }
}