import java.util.Scanner;

public class LetterFrequency {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		
		// Read in the text
		String text = kb.next();
		// Remove spaces from the text and make it all uppercase
		text = text.replaceAll(" ", "").toUpperCase();
		// An array to store frequency values
		int[] frequency = new int[26];
		
		for (int i = 0; i < text.length(); i++)
		{
			// The letter at the current position in the text
			char letter = text.charAt(i);
			// Convert it to index in the alphabet
			int indexInAlphabet = letter - 65;
			// Add a count to this index in the frequency
			frequency[indexInAlphabet]++;
		}
		
		for (int i = 0; i < frequency.length; i++)
		{
			// Convert the index back to a letter
			char letter = (char)(i + 65);
			// Print the letter with its frequency
			System.out.println(letter + " - " + frequency[i]);
		}
		
		kb.close();
	}

}
