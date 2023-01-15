package lk.ijse.dep9.service;

import lk.ijse.dep9.service.custom.impl.BookServiceImpl;
import lk.ijse.dep9.service.custom.impl.IssueServiceImpl;
import lk.ijse.dep9.service.custom.impl.MemberServiceImpl;
import lk.ijse.dep9.service.custom.impl.ReturnServiceImpl;

public class ServiceFactory {
    private static ServiceFactory serviceFactory;

    private ServiceFactory(){

    }

    public static ServiceFactory getInstance(){
        return (serviceFactory == null) ? (serviceFactory = new ServiceFactory()): serviceFactory;
    }

    public <T extends SuperService> T getService(ServiceTypes serviceType){
        switch (serviceType){
            case BOOK:
                return (T) new BookServiceImpl();
            case ISSUE:
                return (T) new IssueServiceImpl();
            case MEMBER:
                return (T) new MemberServiceImpl();
            case RETURN:
                return (T) new ReturnServiceImpl();
            default:
                return null;
        }
    }
}
