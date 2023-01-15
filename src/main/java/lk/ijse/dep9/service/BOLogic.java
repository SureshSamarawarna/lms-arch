package lk.ijse.dep9.service;

import lk.ijse.dep9.dao.DAOFactory;
import lk.ijse.dep9.dao.DAOTypes;
import lk.ijse.dep9.dao.custom.BookDAO;
import lk.ijse.dep9.dto.BookDTO;
import lk.ijse.dep9.dto.MemberDTO;
import lk.ijse.dep9.entity.Book;
import lk.ijse.dep9.util.ConnectionUtil;

public class BOLogic {

    public static boolean deleteMember(String memberId) {
        return true;
    }

    public static boolean createMember(MemberDTO member) {
        return true;
    }

    public static boolean updateMember(MemberDTO member) {
        return true;
    }

    public static boolean saveBook(BookDTO book) {
        BookDAO bookDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.BOOK);
        if (!bookDAO.existsById(book.getIsbn())){
            bookDAO.save(new Book(book.getIsbn(), book.getTitle(), book.getAuthor(),book.getCopies()));
            return true;
        }
        return false;
    }

    public static boolean updateBook(BookDTO book) {
        BookDAO bookDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(),DAOTypes.BOOK);
        if (!bookDAO.existsById(book.getIsbn())){
            bookDAO.update( new Book(book.getIsbn(), book.getTitle(), book.getAuthor(),book.getCopies()));
            return true;
        }
        return false;
    }

}
