package com.arnesi.webclient.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class BeerDto {
  private UUID id;
  private String beerName;
  private String beerStyle;
  private String upc;
  private Integer quantityOnHand;
  private BigDecimal price;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
}