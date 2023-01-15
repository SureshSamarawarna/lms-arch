package lk.ijse.dep9.service.custom.impl;

import lk.ijse.dep9.dao.DAOFactory;
import lk.ijse.dep9.dao.DAOTypes;
import lk.ijse.dep9.dao.custom.MemberDAO;
import lk.ijse.dep9.dao.exception.ConstraintViolationException;
import lk.ijse.dep9.dto.MemberDTO;
import lk.ijse.dep9.service.custom.MemberService;
import lk.ijse.dep9.service.exception.DuplicateException;
import lk.ijse.dep9.service.exception.InUseException;
import lk.ijse.dep9.service.exception.NotFoundException;
import lk.ijse.dep9.service.util.Converter;
import lk.ijse.dep9.util.ConnectionUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO;
    private final Converter converter;

    public MemberServiceImpl() {
        memberDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.MEMBER);
        converter = new Converter();
    }

    @Override
    public void signupMember(MemberDTO member) throws DuplicateException {
        if (memberDAO.existsByContact(member.getContact())) throw new DuplicateException("A member already exists with this contact number");
        member.setId(UUID.randomUUID().toString());
        memberDAO.save(converter.toMember(member));
    }

    @Override
    public void updateMemberDetails(MemberDTO member) throws NotFoundException {
        if (!memberDAO.existsById(member.getId())) throw new NotFoundException("Member doesn't exist");
        memberDAO.update(converter.toMember(member));
    }

    @Override
    public void removeMemberAccount(String memberId) throws NotFoundException, InUseException {
        if (!memberDAO.existsById(memberId)) throw new NotFoundException("Member doesn't exist");
        try {
            memberDAO.deleteById(memberId);
        } catch (ConstraintViolationException e) {
            throw new InUseException("Member details are still in use", e);
        }
    }

    @Override
    public MemberDTO getMemberDetails(String memberId) throws NotFoundException {
        return memberDAO.findById(memberId).map(converter::toMemberDTO)
                .orElseThrow(()-> new NotFoundException("Member doesn't exist"));
    }

    @Override
    public List<MemberDTO> findMembers(String query, int size, int page) {
        return memberDAO.findMembersByQuery(query, size, page).stream()
                .map(converter::toMemberDTO).collect(Collectors.toList());
    }
}
