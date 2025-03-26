package com.ims.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    @NotNull(message = "User details must not be null")
    @Valid
    private UserDetailRequest user;

    @NotEmpty(message = "Product list must not be empty")
    private List<@Valid ProductRequest> products;
}
