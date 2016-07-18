package codemining.ast.distilledchanges;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTNode;

import codemining.ast.java.JavaAstTreeExtractor;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.EntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeEntity;
import codemining.ast.AbstractTreeExtractor;
import codemining.ast.AstNodeSymbol;
import codemining.ast.TreeNode;
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

	public TreeNode<Integer> getTree(SourceCodeChange change) {
		ChangeType ct = change.getChangeType();
		final AstNodeSymbol symbol = new AstNodeSymbol(
				ct.ordinal()); // this won't work, need to multiply or something.  Or make a different kind of symbol?
		SourceCodeEntity entity = change.getChangedEntity();
		EntityType et = entity.getType();
		ASTNode node = entity.getOriginalNode();
		if(node == null) {
		} else {
		TreeNode<Integer> astNode = helperExtractor.getTree(node);
		}
		// TODO Auto-generated method stub
		return null;
	}

}
