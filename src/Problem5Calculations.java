/**
 * This program is strictly for calculating answers in problem 5 of a homework.
 * 
 * @author Gunnar
 */
public class Problem5Calculations {

	static double[] p = new double[] {0.6, 0.2, 0.2};
	static String[] plain = new String[]{"a", "b", "c"};
	
	public static void main(String[] args) {
		// Calculate H(P)
		double hp = 0.0;
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				// Calculate the probability of the two letter string
				double prob = p[i]*p[j];
				// Compute plog_2(p)
				hp += (prob) * Math.log(prob)/Math.log(2);
			}
		}
		hp *= -1;
		System.out.println("H(P): " + hp);
		
		// Calculate H(P|C)
		double hpc = 0.0;
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++){
				String cipher = plain[i] + plain[j];
				hpc += proby(cipher) * hpy(cipher);
			}
		}
		System.out.println("H(P|C): " + hpc);
	}
	
	/**
	 * Find the probability of a ciphertext.
	 * @param cipher
	 * @return the probability
	 */
	private static double proby(String cipher) {
		double prob = 0.0;
		char c1 = cipher.charAt(0);
		char c2 = cipher.charAt(1);
		// Each key could be used on some ciphertext to get this plaintext (in this cryptosystem)
		for (int k = 0; k <= 2; k++) {
			double keyProb = p[k];
			double plainProb = p[Math.floorMod(c1-97-k, 3)]*p[Math.floorMod(c2-97-k, 3)];
			prob += keyProb * plainProb;
		}
		return prob;
	}
	
	/**
	 * Find the entropy of P given a ciphertext.
	 * @param ciphertext
	 * @return the entropy
	 */
	private static double hpy(String ciphertext) {
		double entropy = 0.0;
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <=2; j++) {
				String plaintext = plain[i] + plain[j];
				double condxy = condxy(plaintext, ciphertext);
				// Avoid Math.log(0) = NaN
				if (condxy != 0.0) {
					entropy += condxy * Math.log(condxy)/Math.log(2);
				}
			}
		}
		return -1 * entropy;
	}
	
	/**
	 * Finds the conditional probability of a plaintext given the ciphertext.
	 * @param plain the plaintext
	 * @param cipher the ciphertext
	 * @return the conditional probability
	 */
	private static double condxy(String plain, String cipher) {
		double pPlain = p[plain.charAt(0)-97] * p[plain.charAt(1)-97];
		double pCipher = proby(cipher);
		double pKey = 0.0;
		int dist1 = Math.floorMod(cipher.charAt(0) - plain.charAt(0), 3);
		int dist2 = Math.floorMod(cipher.charAt(1) - plain.charAt(1), 3);
		// With the shift, only one key could convert the given plain to given cipher, if any
		if (dist1 == dist2) {
			pKey = p[dist1];
		}
		return pPlain * pKey / pCipher;
	}

}
