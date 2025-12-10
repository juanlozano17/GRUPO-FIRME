import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class GenerarHTML {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] criterios = {"Costo", "Calidad", "Soporte tecnico", "Vida util", "Garantia"};
        double[] pesos = {0.30, 0.25, 0.20, 0.15, 0.10};
        String[] proveedores = {"A (Ecolite)", "B (Casaferretera)", "C (Casaandina)", "D (Exito)"};

        double[][] puntuaciones = new double[criterios.length][proveedores.length];

        System.out.println("=== MATRIZ DE EVALUACIÓN DE PROVEEDORES ===\n");
        for (int i = 0; i < criterios.length; i++) {
            System.out.println(">> " + criterios[i]);
            for (int j = 0; j < proveedores.length; j++) {
                System.out.print("Ingrese puntaje (1-5) para " + proveedores[j] + ": ");
                puntuaciones[i][j] = sc.nextDouble();
            }
            System.out.println();
        }

        
        double[] totales = new double[proveedores.length];
        for (int i = 0; i < criterios.length; i++) {
            for (int j = 0; j < proveedores.length; j++) {
                totales[j] += puntuaciones[i][j] * pesos[i];
            }
        }

        int mejor = 0;
        for (int i = 1; i < totales.length; i++) {
            if (totales[i] > totales[mejor]) mejor = i;
        }

        try {
            
            String plantilla = Files.readString(Paths.get("plantilla.html"));

            
            StringBuilder tabla = new StringBuilder();
            tabla.append("<tr><th>Criterio</th><th>Peso (%)</th><th>Puntuación (1-5)</th>");
            for (String p : proveedores) tabla.append("<th>").append(p).append("</th>");
            tabla.append("</tr>");

            for (int i = 0; i < criterios.length; i++) {
                tabla.append("<tr>");
                tabla.append("<td>").append(criterios[i]).append("</td>");
                tabla.append("<td>").append((int)(pesos[i]*100)).append("%</td>");
                tabla.append("<td>");
                for (int j = 0; j < proveedores.length; j++) {
                    tabla.append((int)puntuaciones[i][j]);
                    if (j < proveedores.length - 1) tabla.append(", ");
                }
                tabla.append("</td>");
                for (int j = 0; j < proveedores.length; j++) {
                    double valor = puntuaciones[i][j] * pesos[i];
                    tabla.append("<td>").append(String.format("%.2f", valor)).append("</td>");
                }
                tabla.append("</tr>");
            }

            tabla.append("<tr><th colspan='3'>PUNTAJE TOTAL</th>");
            for (int j = 0; j < proveedores.length; j++) {
                if (j == mejor)
                    tabla.append("<td class='mejor'>").append(String.format("%.2f", totales[j])).append("</td>");
                else
                    tabla.append("<td>").append(String.format("%.2f", totales[j])).append("</td>");
            }
            tabla.append("</tr>");

            
            String htmlFinal = plantilla.replace(
                "<!-- Java generará aquí las filas -->", tabla.toString()
            ).replace(
                "<h3 id=\"resultado\"></h3>",
                "<h3 id=\"resultado\">✅ La mejor opción es: <b style='color:green;'>"
                    + proveedores[mejor] + "</b></h3>"
            );

            
            FileWriter w = new FileWriter("resultado.html");
            w.write(htmlFinal);
            w.close();

            System.out.println("\n✅ Archivo generado: resultado.html");
            System.out.println("👉 La mejor opción es: " + proveedores[mejor]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
 