package nessiesson.uselessmod;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] clazz) {
		//LiteLoaderLogger.info(LiteLoaderLogger.Verbosity.REDUCED, name + " " + transformedName);
		return clazz;
	}
}
