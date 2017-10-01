import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The program takes an list of initial six letters from ones days' worth of
 * ENIMGA intercepts and attempts to output the three full compositions of the
 * pairs of letters in the keys.
 * 
 * @author Gunnar
 */
public class EnigmaKeyCompositions {

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		
		// Get the number of lines of data to read in
		int numLines = kb.nextInt();
		
		// Initialize the mappings
		HashMap<String, String> doa = new HashMap<String, String>();
		HashMap<String, String> eob = new HashMap<String, String>();
		HashMap<String, String> foc = new HashMap<String, String>();
		
		// Read each line of input
		while (numLines != 0) {
			String input = kb.next();
			
			// Map first to the fourth letter
			doa.put(input.substring(0, 1), input.substring(3, 4));
			// Map the second to the fifth letter
			eob.put(input.substring(1, 2), input.substring(4, 5));
			// Map the third to the sixth letter
			foc.put(input.substring(2, 3), input.substring(5, 6));
			
			numLines--;
		}
		
		// Read off the mappings to create cycles and print them
		System.out.print("D o A = "); 
		printFormattedCycles(createCycles(doa));
		System.out.print("E o B = ");
		printFormattedCycles(createCycles(eob));
		System.out.print("F o C = ");
		printFormattedCycles(createCycles(foc));
		
		
		kb.close();
	}
	
	/**
	 * Read a mapping of letters to create cycles.
	 * @param map the mapping of letters
	 * @return a list of strings representing cycles
	 */
	private static List<String> createCycles(HashMap<String, String> map) {
		// Create a list for the alphabet
		List<String> alphabet = IntStream.range('a', 'z').mapToObj(c -> ((char) c) + "").collect(Collectors.toList());
		
		List<String> cycles = new ArrayList<String>();
		String curCycle = "";
		while (true) {
			String curLetter;
			
			// Set the current letter to the next letter not in a cycle if we
			// are on a new cycle, otherwise get the last letter in the cycle
			if (curCycle.equals("")) {
				curLetter = alphabet.remove(0);
				curCycle += curLetter;
			}
			else {
				curLetter = curCycle.substring(curCycle.length() - 1);
			}
			
			String mapTo = map.get(curLetter);
			
			// Check if we need to close off the cycle
			if (curCycle.substring(0, 1).equals(mapTo)) {
				cycles.add(curCycle);
				curCycle = "";
			}
			else {
				curCycle += mapTo;
			}
			
			// If the alphabet has run out, we have created all cycles, so exit
			if (alphabet.size() == 0) {
				break;
			}
			
			alphabet.remove(mapTo);
		}
		
		return cycles;
	}
	
	/**
	 * Prints formatted cycles given a list of strings representing cycles
	 * @param cycles the list of cycles of strings
	 */
	private static void printFormattedCycles(List<String> cycles) {
		for (String cycle : cycles) {
			System.out.print("(" + cycle + ")");
		}
		System.out.println();
	}
}
