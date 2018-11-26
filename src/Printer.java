package research.analysis;

import java.lang.StringBuilder;
import java.util.Set;

/**
 * Prints created Implications to string builder in specific format
 * (format is given by type of class that implements this interface).
 */
public interface Printer {
	void print(StringBuilder sb, Set<Implies> defs);
}