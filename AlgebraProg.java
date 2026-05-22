import javax.swing.*;
import java.awt.event.*;

public class AlgebraProg extends JFrame {

    private JTextField eq1, eq2, eq3;
    private JTextArea resultado;

    private char[] variables = new char[3];

    public AlgebraProg() {

        setTitle("Gauss-Jordan Mejorado");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel titulo = new JLabel("Escribe ecuaciones (ej: 2a+b-c=8)");
        titulo.setBounds(180, 10, 400, 25);
        add(titulo);

        eq1 = new JTextField();
        eq1.setBounds(100, 50, 500, 30);
        add(eq1);

        eq2 = new JTextField();
        eq2.setBounds(100, 90, 500, 30);
        add(eq2);

        eq3 = new JTextField();
        eq3.setBounds(100, 130, 500, 30);
        add(eq3);

        JButton resolver = new JButton("Resolver");
        resolver.setBounds(280, 180, 120, 35);
        add(resolver);

        resultado = new JTextArea();
        resultado.setBounds(50, 240, 600, 240);
        resultado.setEditable(false);

        JScrollPane scroll = new JScrollPane(resultado);
        scroll.setBounds(50, 240, 600, 240);
        add(scroll);

        resolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolverSistema();
            }
        });
    }

    // Convertir ecuación a matriz
    private double[] convertir(String ecuacion) {

        double[] fila = new double[4];

        ecuacion = ecuacion.replace(" ", "");

        String[] partes = ecuacion.split("=");

        String izquierda = partes[0];
        fila[3] = Double.parseDouble(partes[1]);

        izquierda = izquierda.replace("-", "+-");

        String[] terminos = izquierda.split("\\+");

        int indice = 0;

        for (String t : terminos) {

            if (t.equals("")) continue;

            char letra = ' ';

            for (char c : t.toCharArray()) {
                if (Character.isLetter(c)) {
                    letra = c;
                    break;
                }
            }

            if (letra == ' ') continue;

            boolean existe = false;

            for (int i = 0; i < variables.length; i++) {

                if (variables[i] == letra) {

                    indice = i;
                    existe = true;
                    break;
                }
            }

            if (!existe) {

                for (int i = 0; i < variables.length; i++) {

                    if (variables[i] == '\0') {

                        variables[i] = letra;
                        indice = i;
                        break;
                    }
                }
            }

            String numero = t.replace(String.valueOf(letra), "");

            double coeficiente;

            if (numero.equals("") || numero.equals("+"))
                coeficiente = 1;

            else if (numero.equals("-"))
                coeficiente = -1;

            else
                coeficiente = Double.parseDouble(numero);

            fila[indice] = coeficiente;
        }

        return fila;
    }

    // Mostrar matriz
    private String matrizTexto(double[][] m) {

        String texto = "";

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 4; j++) {

                texto += String.format("%10.2f", m[i][j]);
            }

            texto += "\n";
        }

        return texto;
    }

    // Método Gauss-Jordan
    private String gaussJordan(double[][] m) {

        String pasos = "PROCEDIMIENTO GAUSS-JORDAN\n\n";

        for (int i = 0; i < 3; i++) {

            double pivote = m[i][i];

            pasos += "Pivote fila " + (i + 1) + ": " + pivote + "\n";

            if (pivote == 0) {

                return "No se puede resolver: pivote igual a 0";
            }

            // Dividir fila entre pivote
            for (int j = 0; j < 4; j++) {

                m[i][j] /= pivote;
            }

            pasos += "\nFila " + (i + 1) + " dividida entre el pivote\n";
            pasos += matrizTexto(m) + "\n";

            // Hacer ceros
            for (int k = 0; k < 3; k++) {

                if (k != i) {

                    double factor = m[k][i];

                    for (int j = 0; j < 4; j++) {

                        m[k][j] -= factor * m[i][j];
                    }

                    pasos += "Haciendo cero en fila " + (k + 1) + "\n";
                    pasos += matrizTexto(m) + "\n";
                }
            }
        }

        return pasos;
    }

    // Comprobación
    private String comprobar(double[][] original, double[] soluciones) {

        String texto = "\nCOMPROBACION\n\n";

        for (int i = 0; i < 3; i++) {

            double resultadoFinal =
                    original[i][0] * soluciones[0] +
                    original[i][1] * soluciones[1] +
                    original[i][2] * soluciones[2];

            texto += "Ecuacion " + (i + 1) + ": ";

            texto += String.format("%.2f = %.2f",
                    resultadoFinal,
                    original[i][3]);

            texto += "\n";
        }

        return texto;
    }

    // Resolver sistema
    private void resolverSistema() {

        try {

            variables = new char[3];

            double[][] matriz = new double[3][4];

            matriz[0] = convertir(eq1.getText());
            matriz[1] = convertir(eq2.getText());
            matriz[2] = convertir(eq3.getText());

            // Copia para comprobación
            double[][] copia = new double[3][4];

            for (int i = 0; i < 3; i++) {

                for (int j = 0; j < 4; j++) {

                    copia[i][j] = matriz[i][j];
                }
            }

            String procedimiento = gaussJordan(matriz);

            double[] soluciones = {

                    matriz[0][3],
                    matriz[1][3],
                    matriz[2][3]
            };

            String salida = procedimiento + "\n";

            salida += "RESULTADOS\n\n";

            salida += variables[0] + " = "
                    + String.format("%.2f", soluciones[0]) + "\n";

            salida += variables[1] + " = "
                    + String.format("%.2f", soluciones[1]) + "\n";

            salida += variables[2] + " = "
                    + String.format("%.2f", soluciones[2]) + "\n";

            salida += comprobar(copia, soluciones);

            resultado.setText(salida);

        } catch (Exception e) {

            resultado.setText("Error en el formato de las ecuaciones.");
        }
    }

    public static void main(String[] args) {

        new AlgebraProg().setVisible(true);
    }
}