package io.bootify.visit_managment_system.repos;

import io.bootify.visit_managment_system.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    boolean existsByEmailIgnoreCase(String email);

}
