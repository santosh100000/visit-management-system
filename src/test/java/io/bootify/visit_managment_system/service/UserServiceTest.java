package io.bootify.visit_managment_system.service;

import io.bootify.visit_managment_system.domain.Address;
import io.bootify.visit_managment_system.domain.Flat;
import io.bootify.visit_managment_system.domain.User;
import io.bootify.visit_managment_system.domain.Visitor;
import io.bootify.visit_managment_system.model.AddressDTO;
import io.bootify.visit_managment_system.model.UserDTO;
import io.bootify.visit_managment_system.model.UserStatus;
import io.bootify.visit_managment_system.repos.AddressRepository;
import io.bootify.visit_managment_system.repos.FlatRepository;
import io.bootify.visit_managment_system.repos.UserRepository;
import io.bootify.visit_managment_system.repos.VisitorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private AutoCloseable autoCloseable;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlatRepository flatRepository;

    @Mock
    private AddressRepository addressRepository;
    private  UserService userService;

    private User user;
    private UserDTO userDTO;
    private Flat flat;
    private Address address;
    private AddressDTO addressDTO;

    @Autowired
    private VisitorRepository visitorRepository;
    private Visitor visitor;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, flatRepository, addressRepository);


            flat = new Flat();
            flat.setNumber("U-38");

        user = User.builder().
                status(UserStatus.INACTIVE).
                id(1l).
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
                 id(3l).
                status(UserStatus.INACTIVE).
                email("santoshi@gmail.com").
                name("Santoshi Kharel").
                phone("0449579684").
                role("ADMIN").address(addressDTO).
                flatNumber(flat.getNumber()).
                build();

        visitor = Visitor.builder().
                id(1l).
                address(address).
                email("sank74@gmail.com").
                phone("Sashank").
                phone("0420423955").idNumber(9850382L).
                build();




    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createUserTest() {
    when(userRepository.save(any())).thenReturn(user);

        Long actualUserId = userService.create(userDTO);
//        User actualUSer = userService.findById(actualUserId);
        assertThat(actualUserId).isEqualTo(user.getId());
    }

    @Test
    public void findAllVisitor(){
        List<Visitor> visitorList = visitorRepository.findAll();
        Visitor visitor1 = visitorList.get(0);
        assertThat(visitor1).isEqualTo(visitor);

    }


}