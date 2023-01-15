package lk.ijse.dep9.service.custom;

import lk.ijse.dep9.dto.MemberDTO;
import lk.ijse.dep9.service.SuperService;
import lk.ijse.dep9.service.exception.DuplicateException;
import lk.ijse.dep9.service.exception.InUseException;
import lk.ijse.dep9.service.exception.NotFoundException;

import java.util.List;

public interface MemberService extends SuperService {

    void signupMember(MemberDTO member) throws DuplicateException;

    void updateMemberDetails(MemberDTO member) throws NotFoundException;

    void removeMemberAccount(String memberId) throws NotFoundException, InUseException;

    MemberDTO getMemberDetails(String memberId) throws NotFoundException;

    List<MemberDTO> findMembers(String query, int size, int page);

}
