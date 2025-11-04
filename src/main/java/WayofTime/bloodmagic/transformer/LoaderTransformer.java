package WayofTime.bloodmagic.transformer;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"WayofTime.bloodmagic.transformer"})
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1066)
public class LoaderTransformer implements IFMLLoadingPlugin {
    static public final Logger log = LogManager.getLogger("BloodMagic");
    static private final String[] ASMTransformerClass = new String[] {
        "WayofTime.bloodmagic.transformer.Transformer"
    };

    public LoaderTransformer() {
        log.info("Initialized.");
    }

    @Override
    public String[] getASMTransformerClass() {
        return ASMTransformerClass;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
