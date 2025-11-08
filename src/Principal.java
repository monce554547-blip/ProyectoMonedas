import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;

public class Principal {
    private static final DecimalFormat DF = new DecimalFormat("#,##0.00");

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);
        Conversor conversor = new Conversor();

        boolean seguir = true;
        while (seguir) {
            System.out.println("""
                    **********************************************
                    Sea bienvenido/a al Conversor de Moneda =)
                    **********************************************
                    1) Dólar => Peso argentino
                    2) Peso argentino => Dólar
                    3) Dólar => Real brasileño
                    4) Real brasileño => Dólar
                    5) Dólar => Peso colombiano
                    6) Peso colombiano => Dólar
                    7) Salir
                    """);
            System.out.print("Elija una opción válida: ");

            int opcion = leerEntero(sc);
            if (opcion == 7) {
                System.out.println("Gracias por usar el conversor. ¡Hasta pronto!");
                break;
            }

            String from, to;
            switch (opcion) {
                case 1 -> { from = "USD"; to = "ARS"; }
                case 2 -> { from = "ARS"; to = "USD"; }
                case 3 -> { from = "USD"; to = "BRL"; }
                case 4 -> { from = "BRL"; to = "USD"; }
                case 5 -> { from = "USD"; to = "COP"; }
                case 6 -> { from = "COP"; to = "USD"; }
                default -> {
                    System.out.println("Opción inválida.\n");
                    continue;
                }
            }

            System.out.print("Ingrese el valor que deseas convertir: ");
            double monto = leerDouble(sc);

            try {
                double resultado = conversor.convertir(from, to, monto);
                System.out.printf("El valor %s [%s] corresponde al valor final de => %s [%s]%n%n",
                        DF.format(monto), from, DF.format(resultado), to);
            } catch (Exception e) {
                System.out.println("Error durante la conversión: " + e.getMessage() + "\n");
            }
        }

        sc.close();
    }

    private static int leerEntero(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try { return Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.print("Ingrese un número entero: "); }
        }
    }

    private static double leerDouble(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim().replace(",", ".");
            try { double v = Double.parseDouble(s); if (v >= 0) return v; }
            catch (NumberFormatException ignored) {}
            System.out.print("Ingrese un número válido: ");
        }
    }
}
