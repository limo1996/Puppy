package research.analysis;

import soot.toolkits.graph.*;

/**
 * Factory pattern of ConditionalReachingDefinitions creation.
 */
public class DefinitionFactory {
	// No need to instanciate it!
	private DefinitionFactory() { }

	/**
	 * Factory method of ConditionalReachingDefinitions creation.
	 * @param graph CFG of the method.
	 * @param settings Settings for analysis, representation field specifies subclass to instantiate.
	 * @return Returns appropriate subclass of ConditionalReachingDefinitions.
	 */
	public static ConditionalReachingDefinitions getRepresentation(UnitGraph graph, Settings settings) {
		switch (settings.getRepresentation()) {
			case JIMPLE:
				return new JConditionalReachingDefinitions(graph, settings);
			case SHIMPLE:
				return new SConditionalReachingDefinitions(graph, settings);
		}
		return null;
	}
}