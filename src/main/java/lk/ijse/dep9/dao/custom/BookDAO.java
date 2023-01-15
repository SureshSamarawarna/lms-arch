package lk.ijse.dep9.dao.custom;

import lk.ijse.dep9.dao.CrudDAO;
import lk.ijse.dep9.entity.Book;

import java.util.List;

public interface BookDAO extends CrudDAO<Book, String> {

    List<Book> findBooksByQuery(String query);

    List<Book> findBooksByQuery(String query, int size, int page);

    List<Book> findAllBooks(int size, int page);
}
