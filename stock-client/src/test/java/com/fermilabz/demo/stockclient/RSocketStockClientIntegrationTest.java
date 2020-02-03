package com.fermilabz.demo.stockclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class RSocketStockClientIntegrationTest {

    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester createRSocketRequester() {
        return builder.connectTcp( "localhost", 7000 ).block();
    }

    @Test
    void shouldRetrieveStockPricesFromTheService() {
        RSocketStockClient rSocketStockClient = new RSocketStockClient( createRSocketRequester() );
        Flux<StockPrice> prices = rSocketStockClient.pricesFor("SYMBOL");
        Flux<StockPrice> fivePrices = prices.take(5);


        // A better way to test reactive applications...
        StepVerifier.create( prices.take( 2 ) )
                .expectNextMatches( stock -> stock.getSymbol().equals( "SYMBOL" ) )
                .expectNextMatches( stock -> stock.getSymbol().equals( "SYMBOL" ) )
                .verifyComplete();
    }
}