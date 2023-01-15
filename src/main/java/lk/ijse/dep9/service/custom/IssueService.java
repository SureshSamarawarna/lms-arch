package lk.ijse.dep9.service.custom;

import lk.ijse.dep9.dto.IssueNoteDTO;
import lk.ijse.dep9.service.SuperService;
import lk.ijse.dep9.service.exception.AlreadyIssuedException;
import lk.ijse.dep9.service.exception.LimitExceedException;
import lk.ijse.dep9.service.exception.NotAvailableException;
import lk.ijse.dep9.service.exception.NotFoundException;

public interface IssueService extends SuperService {

    void placeNewIssueNote(IssueNoteDTO issueNoteDTO) throws NotFoundException,
            NotAvailableException, LimitExceedException, AlreadyIssuedException;

}
