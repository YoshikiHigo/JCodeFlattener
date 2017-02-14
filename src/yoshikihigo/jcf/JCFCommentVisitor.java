package yoshikihigo.jcf;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;

public class JCFCommentVisitor extends ASTVisitor {

	@Override
	public boolean visit(final BlockComment node) {
		final ASTNode naltroot = node.getAlternateRoot();
		if (naltroot != null) {
			naltroot.delete();
		} else {
			node.delete();
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(final Javadoc node) {
		final ASTNode naltroot = node.getAlternateRoot();
		if (naltroot != null) {
			naltroot.delete();
		} else {
			node.delete();
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(final LineComment node) {
		node.getAlternateRoot().delete();
		return super.visit(node);
	}
}
