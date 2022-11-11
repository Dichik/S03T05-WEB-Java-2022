package org.agency.repository.user;

import org.agency.repository.PersonRepository;
import org.agency.entity.User;
import org.agency.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User>, PersonRepository<User> {
}
