package lk.ijse.dep9.service.custom;

import lk.ijse.dep9.dto.BookDTO;
import lk.ijse.dep9.service.SuperService;
import lk.ijse.dep9.service.exception.DuplicateException;
import lk.ijse.dep9.service.exception.NotFoundException;

import java.util.List;

public interface BookService extends SuperService {

    void addNewBook(BookDTO dto) throws DuplicateException;

    void updateBookDetails(BookDTO dto) throws NotFoundException;

    BookDTO getBookDetails(String isbn) throws NotFoundException;

    List<BookDTO> findBooks(String query, int size, int page);

}
