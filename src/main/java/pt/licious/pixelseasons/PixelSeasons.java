package pt.licious.pixelseasons;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.licious.pixelseasons.condition.SeasonSpawnCondition;
import sereneseasons.core.SereneSeasons;

@Mod(
    modid = PixelSeasons.MOD_ID,
    name = PixelSeasons.MOD_NAME,
    version = PixelSeasons.VERSION,
    acceptableRemoteVersions = "*",
    dependencies = "required-after:" + Pixelmon.MODID + ";required-after:" + SereneSeasons.MOD_ID + ";",
    acceptedMinecraftVersions = "[1.12.2]"
)
public class PixelSeasons {

    public static final String MOD_NAME = "@MOD_NAME@";
    public static final String MOD_ID = "@MOD_ID@";
    public static final String VERSION = "@VERSION@";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static PixelSeasons instance;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent e) {
        // I really hate how the API works here.
        if (SpawnCondition.targetedSpawnCondition != SpawnCondition.class)
            LOG.warn("A 3rd party addon already replaced the base Pixelmon SpawnCondition implementation {} will replace it, this will most likely break the 3rd party addon!", MOD_NAME);
        SpawnCondition.targetedSpawnCondition = SeasonSpawnCondition.class;
    }

}
