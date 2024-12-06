package supermemnon.painpacker;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PainPacker.MODID)
public class PainPacker
{
    public static final String MODID = "painpacker";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public PainPacker() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        PainPackerCommands.register(event.getDispatcher());
    }
}
