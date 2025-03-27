package com.ims.dto;

import com.ims.model.InvoiceItem;
import com.ims.model.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private LocalDateTime createdAt;
    private List<InvoiceItem> items;
    private UserDetail user;
}
