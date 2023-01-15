package lk.ijse.dep9.dao.custom;

import com.github.javafaker.Faker;
import lk.ijse.dep9.dao.DAOFactory;
import lk.ijse.dep9.dao.DAOTypes;
import lk.ijse.dep9.util.ConnectionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QueryDAOTest {

    private QueryDAO queryDAO;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException, URISyntaxException, IOException {
        connection = DriverManager.getConnection("jdbc:h2:mem:");
        String dbScript = Files.readString(Paths.get(getClass().getResource("/db-script.sql").toURI()));
        connection.createStatement().execute(dbScript);
        ConnectionUtil.setConnection(connection);
        queryDAO = DAOFactory.getInstance().getDAO(ConnectionUtil.getConnection(), DAOTypes.QUERY);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void getAvailableBookCopies() {
        Faker faker = new Faker();
        String isbn1 = faker.code().isbn10();
        String isbn2 = "1234-7891";
        String isbn3 = "4567-1234";

        assertFalse(queryDAO.getAvailableBookCopies(isbn1).isPresent());
        assertEquals(2, queryDAO.getAvailableBookCopies(isbn2).get());
        assertEquals(1, queryDAO.getAvailableBookCopies(isbn3).get());
    }

    @Test
    void isAlreadyIssued() {
        String memberId = "2714641a-301e-43d5-9d31-ad916d075ba7";
        String isbn = "1234-7891";
        String isbn2 = "9874-1234";

        assertTrue(queryDAO.isAlreadyIssued(isbn, memberId));
        assertFalse(queryDAO.isAlreadyIssued(isbn2, memberId));
    }

    @Test
    void availableBookLimit() {
        String memberId1 = UUID.randomUUID().toString();
        String memberId2 = "104ccff3-c584-4782-a582-8a06479b46f6";
        assertTrue(queryDAO.availableBookLimit(memberId1).isEmpty());
        assertTrue(queryDAO.availableBookLimit(memberId2).get() < 3);
    }
}