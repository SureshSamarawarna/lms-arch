package lk.ijse.dep9.service.util;

import com.github.javafaker.Faker;
import lk.ijse.dep9.dto.BookDTO;
import lk.ijse.dep9.dto.IssueNoteDTO;
import lk.ijse.dep9.dto.MemberDTO;
import lk.ijse.dep9.dto.ReturnItemDTO;
import lk.ijse.dep9.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterTest {

    private final Converter converter = new Converter();

    @Test
    void toBookDTO() {
        Faker faker = new Faker();
        Book bookEntity = new Book(faker.code().isbn10(), faker.book().title(), faker.book().author(),
                faker.number().numberBetween(1, 3));
        BookDTO bookDTO = converter.toBookDTO(bookEntity);
        assertEquals(bookEntity.getIsbn(), bookDTO.getIsbn());
        assertEquals(bookEntity.getTitle(), bookDTO.getTitle());
        assertEquals(bookEntity.getAuthor(), bookDTO.getAuthor());
        assertEquals(bookEntity.getCopies(), bookDTO.getCopies());
    }

    @Test
    void toBook() {
        Faker faker = new Faker();
        BookDTO bookDTO = new BookDTO(faker.code().isbn10(), faker.book().title(), faker.book().author(),
                faker.number().numberBetween(1, 3));
        Book bookEntity = converter.toBook(bookDTO);
        assertEquals(bookDTO.getIsbn(), bookEntity.getIsbn());
        assertEquals(bookDTO.getTitle(), bookEntity.getTitle());
        assertEquals(bookDTO.getAuthor(), bookEntity.getAuthor());
        assertEquals(bookDTO.getCopies(), bookEntity.getCopies());
    }

    @Test
    void toMemberDTO() {
        Faker faker = new Faker();
        Member memberEntity = new Member(UUID.randomUUID().toString(),
                faker.name().fullName(), faker.address().fullAddress(),
                faker.regexify("0\\d{3}-\\d{7}"));

        MemberDTO memberDTO = converter.toMemberDTO(memberEntity);

        assertEquals(memberEntity.getId(), memberDTO.getId());
        assertEquals(memberEntity.getName(), memberDTO.getName());
        assertEquals(memberEntity.getAddress(), memberDTO.getAddress());
        assertEquals(memberEntity.getContact(), memberDTO.getContact());
    }

    @Test
    void toMember() {
        Faker faker = new Faker();
        MemberDTO memberDTO = new MemberDTO(UUID.randomUUID().toString(),
                faker.name().fullName(), faker.address().fullAddress(),
                faker.regexify("0\\d{3}-\\d{7}"));

        Member memberEntity = converter.toMember(memberDTO);

        assertEquals(memberDTO.getId(), memberEntity.getId());
        assertEquals(memberDTO.getName(), memberEntity.getName());
        assertEquals(memberDTO.getAddress(), memberEntity.getAddress());
        assertEquals(memberDTO.getContact(), memberEntity.getContact());
    }

    @Test
    void toIssueNote() {
        ArrayList<String> books = new ArrayList<>(Arrays.asList("1234-1234", "1111-1234", "4561-1234"));
        IssueNoteDTO issueNoteDTO = new IssueNoteDTO(1, LocalDate.now(),
                UUID.randomUUID().toString(), books);

        IssueNote issueNote = converter.toIssueNote(issueNoteDTO);

        assertEquals(issueNoteDTO.getId(), issueNote.getId());
        assertEquals(issueNoteDTO.getMemberId(), issueNote.getMemberId());
        assertEquals(issueNoteDTO.getDate().toString(), issueNote.getDate().toString());
    }

    @Test
    void toIssueItem() {
        ArrayList<String> books = new ArrayList<>(Arrays.asList("1234-1234", "1111-1234", "4561-1234"));
        IssueNoteDTO issueNoteDTO = new IssueNoteDTO(5, LocalDate.now(),
                UUID.randomUUID().toString(), books);

        List<IssueItem> issueItemList = converter.toIssueItemList(issueNoteDTO);

        assertEquals(issueNoteDTO.getBooks().size(), issueItemList.size());
        issueItemList.forEach(System.out::println);
    }

    @Test
    void toReturn() {
        ReturnItemDTO returnItemDTO = new ReturnItemDTO(3, "1234-1234");
        Return returnEntity = converter.toReturn(returnItemDTO);

        assertEquals(returnItemDTO.getIsbn(), returnEntity.getReturnPK().getIsbn());
        assertEquals(returnItemDTO.getIssueNoteId(), returnEntity.getReturnPK().getIssueId());
    }
}