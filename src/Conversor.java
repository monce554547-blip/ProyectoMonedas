import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Map;

public class Conversor {

    private static final String API_KEY = "5c53b821e08782c29e95040a";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final Gson gson = new Gson();

    public double convertir(String from, String to, double monto)
            throws IOException, InterruptedException {

        // Pedimos todas las tasas del "from"
        String url = BASE_URL + API_KEY + "/latest/" + from.toUpperCase();
        HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) {
            throw new IOException("HTTP " + res.statusCode() + " al consultar la API");
        }

        LatestResponse data = gson.fromJson(res.body(), LatestResponse.class);

        if (data == null || !"success".equalsIgnoreCase(data.result)) {
            throw new IOException("Error API: " + (data != null ? data.errorType : "respuesta nula"));
        }

        Double tasa = data.conversionRates.get(to.toUpperCase());
        if (tasa == null) {
            throw new IOException("No se encontró tasa para " + to);
        }

        return monto * tasa;
    }

    static class LatestResponse {
        String result;
        @SerializedName("conversion_rates")
        Map<String, Double> conversionRates;
        @SerializedName("error-type")
        String errorType;
    }
}
