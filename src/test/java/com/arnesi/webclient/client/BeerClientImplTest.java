package com.arnesi.webclient.client;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.arnesi.webclient.config.WebClientConfig;
import com.arnesi.webclient.model.BeerDto;
import com.arnesi.webclient.model.BeerPagedList;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

class BeerClientImplTest {

  BeerClientImpl beerClient;

  @BeforeEach
  void setUp() {
    beerClient = new BeerClientImpl(new WebClientConfig().webClient());
  }

  @Test
  void listBeers() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null,
        null, null);

    BeerPagedList pagedList = beerPagedListMono.block();

    assertThat(pagedList).isNotNull();
    assertThat(pagedList.getContent().size()).isGreaterThan(1);
    System.out.println(pagedList.toList());
  }

  @Test
  void listBeersPageSize() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null,
        null, null);

    BeerPagedList pagedList = beerPagedListMono.block();

    assertThat(pagedList).isNotNull();
    assertThat(pagedList.getContent().size()).isEqualTo(10);
  }

  @Test
  void listBeersPageNoRecords() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(15, 22, null,
        null, null);

    BeerPagedList pagedList = beerPagedListMono.block();

    assertThat(pagedList).isNotNull();
    assertThat(pagedList.getContent().size()).isEqualTo(0);
  }

  @Test
  void createBeer() {
    BeerDto beerDto = BeerDto.builder()
        .beerName("Quilnes")
        .beerStyle("IPA")
        .upc("564654654654")
        .price(new BigDecimal(10.99))
        .build();

    Mono<ResponseEntity<Void>> beerResponse = beerClient.createBeer(beerDto);

    ResponseEntity<Void> beerResponseBlock = beerResponse.block();

    assertThat(beerResponseBlock).isNotNull();
    assertThat(beerResponseBlock.getStatusCode()).isEqualTo(HttpStatus.CREATED);
 }

  @Test
  void updateBeer() {
  }

  @Test
  void deleteBeer() {
  }

  @Test
  void getBeerByUpc() {
  }
}