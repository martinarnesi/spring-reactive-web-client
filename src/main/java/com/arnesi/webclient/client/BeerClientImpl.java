package com.arnesi.webclient.client;

import com.arnesi.webclient.config.WebClientProperties;
import com.arnesi.webclient.model.BeerDto;
import com.arnesi.webclient.model.BeerPagedList;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

  private final WebClient webClient;


  @Override
  public Mono<BeerPagedList> listBeers(Integer page, Integer size, String beerName,
      String beerStyle, Boolean showInventoryOnHand) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH)
            .queryParamIfPresent("pageNumber", Optional.ofNullable(page))
            .queryParamIfPresent("pageSize", Optional.ofNullable(size))
            .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
            .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
            .queryParamIfPresent("showInventoryOnhand", Optional.ofNullable(showInventoryOnHand))
            .build())
        .retrieve().bodyToMono(BeerPagedList.class);
  }

  @Override
  public Mono<BeerDto> getBeerById(UUID beerId, Boolean showInventoryOnHand) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH_GET_BY_ID)
            .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
            .build(beerId))
        .retrieve().bodyToMono(BeerDto.class);
  }


  @Override
  public Mono<ResponseEntity<Void>> createBeer(BeerDto beerDto) {
    return webClient.post()
        .uri(WebClientProperties.BEER_V1_PATH)
        .bodyValue(beerDto)
        .retrieve()
        .toBodilessEntity();
  }

  @Override
  public Mono<ResponseEntity<Void>> updateBeer(UUID beerId, BeerDto beerDto) {
    return webClient.put()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH_GET_BY_ID)
            .build(beerId)).body(BodyInserters.fromValue(beerDto)).retrieve().toBodilessEntity();
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteBeer(UUID beerId) {
    return webClient.delete()
        .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH_GET_BY_ID)
            .build(beerId)).retrieve().toBodilessEntity();
  }

  @Override
  public Mono<BeerDto> getBeerByUpc(String upc, Boolean showInventoryOnHand) {
    return webClient.get().uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_UPC_PATH)
            .queryParamIfPresent("showInventoryOnhand", Optional.ofNullable(showInventoryOnHand))
            .build(upc))
        .retrieve().bodyToMono(BeerDto.class);
  }
}

