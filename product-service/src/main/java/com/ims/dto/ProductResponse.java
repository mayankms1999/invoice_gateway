package com.ims.dto;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, String hsnCode, BigDecimal price) {

}