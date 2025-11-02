package no.roadaccidentrisk.website.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import no.roadaccidentrisk.website.dto.PredictRequest;
import no.roadaccidentrisk.website.dto.PredictResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MlClient {

    private final WebClient webClient;

    public MlClient(@Value("${ml.base-url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public double predict(PredictRequest request) {

        try {
            String json = new ObjectMapper().writeValueAsString(request);
            System.out.println("Payload to ML service: " + json);
        } catch (Error | JsonProcessingException ignored){
        }

        PredictResponse response = webClient.post()
                .uri("/predict")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PredictResponse.class)
                .onErrorMap(e -> new RuntimeException("No responce from MLClient =(. Error: " + e.getMessage()))
                .block();
        return (response != null) ? response.getRisk() : Double.NaN;
    }
}
