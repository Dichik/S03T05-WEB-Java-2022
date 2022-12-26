package com.agency.finalproject.repository.master;

import com.agency.finalproject.entity.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * FIXME take names from properties
 */
@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {

    boolean existsByEmail(String email);

}
