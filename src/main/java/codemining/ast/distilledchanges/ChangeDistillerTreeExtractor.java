package codemining.ast.distilledchanges;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTNode;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
import codemining.ast.AbstractTreeExtractor;
import codemining.ast.AstNodeSymbol;
import codemining.ast.TreeNode;
import codemining.ast.java.JavaAstTreeExtractor;
import codemining.languagetools.ITokenizer;
import codemining.languagetools.ParseType;

public class ChangeDistillerTreeExtractor extends AbstractTreeExtractor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3687067919595744967L;
	private JavaAstTreeExtractor helperExtractor = new JavaAstTreeExtractor();
	
	@Override
	public String getCodeFromTree(TreeNode<Integer> tree) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode<Integer> getKeyForCompilationUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITokenizer getTokenizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode<Integer> getTree(File f) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode<Integer> getTree(String code, ParseType parseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeToString getTreePrinter() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Add further annotations to the given symbol. Useful for classes that will
	 * subclass this one.
	 *
	 * @param symbol
	 * @param node
	 */
	public void annotateSymbol(final AstNodeSymbol symbol, final ASTNode node) {
		// Do nothing
	}

	

	public TreeNode<Integer> getTree(SourceCodeChange change) {
		/* 
		 * These are the fields of a sourcecodeentity.
		 * CLG FIXME: I presently believe that I don't care about them
		 * except for the original node
		 * (and that associated entities are just comments,
		 * which are broken right now anyway)
    private String fUniqueName;
    private EntityType fType;
    private int fModifiers;
    private List<SourceCodeEntity> fAssociatedEntities;
    private SourceRange fRange;
    private ASTNode originalNode;
*/ 
		ChangeType ct = change.getChangeType();
		final AstNodeSymbol symbol = new AstNodeSymbol(ct); 
		SourceCodeEntity entity = change.getChangedEntity();
		// FIXME: entities are harder.  Do I care about root or parent
		// entities??
//		ASTNode node = entity.getOriginalNode();
//		symbol.addChildProperty("changedEntity");
//		annotateSymbol(symbol, node);
		final int symbolId = getOrAddSymbolId(symbol);
		final TreeNode<Integer> treeNode = TreeNode.create(symbolId, 1);
//		treeNode.addChildNode(helperExtractor.getTree(node),  0);
		return treeNode;
	}

}
