package io.bootify.visit_managment_system.service;

import io.bootify.visit_managment_system.domain.Address;
import io.bootify.visit_managment_system.domain.Visitor;
import io.bootify.visit_managment_system.model.AddressDTO;
import io.bootify.visit_managment_system.model.VisitorDTO;
import io.bootify.visit_managment_system.repos.AddressRepository;
import io.bootify.visit_managment_system.repos.VisitorRepository;
import io.bootify.visit_managment_system.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final AddressRepository addressRepository;

    public VisitorService(final VisitorRepository visitorRepository,
            final AddressRepository addressRepository) {
        this.visitorRepository = visitorRepository;
        this.addressRepository = addressRepository;
    }

    public List<VisitorDTO> findAll() {
        final List<Visitor> visitors = visitorRepository.findAll(Sort.by("id"));
        return visitors.stream()
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .toList();
    }

    public VisitorDTO get(final Long id) {
        return visitorRepository.findById(id)
                .map(visitor -> mapToDTO(visitor, new VisitorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VisitorDTO visitorDTO) {
        final Visitor visitor = new Visitor();
        mapToEntity(visitorDTO, visitor);
        return visitorRepository.save(visitor).getId();
    }

    public void update(final Long id, final VisitorDTO visitorDTO) {
        final Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(visitorDTO, visitor);
        visitorRepository.save(visitor);
    }

    public void delete(final Long id) {
        visitorRepository.deleteById(id);
    }

    private VisitorDTO mapToDTO(final Visitor visitor, final VisitorDTO visitorDTO) {
        visitorDTO.setId(visitor.getId());
        visitorDTO.setEmail(visitor.getEmail());
        visitorDTO.setName(visitor.getName());
        visitorDTO.setPhone(visitor.getPhone());
        visitorDTO.setIdNumber(visitor.getIdNumber());
        final AddressDTO address = new AddressDTO();
        address.setLine1(visitor.getAddress().getLine1());
        address.setLine2(visitor.getAddress().getLine2());
        address.setCity(visitor.getAddress().getCity());
        address.setCountry(visitor.getAddress().getCountry());
        visitorDTO.setAddress(address);
        return visitorDTO;
    }

    private Visitor mapToEntity(final VisitorDTO visitorDTO, final Visitor visitor) {
        visitor.setEmail(visitorDTO.getEmail());
        visitor.setName(visitorDTO.getName());
        visitor.setPhone(visitorDTO.getPhone());
        visitor.setIdNumber(visitorDTO.getIdNumber());
        final Address address = new Address();
        address.setLine1(visitorDTO.getAddress().getLine1());
        address.setLine2(visitorDTO.getAddress().getLine2());
        address.setCity(visitorDTO.getAddress().getCity());
        address.setCountry(visitorDTO.getAddress().getCountry());
        addressRepository.save(address);
        visitor.setAddress(address);
        return visitor;
    }

    public boolean emailExists(final String email) {
        return visitorRepository.existsByEmailIgnoreCase(email);
    }

}
