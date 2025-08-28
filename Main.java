import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public class Main {
    // Converts a value string encoded in a certain base into a double number
    public static double decodeValue(String val, int base) {
        BigInteger bigInt = new BigInteger(val, base);
        return bigInt.doubleValue();
    }

    // Calculates constant term (c) of polynomial using Lagrange interpolation at x=0
    public static double findConstantTerm(double[] xs, double[] ys) {
        double c = 0.0;
        int n = xs.length;

        for (int i = 0; i < n; i++) {
            double term = ys[i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (0 - xs[j]) / (xs[i] - xs[j]);
                }
            }
            c += term;
        }
        return c;
    }

    public static void main(String[] args) {
        try(BufferedReader br = new BufferedReader(new FileReader("input.json"))) {
            String line;
            int n = 0;
            double[] xVals = null;
            double[] yVals = null;
            int count = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Get the count 'n' from keys section
                if(line.contains("\"n\"")) {
                    n = Integer.parseInt(line.split(":")[1].replaceAll("[,}]", "").trim());
                    xVals = new double[n];
                    yVals = new double[n];
                }
                // Detect root entries like "1": { ... }
                else if (line.matches("\"\\d+\"\\s*:\\s*\\{")) {
                    int key = Integer.parseInt(line.split("\"")[1]);

                    // Read base line and value line
                    String baseLine = br.readLine().trim();
                    String valueLine = br.readLine().trim();
                    br.readLine(); // consume closing brace line

                    int base = Integer.parseInt(baseLine.split(":")[1].replaceAll("[\",]", "").trim());
                    String value = valueLine.split(":")[1].replaceAll("[\",]", "").trim();

                    xVals[count] = key;
                    yVals[count] = decodeValue(value, base);
                    count++;
                }
            }
            double constantTerm = findConstantTerm(xVals, yVals);
            System.out.println("The constant c of the polynomial is: " + constantTerm);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
