package com.arnesi.webclient.client;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.arnesi.webclient.config.WebClientConfig;
import com.arnesi.webclient.model.BeerDto;
import com.arnesi.webclient.model.BeerPagedList;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
    Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null,null, null, null);

    BeerPagedList pagedList = beerPagedListMono.block();
    BeerDto beerDto = pagedList.getContent().get(0);

    BeerDto updatedBeer = BeerDto.builder()
        .beerName("Santa fe")
        .beerStyle(beerDto.getBeerStyle())
        .price(beerDto.getPrice())
        .upc(beerDto.getUpc())
        .build();

    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerDto.getId(),
        updatedBeer);
    ResponseEntity<Void> responseEntity = responseEntityMono.block();

    assertThat(responseEntity).isNotNull();
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deleteBeer() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null,null, null, null);

    BeerPagedList pagedList = beerPagedListMono.block();
    BeerDto beerDto = pagedList.getContent().get(0);

    ResponseEntity<Void> response = beerClient.deleteBeer(beerDto.getId()).block();

    response.getStatusCode();

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void deleteBeerByIdNotFound() {
    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(UUID.randomUUID());

    assertThrows(WebClientResponseException.class, () -> {
      ResponseEntity<Void> block = responseEntityMono.block();
      assertThat(block.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    });
  }

  @Test
  void deleteBeerByIdNotFoundOnErrorResume() {
    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(UUID.randomUUID());

    ResponseEntity<Void> responseEntity = responseEntityMono.onErrorResume(throwable -> {
      if (throwable instanceof WebClientResponseException) {
        WebClientResponseException webClientResponseException = (WebClientResponseException) throwable;
        return Mono.just(ResponseEntity.status(webClientResponseException.getStatusCode()).build());
      } else {
        throw new RuntimeException(throwable);
      }
    }).block();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void getBeerByUpc() {
  }
}