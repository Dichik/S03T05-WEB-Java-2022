package org.agency.repository.master;

import org.agency.entity.Master;
import org.agency.repository.BaseRepository;
import org.agency.repository.PersonRepository;

public interface MasterRepository extends BaseRepository<Master>, PersonRepository<Master> {
}
