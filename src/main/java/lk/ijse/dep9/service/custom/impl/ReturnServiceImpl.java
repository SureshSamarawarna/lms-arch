package lk.ijse.dep9.service.custom.impl;

import lk.ijse.dep9.dao.DAOFactory;
import lk.ijse.dep9.dao.DAOTypes;
import lk.ijse.dep9.dao.custom.QueryDAO;
import lk.ijse.dep9.dao.custom.ReturnDAO;
import lk.ijse.dep9.dto.ReturnDTO;
import lk.ijse.dep9.entity.Return;
import lk.ijse.dep9.entity.ReturnPK;
import lk.ijse.dep9.service.custom.ReturnService;
import lk.ijse.dep9.service.exception.AlreadyReturnedException;
import lk.ijse.dep9.service.exception.NotFoundException;
import lk.ijse.dep9.service.util.Converter;
import lk.ijse.dep9.service.util.Executor;
import lk.ijse.dep9.util.ConnectionUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;

public class ReturnServiceImpl implements ReturnService {

    private final QueryDAO queryDAO;
    private final ReturnDAO returnDAO;
    private final Converter converter;

    public ReturnServiceImpl() {
        queryDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.QUERY);
        returnDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.RETURN);
        converter = new Converter();
    }

    @Override
    public void updateReturnStatus(ReturnDTO returnDTO) throws NotFoundException, AlreadyReturnedException {
        var returnItems = new HashSet<>(returnDTO.getReturnItems());
        try {
            ConnectionUtil.getConnection().setAutoCommit(false);
            returnItems.forEach(
                    returnItem -> {
                        if (!queryDAO.isValidIssueItem(returnDTO.getMemberId(),
                                returnItem.getIssueNoteId(), returnItem.getIsbn()))
                            throw new NotFoundException("Invalid return");
                        if (returnDAO.existsById(
                                new ReturnPK(returnItem.getIssueNoteId(), returnItem.getIsbn()))) {
                            throw new AlreadyReturnedException("Item has been already returned");
                        }
                        Return returnEntity = converter.toReturn(returnItem);
                        returnEntity.setDate(Date.valueOf(LocalDate.now()));
                        returnDAO.save(returnEntity);
                    });
            ConnectionUtil.getConnection().commit();
        } catch (Throwable t) {
            Executor.execute(ConnectionUtil.getConnection()::rollback);
        } finally {
            Executor.execute(() -> ConnectionUtil.getConnection().setAutoCommit(true));
        }
    }
}
