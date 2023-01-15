package lk.ijse.dep9.dao.custom.impl;

import lk.ijse.dep9.dao.custom.IssueItemDAO;
import lk.ijse.dep9.dao.exception.ConstraintViolationException;
import lk.ijse.dep9.entity.IssueItem;
import lk.ijse.dep9.entity.IssueItemPK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IssueItemDAOImpl implements IssueItemDAO {

    private final Connection connection;

    public IssueItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long count() {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT COUNT(isbn) FROM issue_item");
            ResultSet rst = stm.executeQuery();
            rst.next();
            return rst.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(IssueItemPK issueItemPK) throws ConstraintViolationException {
        try {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM issue_item WHERE isbn = ? AND issue_id = ?");
            stm.setString(1, issueItemPK.getIsbn());
            stm.setInt(2, issueItemPK.getIssueId());
            stm.executeUpdate();
        } catch (SQLException e) {
            if (existsById(issueItemPK)) throw new ConstraintViolationException("Issue Item primary key still exists within other tables", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(IssueItemPK issueItemPK) {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT isbn FROM issue_item WHERE isbn = ? AND issue_id = ?");
            stm.setString(1, issueItemPK.getIsbn());
            stm.setInt(2, issueItemPK.getIssueId());
            return stm.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IssueItem> findAll() {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM issue_item");
            ResultSet rst = stm.executeQuery();
            List<IssueItem> issueItemList = new ArrayList<>();
            while (rst.next()) {
                String isbn = rst.getString("isbn");
                int issueId = rst.getInt("issue_id");
                issueItemList.add(new IssueItem(issueId, isbn));
            }
            return issueItemList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<IssueItem> findById(IssueItemPK issueItemPK) {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM issue_item WHERE isbn = ? AND issue_id = ?");
            stm.setString(1, issueItemPK.getIsbn());
            stm.setInt(2, issueItemPK.getIssueId());
            ResultSet rst = stm.executeQuery();
            if (rst.next()) {
                String isbn = rst.getString("isbn");
                int issueId = rst.getInt("issue_id");
                return Optional.of(new IssueItem(issueId, isbn));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IssueItem save(IssueItem issueItem) {
        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO issue_item (issue_id, isbn) VALUES (?, ?)");
            stm.setInt(1, issueItem.getIssueItemPK().getIssueId());
            stm.setString(2, issueItem.getIssueItemPK().getIsbn());
            if (stm.executeUpdate() == 1) {
                return issueItem;
            } else {
                throw new SQLException("Failed to save the issue item");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IssueItem update(IssueItem entity) throws ConstraintViolationException {
        return null;
    }

}
