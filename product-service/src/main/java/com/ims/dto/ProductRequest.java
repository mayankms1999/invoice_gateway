package com.ims.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(
        Long id,

        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
        String description,

        @NotBlank(message = "HSN Code is required")
        @Pattern(regexp = "^[0-9]{6}$", message = "HSN Code must be exactly 6 digits")
        String hsnCode,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
        BigDecimal price
) {
}
