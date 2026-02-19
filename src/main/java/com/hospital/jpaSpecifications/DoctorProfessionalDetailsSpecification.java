package com.hospital.jpaSpecifications;

import com.hospital.entity.DoctorProfessionalDetails;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class DoctorProfessionalDetailsSpecification {

    public static Specification<DoctorProfessionalDetails>hasDepartment(String department){

        return (root,query,criteriaBuilder)->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("department")),department.toLowerCase());
    }

    public static Specification<DoctorProfessionalDetails> hasFeesPerConsult(BigDecimal feesPerConsult){

        return (root,query,criteriaBuilder)->
                criteriaBuilder.equal(root.get("feesPerConsult"),feesPerConsult);
    }

    public static Specification<DoctorProfessionalDetails> hasCity(String city){
        return (root,query,criteriaBuilder)->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("clinicAddress")), "%"+city.toLowerCase()+"%");
    }


}
