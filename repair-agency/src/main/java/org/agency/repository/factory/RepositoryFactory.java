package org.agency.repository.factory;

import org.agency.entity.Role;
import org.agency.repository.BaseRepository;
import org.agency.repository.manager.ManagerRepository;
import org.agency.repository.master.MasterRepository;
import org.agency.repository.user.UserRepository;

import java.sql.Connection;

public class RepositoryFactory {

    public static BaseRepository<?> create(Connection connection, Role role) throws Exception {
        switch (role) {
            case MASTER:
                return new MasterRepository(connection);
            case USER:
                return new UserRepository(connection);
            case MANAGER:
                return new ManagerRepository(connection);
            default:
                throw new Exception();
        }
    }

}
