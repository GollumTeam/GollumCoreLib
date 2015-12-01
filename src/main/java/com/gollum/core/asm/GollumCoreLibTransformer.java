package com.gollum.core.asm;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.renderer.GCLRenderItem;
import com.gollum.core.common.log.Logger;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author Guilherme Chaguri
 */
public class GollumCoreLibTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String name2, byte[] bytes) {
		if(bytes == null) return null;
		
		try {
			if(name.equals("net.minecraft.client.Minecraft")) {
				return patchMinecraftClass(bytes);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_MESSAGE, "Error on override vanilla class.");
			 FMLCommonHandler.instance().exitJava(1, false);
		}
		
		return bytes;
	}
	
	public byte[] patchMinecraftClass(byte[] bytes) {
		
		Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_MESSAGE, "Applying ASM to Minecraft methods to net.minecraft.client.Minecaft ...");
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext()) {
			MethodNode method = methods.next();
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Search: this.renderItem = new RenderItem(this.renderEngine, this.modelManager); in methde startGame //
			/////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			// TODO Find Refelection Class name
			if((method.name.equals("startGame")) && (method.desc.equals("()V"))) {
				for(AbstractInsnNode node : method.instructions.toArray()) {
					
					// Detect le "new RenderItem(this.renderEngine, this.modelManager)"
					if( 
						(node instanceof MethodInsnNode) && 
						node.getOpcode() == Opcodes.INVOKESPECIAL &&
						((MethodInsnNode)node).name.equals("<init>") &&
						((MethodInsnNode)node).owner.equals(RenderItem.class.getCanonicalName().replace('.', '/'))
					) { 
//						do {
//							node  = node.getNext();
//						} while (!(node instanceof LabelNode) || node.getOpcode() != Opcodes.F_NEW); // rechere du futur retour à la ligne
						
						///////////////////////
						// Start inject code //
						///////////////////////
//						method.instructions.insertBefore(node, new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/gollum/core/client/renderer/GCLRenderItem", "<init>", "()V", false));
//						method.instructions.remove(node);
//						((MethodInsnNode)node).owner = ;
//						method.instructions.insertBefore(node, new MethodInsnNode(Opcodes.INVOKESTATIC, GollumCoreLibTransformer.class.getCanonicalName().replace('.', '/'), "test", "()V", false));
						
						///////////////////////
						// End inject code //
						///////////////////////
							
							
						
					}
				}
				
			}
			
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		
		return writer.toByteArray();
	}
	
	public static void test() {
		Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_MESSAGE, "Cool.");
	}
	
}
