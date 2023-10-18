package pt.licious.pixelseasons;

import com.pixelmonmod.pixelmon.api.spawning.conditions.SpawnCondition;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.licious.pixelseasons.condition.SeasonSpawnCondition;

@Mod(PixelSeasons.MOD_ID)
public class PixelSeasons {

    public static final String MOD_NAME = "@MOD_NAME@";
    public static final String MOD_ID = "@MOD_ID@";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);

    @SubscribeEvent
    public void onPreInit(FMLServerAboutToStartEvent e) {
        // I really hate how the API works here.
        if (SpawnCondition.targetedSpawnCondition != SpawnCondition.class) {
            LOG.warn("A 3rd party addon already replaced the base Pixelmon SpawnCondition implementation {} will replace it, this will most likely break the 3rd party addon!", MOD_NAME);
        }
        SpawnCondition.targetedSpawnCondition = SeasonSpawnCondition.class;
    }

}
