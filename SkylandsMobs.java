package com.example.examplemod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.example.examplemod.entity.SkylandMob;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SkylandsMobs.MODID)
public class SkylandsMobs {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "skylandsmobs";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Entities which will all be registered under the "skylandsmobs" namespace
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    // Create a Deferred Register to hold Items for the spawn egg
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    // Create a Deferred Register to hold Creative Tabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new entity type with the id "skylandsmobs:skyland_mob"
    public static final DeferredHolder<EntityType<?>, EntityType<SkylandMob>> SKYLAND_MOB = ENTITIES.register("skyland_mob",
            () -> EntityType.Builder.of(SkylandMob::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("skyland_mob"));

    // Creates a spawn egg for the skyland mob (aether style: white/yellow - inverted enderman colors)
    public static final DeferredHolder<Item, SpawnEggItem> SKYLAND_MOB_SPAWN_EGG = ITEMS.register("skyland_mob_spawn_egg",
            () -> new SpawnEggItem(SKYLAND_MOB.get(), 0xFFFFFF, 0xFFD700, new Item.Properties()));

    // Creates a creative tab for the mod (after combat tab)
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SKYLANDS_TAB = CREATIVE_TABS.register("skylands_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.skylandsmobs"))
                    .icon(() -> new ItemStack(SKYLAND_MOB_SPAWN_EGG.get()))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .displayItems((parameters, output) -> {
                        output.accept(SKYLAND_MOB_SPAWN_EGG.get());
                    })
                    .build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public SkylandsMobs(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so entities get registered
        ENTITIES.register(modEventBus);
        // Register items (spawn egg)
        ITEMS.register(modEventBus);
        // Register creative tabs
        CREATIVE_TABS.register(modEventBus);

        // Register entity attributes on the mod event bus (IModBusEvent)
        modEventBus.addListener(this::registerEntityAttributes);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (SkylandsMobs) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        // Spawn is handled by biome modifier in data/skylandsmobs/worldgen/biome_modifier/skyland_mob_spawn.json

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(net.minecraft.world.level.block.Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(SKYLAND_MOB.get(), SkylandMob.createAttributes().build());
    }
}
