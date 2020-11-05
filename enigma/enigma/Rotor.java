package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Sahil Gupta
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _hasRatchet = true;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return _hasRatchet;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = mod(posn, size());
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int shiftedPermutation = permutation().permute(p + setting());
        return mod(shiftedPermutation - setting(), size());
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int shiftedInversion = permutation().invert(e + setting());
        return mod(shiftedInversion - setting(), size());
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
        set(setting() + 1);
    }

    /** Returns X mod M. */
    public static int mod(int x, int m) {
        x = x % m;
        if (x < 0) {
            x += m;
        }
        return x;
    }

    /** Returns the ring setting of this rotor. */
    int ringSetting() {
        return _ringSetting;
    }

    /** Sets the ring setting of this rotor to RINGPOSN. */
    void setRingSetting(int ringposn) {
        _ringSetting = mod(ringposn, size());
    }

    /** Sets the ring setting of this rotor to RINGCPOSN. */
    void setRingSetting(char ringcposn) {
        setRingSetting(alphabet().toInt(ringcposn));
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** Denotes if the rotor has a ratchet or not. */
    private boolean _hasRatchet;

    /** Stores the rotation setting of the rotor. */
    private int _setting;

    /** Stores the ring setting of the rotor. */
    private int _ringSetting;

}
