import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Conversor {

    private static final String API_KEY = "af0bc028004ebffc23bcf091";

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.println("\n Bienvenido a tu Conversor de Monedas personalizado!");

        while (true) {
            System.out.println("\n Opciones disponibles:");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Salir");
            System.out.print("Elija una opción: ");
            int opcion = entrada.nextInt();
            entrada.nextLine();

            if (opcion == 1) {
                System.out.println("\n🌎 Monedas disponibles:");
                System.out.println("USD - Dólar estadounidense");
                System.out.println("PEN - Sol peruano");
                System.out.println("ARS - Peso argentino");
                System.out.println("BRL - Real brasileño");
                System.out.println("COP - Peso colombiano");
                System.out.println("EUR - Euro");
                System.out.println("CLP - Peso chileno");
                System.out.println("MXN - Peso mexicano");
                System.out.println("BOB - Boliviano");
                System.out.println("PYG - Guaraní paraguayo");

                System.out.print("\n🔁 Moneda de origen (ej: USD): ");
                String from = entrada.nextLine().toUpperCase();

                System.out.print("➡️ Moneda destino (ej: PEN): ");
                String to = entrada.nextLine().toUpperCase();

                System.out.print("💵 Cantidad a convertir: ");
                double cantidad = entrada.nextDouble();
                entrada.nextLine(); // consumir salto de línea

                double resultado = convertirMoneda(from, to, cantidad);
                if (resultado >= 0) {
                    System.out.printf("✅ %.2f %s = %.2f %s\n", cantidad, from, resultado, to);
                } else {
                    System.out.println("❌ No se pudo completar la conversión.");
                }

            } else if (opcion == 2) {
                System.out.println("\n👋 ¡Gracias por usar el conversor! Hasta pronto.");
                break;
            } else {
                System.out.println("⚠️ Opción inválida. Intente nuevamente.");
            }
        }

        entrada.close();
    }

    public static double convertirMoneda(String from, String to, double cantidad) {
        try {
            String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + from + "/" + to;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            if (json.get("result").getAsString().equals("success")) {
                double tasa = json.get("conversion_rate").getAsDouble();
                return cantidad * tasa;
            } else {
                System.out.println("Error: " + json.get("error-type").getAsString());
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            return -1;
        }
    }
}
