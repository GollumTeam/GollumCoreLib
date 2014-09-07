package mods.gollum.core.tools.registered;

import java.util.Hashtable;
import java.util.Map.Entry;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.gollum.core.ModGollumCoreLib;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RegisteredObjects {
	
	private static RegisteredObjects instance = null;
	
	public static RegisteredObjects instance () {
		
		if (instance == null) {
			instance = new RegisteredObjects();
		}
		
		return instance;
	}
	
	public Hashtable<String, Block> vanillaBlocks = new Hashtable<String, Block>();
	public Hashtable<String, Item>  vanillaItems  = new Hashtable<String, Item>();
	
	public RegisteredObjects() {
		
		// Init vanilla
		
//		vanillaBlocks.put ("minecraft:air"                          , null                 ); //net.minecraft.block.BlockAir
		vanillaBlocks.put ("minecraft:stone"                        , Block.blocksList[1  ]); //net.minecraft.block.BlockStone
		vanillaBlocks.put ("minecraft:grass"                        , Block.blocksList[2  ]); //net.minecraft.block.BlockGrass
		vanillaBlocks.put ("minecraft:dirt"                         , Block.blocksList[3  ]); //net.minecraft.block.BlockDirt
		vanillaBlocks.put ("minecraft:cobblestone"                  , Block.blocksList[4  ]); //net.minecraft.block.Block
		vanillaBlocks.put ("minecraft:planks"                       , Block.blocksList[5  ]); //net.minecraft.block.BlockWood
		vanillaBlocks.put ("minecraft:sapling"                      , Block.blocksList[6  ]); //net.minecraft.block.BlockSapling
		vanillaBlocks.put ("minecraft:bedrock"                      , Block.blocksList[7  ]); //net.minecraft.block.Block
		vanillaBlocks.put ("minecraft:flowing_water"                , Block.blocksList[8  ]); //net.minecraft.block.BlockDynamicLiquid
		vanillaBlocks.put ("minecraft:water"                        , Block.blocksList[9  ]); //net.minecraft.block.BlockStaticLiquid
		vanillaBlocks.put ("minecraft:flowing_lava"                 , Block.blocksList[10 ]); //net.minecraft.block.BlockDynamicLiquid
		vanillaBlocks.put ("minecraft:lava"                         , Block.blocksList[11 ]); //net.minecraft.block.BlockStaticLiquid
		vanillaBlocks.put ("minecraft:sand"                         , Block.blocksList[12 ]); //net.minecraft.block.BlockSand
		vanillaBlocks.put ("minecraft:gravel"                       , Block.blocksList[13 ]); //net.minecraft.block.BlockGravel
		vanillaBlocks.put ("minecraft:gold_ore"                     , Block.blocksList[14 ]); //net.minecraft.block.BlockOre
		vanillaBlocks.put ("minecraft:iron_ore"                     , Block.blocksList[15 ]); //net.minecraft.block.BlockOre
		vanillaBlocks.put ("minecraft:coal_ore"                     , Block.blocksList[16 ]); //net.minecraft.block.BlockOre
		vanillaBlocks.put ("minecraft:log"                          , Block.blocksList[17 ]); //net.minecraft.block.BlockOldLog
		vanillaBlocks.put ("minecraft:leaves"                       , Block.blocksList[18 ]); //net.minecraft.block.BlockOldLeaf
		vanillaBlocks.put ("minecraft:sponge"                       , Block.blocksList[19 ]); //net.minecraft.block.BlockSponge
		vanillaBlocks.put ("minecraft:glass"                        , Block.blocksList[20 ]); //net.minecraft.block.BlockGlass
		vanillaBlocks.put ("minecraft:lapis_ore"                    , Block.blocksList[21 ]); //net.minecraft.block.BlockOre
		vanillaBlocks.put ("minecraft:lapis_block"                  , Block.blocksList[22 ]); //net.minecraft.block.BlockCompressed
		vanillaBlocks.put ("minecraft:dispenser"                    , Block.blocksList[23 ]); //net.minecraft.block.BlockDispenser
		vanillaBlocks.put ("minecraft:sandstone"                    , Block.blocksList[24 ]); //net.minecraft.block.BlockSandStone
		vanillaBlocks.put ("minecraft:noteblock"                    , Block.blocksList[25 ]); //net.minecraft.block.BlockNote
		vanillaBlocks.put ("minecraft:bed"                          , Block.blocksList[26 ]); //net.minecraft.block.BlockBed
		vanillaBlocks.put ("minecraft:golden_rail"                  , Block.blocksList[27 ]); //net.minecraft.block.BlockRailPowered
		vanillaBlocks.put ("minecraft:detector_rail"                , Block.blocksList[28 ]); //net.minecraft.block.BlockRailDetector
		vanillaBlocks.put ("minecraft:sticky_piston"                , Block.blocksList[29 ]); //net.minecraft.block.BlockPistonBase
		vanillaBlocks.put ("minecraft:web"                          , Block.blocksList[30 ]); //net.minecraft.block.BlockWeb
		vanillaBlocks.put ("minecraft:tallgrass"                    , Block.blocksList[31 ]); //net.minecraft.block.BlockTallGrass
		vanillaBlocks.put ("minecraft:deadbush"                     , Block.blocksList[32 ]); //net.minecraft.block.BlockDeadBush
		vanillaBlocks.put ("minecraft:piston"                       , Block.blocksList[33 ]); //net.minecraft.block.BlockPistonBase
		vanillaBlocks.put ("minecraft:piston_head"                  , Block.blocksList[34 ]); //net.minecraft.block.BlockPistonExtension
		vanillaBlocks.put ("minecraft:wool"                         , Block.blocksList[35 ]); //net.minecraft.block.BlockColored
		vanillaBlocks.put ("minecraft:piston_extension"             , Block.blocksList[36 ]); //net.minecraft.block.BlockPistonMoving
		vanillaBlocks.put ("minecraft:yellow_flower"                , Block.blocksList[37 ]); //net.minecraft.block.BlockFlower
		vanillaBlocks.put ("minecraft:red_flower"                   , Block.blocksList[38 ]); //net.minecraft.block.BlockFlower
		vanillaBlocks.put ("minecraft:brown_mushroom"               , Block.blocksList[39 ]); //net.minecraft.block.BlockMushroom
		vanillaBlocks.put ("minecraft:red_mushroom"                 , Block.blocksList[40 ]); //net.minecraft.block.BlockMushroom
		vanillaBlocks.put ("minecraft:gold_block"                   , Block.blocksList[41 ]); //net.minecraft.block.BlockCompressed
		vanillaBlocks.put ("minecraft:iron_block"                   , Block.blocksList[42 ]); //net.minecraft.block.BlockCompressed
		vanillaBlocks.put ("minecraft:double_stone_slab"            , Block.blocksList[43 ]); //net.minecraft.block.BlockStoneSlab
		vanillaBlocks.put ("minecraft:stone_slab"                   , Block.blocksList[44 ]); //net.minecraft.block.BlockStoneSlab
		vanillaBlocks.put ("minecraft:brick_block"                  , Block.blocksList[45 ]); //net.minecraft.block.Block
		vanillaBlocks.put ("minecraft:tnt"                          , Block.blocksList[46 ]); //net.minecraft.block.BlockTNT
		vanillaBlocks.put ("minecraft:bookshelf"                    , Block.blocksList[47 ]); //net.minecraft.block.BlockBookshelf
		vanillaBlocks.put ("minecraft:mossy_cobblestone"            , Block.blocksList[48 ]); //net.minecraft.block.Block
		vanillaBlocks.put ("minecraft:obsidian"                     , Block.blocksList[49 ]); //net.minecraft.block.BlockObsidian
		vanillaBlocks.put ("minecraft:torch"                        , Block.blocksList[50 ]); //net.minecraft.block.BlockTorch
		vanillaBlocks.put ("minecraft:fire"                         , Block.blocksList[51 ]); //net.minecraft.block.BlockFire
		vanillaBlocks.put ("minecraft:mob_spawner"                  , Block.blocksList[52 ]); //net.minecraft.block.BlockMobSpawner
		vanillaBlocks.put ("minecraft:oak_stairs"                   , Block.blocksList[53 ]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:chest"                        , Block.blocksList[54 ]); //net.minecraft.block.BlockChest
		vanillaBlocks.put ("minecraft:redstone_wire"                , Block.blocksList[55 ]); //net.minecraft.block.BlockRedstoneWire
		vanillaBlocks.put ("minecraft:diamond_ore"                  , Block.blocksList[56 ]); //net.minecraft.block.BlockOre
		vanillaBlocks.put ("minecraft:diamond_block"                , Block.blocksList[57 ]); //net.minecraft.block.BlockCompressed
		vanillaBlocks.put ("minecraft:crafting_table"               , Block.blocksList[58 ]); //net.minecraft.block.BlockWorkbench
		vanillaBlocks.put ("minecraft:wheat"                        , Block.blocksList[59 ]); //net.minecraft.block.BlockCrops
		vanillaBlocks.put ("minecraft:farmland"                     , Block.blocksList[60 ]); //net.minecraft.block.BlockFarmland
		vanillaBlocks.put ("minecraft:furnace"                      , Block.blocksList[61 ]); //net.minecraft.block.BlockFurnace
		vanillaBlocks.put ("minecraft:lit_furnace"                  , Block.blocksList[62 ]); //net.minecraft.block.BlockFurnace
		vanillaBlocks.put ("minecraft:standing_sign"                , Block.blocksList[63 ]); //net.minecraft.block.BlockSign
		vanillaBlocks.put ("minecraft:wooden_door"                  , Block.blocksList[64 ]); //net.minecraft.block.BlockDoor
		vanillaBlocks.put ("minecraft:ladder"                       , Block.blocksList[65 ]); //net.minecraft.block.BlockLadder
		vanillaBlocks.put ("minecraft:rail"                         , Block.blocksList[66 ]); //net.minecraft.block.BlockRail
		vanillaBlocks.put ("minecraft:stone_stairs"                 , Block.blocksList[67 ]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:wall_sign"                    , Block.blocksList[68 ]); //net.minecraft.block.BlockSign
		vanillaBlocks.put ("minecraft:lever"                        , Block.blocksList[69 ]); //net.minecraft.block.BlockLever
		vanillaBlocks.put ("minecraft:stone_pressure_plate"         , Block.blocksList[70 ]); //net.minecraft.block.BlockPressurePlate
		vanillaBlocks.put ("minecraft:iron_door"                    , Block.blocksList[71 ]); //net.minecraft.block.BlockDoor
		vanillaBlocks.put ("minecraft:wooden_pressure_plate"        , Block.blocksList[72 ]); //net.minecraft.block.BlockPressurePlate
		vanillaBlocks.put ("minecraft:redstone_ore"                 , Block.blocksList[73 ]); //net.minecraft.block.BlockRedstoneOre
		vanillaBlocks.put ("minecraft:lit_redstone_ore"             , Block.blocksList[74 ]); //net.minecraft.block.BlockRedstoneOre
		vanillaBlocks.put ("minecraft:unlit_redstone_torch"         , Block.blocksList[75 ]); //net.minecraft.block.BlockRedstoneTorch
		vanillaBlocks.put ("minecraft:redstone_torch"               , Block.blocksList[76 ]); //net.minecraft.block.BlockRedstoneTorch
		vanillaBlocks.put ("minecraft:stone_button"                 , Block.blocksList[77 ]); //net.minecraft.block.BlockButtonStone
		vanillaBlocks.put ("minecraft:snow_layer"                   , Block.blocksList[78 ]); //net.minecraft.block.BlockSnow
		vanillaBlocks.put ("minecraft:ice"                          , Block.blocksList[79 ]); //net.minecraft.block.BlockIce
		vanillaBlocks.put ("minecraft:snow"                         , Block.blocksList[80 ]); //net.minecraft.block.BlockSnowBlock
		vanillaBlocks.put ("minecraft:cactus"                       , Block.blocksList[81 ]); //net.minecraft.block.BlockCactus
		vanillaBlocks.put ("minecraft:clay"                         , Block.blocksList[82 ]); //net.minecraft.block.BlockClay
		vanillaBlocks.put ("minecraft:reeds"                        , Block.blocksList[83 ]); //net.minecraft.block.BlockReed
		vanillaBlocks.put ("minecraft:jukebox"                      , Block.blocksList[84 ]); //net.minecraft.block.BlockJukebox
		vanillaBlocks.put ("minecraft:fence"                        , Block.blocksList[85 ]); //net.minecraft.block.BlockFence
		vanillaBlocks.put ("minecraft:pumpkin"                      , Block.blocksList[86 ]); //net.minecraft.block.BlockPumpkin
		vanillaBlocks.put ("minecraft:netherrack"                   , Block.blocksList[87 ]); //net.minecraft.block.BlockNetherrack
		vanillaBlocks.put ("minecraft:soul_sand"                    , Block.blocksList[88 ]); //net.minecraft.block.BlockSoulSand
		vanillaBlocks.put ("minecraft:glowstone"                    , Block.blocksList[89 ]); //net.minecraft.block.BlockGlowstone
		vanillaBlocks.put ("minecraft:portal"                       , Block.blocksList[90 ]); //net.minecraft.block.BlockPortal
		vanillaBlocks.put ("minecraft:lit_pumpkin"                  , Block.blocksList[91 ]); //net.minecraft.block.BlockPumpkin
		vanillaBlocks.put ("minecraft:cake"                         , Block.blocksList[92 ]); //net.minecraft.block.BlockCake
		vanillaBlocks.put ("minecraft:unpowered_repeater"           , Block.blocksList[93 ]); //net.minecraft.block.BlockRedstoneRepeater
		vanillaBlocks.put ("minecraft:powered_repeater"             , Block.blocksList[94 ]); //net.minecraft.block.BlockRedstoneRepeater
		vanillaBlocks.put ("minecraft:stained_glass"                , Block.blocksList[95 ]); //net.minecraft.block.BlockStainedGlass
		vanillaBlocks.put ("minecraft:trapdoor"                     , Block.blocksList[96 ]); //net.minecraft.block.BlockTrapDoor
		vanillaBlocks.put ("minecraft:monster_egg"                  , Block.blocksList[97 ]); //net.minecraft.block.BlockSilverfish
		vanillaBlocks.put ("minecraft:stonebrick"                   , Block.blocksList[98 ]); //net.minecraft.block.BlockStoneBrick
		vanillaBlocks.put ("minecraft:brown_mushroom_block"         , Block.blocksList[99 ]); //net.minecraft.block.BlockHugeMushroom
		vanillaBlocks.put ("minecraft:red_mushroom_block"           , Block.blocksList[100]); //net.minecraft.block.BlockHugeMushroom
		vanillaBlocks.put ("minecraft:iron_bars"                    , Block.blocksList[101]); //net.minecraft.block.BlockPane
		vanillaBlocks.put ("minecraft:glass_pane"                   , Block.blocksList[102]); //net.minecraft.block.BlockPane
		vanillaBlocks.put ("minecraft:melon_block"                  , Block.blocksList[103]); //net.minecraft.block.BlockMelon
		vanillaBlocks.put ("minecraft:pumpkin_stem"                 , Block.blocksList[104]); //net.minecraft.block.BlockStem
		vanillaBlocks.put ("minecraft:melon_stem"                   , Block.blocksList[105]); //net.minecraft.block.BlockStem
		vanillaBlocks.put ("minecraft:vine"                         , Block.blocksList[106]); //net.minecraft.block.BlockVine
		vanillaBlocks.put ("minecraft:fence_gate"                   , Block.blocksList[107]); //net.minecraft.block.BlockFenceGate
		vanillaBlocks.put ("minecraft:brick_stairs"                 , Block.blocksList[108]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:stone_brick_stairs"           , Block.blocksList[109]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:mycelium"                     , Block.blocksList[110]); //net.minecraft.block.BlockMycelium
		vanillaBlocks.put ("minecraft:waterlily"                    , Block.blocksList[111]); //net.minecraft.block.BlockLilyPad
		vanillaBlocks.put ("minecraft:nether_brick"                 , Block.blocksList[112]); //net.minecraft.block.Block
		vanillaBlocks.put ("minecraft:nether_brick_fence"           , Block.blocksList[113]); //net.minecraft.block.BlockFence
		vanillaBlocks.put ("minecraft:nether_brick_stairs"          , Block.blocksList[114]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:nether_wart"                  , Block.blocksList[115]); //net.minecraft.block.BlockNetherWart
		vanillaBlocks.put ("minecraft:enchanting_table"             , Block.blocksList[116]); //net.minecraft.block.BlockEnchantmentTable
		vanillaBlocks.put ("minecraft:brewing_stand"                , Block.blocksList[117]); //net.minecraft.block.BlockBrewingStand
		vanillaBlocks.put ("minecraft:cauldron"                     , Block.blocksList[118]); //net.minecraft.block.BlockCauldron
		vanillaBlocks.put ("minecraft:end_portal"                   , Block.blocksList[119]); //net.minecraft.block.BlockEndPortal
		vanillaBlocks.put ("minecraft:end_portal_frame"             , Block.blocksList[120]); //net.minecraft.block.BlockEndPortalFrame
		vanillaBlocks.put ("minecraft:end_stone"                    , Block.blocksList[121]); //net.minecraft.block.Block
		vanillaBlocks.put ("minecraft:dragon_egg"                   , Block.blocksList[122]); //net.minecraft.block.BlockDragonEgg
		vanillaBlocks.put ("minecraft:redstone_lamp"                , Block.blocksList[123]); //net.minecraft.block.BlockRedstoneLight
		vanillaBlocks.put ("minecraft:lit_redstone_lamp"            , Block.blocksList[124]); //net.minecraft.block.BlockRedstoneLight
		vanillaBlocks.put ("minecraft:double_wooden_slab"           , Block.blocksList[125]); //net.minecraft.block.BlockWoodSlab
		vanillaBlocks.put ("minecraft:wooden_slab"                  , Block.blocksList[126]); //net.minecraft.block.BlockWoodSlab
		vanillaBlocks.put ("minecraft:cocoa"                        , Block.blocksList[127]); //net.minecraft.block.BlockCocoa
		vanillaBlocks.put ("minecraft:sandstone_stairs"             , Block.blocksList[128]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:emerald_ore"                  , Block.blocksList[129]); //net.minecraft.block.BlockOre
		vanillaBlocks.put ("minecraft:ender_chest"                  , Block.blocksList[130]); //net.minecraft.block.BlockEnderChest
		vanillaBlocks.put ("minecraft:tripwire_hook"                , Block.blocksList[131]); //net.minecraft.block.BlockTripWireHook
		vanillaBlocks.put ("minecraft:tripwire"                     , Block.blocksList[132]); //net.minecraft.block.BlockTripWire
		vanillaBlocks.put ("minecraft:emerald_block"                , Block.blocksList[133]); //net.minecraft.block.BlockCompressed
		vanillaBlocks.put ("minecraft:spruce_stairs"                , Block.blocksList[134]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:birch_stairs"                 , Block.blocksList[135]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:jungle_stairs"                , Block.blocksList[136]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:command_block"                , Block.blocksList[137]); //net.minecraft.block.BlockCommandBlock
		vanillaBlocks.put ("minecraft:beacon"                       , Block.blocksList[138]); //net.minecraft.block.BlockBeacon
		vanillaBlocks.put ("minecraft:cobblestone_wall"             , Block.blocksList[139]); //net.minecraft.block.BlockWall
		vanillaBlocks.put ("minecraft:flower_pot"                   , Block.blocksList[140]); //net.minecraft.block.BlockFlowerPot
		vanillaBlocks.put ("minecraft:carrots"                      , Block.blocksList[141]); //net.minecraft.block.BlockCarrot
		vanillaBlocks.put ("minecraft:potatoes"                     , Block.blocksList[142]); //net.minecraft.block.BlockPotato
		vanillaBlocks.put ("minecraft:wooden_button"                , Block.blocksList[143]); //net.minecraft.block.BlockButtonWood
		vanillaBlocks.put ("minecraft:skull"                        , Block.blocksList[144]); //net.minecraft.block.BlockSkull
		vanillaBlocks.put ("minecraft:anvil"                        , Block.blocksList[145]); //net.minecraft.block.BlockAnvil
		vanillaBlocks.put ("minecraft:trapped_chest"                , Block.blocksList[146]); //net.minecraft.block.BlockChest
		vanillaBlocks.put ("minecraft:light_weighted_pressure_plate", Block.blocksList[147]); //net.minecraft.block.BlockPressurePlateWeighted
		vanillaBlocks.put ("minecraft:heavy_weighted_pressure_plate", Block.blocksList[148]); //net.minecraft.block.BlockPressurePlateWeighted
		vanillaBlocks.put ("minecraft:unpowered_comparator"         , Block.blocksList[149]); //net.minecraft.block.BlockRedstoneComparator
		vanillaBlocks.put ("minecraft:powered_comparator"           , Block.blocksList[150]); //net.minecraft.block.BlockRedstoneComparator
		vanillaBlocks.put ("minecraft:daylight_detector"            , Block.blocksList[151]); //net.minecraft.block.BlockDaylightDetector
		vanillaBlocks.put ("minecraft:redstone_block"               , Block.blocksList[152]); //net.minecraft.block.BlockCompressedPowered
		vanillaBlocks.put ("minecraft:quartz_ore"                   , Block.blocksList[153]); //net.minecraft.block.BlockOre
		vanillaBlocks.put ("minecraft:hopper"                       , Block.blocksList[154]); //net.minecraft.block.BlockHopper
		vanillaBlocks.put ("minecraft:quartz_block"                 , Block.blocksList[155]); //net.minecraft.block.BlockQuartz
		vanillaBlocks.put ("minecraft:quartz_stairs"                , Block.blocksList[156]); //net.minecraft.block.BlockStairs
		vanillaBlocks.put ("minecraft:activator_rail"               , Block.blocksList[157]); //net.minecraft.block.BlockRailPowered
		vanillaBlocks.put ("minecraft:dropper"                      , Block.blocksList[158]); //net.minecraft.block.BlockDropper
		vanillaBlocks.put ("minecraft:stained_hardened_clay"        , Block.blocksList[159]); //net.minecraft.block.BlockColored
		vanillaBlocks.put ("minecraft:hay_block"                    , Block.blocksList[170]); //net.minecraft.block.BlockHay
		vanillaBlocks.put ("minecraft:carpet"                       , Block.blocksList[171]); //net.minecraft.block.BlockCarpet
		vanillaBlocks.put ("minecraft:hardened_clay"                , Block.blocksList[172]); //net.minecraft.block.BlockHardenedClay
		vanillaBlocks.put ("minecraft:coal_block"                   , Block.blocksList[173]); //net.minecraft.block.Block
		
		vanillaItems.put ("minecraft:stone"                        , Item.itemsList[1   ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:grass"                        , Item.itemsList[2   ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:dirt"                         , Item.itemsList[3   ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:cobblestone"                  , Item.itemsList[4   ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:planks"                       , Item.itemsList[5   ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:sapling"                      , Item.itemsList[6   ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:bedrock"                      , Item.itemsList[7   ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:flowing_water"                , Item.itemsList[8   ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:water"                        , Item.itemsList[9   ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:flowing_lava"                 , Item.itemsList[10  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:lava"                         , Item.itemsList[11  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:sand"                         , Item.itemsList[12  ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:gravel"                       , Item.itemsList[13  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:gold_ore"                     , Item.itemsList[14  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:iron_ore"                     , Item.itemsList[15  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:coal_ore"                     , Item.itemsList[16  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:log"                          , Item.itemsList[17  ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:leaves"                       , Item.itemsList[18  ]); // net.minecraft.item.ItemLeaves
		vanillaItems.put ("minecraft:sponge"                       , Item.itemsList[19  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:glass"                        , Item.itemsList[20  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:lapis_ore"                    , Item.itemsList[21  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:lapis_block"                  , Item.itemsList[22  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:dispenser"                    , Item.itemsList[23  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:sandstone"                    , Item.itemsList[24  ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:noteblock"                    , Item.itemsList[25  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:golden_rail"                  , Item.itemsList[27  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:detector_rail"                , Item.itemsList[28  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:sticky_piston"                , Item.itemsList[29  ]); // net.minecraft.item.ItemPiston
		vanillaItems.put ("minecraft:web"                          , Item.itemsList[30  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:tallgrass"                    , Item.itemsList[31  ]); // net.minecraft.item.ItemColored
		vanillaItems.put ("minecraft:deadbush"                     , Item.itemsList[32  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:piston"                       , Item.itemsList[33  ]); // net.minecraft.item.ItemPiston
		vanillaItems.put ("minecraft:wool"                         , Item.itemsList[35  ]); // net.minecraft.item.ItemCloth
		vanillaItems.put ("minecraft:yellow_flower"                , Item.itemsList[37  ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:red_flower"                   , Item.itemsList[38  ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:brown_mushroom"               , Item.itemsList[39  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:red_mushroom"                 , Item.itemsList[40  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:gold_block"                   , Item.itemsList[41  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:iron_block"                   , Item.itemsList[42  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:double_stone_slab"            , Item.itemsList[43  ]); // net.minecraft.item.ItemSlab
		vanillaItems.put ("minecraft:stone_slab"                   , Item.itemsList[44  ]); // net.minecraft.item.ItemSlab
		vanillaItems.put ("minecraft:brick_block"                  , Item.itemsList[45  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:tnt"                          , Item.itemsList[46  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:bookshelf"                    , Item.itemsList[47  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:mossy_cobblestone"            , Item.itemsList[48  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:obsidian"                     , Item.itemsList[49  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:torch"                        , Item.itemsList[50  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:fire"                         , Item.itemsList[51  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:mob_spawner"                  , Item.itemsList[52  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:oak_stairs"                   , Item.itemsList[53  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:chest"                        , Item.itemsList[54  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:diamond_ore"                  , Item.itemsList[56  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:diamond_block"                , Item.itemsList[57  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:crafting_table"               , Item.itemsList[58  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:farmland"                     , Item.itemsList[60  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:furnace"                      , Item.itemsList[61  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:lit_furnace"                  , Item.itemsList[62  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:ladder"                       , Item.itemsList[65  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:rail"                         , Item.itemsList[66  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:stone_stairs"                 , Item.itemsList[67  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:lever"                        , Item.itemsList[69  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:stone_pressure_plate"         , Item.itemsList[70  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:wooden_pressure_plate"        , Item.itemsList[72  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:redstone_ore"                 , Item.itemsList[73  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:redstone_torch"               , Item.itemsList[76  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:stone_button"                 , Item.itemsList[77  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:snow_layer"                   , Item.itemsList[78  ]); // net.minecraft.item.ItemSnow
		vanillaItems.put ("minecraft:ice"                          , Item.itemsList[79  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:snow"                         , Item.itemsList[80  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:cactus"                       , Item.itemsList[81  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:clay"                         , Item.itemsList[82  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:jukebox"                      , Item.itemsList[84  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:fence"                        , Item.itemsList[85  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:pumpkin"                      , Item.itemsList[86  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:netherrack"                   , Item.itemsList[87  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:soul_sand"                    , Item.itemsList[88  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:glowstone"                    , Item.itemsList[89  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:portal"                       , Item.itemsList[90  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:lit_pumpkin"                  , Item.itemsList[91  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:stained_glass"                , Item.itemsList[95  ]); // net.minecraft.item.ItemCloth
		vanillaItems.put ("minecraft:trapdoor"                     , Item.itemsList[96  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:monster_egg"                  , Item.itemsList[97  ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:stonebrick"                   , Item.itemsList[98  ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:brown_mushroom_block"         , Item.itemsList[99  ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:red_mushroom_block"           , Item.itemsList[100 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:iron_bars"                    , Item.itemsList[101 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:glass_pane"                   , Item.itemsList[102 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:melon_block"                  , Item.itemsList[103 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:vine"                         , Item.itemsList[106 ]); // net.minecraft.item.ItemColored
		vanillaItems.put ("minecraft:fence_gate"                   , Item.itemsList[107 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:brick_stairs"                 , Item.itemsList[108 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:stone_brick_stairs"           , Item.itemsList[109 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:mycelium"                     , Item.itemsList[110 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:waterlily"                    , Item.itemsList[111 ]); // net.minecraft.item.ItemLilyPad
		vanillaItems.put ("minecraft:nether_brick"                 , Item.itemsList[112 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:nether_brick_fence"           , Item.itemsList[113 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:nether_brick_stairs"          , Item.itemsList[114 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:enchanting_table"             , Item.itemsList[116 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:end_portal"                   , Item.itemsList[119 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:end_portal_frame"             , Item.itemsList[120 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:end_stone"                    , Item.itemsList[121 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:dragon_egg"                   , Item.itemsList[122 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:redstone_lamp"                , Item.itemsList[123 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:double_wooden_slab"           , Item.itemsList[125 ]); // net.minecraft.item.ItemSlab
		vanillaItems.put ("minecraft:wooden_slab"                  , Item.itemsList[126 ]); // net.minecraft.item.ItemSlab
		vanillaItems.put ("minecraft:cocoa"                        , Item.itemsList[127 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:sandstone_stairs"             , Item.itemsList[128 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:emerald_ore"                  , Item.itemsList[129 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:ender_chest"                  , Item.itemsList[130 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:tripwire_hook"                , Item.itemsList[131 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:emerald_block"                , Item.itemsList[133 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:spruce_stairs"                , Item.itemsList[134 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:birch_stairs"                 , Item.itemsList[135 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:jungle_stairs"                , Item.itemsList[136 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:command_block"                , Item.itemsList[137 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:beacon"                       , Item.itemsList[138 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:cobblestone_wall"             , Item.itemsList[139 ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:carrots"                      , Item.itemsList[141 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:potatoes"                     , Item.itemsList[142 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:wooden_button"                , Item.itemsList[143 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:anvil"                        , Item.itemsList[145 ]); // net.minecraft.item.ItemAnvilBlock
		vanillaItems.put ("minecraft:trapped_chest"                , Item.itemsList[146 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:light_weighted_pressure_plate", Item.itemsList[147 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:heavy_weighted_pressure_plate", Item.itemsList[148 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:daylight_detector"            , Item.itemsList[151 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:redstone_block"               , Item.itemsList[152 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:quartz_ore"                   , Item.itemsList[153 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:hopper"                       , Item.itemsList[154 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:quartz_block"                 , Item.itemsList[155 ]); // net.minecraft.item.ItemMultiTexture
		vanillaItems.put ("minecraft:quartz_stairs"                , Item.itemsList[156 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:activator_rail"               , Item.itemsList[157 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:dropper"                      , Item.itemsList[158 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:stained_hardened_clay"        , Item.itemsList[159 ]); // net.minecraft.item.ItemCloth
		vanillaItems.put ("minecraft:hay_block"                    , Item.itemsList[170 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:carpet"                       , Item.itemsList[171 ]); // net.minecraft.item.ItemCloth
		vanillaItems.put ("minecraft:hardened_clay"                , Item.itemsList[172 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:coal_block"                   , Item.itemsList[173 ]); // net.minecraft.item.ItemBlock
		vanillaItems.put ("minecraft:iron_shovel"                  , Item.itemsList[256 ]); // net.minecraft.item.ItemSpade
		vanillaItems.put ("minecraft:iron_pickaxe"                 , Item.itemsList[257 ]); // net.minecraft.item.ItemPickaxe
		vanillaItems.put ("minecraft:iron_axe"                     , Item.itemsList[258 ]); // net.minecraft.item.ItemAxe
		vanillaItems.put ("minecraft:flint_and_steel"              , Item.itemsList[259 ]); // net.minecraft.item.ItemFlintAndSteel
		vanillaItems.put ("minecraft:apple"                        , Item.itemsList[260 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:bow"                          , Item.itemsList[261 ]); // net.minecraft.item.ItemBow
		vanillaItems.put ("minecraft:arrow"                        , Item.itemsList[262 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:coal"                         , Item.itemsList[263 ]); // net.minecraft.item.ItemCoal
		vanillaItems.put ("minecraft:diamond"                      , Item.itemsList[264 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:iron_ingot"                   , Item.itemsList[265 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:gold_ingot"                   , Item.itemsList[266 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:iron_sword"                   , Item.itemsList[267 ]); // net.minecraft.item.ItemSword
		vanillaItems.put ("minecraft:wooden_sword"                 , Item.itemsList[268 ]); // net.minecraft.item.ItemSword
		vanillaItems.put ("minecraft:wooden_shovel"                , Item.itemsList[269 ]); // net.minecraft.item.ItemSpade
		vanillaItems.put ("minecraft:wooden_pickaxe"               , Item.itemsList[270 ]); // net.minecraft.item.ItemPickaxe
		vanillaItems.put ("minecraft:wooden_axe"                   , Item.itemsList[271 ]); // net.minecraft.item.ItemAxe
		vanillaItems.put ("minecraft:stone_sword"                  , Item.itemsList[272 ]); // net.minecraft.item.ItemSword
		vanillaItems.put ("minecraft:stone_shovel"                 , Item.itemsList[273 ]); // net.minecraft.item.ItemSpade
		vanillaItems.put ("minecraft:stone_pickaxe"                , Item.itemsList[274 ]); // net.minecraft.item.ItemPickaxe
		vanillaItems.put ("minecraft:stone_axe"                    , Item.itemsList[275 ]); // net.minecraft.item.ItemAxe
		vanillaItems.put ("minecraft:diamond_sword"                , Item.itemsList[276 ]); // net.minecraft.item.ItemSword
		vanillaItems.put ("minecraft:diamond_shovel"               , Item.itemsList[277 ]); // net.minecraft.item.ItemSpade
		vanillaItems.put ("minecraft:diamond_pickaxe"              , Item.itemsList[278 ]); // net.minecraft.item.ItemPickaxe
		vanillaItems.put ("minecraft:diamond_axe"                  , Item.itemsList[279 ]); // net.minecraft.item.ItemAxe
		vanillaItems.put ("minecraft:stick"                        , Item.itemsList[280 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:bowl"                         , Item.itemsList[281 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:mushroom_stew"                , Item.itemsList[282 ]); // net.minecraft.item.ItemSoup
		vanillaItems.put ("minecraft:golden_sword"                 , Item.itemsList[283 ]); // net.minecraft.item.ItemSword
		vanillaItems.put ("minecraft:golden_shovel"                , Item.itemsList[284 ]); // net.minecraft.item.ItemSpade
		vanillaItems.put ("minecraft:golden_pickaxe"               , Item.itemsList[285 ]); // net.minecraft.item.ItemPickaxe
		vanillaItems.put ("minecraft:golden_axe"                   , Item.itemsList[286 ]); // net.minecraft.item.ItemAxe
		vanillaItems.put ("minecraft:string"                       , Item.itemsList[287 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:feather"                      , Item.itemsList[288 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:gunpowder"                    , Item.itemsList[289 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:wooden_hoe"                   , Item.itemsList[290 ]); // net.minecraft.item.ItemHoe
		vanillaItems.put ("minecraft:stone_hoe"                    , Item.itemsList[291 ]); // net.minecraft.item.ItemHoe
		vanillaItems.put ("minecraft:iron_hoe"                     , Item.itemsList[292 ]); // net.minecraft.item.ItemHoe
		vanillaItems.put ("minecraft:diamond_hoe"                  , Item.itemsList[293 ]); // net.minecraft.item.ItemHoe
		vanillaItems.put ("minecraft:golden_hoe"                   , Item.itemsList[294 ]); // net.minecraft.item.ItemHoe
		vanillaItems.put ("minecraft:wheat_seeds"                  , Item.itemsList[295 ]); // net.minecraft.item.ItemSeeds
		vanillaItems.put ("minecraft:wheat"                        , Item.itemsList[296 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:bread"                        , Item.itemsList[297 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:leather_helmet"               , Item.itemsList[298 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:leather_chestplate"           , Item.itemsList[299 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:leather_leggings"             , Item.itemsList[300 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:leather_boots"                , Item.itemsList[301 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:chainmail_helmet"             , Item.itemsList[302 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:chainmail_chestplate"         , Item.itemsList[303 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:chainmail_leggings"           , Item.itemsList[304 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:chainmail_boots"              , Item.itemsList[305 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:iron_helmet"                  , Item.itemsList[306 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:iron_chestplate"              , Item.itemsList[307 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:iron_leggings"                , Item.itemsList[308 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:iron_boots"                   , Item.itemsList[309 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:diamond_helmet"               , Item.itemsList[310 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:diamond_chestplate"           , Item.itemsList[311 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:diamond_leggings"             , Item.itemsList[312 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:diamond_boots"                , Item.itemsList[313 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:golden_helmet"                , Item.itemsList[314 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:golden_chestplate"            , Item.itemsList[315 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:golden_leggings"              , Item.itemsList[316 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:golden_boots"                 , Item.itemsList[317 ]); // net.minecraft.item.ItemArmor
		vanillaItems.put ("minecraft:flint"                        , Item.itemsList[318 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:porkchop"                     , Item.itemsList[319 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:cooked_porkchop"              , Item.itemsList[320 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:painting"                     , Item.itemsList[321 ]); // net.minecraft.item.ItemHangingEntity
		vanillaItems.put ("minecraft:golden_apple"                 , Item.itemsList[322 ]); // net.minecraft.item.ItemAppleGold
		vanillaItems.put ("minecraft:sign"                         , Item.itemsList[323 ]); // net.minecraft.item.ItemSign
		vanillaItems.put ("minecraft:wooden_door"                  , Item.itemsList[324 ]); // net.minecraft.item.ItemDoor
		vanillaItems.put ("minecraft:bucket"                       , Item.itemsList[325 ]); // net.minecraft.item.ItemBucket
		vanillaItems.put ("minecraft:water_bucket"                 , Item.itemsList[326 ]); // net.minecraft.item.ItemBucket
		vanillaItems.put ("minecraft:lava_bucket"                  , Item.itemsList[327 ]); // net.minecraft.item.ItemBucket
		vanillaItems.put ("minecraft:minecart"                     , Item.itemsList[328 ]); // net.minecraft.item.ItemMinecart
		vanillaItems.put ("minecraft:saddle"                       , Item.itemsList[329 ]); // net.minecraft.item.ItemSaddle
		vanillaItems.put ("minecraft:iron_door"                    , Item.itemsList[330 ]); // net.minecraft.item.ItemDoor
		vanillaItems.put ("minecraft:redstone"                     , Item.itemsList[331 ]); // net.minecraft.item.ItemRedstone
		vanillaItems.put ("minecraft:snowball"                     , Item.itemsList[332 ]); // net.minecraft.item.ItemSnowball
		vanillaItems.put ("minecraft:boat"                         , Item.itemsList[333 ]); // net.minecraft.item.ItemBoat
		vanillaItems.put ("minecraft:leather"                      , Item.itemsList[334 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:milk_bucket"                  , Item.itemsList[335 ]); // net.minecraft.item.ItemBucketMilk
		vanillaItems.put ("minecraft:brick"                        , Item.itemsList[336 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:clay_ball"                    , Item.itemsList[337 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:reeds"                        , Item.itemsList[338 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:paper"                        , Item.itemsList[339 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:book"                         , Item.itemsList[340 ]); // net.minecraft.item.ItemBook
		vanillaItems.put ("minecraft:slime_ball"                   , Item.itemsList[341 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:chest_minecart"               , Item.itemsList[342 ]); // net.minecraft.item.ItemMinecart
		vanillaItems.put ("minecraft:furnace_minecart"             , Item.itemsList[343 ]); // net.minecraft.item.ItemMinecart
		vanillaItems.put ("minecraft:egg"                          , Item.itemsList[344 ]); // net.minecraft.item.ItemEgg
		vanillaItems.put ("minecraft:compass"                      , Item.itemsList[345 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:fishing_rod"                  , Item.itemsList[346 ]); // net.minecraft.item.ItemFishingRod
		vanillaItems.put ("minecraft:clock"                        , Item.itemsList[347 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:glowstone_dust"               , Item.itemsList[348 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:fish"                         , Item.itemsList[349 ]); // net.minecraft.item.ItemFishFood
		vanillaItems.put ("minecraft:cooked_fished"                , Item.itemsList[350 ]); // net.minecraft.item.ItemFishFood
		vanillaItems.put ("minecraft:dye"                          , Item.itemsList[351 ]); // net.minecraft.item.ItemDye
		vanillaItems.put ("minecraft:bone"                         , Item.itemsList[352 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:sugar"                        , Item.itemsList[353 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:cake"                         , Item.itemsList[354 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:bed"                          , Item.itemsList[355 ]); // net.minecraft.item.ItemBed
		vanillaItems.put ("minecraft:repeater"                     , Item.itemsList[356 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:cookie"                       , Item.itemsList[357 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:filled_map"                   , Item.itemsList[358 ]); // net.minecraft.item.ItemMap
		vanillaItems.put ("minecraft:shears"                       , Item.itemsList[359 ]); // net.minecraft.item.ItemShears
		vanillaItems.put ("minecraft:melon"                        , Item.itemsList[360 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:pumpkin_seeds"                , Item.itemsList[361 ]); // net.minecraft.item.ItemSeeds
		vanillaItems.put ("minecraft:melon_seeds"                  , Item.itemsList[362 ]); // net.minecraft.item.ItemSeeds
		vanillaItems.put ("minecraft:beef"                         , Item.itemsList[363 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:cooked_beef"                  , Item.itemsList[364 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:chicken"                      , Item.itemsList[365 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:cooked_chicken"               , Item.itemsList[366 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:rotten_flesh"                 , Item.itemsList[367 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:ender_pearl"                  , Item.itemsList[368 ]); // net.minecraft.item.ItemEnderPearl
		vanillaItems.put ("minecraft:blaze_rod"                    , Item.itemsList[369 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:ghast_tear"                   , Item.itemsList[370 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:gold_nugget"                  , Item.itemsList[371 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:nether_wart"                  , Item.itemsList[372 ]); // net.minecraft.item.ItemSeeds
		vanillaItems.put ("minecraft:potion"                       , Item.itemsList[373 ]); // net.minecraft.item.ItemPotion
		vanillaItems.put ("minecraft:glass_bottle"                 , Item.itemsList[374 ]); // net.minecraft.item.ItemGlassBottle
		vanillaItems.put ("minecraft:spider_eye"                   , Item.itemsList[375 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:fermented_spider_eye"         , Item.itemsList[376 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:blaze_powder"                 , Item.itemsList[377 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:magma_cream"                  , Item.itemsList[378 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:brewing_stand"                , Item.itemsList[379 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:cauldron"                     , Item.itemsList[380 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:ender_eye"                    , Item.itemsList[381 ]); // net.minecraft.item.ItemEnderEye
		vanillaItems.put ("minecraft:speckled_melon"               , Item.itemsList[382 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:spawn_egg"                    , Item.itemsList[383 ]); // net.minecraft.item.ItemMonsterPlacer
		vanillaItems.put ("minecraft:experience_bottle"            , Item.itemsList[384 ]); // net.minecraft.item.ItemExpBottle
		vanillaItems.put ("minecraft:fire_charge"                  , Item.itemsList[385 ]); // net.minecraft.item.ItemFireball
		vanillaItems.put ("minecraft:writable_book"                , Item.itemsList[386 ]); // net.minecraft.item.ItemWritableBook
		vanillaItems.put ("minecraft:written_book"                 , Item.itemsList[387 ]); // net.minecraft.item.ItemEditableBook
		vanillaItems.put ("minecraft:emerald"                      , Item.itemsList[388 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:item_frame"                   , Item.itemsList[389 ]); // net.minecraft.item.ItemHangingEntity
		vanillaItems.put ("minecraft:flower_pot"                   , Item.itemsList[390 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:carrot"                       , Item.itemsList[391 ]); // net.minecraft.item.ItemSeedFood
		vanillaItems.put ("minecraft:potato"                       , Item.itemsList[392 ]); // net.minecraft.item.ItemSeedFood
		vanillaItems.put ("minecraft:baked_potato"                 , Item.itemsList[393 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:poisonous_potato"             , Item.itemsList[394 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:map"                          , Item.itemsList[395 ]); // net.minecraft.item.ItemEmptyMap
		vanillaItems.put ("minecraft:golden_carrot"                , Item.itemsList[396 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:skull"                        , Item.itemsList[397 ]); // net.minecraft.item.ItemSkull
		vanillaItems.put ("minecraft:carrot_on_a_stick"            , Item.itemsList[398 ]); // net.minecraft.item.ItemCarrotOnAStick
		vanillaItems.put ("minecraft:nether_star"                  , Item.itemsList[399 ]); // net.minecraft.item.ItemSimpleFoiled
		vanillaItems.put ("minecraft:pumpkin_pie"                  , Item.itemsList[400 ]); // net.minecraft.item.ItemFood
		vanillaItems.put ("minecraft:fireworks"                    , Item.itemsList[401 ]); // net.minecraft.item.ItemFirework
		vanillaItems.put ("minecraft:firework_charge"              , Item.itemsList[402 ]); // net.minecraft.item.ItemFireworkCharge
		vanillaItems.put ("minecraft:enchanted_book"               , Item.itemsList[403 ]); // net.minecraft.item.ItemEnchantedBook
		vanillaItems.put ("minecraft:comparator"                   , Item.itemsList[404 ]); // net.minecraft.item.ItemReed
		vanillaItems.put ("minecraft:netherbrick"                  , Item.itemsList[405 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:quartz"                       , Item.itemsList[406 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:tnt_minecart"                 , Item.itemsList[407 ]); // net.minecraft.item.ItemMinecart
		vanillaItems.put ("minecraft:hopper_minecart"              , Item.itemsList[408 ]); // net.minecraft.item.ItemMinecart
		vanillaItems.put ("minecraft:iron_horse_armor"             , Item.itemsList[417 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:golden_horse_armor"           , Item.itemsList[418 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:diamond_horse_armor"          , Item.itemsList[419 ]); // net.minecraft.item.Item
		vanillaItems.put ("minecraft:lead"                         , Item.itemsList[420 ]); // net.minecraft.item.ItemLead
		vanillaItems.put ("minecraft:name_tag"                     , Item.itemsList[421 ]); // net.minecraft.item.ItemNameTag
		vanillaItems.put ("minecraft:record_13"                    , Item.itemsList[2256]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_cat"                   , Item.itemsList[2257]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_blocks"                , Item.itemsList[2258]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_chirp"                 , Item.itemsList[2259]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_far"                   , Item.itemsList[2260]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_mall"                  , Item.itemsList[2261]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_mellohi"               , Item.itemsList[2262]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_stal"                  , Item.itemsList[2263]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_strad"                 , Item.itemsList[2264]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_ward"                  , Item.itemsList[2265]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_11"                    , Item.itemsList[2266]); // net.minecraft.item.ItemRecord
		vanillaItems.put ("minecraft:record_wait"                  , Item.itemsList[2267]); // net.minecraft.item.ItemRecord
		
	}

	
	public Block getBlock (String registerName) {
		if (this.vanillaBlocks.containsKey(registerName)) {
			return this.vanillaBlocks.get(registerName);
		}
		
		try {

			String modId = registerName.substring(0, registerName.indexOf(":"));
			String name  = registerName.substring(registerName.indexOf(":")+1);
			ItemStack s  = GameRegistry.findItemStack(modId, name, 1);
			return Block.blocksList[s.itemID];
			
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Block not found : "+registerName);
		
		return null;
	}
	
	public Item getItem (String registerName) {
		if (this.vanillaItems.containsKey(registerName)) {
			return this.vanillaItems.get(registerName);
		}

		try {

			String modId = registerName.substring(0, registerName.indexOf(":"));
			String name  = registerName.substring(registerName.indexOf(":")+1);
			ItemStack s  = GameRegistry.findItemStack(modId, name, 1);
			return s.getItem();
			
		} catch (Exception e) {
		}
		
		ModGollumCoreLib.log.warning("Item not found : "+registerName);
		
		return null;
	}
	
	public String getRegisterName (Block block) {
		if (this.vanillaBlocks.contains(block)) {
			
			for (Entry<String, Block> entry: this.vanillaBlocks.entrySet()) {
				if (entry.getValue() == block) {
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
	public String getRegisterName (Item item) {
		if (this.vanillaItems.contains(item)) {
			
			for (Entry<String, Item> entry: this.vanillaItems.entrySet()) {
				if (entry.getValue() == item) {
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
}
