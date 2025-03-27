package com.ims.repository;

import com.ims.model.Invoice;
import com.ims.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM InvoiceItem i WHERE i.id = :itemId")
    Optional<InvoiceItem> findInvoiceItemById(@Param("itemId") Long itemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM InvoiceItem i WHERE i.id = :itemId")
    void deleteInvoiceItemById(@Param("itemId") Long itemId);
}
