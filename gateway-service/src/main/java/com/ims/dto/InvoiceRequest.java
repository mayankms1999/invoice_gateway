package com.ims.dto;

import com.ims.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceRequest {
    private String invoiceNumber;
    private String invoiceDate;
    private String shippingName;
    private String shippingAddress;
    private String shippingEmail;
    private List<Product> products;
}
