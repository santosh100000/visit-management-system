package io.bootify.visit_managment_system.rest;


import io.bootify.visit_managment_system.model.*;
import io.bootify.visit_managment_system.service.UserService;
import io.bootify.visit_managment_system.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminPanelController {
    @Autowired
    private UserService userService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private RedisTemplate<String, VisitListRedisDTO> redisTemplate;

    @PostMapping("/createUser")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final Long createdId = userService.create(userDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/markInactive/{id}")
    public ResponseEntity<Void> markInactive(@PathVariable Long id){
        userService.markInactive(id);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/user-csv/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("file")MultipartFile file){
             List<String> response = new ArrayList<>();

             try{
                 BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));

                 CSVFormat csvFormat = CSVFormat.DEFAULT
                         .withHeader("name", "email", "phone", "flatNumber", "role", "status", "line1", "line2", "pincode", "city", "country")
                         .withFirstRecordAsHeader()
                         .withIgnoreHeaderCase()
                         .withTrim();

//                 CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

                 CSVParser csvParser = new CSVParser(fileReader, csvFormat);

                 Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                 List<UserDTO> userDTOList = new ArrayList<>();

                 for(CSVRecord csvRecord : csvRecords){
                     UserDTO userDTO = new UserDTO();
                     userDTO.setName(csvRecord.get("name"));
                     userDTO.setEmail(csvRecord.get("email"));
                     userDTO.setPhone(csvRecord.get("phone"));
                     userDTO.setFlatNumber(csvRecord.get("flatNumber"));
                     userDTO.setRole(csvRecord.get("role"));
                     userDTO.setStatus(UserStatus.valueOf(csvRecord.get("status")));

                     AddressDTO addressDTO = new AddressDTO();
                     addressDTO.setLine1(csvRecord.get("line1"));
                     addressDTO.setLine2(csvRecord.get("line2"));
                     addressDTO.setPincode(Integer.valueOf(csvRecord.get("pincode")));
                     addressDTO.setCity(csvRecord.get("city"));
                     addressDTO.setCountry(csvRecord.get("country"));

                     userDTO.setAddress(addressDTO);

                     try {
                       Long id = userService.create(userDTO);
                       response.add("Created user "+userDTO.getName() + " with id "+id);
                     }catch(Exception e){
                         response.add("Exception while creating "+userDTO.getName());
                     }


                 }


             }catch (Exception e){
               response.add("Exception : "+e.getMessage());
             }
             return ResponseEntity.ok(response);
    }
    
    @GetMapping("/allVisits")
    public ResponseEntity<List<VisitDTO>> getAllVisit(@RequestParam Integer pageSize, @RequestParam Integer pageNo){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNo);
      List<VisitDTO> visitDTOS = visitService.findAll(pageable);
      return ResponseEntity.ok(visitDTOS);
    }

    @GetMapping("/allExpireVisits")
    public ResponseEntity<List<VisitDTO>> getAllExpireVisits(){
        String key = "ExpiredVisits";
        VisitListRedisDTO visitListRedisDTO = redisTemplate.opsForValue().get(key);
        if(visitListRedisDTO == null){
            List<VisitDTO> visitDTOS = visitService.findAllExpireVisits();
            visitListRedisDTO = new VisitListRedisDTO();
            visitListRedisDTO.setVisitDTOList(visitDTOS);
            redisTemplate.opsForValue().set(key, visitListRedisDTO);

        }

        return ResponseEntity.ok(visitListRedisDTO.getVisitDTOList());
    }

}
