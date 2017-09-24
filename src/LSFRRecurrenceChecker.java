import java.util.Scanner;

/**
 * Takes a keystream and a list of coefficients in order and informs the user
 * whether or not the given coefficients will generate that keystream.
 * 
 * @author Gunnar
 */
public class LSFRRecurrenceChecker {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		String keystreamString = kb.nextLine();
		String cofString = kb.nextLine();
		
		// Convert the keystream input to an int array
		int[] keystream = new int[keystreamString.length()];
		for (int i = 0; i < keystreamString.length(); i++) {
			keystream[i] = keystreamString.charAt(i) == '0' ? 0 : 1; 
		}
		
		// Convert the input of coefficients to an int array
		String[] cofStrings = cofString.split(" ");
		int[] cofs = new int[cofStrings.length];
		for (int i = 0; i < cofStrings.length; i++) {
			cofs[i] = Integer.parseInt(cofStrings[i]);
		}
		
		// Check each bit of the keystream is generated correctly
		for (int i = 0; i < keystream.length - cofs.length; i++) {
			int targetBit = keystream[i + cofs.length];
			int sum = 0;
			// Loop through the coefficients and calculate the sum
			for (int j = 0; j < cofs.length; j++) {
				sum += cofs[j] * keystream[i + j];
			}
			sum = Math.floorMod(sum, 2);
			if (sum != targetBit) {
				System.out.println("Error for target bit at index: " + (i + cofs.length));
				System.exit(0);
			}
		}
		System.out.println("All good!");
		
		kb.close();
	}

}
