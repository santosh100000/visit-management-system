package io.bootify.visit_managment_system.repos;

import io.bootify.visit_managment_system.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VisitRepository extends JpaRepository<Visit, Long> {
}
