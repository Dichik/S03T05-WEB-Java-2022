package org.agency.service.master;

import org.agency.repository.master.MasterRepository;

public class MasterService {

    private final MasterRepository masterRepository;

    public MasterService(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

}
