package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Sahil Gupta
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;

        ArrayList<String> possibleCycles = new ArrayList<>();
        int leftPos, rightPos; String copyCycles = cycles.trim();

        while (copyCycles.length() > 0) {
            leftPos = copyCycles.indexOf("(");
            rightPos = copyCycles.indexOf(")");

            if (leftPos != -1 && rightPos != -1 && rightPos > leftPos) {
                possibleCycles.add(copyCycles.substring(leftPos, rightPos + 1));
                copyCycles = copyCycles.substring(rightPos + 1);
            } else {
                throw new EnigmaException("Bad input format: "
                       + "Matching parentheses not found.");
            }
        }

        checkUnique = "";
        _cycles = new ArrayList<>();

        for (String cycle: possibleCycles) {
            String cycleChars = cycle.substring(1, cycle.length() - 1);
            if (cycleChars.length() > 0) {
                addCycle(cycleChars);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {

        for (int i = 0; i < cycle.length(); i += 1) {
            char c = cycle.charAt(i);
            if (Character.isWhitespace(c)) {
                throw new EnigmaException(
                        "Bad input format: Cycle "
                                + cycle + " contains whitespace.");
            } else if (!_alphabet.contains(c)) {
                throw new EnigmaException(
                        String.format("\"%c\" not in alphabet.", c));
            } else if (checkUnique.indexOf(c) != -1) {
                throw new EnigmaException(
                        String.format("Bad input format: "
                                + "character \"%c\" maps to "
                                + "more than 1 letter.", c));
            }
            checkUnique += c;
        }
        _cycles.add(cycle.toCharArray());
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        char toPermute = _alphabet.toChar(p);
        char permutation = toPermute;
        for (char[] cycle: _cycles) {
            for (int i = 0; i < cycle.length; i += 1) {
                if (cycle[i] == toPermute) {
                    permutation = cycle[(i + 1) % cycle.length];
                    break;
                }
            }
        }
        return _alphabet.toInt(permutation);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char toInvert = _alphabet.toChar(c);
        char inversion = toInvert;
        for (char[] cycle: _cycles) {
            for (int i = 0; i < cycle.length; i += 1) {

                if (cycle[i] == toInvert) {
                    int index = i - 1;
                    if (index < 0) {
                        index += cycle.length;
                    }
                    inversion = cycle[index];
                    break;
                }
            }
        }
        return _alphabet.toInt(inversion);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        if (size() > checkUnique.length()) {
            return false;
        } else {
            for (char[] cycle: _cycles) {
                if (cycle.length == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** The cycles of this permutation.*/
    private ArrayList<char[]> _cycles;

    /** Tracks the cycle characters in order to check they are unique. */
    private String checkUnique;
}
