package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Sahil Gupta
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _hasRatchet = false;
    }

    /** Updated method that returns false for fixed rotors. */
    @Override
    boolean rotates() {
        return _hasRatchet;
    }

    /** Does not advance the rotor. */
    @Override
    void advance() {
        throw new EnigmaException("Fixed rotor cannot advance positions.");
    }


    /** Set setting() to POSN.  */
    @Override
    void set(int posn) {
        super.set(posn);
    }

    /** Set setting() to character CPOSN. */
    @Override
    void set(char cposn) {
        set(alphabet().toInt(cposn));
    }

    /** Sets the ring setting of this rotor to RINGPOSN. */
    @Override
    void setRingSetting(int ringposn) {
        super.setRingSetting(ringposn);
    }

    /** Sets the ring setting of this rotor to RINGCPOSN. */
    @Override
    void setRingSetting(char ringcposn) {
        setRingSetting(alphabet().toInt(ringcposn));
    }

    /** My non-existent ratchet. */
    private boolean _hasRatchet;
}
