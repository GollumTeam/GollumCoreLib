package mods.gollum.core.blocks;

import mods.gollum.core.helper.blocks.HBlockContainer;
import mods.gollum.core.tileentities.TileEntityBlockSpawner;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockSpawner extends HBlockContainer {

	public BlockSpawner(int id, String registerName) {
		super(id, registerName, Material.air);
		
		this.helper.vanillaTexture = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBlockSpawner();
	}
	
	@Override
	public boolean isAirBlock(World world, int x, int y, int z) {
		return true;
	}
	
	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	@Override
	public int idPicked(World var1, int var2, int var3, int var4) {
		return 0;
	}
	
	/**
	 * Enleve les collisions
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		return null;
	}
	
	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return -1;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
}
