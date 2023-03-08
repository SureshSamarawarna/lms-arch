package lk.ijse.dep9.api;

import jakarta.annotation.Resource;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lk.ijse.dep9.api.util.HttpServlet2;
import lk.ijse.dep9.dto.IssueNoteDTO;
import lk.ijse.dep9.exception.ResponseStatusException;
import lk.ijse.dep9.service.ServiceFactory;
import lk.ijse.dep9.service.ServiceTypes;
import lk.ijse.dep9.service.custom.IssueService;
import lk.ijse.dep9.util.ConnectionUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "IssueNoteServlet", value = "/issue-notes/*")
public class IssueNoteServlet extends HttpServlet2 {

    @Resource(lookup = "java:comp/env/jdbc/dep9-lms")
    private DataSource pool;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() != null && !request.getPathInfo().equals("/")) {
            throw new ResponseStatusException(501);
        }

        try {
            if (request.getContentType() == null || !request.getContentType().startsWith("application/json")) {
                throw new JsonbException("Invalid JSON");
            }

            IssueNoteDTO issueNote = JsonbBuilder.create().fromJson(request.getReader(), IssueNoteDTO.class);
            createNewIssueNote(issueNote, response);
        } catch (JsonbException e) {
            throw new ValidationException(e);
        }
    }

    private void createNewIssueNote(IssueNoteDTO issueNoteDTO, HttpServletResponse response) throws IOException {
        /* Data Validation */
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<IssueNoteDTO>> validate = validator.validate(issueNoteDTO);
        validate.stream().findAny().ifPresent(violate -> {
            throw new ValidationException(violate.getMessage());
        });
        if (issueNoteDTO.getBooks().stream().collect(Collectors.toSet()).size() !=
                issueNoteDTO.getBooks().size()) {
            throw new ValidationException("Duplicate isbn has been found");
        }
        try (Connection connection = pool.getConnection()) {
            ConnectionUtil.setConnection(connection);
            IssueService issueService = ServiceFactory.getInstance().getService(ServiceTypes.ISSUE);
            issueService.placeNewIssueNote(issueNoteDTO);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_CREATED);
            JsonbBuilder.create().toJson(issueNoteDTO, response.getWriter());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
