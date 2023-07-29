package io.bootify.visit_managment_system.service;

import io.bootify.visit_managment_system.domain.Flat;
import io.bootify.visit_managment_system.domain.Visit;
import io.bootify.visit_managment_system.domain.Visitor;
import io.bootify.visit_managment_system.model.VisitDTO;
import io.bootify.visit_managment_system.model.VisitStatus;
import io.bootify.visit_managment_system.repos.FlatRepository;
import io.bootify.visit_managment_system.repos.VisitRepository;
import io.bootify.visit_managment_system.repos.VisitorRepository;
import io.bootify.visit_managment_system.util.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitorRepository visitorRepository;
    private final FlatRepository flatRepository;

    public VisitService(final VisitRepository visitRepository,
            final VisitorRepository visitorRepository, final FlatRepository flatRepository) {
        this.visitRepository = visitRepository;
        this.visitorRepository = visitorRepository;
        this.flatRepository = flatRepository;
    }

    public List<VisitDTO> findAll() {
        final List<Visit> visits = visitRepository.findAll(Sort.by("id"));
        return visits.stream()
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .toList();
    }

    public VisitDTO get(final Long id) {
        return visitRepository.findById(id)
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VisitDTO visitDTO) {
        final Visit visit = new Visit();
        mapToEntity(visitDTO, visit);
        return visitRepository.save(visit).getId();
    }

    public  void markEntry(Long id){
      Visit visit = visitRepository.findById(id).orElseThrow(() ->new NotFoundException("Visit not found"));
      if(VisitStatus.APPROVED.equals(visit.getStatus())){
          visit.setInTime(LocalDateTime.now());
      }
      else {

      }
      visitRepository.save(visit);
    }

    public  void markExit(Long id){
        Visit visit = visitRepository.findById(id).orElseThrow(() ->new NotFoundException("Visit not found"));
        if(VisitStatus.APPROVED.equals(visit.getStatus())){
            visit.setOutTime(LocalDateTime.now());
            visit.setStatus(VisitStatus.COMPLETED);
        }
        else {

        }
        visitRepository.save(visit);
    }


    public void update(final Long id, final VisitDTO visitDTO) {
        final Visit visit = visitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(visitDTO, visit);
        visitRepository.save(visit);
    }

    public void approveVisit(Long id){
        Visit visit = visitRepository.findById(id).orElseThrow(() ->new NotFoundException("Visit not found"));
        if(VisitStatus.WAITING.equals(visit.getStatus())){
            visit.setStatus(VisitStatus.APPROVED);
        }
        else {

        }
        visitRepository.save(visit);
    }

    public void rejectVisit(Long id){
        Visit visit = visitRepository.findById(id).orElseThrow(() ->new NotFoundException("Visit not found"));
        if(VisitStatus.WAITING.equals(visit.getStatus())){
            visit.setStatus(VisitStatus.REJECTED);
        }
        else {

        }
        visitRepository.save(visit);

    }

    public void delete(final Long id) {
        visitRepository.deleteById(id);
    }

    private VisitDTO mapToDTO(final Visit visit, final VisitDTO visitDTO) {
        visitDTO.setId(visit.getId());
        visitDTO.setStatus(visit.getStatus());
        visitDTO.setInTime(visit.getInTime());
        visitDTO.setOutTime(visit.getOutTime());
        visitDTO.setPurpose(visit.getPurpose());
        visitDTO.setImageUrl(visit.getImageUrl());
        visitDTO.setNumOfPeople(visit.getNumOfPeople());
        visitDTO.setVisitor(visit.getVisitor() == null ? null : visit.getVisitor().getId());
        visitDTO.setFlat(visit.getFlat() == null ? null : visit.getFlat().getId());
        return visitDTO;
    }

    private Visit mapToEntity(final VisitDTO visitDTO, final Visit visit) {
        visit.setStatus(VisitStatus.WAITING);
        visit.setInTime(visitDTO.getInTime());
        visit.setOutTime(visitDTO.getOutTime());
        visit.setPurpose(visitDTO.getPurpose());
        visit.setImageUrl(visitDTO.getImageUrl());
        visit.setNumOfPeople(visitDTO.getNumOfPeople());
        final Visitor visitor = visitDTO.getVisitor() == null ? null : visitorRepository.findById(visitDTO.getVisitor())
                .orElseThrow(() -> new NotFoundException("visitor not found"));
        visit.setVisitor(visitor);
        final Flat flat = visitDTO.getFlat() == null ? null : flatRepository.findById(visitDTO.getFlat())
                .orElseThrow(() -> new NotFoundException("flat not found"));
        visit.setFlat(flat);
        return visit;
    }

}
