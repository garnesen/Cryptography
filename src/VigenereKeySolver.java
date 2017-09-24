import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Given a ciphertext and key length, gives a table of values used to
 * determine the actual key for a vigenere cipher.
 * 
 * @author Gunnar
 */
public class VigenereKeySolver {

	// English language probabilities
	static double[] p = {8.2, 1.5, 2.8, 4.3, 12.7, 2.2, 2, 6.1, 7, 0.2, 0.8, 4, 2.4, 6.7, 7.5, 1.9, 0.1, 6, 6.3, 9.1, 2.8, 1, 2.3, 0.1, 2, 0.1};
	
	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		int keyLength = kb.nextInt();
		String ciphertext = kb.next();
		
		// Get the substrings and calculate their shift values
		List<String> substrings = getSubstrings(ciphertext, keyLength);
		List<double[]> calcs = new ArrayList<double[]>();
		for (int i = 0; i < substrings.size(); i++) {
			calcs.add(calcShifts(substrings.get(i), keyLength));
		}
		
		// Print the formatted table for latex
		System.out.print("_ & ");
		for (int i = 0; i < calcs.size(); i++) {
			System.out.print("$M_{" + i + "}$ & ");
		}
		System.out.println();
		for (int i = 0; i <= 25; i++) {
			System.out.print(i + " & ");
			for (int j = 0; j < calcs.size(); j++) {
				String out = String.format("%.3f", calcs.get(j)[i]);
				System.out.print(out + " & ");
			}
			System.out.println();
		}
		
		kb.close();
	}
	
	/**
	 * Returns the list of substrings made for a ciphertext given the key length
	 * @param c
	 * @param m
	 * @return the list of substrings
	 */
	private static List<String> getSubstrings(String c, int m) {
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
        
        return substrings;
    }

	/**
	 * Calculates the M value from the notes for a given substring
	 * @param substring
	 * @param m
	 * @return
	 */
	private static double[] calcShifts(String substring, int m) {
		double[] calcs = new double[26];
		List<Double> q = new ArrayList<Double>();
		int len = substring.length();
		
		// Each value in the vector is occurences of a letter / length
		for (int i = 65; i<= 90; i++) {
			int occurrences = countOccurrences(substring, (char)i);
			q.add((double) occurrences / len);
		}
		
		// Compute the dot product of each shifted vector with p
		for (int i = 0; i <= 25; i++) {
			calcs[i] = dotproductWithProb(q);
			Collections.rotate(q, 25);
		}
			
		return calcs;
	}
	
	/**
	 * Count occurrences of a string in some text.
	 * @param text
	 * @param search
	 * @return the number of occurences
	 */
	private static int countOccurrences(String text, char search) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == search) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Computes the dot product of the enlgish probability vector with a given
	 * vector.
	 * @param vec
	 * @return the dot product
	 */
	private static double dotproductWithProb(List<Double> vec) {
		double product = 0;
		for (int i = 0; i < p.length; i++) {
			product += p[i] * vec.get(i);
		}
		return product;
	}
}
