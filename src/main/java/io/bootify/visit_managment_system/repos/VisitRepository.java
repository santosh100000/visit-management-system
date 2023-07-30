package io.bootify.visit_managment_system.repos;

import io.bootify.visit_managment_system.domain.Visit;
import io.bootify.visit_managment_system.model.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByStatus(VisitStatus status);
}
