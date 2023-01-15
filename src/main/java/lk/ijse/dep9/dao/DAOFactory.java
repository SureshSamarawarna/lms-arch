package lk.ijse.dep9.dao;

import lk.ijse.dep9.dao.custom.impl.*;

import java.sql.Connection;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {

    }

    public static DAOFactory getInstance() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public <T extends SuperDAO> T getDAO(Connection connection, DAOTypes daoType) {
        switch (daoType) {
            case MEMBER:
                return (T) new MemberDAOImpl(connection);
            case BOOK:
                return (T) new BookDAOImpl(connection);
            case QUERY:
                return (T) new QueryDAOImpl(connection);
            case RETURN:
                return (T) new ReturnDAOImpl(connection);
            case ISSUE_NOTE:
                return (T) new IssueNoteDAOImpl(connection);
            case ISSUE_ITEM:
                return (T) new IssueItemDAOImpl(connection);
            default:
                return null;
        }

    }

//    public MemberDAO getMemberDAO(Connection connection) {
//        return new MemberDAOImpl(connection);
//    }
//
//    public BookDAO getBookDAO(Connection connection) {
//        return new BookDAOImpl(connection);
//    }
//
//    public IssueNoteDAO getIssueNoteDAO(Connection connection) {
//        return new IssueNoteDAOImpl(connection);
//    }
//
//    public IssueItemDAO getIssueItemDAO(Connection connection) {
//        return new IssueItemDAOImpl(connection);
//    }
//
//    public ReturnDAO getReturnDAO(Connection connection) {
//        return new ReturnDAOImpl(connection);
//    }
//
//    public QueryDAO getQueryDAO(Connection connection) {
//        return new QueryDAOImpl(connection);
//    }
}
