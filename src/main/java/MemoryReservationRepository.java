//Memory implementation of reservation warehouse
import java.util.ArrayList;
import java.util.List;

public class MemoryReservationRepository implements IReservationRepository {
  private final List<Reservation> data = new ArrayList<>();
  public void save(Reservation reservation) { data.add(reservation); }
  public void delete(String userId, String bookId) {
    data.removeIf(r -> r.getUserId().equals(userId) && r.getBookId().equals(bookId));
  }
  public boolean existsByUserAndBook(String userId, String bookId) {
    for (var r : data) if (r.getUserId().equals(userId) && r.getBookId().equals(bookId)) return true;
    return false;
  }
  public List<Reservation> findByUser(String userId) {
    List<Reservation> out = new ArrayList<>();
    for (var r : data) if (r.getUserId().equals(userId)) out.add(r);
    return out;
  }
  public List<Reservation> findByBook(String bookId) {
    List<Reservation> out = new ArrayList<>();
    for (var r : data) if (r.getBookId().equals(bookId)) out.add(r);
    return out;
  }
}
