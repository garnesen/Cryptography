import java.util.Scanner;

/**
 * Decodes a ciphertext from a vigenere cipher given the key.
 * 
 * @author Gunnar
 */
public class VigenereCipherDecoder {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		String ciphertext = kb.next();
		String key = kb.next().toUpperCase();

		for (int i = 0; i < ciphertext.length(); i++) {
			char cipherChar = ciphertext.charAt(i);
			char keyChar = key.charAt(i % key.length());
			// Subtract the letters, mod 26
			char plainChar = (char)(Math.floorMod(cipherChar - keyChar, 26) + 97);
			System.out.print(plainChar);
		}
		
		kb.close();
	}

}
