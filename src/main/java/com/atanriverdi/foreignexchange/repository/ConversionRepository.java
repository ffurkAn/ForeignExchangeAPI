package com.atanriverdi.foreignexchange.repository;

import com.atanriverdi.foreignexchange.model.entity.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long>, JpaSpecificationExecutor<Conversion> {

    Page<Conversion> findAllByTransactionDateAndTransactionId(LocalDate localDate, String transactionId, Pageable pageable);
}
