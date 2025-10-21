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

    @Test
    void reserve_sameUserSameBook_twice_throws() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();

        books.save(new Book("b1", "Clean Code", 2));

        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1"); 

        assertThrows(IllegalStateException.class, () -> service.reserve("u1", "b1")); 
    }

    @Test
    void reserve_noCopies_throws() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();

        books.save(new Book("b1", "DDD", 0));

        var service = new ReservationService(books, reservations);

        assertThrows(IllegalStateException.class, () -> service.reserve("u1", "b1"));
    }
    
    @Test
    void reserve_bookNotFound_throws() {
        var books = new MemoryBookRepository(); 
        var reservations = new MemoryReservationRepository();

        var service = new ReservationService(books, reservations);

        assertThrows(IllegalArgumentException.class, () -> service.reserve("u1", "bX"));
    }

    @Test
    void cancel_existingReservation_increments_and_deletes() {
        var books = new MemoryBookRepository();
        var reservations = new MemoryReservationRepository();
        books.save(new Book("b1", "Clean Code", 2));
        var service = new ReservationService(books, reservations);

        service.reserve("u1", "b1");
        service.cancel("u1", "b1");

        assertEquals(2, books.findById("b1").getCopiesAvailable());
        assertFalse(reservations.existsByUserAndBook("u1", "b1"));
}



}