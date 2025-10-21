//Reservation warehouse interface
import java.util.List;

public interface IReservationRepository {
  void save(Reservation reservation);
  void delete(String userId, String bookId);
  boolean existsByUserAndBook(String userId, String bookId);
  List<Reservation> findByUser(String userId);
  List<Reservation> findByBook(String bookId);
}

