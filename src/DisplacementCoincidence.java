import java.util.Scanner;

/**
 * Given a ciphertext, the program outputs the number coincidences betweeen the
 * ciphertext and shifts of the ciphertext.
 * 
 * @author Gunnar
 */
public class DisplacementCoincidence {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        // Read in the ciphertext
        String ciphertext = kb.next();
        // Compute how many shifts we will do
        int maxShift = Math.min(ciphertext.length(), 20);
        // Compute the coincidences of each shift
        String shift = ciphertext;
        for (int i = 1; i < maxShift; i++) {
            shift = " " + shift.substring(0, shift.length() - 1);
            int coincidences = computeCoincidence(ciphertext, shift);
            System.out.println("Shift: " + i + "  Coincidences: " + coincidences);
        }


        kb.close();
    }

    /**
     * Computes the number of coincidences between two strings.
     * @param a
     * @param b
     * @return number of coincidences
     */
    private static int computeCoincidence(String a, String b) {
        int len = Math.min(a.length(), b.length());
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                count++;
            }
        }
        return count;
    }
}
