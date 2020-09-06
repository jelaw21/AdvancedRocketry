package zmaster587.advancedRocketry.client.render.multiblocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import zmaster587.advancedRocketry.backwardCompat.ModelFormatException;
import zmaster587.advancedRocketry.backwardCompat.WavefrontObject;
import zmaster587.advancedRocketry.tile.multiblock.machine.TilePrecisionAssembler;
import zmaster587.libVulpes.block.RotatableBlock;
import zmaster587.libVulpes.render.RenderHelper;
import zmaster587.libVulpes.tile.multiblock.TileMultiblockMachine;

import java.util.List;

public class RendererPrecisionAssembler extends TileEntityRenderer<TilePrecisionAssembler> {
	WavefrontObject model;

	ResourceLocation texture = new ResourceLocation("advancedrocketry","textures/models/precassembler.png");

	//private final RenderItem dummyItem = Minecraft.getInstance().getRenderItem();
	
	//Model Names:
	// Tray
	// Hull
	// ProcessA
	// ProcessB
	// ProcessC
	
	public RendererPrecisionAssembler(TileEntityRendererDispatcher tile) {
		super(tile);
		try {
			model = new WavefrontObject(new ResourceLocation("advancedrocketry","models/precassembler.obj"));
		} catch (ModelFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(TilePrecisionAssembler tile, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {

		if(!tile.canRender())
			return;
		
		matrix.push();

		//Rotate and move the model into position
		matrix.translate(.5f, 0, .5f);
		Direction front = RotatableBlock.getFront(tile.getWorld().getBlockState(tile.getPos())); //tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord));
		matrix.rotate(new Quaternion(0, (front.getXOffset() == 1 ? 180 : 0) + front.getZOffset()*90f, 0, true));
		matrix.translate(-.5f, 0, -.5f);
		
		IVertexBuilder entitySolidBuilder = buffer.getBuffer(RenderHelper.getSolidEntityModelRenderType(texture));
		
		if(tile.isRunning()) {

			float progress = tile.getProgress(0)/(float)tile.getTotalProgress(0);
			float process,tray;
			tray = process = 3*progress;



			/*List<ItemStack> outputList = tile.getOutputs();
			if(outputList != null && !outputList.isEmpty()) {
				ItemStack stack = outputList.get(0);
				ItemEntity entity = new ItemEntity(tile.getWorld());
				
				entity.setItem(stack);
				entity.hoverStart = 0;
				
				matrix.push();
				GL11.glRotatef(90, 1, 0, 0);
				matrix.translate(1, tray + .75, -1.2f);
				RenderHelper.renderItem(tile, entity,  Minecraft.getInstance().getRenderItem());
				matrix.pop();
			}*/
			
			model.tessellatePart(entitySolidBuilder, "Hull");
			
			matrix.translate(0, 0, tray);
			model.tessellatePart(entitySolidBuilder, "Tray"); // 0-> 3
			matrix.translate(0, 0, -tray);
			
			process *= 6;
			if(process > 2 && process < 4){
				if(process < 3) {
					process-=2;
					matrix.translate(0, -.25f*process, 0); // 0 -> -.25
					model.tessellatePart(entitySolidBuilder, "ProcessA");
					matrix.translate(0, .25f*process, 0);
				}
				else if(process < 4) {
					process = -process + 4;
					matrix.translate(0, -.25f*process, 0); // 0 -> -.25
					model.tessellatePart(entitySolidBuilder, "ProcessA");
					matrix.translate(0, .25f*process, 0);
				}
			}
			else
				model.tessellatePart(entitySolidBuilder, "ProcessA");
			
			
			
			process -= 6;
			if(process > 2 && process < 4){
				if(process < 3) {
					process-=2;
					matrix.translate(0, -.25f*process, 0); // 0 -> -.25
					model.tessellatePart(entitySolidBuilder, "ProcessB");
					matrix.translate(0, .25f*process, 0);
				}
				else if(process < 4) {
					process = -process + 4;
					matrix.translate(0, -.25f*process, 0); // 0 -> -.25
					model.tessellatePart(entitySolidBuilder, "ProcessB");
					matrix.translate(0, .25f*process, 0);
				}
			}
			else
				model.tessellatePart(entitySolidBuilder, "ProcessB");
			
			process -= 6;
			if(process > 1 && process < 3){
				if(process < 2) {
					process-=1;
					
					matrix.translate(1.55, 1.47, 0);
					matrix.rotate(new Quaternion(0,0, 90*process,true));
					matrix.translate(-1.55, -1.47, 0);
					
					model.tessellatePart(entitySolidBuilder, "ProcessC");
				}
				else if(process < 3) {
					process = -process + 3;
					
					matrix.translate(1.55, 1.47, 0);
					matrix.rotate(new Quaternion(0,0, 90*process,true));
					matrix.translate(-1.55, -1.47, 0);
					
					model.tessellatePart(entitySolidBuilder, "ProcessC");
				}
			}
			else
				model.tessellatePart(entitySolidBuilder, "ProcessC");



			
		}
		else {
			model.tessellateAll(entitySolidBuilder);
		}
		matrix.pop();
	}
}
