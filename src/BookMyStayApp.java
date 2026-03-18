import java.util.*;

class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 1);
        availability.put("Double", 1);
    }

    public Map<String, Integer> getRoomAvailability() {
        return availability;
    }

    public void update(String type, int count) {
        availability.put(type, count);
    }
}

class RoomAllocationService {

    public void allocateRoom(Reservation r, RoomInventory inventory) {

        String type = r.roomType;
        int available = inventory.getRoomAvailability().get(type);

        if (available > 0) {
            inventory.update(type, available - 1);
            System.out.println(Thread.currentThread().getName() +
                    " booked " + type + " for " + r.guestName);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " failed for " + r.guestName);
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue queue;
    private RoomInventory inventory;
    private RoomAllocationService service;

    public ConcurrentBookingProcessor(BookingRequestQueue queue,
                                      RoomInventory inventory,
                                      RoomAllocationService service) {
        this.queue = queue;
        this.inventory = inventory;
        this.service = service;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getNextRequest();
            }

            synchronized (inventory) {
                service.allocateRoom(r, inventory);
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Double"));
        queue.addRequest(new Reservation("Vannathi", "Single"));

        RoomInventory inventory = new RoomInventory();
        RoomAllocationService service = new RoomAllocationService();

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory, service));

        t1.start();
        t2.start();
    }
}