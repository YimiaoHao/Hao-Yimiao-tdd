import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ReservationServicePriorityTest {

    @Test
    void priorityUserEnqueuedWhenNoCopies() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        var waitlist = new MemoryWaitlistRepository();

        var service = new ReservationService(books, reservations, waitlist);

        books.save(new Book("b1", "X", 0)); // Out of stock

        service.reservePriority("uP", "b1"); // Expect to enter the waiting queue

        assertFalse(reservations.existsByUserAndBook("uP", "b1"));
        assertEquals(1, waitlist.size("b1"));
    }

    @Test
    void cancelAssignsFirstWaitingUserAndKeepsCopiesAtZero() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        var waitlist = new MemoryWaitlistRepository();
        var service = new ReservationService(books, reservations, waitlist);

        books.save(new Book("b2", "Y", 1));

        service.reserve("u1", "b2");
        service.reservePriority("uP", "b2");

        service.cancel("u1", "b2");

        assertTrue(reservations.existsByUserAndBook("uP", "b2"));
        assertEquals(0, books.findById("b2").getCopiesAvailable());
    }

    @Test
    void waitlistIsFifoAcrossMultipleCancellations() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        var waitlist = new MemoryWaitlistRepository();
        var service = new ReservationService(books, reservations, waitlist);

        books.save(new Book("b3", "Z", 1));

        service.reserve("u1", "b3");
        service.reservePriority("uA", "b3");
        service.reservePriority("uB", "b3");

        service.cancel("u1", "b3");
        assertTrue(reservations.existsByUserAndBook("uA", "b3"));
        assertFalse(reservations.existsByUserAndBook("uB", "b3"));
        assertEquals(0, books.findById("b3").getCopiesAvailable());

        service.cancel("uA", "b3");
        assertTrue(reservations.existsByUserAndBook("uB", "b3"));
        assertEquals(0, books.findById("b3").getCopiesAvailable());
    }

    @Test
    void queuedUserUsingNormalReserveAfterRestock_removesTheirWaitEntry() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        var waitlist = new MemoryWaitlistRepository();
        var s = new ReservationService(books, reservations, waitlist);

        books.save(new Book("b3c", "QueueClean", 0));
        s.reservePriority("uA", "b3c");
        s.reservePriority("uB", "b3c");
        assertEquals(2, waitlist.size("b3c"));

        books.findById("b3c").setCopiesAvailable(1);


        s.reserve("uA", "b3c");


        assertTrue(reservations.existsByUserAndBook("uA", "b3c"));
        assertEquals(0, books.findById("b3c").getCopiesAvailable());
        assertEquals(1, waitlist.size("b3c"));
    }
}