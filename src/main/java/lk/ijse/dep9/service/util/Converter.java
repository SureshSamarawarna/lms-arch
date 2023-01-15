package lk.ijse.dep9.service.util;

import lk.ijse.dep9.dto.BookDTO;
import lk.ijse.dep9.dto.IssueNoteDTO;
import lk.ijse.dep9.dto.MemberDTO;
import lk.ijse.dep9.dto.ReturnItemDTO;
import lk.ijse.dep9.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Converter {

    private ModelMapper mapper;

    public Converter() {
        this.mapper = new ModelMapper();
        mapper.typeMap(LocalDate.class, Date.class).setConverter(mc -> Date.valueOf(mc.getSource()));
    }

    public BookDTO toBookDTO(Book bookEntity) {
        return mapper.map(bookEntity, BookDTO.class);
    }

    public Book toBook(BookDTO bookDTO) {
        return mapper.map(bookDTO, Book.class);
    }

    public MemberDTO toMemberDTO(Member memberEntity){
        return mapper.map(memberEntity, MemberDTO.class);
    }

    public Member toMember(MemberDTO memberDTO){
        return mapper.map(memberDTO, Member.class);
    }

    public IssueNote toIssueNote(IssueNoteDTO issueNoteDTO){
        return mapper.map(issueNoteDTO, IssueNote.class);
    }

    public List<IssueItem> toIssueItemList(IssueNoteDTO issueNoteDTO){
        Type typeToken = new TypeToken<List<IssueItem>>() {}.getType();
        mapper.typeMap(IssueNoteDTO.class, List.class)
                .setConverter(mc -> {
                    IssueNoteDTO source = mc.getSource();
                    return source.getBooks().stream().map(isbn ->
                            new IssueItem(source.getId(), isbn)).collect(Collectors.toList());
                });
        return mapper.map(issueNoteDTO, typeToken);
    }

    public Return toReturn(ReturnItemDTO returnItemDTO){
        mapper.typeMap(ReturnItemDTO.class, Return.class).
                setConverter(mc ->
                        new Return(null, mc.getSource().getIssueNoteId(), mc.getSource().getIsbn()));
        return mapper.map(returnItemDTO, Return.class);
    }
}
