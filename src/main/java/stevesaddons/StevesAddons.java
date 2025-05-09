package stevesaddons;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import stevesaddons.asm.StevesHooks;
import stevesaddons.helpers.Config;
import stevesaddons.helpers.StevesEnum;
import stevesaddons.interfaces.GuiHandler;
import stevesaddons.naming.EventHandler;
import stevesaddons.naming.NameData;
import stevesaddons.naming.NameRegistry;
import stevesaddons.network.MessageHandler;
import stevesaddons.proxy.CommonProxy;
import stevesaddons.recipes.ClusterUncraftingRecipe;
import stevesaddons.reference.Metadata;
import stevesaddons.registry.BlockRegistry;
import stevesaddons.registry.CommandRegistry;
import stevesaddons.registry.ItemRegistry;
import vswe.stevesfactory.blocks.TileEntityManager;
import vswe.stevesfactory.compat.Compat;

@Mod(
        modid = StevesAddons.ID,
        name = StevesAddons.NAME,
        version = Tags.VERSION,
        dependencies = "required-after:StevesFactoryManager;required-after:CoFHCore")
public class StevesAddons {

    // User friendly version of our mods name.
    public static final String NAME = "Steve's Addons";

    // Internal mod name used for reference purposes and resource gathering.
    public static final String ID = "StevesAddons";

    @Mod.Instance(value = ID)
    public static StevesAddons INSTANCE;

    @Mod.Metadata(ID)
    public static ModMetadata metadata;

    @SidedProxy(clientSide = "stevesaddons.proxy.ClientProxy", serverSide = "stevesaddons.proxy.CommonProxy")
    public static CommonProxy PROXY;

    public static GuiHandler guiHandler = new GuiHandler();
    public static Logger log = LogManager.getLogger(ID);
    public static boolean isMFRLoaded;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Compat.ADDONS_HOOKS = new StevesHooks();

        metadata = Metadata.init(metadata);
        Config.init(event.getSuggestedConfigurationFile());
        ItemRegistry.registerItems();
        BlockRegistry.registerBlocks();
        MessageHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(StevesAddons.INSTANCE, guiHandler);
        isMFRLoaded = Loader.isModLoaded("MineFactoryReloaded");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        ItemRegistry.registerRecipes();
        BlockRegistry.registerRecipes();
        ClusterUncraftingRecipe uncrafting = new ClusterUncraftingRecipe();
        GameRegistry.addRecipe(uncrafting);
        FMLCommonHandler.instance().bus().register(uncrafting);
        EventHandler handler = new EventHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
        PROXY.initHandlers();
        if (Config.wailaIntegration) {
            FMLInterModComms.sendMessage("Waila", "register", "stevesaddons.waila.WailaManager.callbackRegister");
        }
    }

    @Mod.EventHandler
    @SuppressWarnings(value = "unchecked")
    public void postInit(FMLPostInitializationEvent e) {
        if (Loader.isModLoaded("JABBA")) {
            try {
                Class dolly = Class.forName("mcp.mobius.betterbarrels.common.items.dolly.ItemBarrelMover");
                Field classExtensions = dolly.getDeclaredField("classExtensions");
                Field classExtensionsNames = dolly.getDeclaredField("classExtensionsNames");
                Field classMap = dolly.getDeclaredField("classMap");
                classExtensions.setAccessible(true);
                classExtensionsNames.setAccessible(true);
                classMap.setAccessible(true);
                ArrayList<Class> extensions = (ArrayList<Class>) classExtensions.get(null);
                ArrayList<String> extensionsNames = (ArrayList<String>) classExtensionsNames.get(null);
                HashMap<String, Class> map = (HashMap<String, Class>) classMap.get(null);
                extensions.add(TileEntityManager.class);
                extensionsNames.add(TileEntityManager.class.getSimpleName());
                map.put(TileEntityManager.class.getSimpleName(), TileEntityManager.class);
            } catch (Exception ignore) {}
        }
        StevesEnum.applyEnumHacks();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        NameRegistry.setNameData(new HashMap<Integer, NameData>());
        event.registerServerCommand(CommandRegistry.instance);
        File file = new File(DimensionManager.getCurrentSaveRootDirectory().getPath() + File.separator + "managers");
        if (!file.exists()) file.mkdirs();
    }
}
