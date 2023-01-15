package lk.ijse.dep9.dao.custom;

import lk.ijse.dep9.dao.SuperDAO;

import java.util.Optional;

public interface QueryDAO extends SuperDAO {

    Optional<Integer> getAvailableBookCopies(String isbn);

    boolean isAlreadyIssued(String isbn, String memberId);

    Optional<Integer> availableBookLimit(String memberId);

    boolean isValidIssueItem(String memberId, int issueId, String isbn);
}
