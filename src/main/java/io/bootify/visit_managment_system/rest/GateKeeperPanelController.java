package io.bootify.visit_managment_system.rest;

import io.bootify.visit_managment_system.model.VisitDTO;
import io.bootify.visit_managment_system.model.VisitStatus;
import io.bootify.visit_managment_system.model.VisitorDTO;
import io.bootify.visit_managment_system.service.VisitService;
import io.bootify.visit_managment_system.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/gatekeeper")
public class GateKeeperPanelController {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitService visitService;

    @PostMapping("/createVisitor")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody @Valid final VisitorDTO visitorDTO) {
        final Long createdId = visitorService.create(visitorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PostMapping("/createVisit")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid VisitDTO visitDTO) {
//        visitDTO.setStatus(VisitStatus.WAITING);
//        visitDTO.setInTime(LocalDateTime.now());
        final Long createdId = visitService.create(visitDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

//    @PutMapping("/updateVisit/{id}")
//    public ResponseEntity<Long> updateVisit(@PathVariable(name = "id") final Long id,
//                                            @RequestBody @Valid final VisitDTO visitDTO) {
//        visitService.update(id, visitDTO);
//        return ResponseEntity.ok(id);
//    }

    @PutMapping("/markEntry/{id}")
    public ResponseEntity<Long> markEntry(@PathVariable(name = "id") final Long id
                                            ) {
       visitService.markEntry(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/markExit/{id}")
    public ResponseEntity<Long> markExit(@PathVariable(name = "id") final Long id
                                          ) {
        //why final ? This wont let dev to change value

        visitService.markExit(id);
//        id = 20l, can't do it
        return ResponseEntity.ok(id);
    }

    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
          String response = "";
        try {
            String desktopPath = System.getProperty("user.home") + "/Desktop/";
            String uploadPath = desktopPath+System.currentTimeMillis()+ "_"+ file.getOriginalFilename();
            file.transferTo(new File(uploadPath));
            response = uploadPath;
        } catch (IOException e) {
            e.printStackTrace();
            response = "Exception";
            return  ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);

    }

}
