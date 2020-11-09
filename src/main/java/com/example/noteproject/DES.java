package com.example.noteproject;


import java.math.BigInteger;

// Source code: https://www.educative.io/edpresso/how-to-implement-the-des-algorithm-in-cpp
public class DES {
    // The initial permutation table
    private final int[] initial_permutation =
            {
                    58, 50, 42, 34, 26, 18, 10, 2,
                    60, 52, 44, 36, 28, 20, 12, 4,
                    62, 54, 46, 38, 30, 22, 14, 6,
                    64, 56, 48, 40, 32, 24, 16, 8,
                    57, 49, 41, 33, 25, 17, 9, 1,
                    59, 51, 43, 35, 27, 19, 11, 3,
                    61, 53, 45, 37, 29, 21, 13, 5,
                    63, 55, 47, 39, 31, 23, 15, 7
            };

    // The PC1 table
    //int pc1[56] = {
    private final int[] pc1 =
            {
                    57, 49, 41, 33, 25, 17, 9,
                    1, 58, 50, 42, 34, 26, 18,
                    10, 2, 59, 51, 43, 35, 27,
                    19, 11, 3, 60, 52, 44, 36,
                    63, 55, 47, 39, 31, 23, 15,
                    7, 62, 54, 46, 38, 30, 22,
                    14, 6, 61, 53, 45, 37, 29,
                    21, 13, 5, 28, 20, 12, 4
            };

    // The PC2 table
    //int pc2[48] = {
    private final int[] pc2 =
            {
                    14, 17, 11, 24, 1, 5,
                    3, 28, 15, 6, 21, 10,
                    23, 19, 12, 4, 26, 8,
                    16, 7, 27, 20, 13, 2,
                    41, 52, 31, 37, 47, 55,
                    30, 40, 51, 45, 33, 48,
                    44, 49, 39, 56, 34, 53,
                    46, 42, 50, 36, 29, 32
            };

    // The expansion table
    private final int[] expansion_table =
            {
                    32, 1, 2, 3, 4, 5, 4, 5,
                    6, 7, 8, 9, 8, 9, 10, 11,
                    12, 13, 12, 13, 14, 15, 16, 17,
                    16, 17, 18, 19, 20, 21, 20, 21,
                    22, 23, 24, 25, 24, 25, 26, 27,
                    28, 29, 28, 29, 30, 31, 32, 1
            };

    // The substitution boxes. The should contain values
    // from 0 to 15 in any order.
    private final int[][][] substitution_boxes =
            {
                    {
                            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                    },
                    {
                            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                    },
                    {
                            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                    },
                    {
                            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                    },
                    {
                            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
                    },
                    {
                            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
                    },
                    {
                            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                    },
                    {
                            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                    }
            };

    // The permutation table
    //int permutation_tab[32] =
    private final int[] permutation_tab =
            {
                    16, 7, 20, 21, 29, 12, 28, 17,
                    1, 15, 23, 26, 5, 18, 31, 10,
                    2, 8, 24, 14, 32, 27, 3, 9,
                    19, 13, 30, 6, 22, 11, 4, 25
            };

    // The inverse permutation table
    //int inverse_permutation[64] =
    private final int[] inverse_permutation =
            {
                    40, 8, 48, 16, 56, 24, 64, 32,
                    39, 7, 47, 15, 55, 23, 63, 31,
                    38, 6, 46, 14, 54, 22, 62, 30,
                    37, 5, 45, 13, 53, 21, 61, 29,
                    36, 4, 44, 12, 52, 20, 60, 28,
                    35, 3, 43, 11, 51, 19, 59, 27,
                    34, 2, 42, 10, 50, 18, 58, 26,
                    33, 1, 41, 9, 49, 17, 57, 25
            };

    // Function to convert a string to binary
    private String convertStringToBinary(String text)
    {
        return new BigInteger(text.getBytes()).toString(2);
    }

    // Function to convert a binary number to a string
    private String convertBinaryToString(String binary)
    {
        return new String(new BigInteger(binary, 2).toByteArray());
    }

    // Function to convert a number in decimal to binary
    private String convertDecimalToBinary(int decimal)
    {
        StringBuilder binaryBuilder = new StringBuilder();
        while (decimal != 0)
        {
            binaryBuilder.insert(0, (decimal % 2 == 0 ? "0" : "1"));
            decimal = decimal / 2;
        }
        StringBuilder binary = new StringBuilder(binaryBuilder.toString());
        while (binary.length() < 4)
        {
            binary.insert(0, "0");
        }
        return binary.toString();
    }

    // Function to convert a number in binary to decimal
    private int convertBinaryToDecimal(String binary)
    {
        int decimal = 0;
        int counter = 0;
        int size = binary.length();
        for (int i = size - 1; i >= 0; i--)
        {
            if (binary.charAt(i) == '1')
            {
                //decimal += pow(2, counter);
                decimal += (counter * counter);
            }
            counter++;
        }
        return decimal;
    }

    // Function to do a circular left shift by 1
    private String shift_left_once(String key_chunk)
    {
        StringBuilder shifted = new StringBuilder();
        for (int i = 1; i < 28; i++)
        {
            shifted.append(key_chunk.charAt(i));
        }
        shifted.append(key_chunk.charAt(0));
        return shifted.toString();
    }

    // Function to do a circular left shift by 2
    private String shift_left_twice(String key_chunk)
    {
        StringBuilder shifted = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 28; j++)
            {
                shifted.append(key_chunk.charAt(j));
            }
            shifted.append(key_chunk.charAt(0));
            key_chunk = shifted.toString();
            shifted = new StringBuilder();
        }
        return key_chunk;
    }

    // Function to compute xor between two strings
    private String Xor(String a, String b)
    {
        StringBuilder result = new StringBuilder();
        //int size = b.size();
        int size = b.length();
        for (int i = 0; i < size; i++)
        {
            if (a.charAt(i) != b.charAt(i))
            {
                result.append("1");
            } else
            {
                result.append("0");
            }
        }
        return result.toString();
    }

    // Function to generate the 16 keys.
    private String[] generate_keys(String key)
    {
        // Array to hold the 16 keys
        String[] round_keys = new String[16];

        // 1. Compressing the key using the PC1 table
        StringBuilder perm_key = new StringBuilder();
        for (int i = 0; i < 56; i++)
        {
            perm_key.append(key.charAt(pc1[i] - 1));
        }
        // 2. Dividing the key into two equal halves
        String left = perm_key.substring(0, 28);
        String right = perm_key.substring(28, 28);
        for (int i = 0; i < 16; i++) {
            // 3.1. For rounds 1, 2, 9, 16 the key_chunks
            // are shifted by one.
            if (i == 0 || i == 1 || i == 8 || i == 15)
            {
                left = shift_left_once(left);
                right = shift_left_once(right);
            }
            // 3.2. For other rounds, the key_chunks
            // are shifted by two
            else
            {
                left = shift_left_twice(left);
                right = shift_left_twice(right);
            }
            // Combining the two chunks
            String combined_key = left + right;
            StringBuilder round_key = new StringBuilder();
            // Finally, using the PC2 table to transpose the key bits
            for (int j = 0; j < 48; j++)
            {
                round_key.append(combined_key.charAt(pc2[i] - 1));
            }
            round_keys[i] = round_key.toString();
        }

        return round_keys;
    }

    // Implementing the algorithm
    private String algorithm(String message, String[] round_keys)
    {
        //1. Applying the initial permutation
        StringBuilder perm = new StringBuilder();
        for (int i = 0; i < 64; i++)
        {
            perm.append(message.charAt(initial_permutation[i] - 1));
        }
        // 2. Dividing the result into two equal halves
        String left = perm.substring(0, 32);
        String right = perm.substring(32, 32);
        // The plain text is encrypted 16 times
        for (int i = 0; i < 16; i++)
        {
            StringBuilder right_expanded = new StringBuilder();
            // 3.1. The right half of the plain text is expanded
            for (int j = 0; j < 48; j++)
            {
                right_expanded.append(right.charAt(expansion_table[j] - 1));
            }
            // 3.3. The result is xored with a key
            String xored = Xor(round_keys[i], right_expanded.toString());
            StringBuilder res = new StringBuilder();
            // 3.4. The result is divided into 8 equal parts and passed
            // through 8 substitution boxes. After passing through a
            // substitution box, each box is reduces from 6 to 4 bits.
            for (int j = 0; j < 8; j++)
            {
                // Finding row and column indices to lookup the
                // substitution box
                String row1 = xored.substring(j * 6, 1) + xored.substring(j * 6 + 5, 1);
                int row = convertBinaryToDecimal(row1);
                String col1 = xored.substring(j * 6 + 1, 1) + xored.substring(j * 6 + 2, 1) + xored.substring(j * 6 + 3, 1) + xored.substring(j * 6 + 4, 1);
                int col = convertBinaryToDecimal(col1);
                int val = substitution_boxes[j][row][col];
                res.append(convertDecimalToBinary(val));
            }
            // 3.5. Another permutation is applied
            StringBuilder perm2 = new StringBuilder();
            for (int j = 0; j < 32; j++)
            {
                perm2.append(res.charAt(permutation_tab[j] - 1));
            }
            // 3.6. The result is xored with the left half
            xored = Xor(perm2.toString(), left);
            // 3.7. The left and the right parts of the plain text are swapped
            left = xored;
            if (i < 15)
            {
                String temp = right;
                right = xored;
                left = temp;
            }
        }
        // 4. The halves of the plain text are applied
        String combined_text = left + right;
        StringBuilder ciphertext = new StringBuilder();
        // The inverse of the initial permutation is applied
        for (int i = 0; i < 64; i++)
        {
            ciphertext.append(combined_text.charAt(inverse_permutation[i] - 1));
        }
        //And we finally get the cipher text
        return ciphertext.toString();
    }

    // Function to encrypt a message with a given (random) key
    public String Encrypt(String message, String key)
    {
        String[] round_keys;
        round_keys = generate_keys(key);

        //convert to binary for DES
        StringBuilder text = new StringBuilder(convertStringToBinary(message));
        //initialize output string
        StringBuilder ciphertext = new StringBuilder();

        //Remove the first 64 bits of the string at a time, return them as ciphertext
        while(text.length() >= 64)
        {
            ciphertext.append(algorithm(text.substring(0, 63), round_keys));
            text = new StringBuilder(text.substring(64, text.length() - 1));
        }

        //Take the remaining bits (if any), pad them, and encrypt them
        if(text.length() != 0)
        {
            while(text.length() != 64)
            {
                text.append("0");
            }
            ciphertext.append(algorithm(text.substring(0,63), round_keys));
        }

        return ciphertext.toString();
    }

    public String Decrypt(String ciphertext, String key)
    {
        String[] round_keys;
        round_keys = generate_keys(key);

        // Reversing the round_keys array for decryption
        int i = 15;
        int j = 0;
        while (i > j) {
            String temp = round_keys[i];
            round_keys[i] = round_keys[j];
            round_keys[j] = temp;
            i--;
            j++;
        }

        //convert to binary for DES
        StringBuilder ct = new StringBuilder(convertStringToBinary(ciphertext));
        //initialize output string
        StringBuilder plaintext = new StringBuilder();

        //Remove the first 64 bits of the string at a time, return them as plaintext
        while(ct.length() >= 64)
        {
            plaintext.append(algorithm(ct.substring(0, 63), round_keys));
            ct = new StringBuilder(ct.substring(64, ct.length() - 1));
        }

        //Take the remaining bits (if any), pad them, and decrypt them
        if(ct.length() != 0)
        {
            while(ct.length() != 64)
            {
                ct.append("0");
            }
            plaintext.append(algorithm(ct.substring(0,63), round_keys));
        }

        return convertBinaryToString(plaintext.toString());
    }
}
