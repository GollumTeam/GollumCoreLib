package com.gollum.core.asm;

import java.lang.reflect.Field;
import java.security.acl.Owner;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.log.Logger;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderEndermite;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.launchwrapper.IClassTransformer;

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
				InsnList list = new InsnList();
				for(AbstractInsnNode node : method.instructions.toArray()) {
					
					// Detect le "new RenderItem(this.renderEngine, this.modelManager)"
					if( 
						(node instanceof MethodInsnNode) && 
						node.getOpcode() == Opcodes.INVOKESPECIAL &&
						((MethodInsnNode)node).name.equals("<init>") &&
						((MethodInsnNode)node).owner.equals(RenderItem.class.getCanonicalName().replace('.', '/'))
					) { 
						do {
							list.add(node);
							node  = node.getNext();
						} while (!(node instanceof LabelNode) || node.getOpcode() != Opcodes.F_NEW); // rechere du futur retour à la ligne
						
						///////////////////////
						// Start inject code //
						///////////////////////
						
						///////////////////////
						// End inject code //
						///////////////////////
							
							
						
					}
					// Ajout du retour à la ligne
					list.add(node);

				}
				
				method.instructions.clear();
				method.instructions.add(list);
			}
			
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	private LocalVariableNode getLocalVariableNode(VarInsnNode varInsnNode, MethodNode methodNode) {
		int varIdx = varInsnNode.var;
		int instrIdx = getInstrIndex(varInsnNode);
		List<?> localVariables = methodNode.localVariables;
		for (int idx = 0; idx < localVariables.size(); idx++) {
			LocalVariableNode localVariableNode = (LocalVariableNode) localVariables.get(idx);
			if (localVariableNode.index == varIdx) {
				int scopeEndInstrIndex = getInstrIndex(localVariableNode.end);
				if (scopeEndInstrIndex >= instrIdx) {
					// still valid for current line
					return localVariableNode;
				}
			}
		}
		throw new RuntimeException("Variable with index " + varIdx + " and scope end >= " + instrIdx + " not found for method " + methodNode.name + "!");
	}
	
	private int getInstrIndex(AbstractInsnNode insnNode) {
		try {
			Field indexField = AbstractInsnNode.class.getDeclaredField("index");
			indexField.setAccessible(true);
			Object indexValue = indexField.get(insnNode);
			return ((Integer) indexValue).intValue();
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}
}
