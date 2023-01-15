package lk.ijse.dep9.service.custom;

import lk.ijse.dep9.dto.ReturnDTO;
import lk.ijse.dep9.service.SuperService;
import lk.ijse.dep9.service.exception.AlreadyReturnedException;
import lk.ijse.dep9.service.exception.NotFoundException;

public interface ReturnService extends SuperService {

    void updateReturnStatus(ReturnDTO returnDTO) throws NotFoundException, AlreadyReturnedException;

}
