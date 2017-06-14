package codemining.ast.js;

import org.eclipse.jdt.core.dom.PackageDeclaration;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureFinalDiffNode;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.java.JavaStructureChangeNode.Type;
import codemining.ast.java.JavaAstTreeExtractor.TreeNodeExtractor;

public class ChangeASTVisitor {

	public static void acceptChange(Type child, TreeNodeExtractor visitor) {
		if(visitor == null){
			throw new IllegalArgumentException();
		}
		//generic pre-visit
		else{
			//accept0(child,visitor);
		}
		//post-visit
		//visitor.postVisit(node); // TODO worry about this later
	}
/*
	private static void accept0(StructureFinalDiffNode node, TreeNodeExtractor visitor) {
		boolean visitChildren = true;
		if(visitChildren){
			acceptChildren(visitor, node.getTypes());
		}
	}
	
	private static void acceptChild(TreeNodeExtractor visitor, StructureFinalDiffNode child) {
		if(child == null){
			return;
		}
		acceptChange(child, visitor);
	}
	
	private static void acceptChildren(TreeNodeExtractor visitor, Type type){
		//Should have cursor.hasNext()
		Type child = type; //should get the next iteration or next node
		acceptChange(child,visitor);
	}

	public static PackageDeclaration getPackage(StructureFinalDiffNode node){
		return node.getPackage();
	}
*/

}
