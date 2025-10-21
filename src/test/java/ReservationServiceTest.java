import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ReservationServiceTest {
    @Test
    void reserve_success_decrements_and_saves() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();

        books.save(new Book("b1", "Clean Code", 2));

        var service = new ReservationService(books, reservations);
        
        service.reserve("u1", "b1");

        assertEquals(1, books.findById("b1").getCopiesAvailable());
        assertTrue(reservations.existsByUserAndBook("u1", "b1"));
    }
}