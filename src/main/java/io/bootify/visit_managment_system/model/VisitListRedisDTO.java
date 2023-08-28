package io.bootify.visit_managment_system.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VisitListRedisDTO implements Serializable {
    private List<VisitDTO> visitDTOList;
}
