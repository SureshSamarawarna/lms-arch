package lk.ijse.dep9.service.custom;

import com.github.javafaker.Faker;
import lk.ijse.dep9.dao.DAOFactory;
import lk.ijse.dep9.dao.DAOTypes;
import lk.ijse.dep9.dao.custom.MemberDAO;
import lk.ijse.dep9.dto.MemberDTO;
import lk.ijse.dep9.service.ServiceFactory;
import lk.ijse.dep9.service.ServiceTypes;
import lk.ijse.dep9.service.exception.DuplicateException;
import lk.ijse.dep9.service.exception.InUseException;
import lk.ijse.dep9.service.exception.NotFoundException;
import lk.ijse.dep9.service.util.Converter;
import lk.ijse.dep9.util.ConnectionUtil;
import lombok.Cleanup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    private MemberService memberService;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:h2:mem:");
        @Cleanup BufferedReader bfr = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/db-script.sql")));
        String dbScript = bfr.lines().reduce((previous, current) -> previous + current).get();
        connection.createStatement().execute(dbScript);
        ConnectionUtil.setConnection(connection);
        memberService = ServiceFactory.getInstance().getService(ServiceTypes.MEMBER);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void signupMember() {
        Faker faker = new Faker();
        MemberDTO member1 = new MemberDTO(UUID.randomUUID().toString(), faker.name().fullName(),
                faker.address().fullAddress(), "078-1234567");
        MemberDTO member2 = new MemberDTO(UUID.randomUUID().toString(), faker.name().fullName(),
                faker.address().fullAddress(), faker.regexify("0\\d{2}-\\d{7}"));

        assertThrows(DuplicateException.class, () -> memberService.signupMember(member1));
        memberService.signupMember(member2);
    }

    @Test
    void updateMemberDetails() {
        Faker faker = new Faker();
        MemberDAO memberDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.MEMBER);
        MemberDTO member1 = new Converter().
                toMemberDTO(memberDAO.findById("104ccff3-c584-4782-a582-8a06479b46f6").get());
        member1.setName(faker.name().fullName());
        member1.setAddress(faker.address().fullAddress());
        member1.setContact(faker.regexify("0\\d{2}-\\d{7}"));

        MemberDTO member2 = new MemberDTO(UUID.randomUUID().toString(), faker.name().fullName(),
                faker.address().fullAddress(), faker.regexify("0\\d{2}-\\d{7}"));

        memberService.updateMemberDetails(member1);
        MemberDTO updatedMember = new Converter().
                toMemberDTO(memberDAO.findById(member1.getId()).get());
        assertEquals(member1, updatedMember);
        assertThrows(NotFoundException.class, () -> memberService.updateMemberDetails(member2));
    }

    @Test
    void removeMemberAccount() {
        String member1Id = UUID.randomUUID().toString();
        String member2Id = "104ccff3-c584-4782-a582-8a06479b46f6";
        String member3Id = "2714641a-301e-43d5-9d31-ad916d075ba6";

        assertThrows(NotFoundException.class, () -> memberService.removeMemberAccount(member1Id));
        assertThrows(InUseException.class, () -> memberService.removeMemberAccount(member2Id));
        assertDoesNotThrow(() -> memberService.removeMemberAccount(member3Id));

        MemberDAO memberDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.MEMBER);
        assertFalse(memberDAO.existsById(member3Id));
    }

    @Test
    void getMemberDetails() {
        String member1Id = UUID.randomUUID().toString();
        String member2Id = "104ccff3-c584-4782-a582-8a06479b46f6";

        assertThrows(NotFoundException.class, ()-> memberService.getMemberDetails(member1Id));
        MemberDTO memberDetails = memberService.getMemberDetails(member2Id);
        assertNotNull(memberDetails.getName());
        assertNotNull(memberDetails.getAddress());
        assertNotNull(memberDetails.getContact());
        System.out.println(memberDetails);
    }

    @Test
    void findMembers() {
        List<MemberDTO> memberList1 = memberService.findMembers("", 2, 1);
        List<MemberDTO> memberList2 = memberService.findMembers("", 2, 2);
        List<MemberDTO> memberList3 = memberService.findMembers("Galle", 10, 1);

        assertEquals(2, memberList1.size());
        assertEquals(1, memberList2.size());
        assertEquals(2, memberList3.size());

        memberList3.forEach(System.out::println);
    }
}