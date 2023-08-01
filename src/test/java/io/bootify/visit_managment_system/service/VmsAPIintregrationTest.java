package io.bootify.visit_managment_system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bootify.visit_managment_system.domain.Address;
import io.bootify.visit_managment_system.domain.Flat;
import io.bootify.visit_managment_system.domain.User;
import io.bootify.visit_managment_system.domain.Visitor;
import io.bootify.visit_managment_system.model.AddressDTO;
import io.bootify.visit_managment_system.model.UserDTO;
import io.bootify.visit_managment_system.model.UserStatus;
import io.bootify.visit_managment_system.model.VisitorDTO;
import io.bootify.visit_managment_system.repos.AddressRepository;
import io.bootify.visit_managment_system.repos.FlatRepository;
import io.bootify.visit_managment_system.repos.UserRepository;
import io.bootify.visit_managment_system.repos.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VmsAPIintregrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private  UserService userService;

    @Autowired
    private VisitorRepository visitorRepository;

    private User user;
    private UserDTO userDTO;
    private Flat flat;
    private Address address;
    private AddressDTO addressDTO;

    private VisitorDTO visitorDTO;
    private Visitor visitor;


@BeforeEach
    void setUp() {

        userService = new UserService(userRepository, flatRepository, addressRepository);


        flat = new Flat();
        flat.setNumber("U-38");

        user = User.builder().
                status(UserStatus.INACTIVE).
                email("santoshKharel9684@gmail.com").
                name("Santosh Kharel").
                phone("0449579684").
                role("ADMIN").
                flat(flat).address(address).
                build();


        flat.setUser(user);


        address = Address.
                builder().
                line1("21 Christina Stead Street").
                line2("Franklin").
                city("Canberra").
                country("Australia")
                .build();

        addressDTO = AddressDTO.
                builder().
                line1("21 Christina Stead Street").
                line2("Franklin").
                city("Canberra").
                country("Australia")
                .build();

        userDTO = UserDTO.builder().
                status(UserStatus.INACTIVE).
                email("santoshi@gmail.com").
                name("Santoshi Kharel").
                phone("0449579684").
                role("ADMIN").address(addressDTO).
                flatNumber(flat.getNumber()).
                build();

        visitorDTO = VisitorDTO.builder().
                address(addressDTO).
                email("sank74@gmail.com").
                name("Sashank").
                phone("0420423955").idNumber(9850382L).
                build();

        visitor = Visitor.builder().
                id(1l).
                address(address).
                email("sank74@gmail.com").
                phone("Sashank").
                phone("0420423955").idNumber(9850382L).
                build();

    }



    @Test
    public void testCreateUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Integer initialSize = userRepository.findAll().size();
        String jsonData = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/api/admin/createUser").contentType
                ("application/json").content(jsonData)).andDo(print())
                .andExpect(status().isCreated());
        Integer finalSize = userRepository.findAll().size();
        assertThat(finalSize-initialSize).isEqualTo(1);
    }

    @Test
    public void testCreateVisitor() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(visitorDTO);
        mockMvc.perform(post("/api/gatekeeper/createVisitor").
                contentType("application/json").content(jsonData)
                ).andDo(print()).andExpect(status().isCreated());

        List<Visitor> visitorList = visitorRepository.findAll();
        Visitor visitor1 = visitorList.get(0);

        assertThat(visitor1.getEmail()).isEqualTo(visitorDTO.getEmail());


    }

    @Test
    public void findAllVisitor(){
        List<Visitor> visitorList = visitorRepository.findAll();

        assertThat(visitorList.size()).isEqualTo(0);

    }

    @Test
    public void findVisitorById(){
        Visitor visitor = visitorRepository.findById(1l).get();

        assertThat(visitor.getName()).isEqualTo("santosh");

    }


}
