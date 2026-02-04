import java.io.*;

/**
 * Utility class to fix CSV lines by quoting the address field if it contains commas.
 * <p>
 * Assumes the address is the 5th field (index 4) in an 8-column CSV.
 * </p>
 */
public class CsvFixer {

    /**
     * Main method to read an input CSV file, fix lines, and write to an output file.
     * Modify inputFile and outputFile paths as needed.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        String inputFile = "input.csv";   // Replace with your CSV file path
        String outputFile = "fixed.csv";  // Output fixed CSV file

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String fixedLine = fixLine(line);
                bw.write(fixedLine);
                bw.newLine();
            }

            System.out.println("CSV fixing completed. Output file: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fixes a CSV line by quoting the address field if it is not already quoted.
     *
     * @param line the CSV line to fix
     * @return the fixed CSV line
     */
    private static String fixLine(String line) {
        // Split by comma, limit to 8 parts (expected columns)
        String[] parts = line.split(",", 8);

        if (parts.length < 8) {
            // Line has fewer columns than expected, return as is
            return line;
        }

        // The 5th field (index 4) is the address, which may contain commas
        String address = parts[4].trim();
        if (!address.startsWith("\"") || !address.endsWith("\"")) {
            // Enclose the address field in quotes
            parts[4] = "\"" + address + "\"";
        }

        // Rebuild the line with commas
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i].trim());
            if (i < parts.length - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }
}