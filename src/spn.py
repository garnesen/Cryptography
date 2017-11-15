import argparse

default_sub_encrypt = {'0': 'E', 
		               '1': '4', 
		               '2': 'D', 
		               '3': '1', 
		               '4': '2', 
		               '5': 'F', 
		               '6': 'B', 
		               '7': '8', 
		               '8': '3', 
		               '9': 'A', 
		               'A': '6', 
		               'B': 'C', 
		               'C': '5', 
		               'D': '9', 
		               'E': '0', 
		               'F': '7'}

default_perm_encrypt = {1: 1,
		                2: 5,
		                3: 9,
		                4: 13,
		                5: 2,
		                6: 6,
		                7: 10,
		                8: 14,
		                9: 3,
		                10: 7,
		                11: 11,
		                12: 15,
		                13: 4,
		                14: 8,
		                15: 12,
		                16: 16}

# Parse a substitution permutation definition from a file
def parse_sub_map(filename):
	file = open(filename, "r")
	sub_map = {}
	for line in file:
		values = line.rstrip().split(" ")
		sub_map[values[0]] = values[1]
	return sub_map

# Parse a permutation definition from a file
def parse_perm_map(filename):
	file = open(filename, "r")
	perm_map = {}
	for line in file:
		values = line.rstrip().split(" ")
		perm_map[int(values[0])] = int(values[1])
	return perm_map

# Convert hex to binary
def text_to_bin(text):
	bin_text = ""
	for c in text:
		bin_text += bin(int(c, 16))[2:].zfill(4)
	return bin_text

# Convert binary to hex
def bin_to_text(bin_text):
	return hex(int(bin_text, 2))[2:].zfill(4).upper()

# Given a seed key, generate a list of round keys
def generate_keys(initial_key, num_keys, key_len):
	keys = []
	for i in range(num_keys):
		keys.append(initial_key[i:i+key_len])
	return keys

# XOR the key with the text
def subkey_mixing(text, key):
	bin_text = text_to_bin(text)
	bin_key = text_to_bin(key)
	xor = ""
	for i in range(len(bin_text)):
		xor += "0" if bin_text[i] == bin_key[i] else "1"
	return bin_to_text(xor)

# Use the S-Box permutation on the text
def substitute(text, sub_map):
	substitution = ""
	for c in text:
		substitution += sub_map[c.upper()]
	return substitution

# User the bit permutation on the text
def permute(text, perm_map):
	bin_text = text_to_bin(text)
	permutation = list(bin_text)
	for i in range(len(bin_text)):
		permutation[perm_map[i + 1] - 1] = bin_text[i]
	return bin_to_text("".join(permutation))

# Helper method to split encryption and decryption
def spn(plaintext, rounds, key, sub_encrypt, perm_encrypt, verbose=False, decrypt=False):
	if decrypt:
		sub_decrypt = dict(reversed(item) for item in sub_encrypt.items())
		perm_decrypt = dict(reversed(item) for item in perm_encrypt.items())
		spn_decrypt(plaintext, rounds, key, sub_decrypt, perm_decrypt, verbose)
	else:
		spn_encrypt(plaintext, rounds, key, sub_encrypt, perm_encrypt, verbose)

# SPN encryption
def spn_encrypt(plaintext, rounds, key, sub_encrypt, perm_encrypt, verbose=False):
	keys = generate_keys(key, rounds + 1, len(plaintext))

	if verbose:
		print "Start: %s" % (plaintext)
		print ""

	# Rounds 1 to (r - 1)
	current_state = plaintext
	for i in range(rounds - 1):
		kmix = subkey_mixing(current_state, keys[i])
		subs = substitute(kmix, sub_encrypt)
		perm = permute(subs, perm_encrypt)

		if verbose:
			print "Round %d" % (i + 1)
			print "%s ^ %s = %s" % (current_state, keys[i], kmix)
			print "s(%s) = %s" % (kmix, subs)
			print "p(%s) = %s" % (subs, perm)
			print ""

		current_state = perm

	# Final Round
	kmix = subkey_mixing(current_state, keys[rounds - 1])
	subs = substitute(kmix, sub_encrypt)
	if verbose:
		print "Round %d" % (rounds)
		print "%s ^ %s = %s" % (current_state, keys[rounds - 1], kmix)
		print "s(%s) = %s" % (kmix, subs)
		print ""

	current_state = subs

	# Whitening
	final = subkey_mixing(current_state, keys[rounds])
	if verbose:
		print "Final Mixing"
		print "%s ^ %s = %s" % (current_state, keys[rounds], final)
		print ""

	print final.upper()

# SPN decryption
def spn_decrypt(ciphertext, rounds, key, sub_decrypt, perm_decrypt, verbose=False):
	keys = generate_keys(key, rounds + 1, len(ciphertext))

	# Whitening
	current_state = ciphertext
	pre_whiten = subkey_mixing(current_state, keys[rounds])
	if verbose:
		print "Final Mixing"
		print "%s ^ %s = %s" % (current_state, keys[rounds], pre_whiten)
		print ""
	current_state = pre_whiten

	# Final Round
	subs = substitute(current_state, sub_decrypt)
	kmix = subkey_mixing(subs, keys[rounds - 1])
	if verbose:
		print "Round %d" % (rounds)
		print "s(%s) = %s" % (current_state, subs)
		print "%s ^ %s = %s" % (subs, keys[rounds - 1], kmix)
		print ""
	current_state = kmix

	# Rounds 1 to (r - 1)
	for i in range(rounds - 2, -1, -1):
		perm = permute(current_state, perm_decrypt)
		subs = substitute(perm, sub_decrypt)
		kmix = subkey_mixing(subs, keys[i])

		if verbose:
			print "Round %d" % (i + 1)
			print "p(%s) = %s" % (current_state, perm)
			print "s(%s) = %s" % (perm, subs)
			print "%s ^ %s = %s" % (subs, keys[i], kmix)
			print ""

		current_state = kmix

	if verbose:
		print "Start: %s" % (current_state)
		print ""

	print current_state

def main():
	# Create the arg parser
	parser = argparse.ArgumentParser(description='SPN encryption.')
	parser.add_argument('plaintext', metavar='P', nargs=1, help='the plaintext (or ciphertext in decrypt mode)')
	parser.add_argument('num_rounds', metavar='R', nargs=1, type=int, help='number of rounds')
	parser.add_argument('key', metavar='K', nargs=1, help='32 bit key string')
	parser.add_argument('-v','--verbose', action='store_true', help='displays internal state', default=False)
	parser.add_argument('-d', '--decrypt', action='store_true', help='sets to decrypt mode', default=False)
	parser.add_argument('-s', '--substitution', nargs=1, help='name of file that holds alternate substitution permutation')
	parser.add_argument('-p', '--permutation', nargs=1, help='name of file that holds alternate permutation')
	args = parser.parse_args()

	# Set up substitution map
	if args.substitution:
		try:
			sub_encrypt = parse_sub_map(args.substitution[0])
		except:
			print "Failed to read file %s" % (args.substitution[0])
			sub_encrypt = default_sub_encrypt
	else:
		sub_encrypt = default_sub_encrypt

	# Set up permutation map
	if args.permutation:
		try:
			perm_encrypt = parse_perm_map(args.permutation[0])
		except:
			print "Failed to read file %s" % (args.permutation[0])
			perm_encrypt = default_perm_encrypt
	else:
		perm_encrypt = default_perm_encrypt
	
	# Do SPN encryption with the input
	spn(args.plaintext[0], args.num_rounds[0], args.key[0], sub_encrypt, perm_encrypt, verbose=args.verbose, decrypt = args.decrypt)

if __name__ == "__main__":
    main()