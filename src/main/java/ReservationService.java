import java.util.List;

public class ReservationService {
  private final IBookRepository bookRepo;
  private final IReservationRepository reservationRepo;

  public ReservationService(IBookRepository bookRepo, IReservationRepository reservationRepo) {
    this.bookRepo = bookRepo;
    this.reservationRepo = reservationRepo;
  }

  /**
   * Reserve a book for a user.
   * Throws IllegalArgumentException if book not found.
   * Throws IllegalStateException if no copies available or user already reserved.
   */
  public void reserve(String userId, String bookId) {

    if (reservationRepo.existsByUserAndBook(userId, bookId)) {
      throw new IllegalStateException("Already reserved");
    }

    Book book = bookRepo.findById(bookId);

    if (book == null) {
      throw new IllegalArgumentException("Book not found");
    }

    if (book.getCopiesAvailable() <= 0) {
      throw new IllegalStateException("No copies available");
    }
    book.setCopiesAvailable(book.getCopiesAvailable() - 1);
    reservationRepo.save(new Reservation(userId, bookId));
  }

  /**
   * Cancel an existing reservation for a user.
   * Throws IllegalArgumentException if no such reservation exists.
   */
  public void cancel(String userId, String bookId) {

    if (!reservationRepo.existsByUserAndBook(userId, bookId)) {
      throw new IllegalArgumentException("No such reservation");
    }

    Book book = bookRepo.findById(bookId);
    book.setCopiesAvailable(book.getCopiesAvailable() + 1);
    reservationRepo.delete(userId, bookId);
  }

  /** List all active reservations for a given user. */
  public List<Reservation> listReservations(String userId) {
    return reservationRepo.findByUser(userId);
  }

  /** List all reservations for a book. */
  public List<Reservation> listReservationsForBook(String bookId) {
    return reservationRepo.findByBook(bookId);
  }
}
