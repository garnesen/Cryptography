import java.util.Scanner;

public class AffineCipherDecoder {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		
		// Read in the cipher text
		String cipherText = kb.next();
		
		// The decryption function is in the form (coefficient)y - (constant) so
		// we get these values
		int coefficient = kb.nextInt();
		int constant = kb.nextInt();
		
		for (int i = 0; i < cipherText.length(); i++)
		{
			// Get the letter in the string
			char letter = cipherText.charAt(i);
			// Convert to index in alphabet
			int index = convertToIndex(letter);
			// Perform decryption operation
			int plainTextLetter = coefficient * index - constant;
			// Mod the answer to be in Z_26
			plainTextLetter = Math.floorMod(plainTextLetter, 26);
			// Print the result as a letter
			System.out.print(convertFromIndex(plainTextLetter));
		}
		kb.close();
	}
	
	/**
	 * Takes an uppercase letter and converts it to its
	 * index in the alphabet
	 * @param c the letter
	 * @return the index
	 */
	private static int convertToIndex(char c)
	{
		return c - 65;
	}
	
	/**
	 * Takes an index in the alphabet and returns the letter
	 * based on the ascii value.
	 * @param i the index
	 * @return the letter
	 */
	private static char convertFromIndex(int i)
	{
		return (char)(i + 97);
	}

}
