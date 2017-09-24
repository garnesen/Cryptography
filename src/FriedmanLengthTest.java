import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Calculates the index of coincidences for the substrings of multiple guesses
 * of the key length m for a vigenere cipher.
 * 
 * @author Gunnar
 */
public class FriedmanLengthTest {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        // Read in the ciphertext
        String ciphertext = kb.next();
        for (int i = 2; i < 20; i++) {
            friedmanTest(ciphertext, i);
            System.out.print("\n\n");
        }
        kb.close();
    }

    /**
     * Prints the results of the Friedman test for key length m
     * @param c ciphertext
     * @param m key length
     */
    private static void friedmanTest(String c, int m) {
        System.out.print("[" + m + "]: ");
        
        // Initialize the array to empty strings
        List<String> substrings = new ArrayList<String>();
        for (int i = 0; i < m; i++) {
            substrings.add("");
        }

        // Add 1 letter to each substring rotationally
        int subIdx = 0;
        for (int i = 0; i < c.length(); i++) {
            substrings.set(subIdx, substrings.get(subIdx) + c.charAt(i));
            subIdx = (subIdx + 1) % m;
        }

        // Compute the IOC for each substring
        for (String substring : substrings) {
            double ioc = computeIndexOfCoincidence(substring);
            System.out.print(String.format("%.3f", ioc) + ", ");
        }
    }

    /**
     * Computes the index of coincidence for a string
     * @param s the string
     * @return the index of coincidence
     */
    private static double computeIndexOfCoincidence(String s) {
    	// Map each letter to its frequency
        HashMap<String, Integer> freq = new HashMap<String, Integer>();
        for (int i = 0; i < s.length(); i++) {
            String letter = s.charAt(i) + "";
            if (freq.containsKey(letter)) {
                freq.put(letter, freq.get(letter) + 1);
            }
            else {
                freq.put(letter, 1);
            }
        }

        // Calculate the IOC and return it
        double summation = 0.0;
        for (String letter : freq.keySet()) {
            int letterFreq = freq.get(letter);
            summation += (letterFreq * (letterFreq - 1));
        }
        summation /= (s.length() * (s.length() - 1));
        return summation;
    }
}
