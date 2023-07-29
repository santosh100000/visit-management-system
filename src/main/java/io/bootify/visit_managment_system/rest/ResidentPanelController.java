package io.bootify.visit_managment_system.rest;


import io.bootify.visit_managment_system.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resident")

public class ResidentPanelController {

    @Autowired
    private VisitService visitService;

    @PutMapping("/approve/{id}")
    public ResponseEntity<Void> approveVisit(@PathVariable Long id){
        visitService.approveVisit(id);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Void> rejectVisit(@PathVariable Long id){
        visitService.rejectVisit(id);
        return ResponseEntity.ok().build();

    }
}
