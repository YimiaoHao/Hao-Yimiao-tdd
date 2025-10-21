//Memory implementation of book warehouse
// MemoryBookRepository.java
import java.util.HashMap;
import java.util.Map;

public class MemoryBookRepository implements IBookRepository {
  private final Map<String, Book> data = new HashMap<>();
  public void save(Book book) { data.put(book.getId(), book); }
  public Book findById(String id) { return data.get(id); }
}

