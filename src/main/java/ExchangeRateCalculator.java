
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class ExchangeRateCalculator {
    private String baseCurrency;
    private Integer savings;
    private String urlString;

    public static void main(String[] args) {
        final String apiKey = "03d736f7027a0c5c85e4a8ed";
        ExchangeRateCalculator obj = new ExchangeRateCalculator();
        System.out.println("Where would my savings make me a MILLIONAIR: ");
        obj.getInput();
        obj.findCurrencies();
    }

    public void getInput() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Savings (Basiscurrency): ");
        baseCurrency = scan.nextLine();
        System.out.println("Savings amount: ");
        savings = scan.nextInt();

        urlString = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
        scan.close();
    }

    public void findCurrencies() {
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // Exception wenn die Verbing zu lang dauert
            connection.setReadTimeout(10000); // Exception wenn der Server nichts schickt

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            // Get the exchange rates
            JsonObject rates = jsonResponse.getAsJsonObject("rates");
            for (Map.Entry<String, JsonElement> entry : rates.entrySet()) {

                if (savings * entry.getValue().getAsDouble() >= 1000000) {
                    System.out.printf("%5s: %12.2f (Exchange rate: %12.4f)%n", entry.getKey(),
                            savings * entry.getValue().getAsDouble(), entry.getValue().getAsDouble());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
