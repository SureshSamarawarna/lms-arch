package lk.ijse.dep9.dao.custom;

import lk.ijse.dep9.dao.CrudDAO;
import lk.ijse.dep9.entity.Member;

import java.util.List;

public interface MemberDAO extends CrudDAO<Member, String> {

    boolean existsByContact(String contact);

    List<Member> findMembersByQuery(String query);

    List<Member> findMembersByQuery(String query, int size, int page);

    List<Member> findAllMembers(int size, int page);
}
