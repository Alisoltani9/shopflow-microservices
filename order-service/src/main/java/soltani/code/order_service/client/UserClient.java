package soltani.code.order_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient.Builder webClientBuilder;
    private static final String USER_SERVICE_URL = "http://localhost:8081";


    public boolean userExist(Long userId)
    {
        try {
            webClientBuilder.build()
                    .get()
                    .uri(USER_SERVICE_URL + "/api/users/" + userId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        }
        catch (WebClientResponseException.NotFound e)
        {
            return false;
        }

    }

}
