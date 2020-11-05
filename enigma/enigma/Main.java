package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Sahil Gupta
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        ArrayList<String> allLines = new ArrayList<>();

        while (_input.hasNextLine()) {
            allLines.add(_input.nextLine());
        }

        int index = 0;
        while (allLines.size() > 0 && !allLines.get(index).contains("*")) {
            allLines.remove(index);
        }

        if (allLines.size() == 0) {
            throw new EnigmaException("No configuration found.");
        }

        while (allLines.size() != 0) {
            String settings = allLines.remove(index);
            ArrayList<String> messages = new ArrayList<>();

            while (allLines.size() > 0 && !allLines.get(index).contains("*")) {
                messages.add(allLines.remove(index));
            }
            setUp(machine, settings);
            for (String message: messages) {
                String encryption = machine.convert(message.replace(" ", ""));
                printMessageLine(encryption);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {

            if (!_config.hasNext()) {
                throw new EnigmaException("Incorrect format. Empty file.");
            }
            _alphabet = new Alphabet(_config.next());

            if (!_config.hasNextInt()) {
                throw new EnigmaException("Incorrect format. "
                       + "Rotor count parameter not found.");
            }
            int numRotors = _config.nextInt();

            if (!_config.hasNextInt()) {
                throw new EnigmaException("Incorrect format. "
                       + "Pawl count parameter not found.");
            }
            int numPawls = _config.nextInt();
            ArrayList<Rotor> rotorConfigs = new ArrayList<>();

            while (_config.hasNext()) {
                rotorConfigs.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, rotorConfigs);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String typeNotches = _config.next();
            Pattern p = Pattern.compile("\\(.*\\)");
            String cycles = "";
            while (_config.hasNext(p)) {
                cycles += _config.next(p) + " ";
            }
            char type = typeNotches.charAt(0);
            Permutation perm = new Permutation(cycles, _alphabet);

            switch (type) {
            case 'M':
                return new MovingRotor(name, perm,
                    typeNotches.substring(1));
            case 'N':
                return new FixedRotor(name, perm);

            case 'R':
                return new Reflector(name, perm);

            default:
                throw new EnigmaException("\""
                           + type + "\" is not a type of rotor.");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            Scanner settingsFeed = new Scanner(settings);
            settingsFeed.next();
            String[] rotorInserts = new String[M.numRotors()];

            for (int i = 0; i < rotorInserts.length; i += 1) {
                rotorInserts[i] = settingsFeed.next();
            }

            for (int i = 0; i < rotorInserts.length; i += 1) {
                String name = rotorInserts[i];
                for (int j = i + 1; j < rotorInserts.length; j += 1) {
                    if (name.equals(rotorInserts[j])) {
                        throw new EnigmaException("Cannot insert "
                               + "the same rotor twice.");
                    }
                }
            }

            String notchSettings = settingsFeed.next();
            String plugboardCycles = "";
            Pattern p = Pattern.compile("\\(.*\\)");
            Pattern q = Pattern.compile("[^\\(]*");

            String ringSettings = "";
            for (int i = 0; i < M.numRotors() - 1; i += 1) {
                ringSettings += _alphabet.toChar(0);
            }

            if (settingsFeed.hasNext(q)) {
                ringSettings = settingsFeed.next();
            }

            while (settingsFeed.hasNext(p)) {
                plugboardCycles += settingsFeed.next(p) + " ";
            }

            M.insertRotors(rotorInserts);
            M.setRotors(notchSettings);
            M.setRingSetting(ringSettings);
            M.setPlugboard(new Permutation(plugboardCycles, _alphabet));

        } catch (NoSuchElementException e) {
            throw new EnigmaException("Bad rotor settings.");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.length() == 0 || msg.equals("\n")) {
            _output.println();
        } else {
            String formattedMsg = "";
            for (int i = 0; i < msg.length(); i += 1) {
                if (i % 5 == 0 && i / 5 > 0) {
                    formattedMsg += " ";
                }
                formattedMsg += msg.charAt(i);
            }
            _output.println(formattedMsg);
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
