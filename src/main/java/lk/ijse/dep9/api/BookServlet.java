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
import lk.ijse.dep9.dto.BookDTO;
import lk.ijse.dep9.exception.ResponseStatusException;
import lk.ijse.dep9.service.BOLogic;
import lk.ijse.dep9.service.ServiceFactory;
import lk.ijse.dep9.service.ServiceTypes;
import lk.ijse.dep9.service.custom.BookService;
import lk.ijse.dep9.util.ConnectionUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "BookServlet", value = "/books/*", loadOnStartup = 0)
public class BookServlet extends HttpServlet2 {

    @Resource(lookup = "java:comp/env/jdbc/dep9-lms")
    private DataSource pool;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {

            String query = request.getParameter("q");
            String size = request.getParameter("size");
            String page = request.getParameter("page");

            if (query != null && size != null && page != null) {
                if (!size.matches("\\d+") || !page.matches("\\d+")) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page or size");
                } else {
                    searchPaginatedBooks(query, Integer.parseInt(size), Integer.parseInt(page), response);
                }
            } else if (query != null) {
                //searchBooks(query, response);
            } else if (size != null && page != null) {
                if (!size.matches("\\d+") || !page.matches("\\d+")) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page or size");
                } else {
                    //loadAllPaginatedBooks(Integer.parseInt(size), Integer.parseInt(page), response);
                }
            } else {
                //loadAllBooks(response);
            }
        } else {
            Matcher matcher = Pattern.compile("^/([0-9][0-9\\\\-]*[0-9])/?$")
                    .matcher(request.getPathInfo());
            if (matcher.matches()) {
                getBookDetails(matcher.group(1), response);
            } else {
                throw new ResponseStatusException(501);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            saveBook(request, response);
        } else {
            throw new ResponseStatusException(501);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            throw new ResponseStatusException(501);
        }

        Matcher matcher = Pattern.
                compile("^/([0-9][0-9\\\\-]*[0-9])/?$")
                .matcher(request.getPathInfo());
        if (matcher.matches()) {
            updateBookDetails(matcher.group(1), request, response);
        } else {
            throw new ResponseStatusException(501);
        }
    }

    private void searchPaginatedBooks(String query, int size, int page, HttpServletResponse response) throws IOException {
        try (Connection connection = pool.getConnection()) {
            ConnectionUtil.setConnection(connection);
            BookService bookService= ServiceFactory.getInstance().getService(ServiceTypes.BOOK);
            List<BookDTO> books = bookService.findBooks(query, size, page);
            response.setIntHeader("X-Total-Count", books.size());
            response.setContentType("application/json");
            JsonbBuilder.create().toJson(books, response.getWriter());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getBookDetails(String isbn, HttpServletResponse response) throws IOException {
        try (Connection connection = pool.getConnection()) {
            ConnectionUtil.setConnection(connection);
            BookService bookService = ServiceFactory.getInstance().getService(ServiceTypes.BOOK);
            BookDTO bookDetails = bookService.getBookDetails(isbn);
            response.setContentType("application/json");
            JsonbBuilder.create().toJson(bookDetails, response.getWriter());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (request.getContentType() == null || !request.getContentType().startsWith("application/json")) {
                throw new JsonbException("Invalid JSON");
            }

            BookDTO book = JsonbBuilder.create().
                    fromJson(request.getReader(), BookDTO.class);

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<BookDTO>> violations = validator.validate(book);
            violations.stream().findAny().ifPresent(violation -> {
                throw new ValidationException(violation.getMessage());
            });

            try (Connection connection = pool.getConnection()) {
                ConnectionUtil.setConnection(connection);
                BookService bookService = ServiceFactory.getInstance().getService(ServiceTypes.BOOK);
                bookService.addNewBook(book);
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json");
                JsonbBuilder.create().toJson(book, response.getWriter());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (JsonbException e) {
            throw new ValidationException(e);
        }
    }

    private void updateBookDetails(String isbn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (request.getContentType() == null || !request.getContentType().startsWith("application/json")) {
                throw new JsonbException("Invalid JSON");
            }
            BookDTO book = JsonbBuilder.create().fromJson(request.getReader(), BookDTO.class);

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<BookDTO>> violations = validator.validate(book);
            violations.stream().findAny().ifPresent(violation -> {
                throw new ValidationException(violation.getMessage());
            });

            if (!book.getIsbn().equals(isbn)) throw new ValidationException("Book isbns are mismatched");

            try (Connection connection = pool.getConnection()) {
                ConnectionUtil.setConnection(connection);
                BookService bookService = ServiceFactory.getInstance().getService(ServiceTypes.BOOK);
                bookService.updateBookDetails(book);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (JsonbException e) {
            throw new ValidationException(e);
        }
    }
}
