import java.util.ArrayList;
import java.util.Scanner;

/**
 * Given a ciphertext from LSFR, a list of coefficients for a recurrence
 * relation, and initial values for the keystream, this program outputs
 * the plaintext.
 * 
 * @author Gunnar
 */
public class LSFRDecoder {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		String cipherText = kb.nextLine();
		String cofString = kb.nextLine();
		String initialKeystreamString = kb.nextLine();

		// Convert the input of coefficients to an int array
		String[] cofStrings = cofString.split(" ");
		int[] cofs = new int[cofStrings.length];
		for (int i = 0; i < cofStrings.length; i++) {
			cofs[i] = Integer.parseInt(cofStrings[i]);
		}
		
		// Convert the keystream input to an int array
		ArrayList<Integer> keystream = new ArrayList<Integer>();
		for (int i = 0; i < initialKeystreamString.length(); i++) {
			keystream.add(initialKeystreamString.charAt(i) == '0' ? 0 : 1);
		}
		
		// Generate the full keystream
		for (int i = 0; keystream.size() < cipherText.length(); i++) {
			int sum = 0;
			// Loop through the coefficients and calculate the sum
			for (int j = 0; j < cofs.length; j++) {
				sum += cofs[j] * keystream.get(i + j);
			}
			sum = Math.floorMod(sum, 2);
			keystream.add(sum);
		}
		
		// Find plaintext by adding ciphertext to keystream
		for (int i = 0; i < cipherText.length(); i++) {
			int bit1 = cipherText.charAt(i) == '0' ? 0 : 1;
			int bit2 = keystream.get(i);
			System.out.print(Math.floorMod(bit1 + bit2, 2));
		}
		kb.close();
	}

}
