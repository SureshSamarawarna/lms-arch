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
import lk.ijse.dep9.dto.ReturnDTO;
import lk.ijse.dep9.dto.ReturnItemDTO;
import lk.ijse.dep9.exception.ResponseStatusException;
import lk.ijse.dep9.service.ServiceFactory;
import lk.ijse.dep9.service.ServiceTypes;
import lk.ijse.dep9.service.custom.ReturnService;
import lk.ijse.dep9.util.ConnectionUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "ReturnServlet", value = "/returns/*")
public class ReturnServlet extends HttpServlet2 {

    @Resource(lookup = "java:comp/env/jdbc/dep9-lms")
    private DataSource pool;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() != null && !request.getPathInfo().equals("/")) {
            throw new ResponseStatusException(501);
        }

        try {
            if (request.getContentType() == null ||
                    !request.getContentType().startsWith("application/json")) {
                throw new JsonbException("Invalid JSON");
            }

            ReturnDTO returnDTO = JsonbBuilder.create().fromJson(request.getReader(), ReturnDTO.class);
            addReturnItems(returnDTO, response);
        } catch (JsonbException e) {
            throw new ValidationException(e.getMessage());
        }

    }

    private void addReturnItems(ReturnDTO returnDTO, HttpServletResponse response) throws IOException {
        /* Data Validation */
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ReturnDTO>> violations = validator.validate(returnDTO);
        violations.stream().findAny().ifPresent(violate -> {
            throw new ValidationException(violate.getMessage());
        });

        /* Business Validation */
        try (Connection connection = pool.getConnection()) {
            ConnectionUtil.setConnection(connection);
            ReturnService returnService = ServiceFactory.getInstance().getService(ServiceTypes.RETURN);
            returnService.updateReturnStatus(returnDTO);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            JsonbBuilder.create().toJson(returnDTO, response.getWriter());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
