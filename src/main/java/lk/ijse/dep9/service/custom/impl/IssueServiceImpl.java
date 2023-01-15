package lk.ijse.dep9.service.custom.impl;

import lk.ijse.dep9.dao.DAOFactory;
import lk.ijse.dep9.dao.DAOTypes;
import lk.ijse.dep9.dao.custom.*;
import lk.ijse.dep9.dto.IssueNoteDTO;
import lk.ijse.dep9.entity.IssueItem;
import lk.ijse.dep9.entity.IssueNote;
import lk.ijse.dep9.service.custom.IssueService;
import lk.ijse.dep9.service.exception.AlreadyIssuedException;
import lk.ijse.dep9.service.exception.LimitExceedException;
import lk.ijse.dep9.service.exception.NotAvailableException;
import lk.ijse.dep9.service.exception.NotFoundException;
import lk.ijse.dep9.service.util.Converter;
import lk.ijse.dep9.service.util.Executor;
import lk.ijse.dep9.util.ConnectionUtil;

import java.util.List;

public class IssueServiceImpl implements IssueService {

    private final IssueNoteDAO issueNoteDAO;
    private final IssueItemDAO issueItemDAO;
    private final MemberDAO memberDAO;
    private final BookDAO bookDAO;
    private final QueryDAO queryDAO;
    private final Converter converter;

    public IssueServiceImpl() {
        issueNoteDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.ISSUE_NOTE);
        issueItemDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.ISSUE_ITEM);
        memberDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.MEMBER);
        bookDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.BOOK);
        queryDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.QUERY);
        converter = new Converter();
    }

    @Override
    public void placeNewIssueNote(IssueNoteDTO issueNoteDTO) throws NotFoundException, NotAvailableException, LimitExceedException, AlreadyIssuedException {
        // Check member existence
        if (!memberDAO.existsById(issueNoteDTO.getMemberId())) throw new NotFoundException("Member doesn't exist");
        // Check books existence and availability
        // Check whether a book (in the issue note) has been already issued to this member
        for (String isbn : issueNoteDTO.getBooks()) {
            int availableCopies = queryDAO.getAvailableBookCopies(isbn).
                    orElseThrow(() -> new NotFoundException("Book: " + isbn + " doesn't exist"));
            if (availableCopies == 0) throw new NotAvailableException("Book: " + isbn + " not available at the moment");
            if (queryDAO.isAlreadyIssued(isbn, issueNoteDTO.getMemberId()))
                throw new AlreadyIssuedException("Book: " + isbn + " has been already issued to the same member");
        }
        // Check how many books can be issued for this member (maximum = 3)
        Integer availableLimit = queryDAO.availableBookLimit(issueNoteDTO.getMemberId()).get();
        if (availableLimit < issueNoteDTO.getBooks().size())
            throw new LimitExceedException("Member's book limit has been exceeded");
        try {
            ConnectionUtil.getConnection().setAutoCommit(false);

            IssueNote issueNote = converter.toIssueNote(issueNoteDTO);
            List<IssueItem> issueItemList = converter.toIssueItemList(issueNoteDTO);

            int issueNoteId = issueNoteDAO.save(issueNote).getId();
            issueItemList.forEach(item -> {
                item.getIssueItemPK().setIssueId(issueNoteId);
                issueItemDAO.save(item);
            });

            ConnectionUtil.getConnection().commit();
        } catch (Throwable t) {
            Executor.execute(ConnectionUtil.getConnection()::rollback);
            throw new RuntimeException(t);
        } finally {
            Executor.execute(() -> ConnectionUtil.getConnection().setAutoCommit(true));
        }
    }
}
