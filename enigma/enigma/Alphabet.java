package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Sahil Gupta
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {

        for (int i = 0; i < chars.length() - 1; i += 1) {
            if (chars.substring(i + 1).indexOf(chars.charAt(i)) != -1) {
                throw new EnigmaException(String.format(
                        "Duplicate character \"%c\" found.",
                        chars.charAt(i)));
            } else if (chars.charAt(i) == '*'
                    || chars.charAt(i) == '(' || chars.charAt(i) == ')') {
                throw new EnigmaException(
                        String.format(
                                "Forbidden character \"%c\" found.",
                                chars.charAt(i)));
            }
        }
        _chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException(
                    String.format(
                            "Tried to access nonexistent "
                                   + "letter at index %d.", index));
        } else {
            return _chars.charAt(index);
        }
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (contains(ch)) {
            return _chars.indexOf(ch);
        } else {
            throw new EnigmaException(
                    String.format(
                            "Letter \"%c\" does not "
                                   + "exist in this alphabet.", ch));
        }
    }

    /** Returns if this and OBJ are equivalent objects. */
    public boolean equals(Object obj) {
        Alphabet alpha1 = (Alphabet) obj;
        return _chars.equals(alpha1._chars);
    }

    /** Returns the hashCode for this alphabet. */
    public int hashCode() {
        return _chars.hashCode();
    }

    /** The characters of the alphabet. */
    private String _chars;
}
