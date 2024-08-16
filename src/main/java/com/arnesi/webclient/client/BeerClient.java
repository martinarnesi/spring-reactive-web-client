package com.arnesi.webclient.client;

import com.arnesi.webclient.model.BeerDto;
import com.arnesi.webclient.model.BeerPagedList;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface BeerClient {
  Mono<BeerPagedList> listBeers(Integer page, Integer size, String beerName, String beerStyle, Boolean showInventoryOnHand);
  Mono<BeerDto> getBeerById(UUID beerId, Boolean showInventoryOnHand);
  Mono<ResponseEntity<Void>> createBeer(BeerDto beerDto);
  Mono<ResponseEntity<Void>> updateBeer(UUID beerId, BeerDto beerDto);
  Mono<ResponseEntity<Void>> deleteBeer(UUID beerId);
  Mono<BeerDto> getBeerByUpc(String upc, Boolean showInventoryOnHand);
}