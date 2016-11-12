package yoshikihigo.jcf;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

public class JCFASTVisitor extends ASTVisitor {

	final private AtomicInteger generatedID;
	final private AST ast;
	final private ASTRewrite astRewriter;

	public JCFASTVisitor(final AST ast, final ASTRewrite astRewriter) {
		this.generatedID = new AtomicInteger(0);
		this.ast = ast;
		this.astRewriter = astRewriter;
	}

	@Override
	public void endVisit(AnnotationTypeDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(AnnotationTypeMemberDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(AnonymousClassDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ArrayAccess node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ArrayCreation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ArrayInitializer node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ArrayType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(AssertStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(Assignment node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(Block node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(BlockComment node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(BooleanLiteral node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(BreakStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(CastExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(CatchClause node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(CharacterLiteral node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ClassInstanceCreation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(CompilationUnit node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ConditionalExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(final ConstructorInvocation node) {
		// @SuppressWarnings("unchecked")
		// final List<Expression> arguments = node.arguments();
		// arguments.stream().forEach(e -> this.dissolveExpression(e));
		super.endVisit(node);
	}

	@Override
	public void endVisit(ContinueStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(CreationReference node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(DoStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(EmptyStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(EnhancedForStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(EnumConstantDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(EnumDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ExpressionMethodReference node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ExpressionStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(Dimension node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(FieldAccess node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(FieldDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ForStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(final IfStatement node) {
		// this.dissolveExpression(node.getExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(ImportDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(final InfixExpression node) {
		// this.dissolveExpression(node.getLeftOperand());
		// this.dissolveExpression(node.getRightOperand());
		super.endVisit(node);
	}

	@Override
	public void endVisit(InstanceofExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(Initializer node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(Javadoc node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(LabeledStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(LambdaExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(LineComment node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(MarkerAnnotation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(MemberRef node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(MemberValuePair node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(MethodRef node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(MethodRefParameter node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(MethodDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(final MethodInvocation node) {
		/*
		 * final Expression expression = node.getExpression(); final Expression
		 * newExpression = this.dissolveExpression(expression); if (null !=
		 * newExpression) { // node.setExpression(newExpression); }
		 */

		// @SuppressWarnings("unchecked")
		// final List<Expression> arguments = node.arguments();
		// arguments.stream().forEach(e -> this.dissolveExpression(e));
		super.endVisit(node);
	}

	@Override
	public void endVisit(Modifier node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(NameQualifiedType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(NormalAnnotation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(NullLiteral node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(NumberLiteral node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(PackageDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ParameterizedType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ParenthesizedExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(PostfixExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(PrefixExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(PrimitiveType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(QualifiedName node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(QualifiedType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ReturnStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SimpleName node) {
		super.endVisit(node);
	}

	@Override
	public void endVisit(SimpleType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SingleMemberAnnotation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SingleVariableDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(StringLiteral node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SuperConstructorInvocation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SuperFieldAccess node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SuperMethodInvocation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SuperMethodReference node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SwitchCase node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SwitchStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SynchronizedStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TagElement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TextElement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ThisExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(ThrowStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TryStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TypeDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TypeDeclarationStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TypeLiteral node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TypeMethodReference node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(TypeParameter node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(UnionType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(IntersectionType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(VariableDeclarationExpression node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(VariableDeclarationStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(VariableDeclarationFragment node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(WhileStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(WildcardType node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	private static Block getParentBlock(final ASTNode node) {
		ASTNode parent = node;
		boolean statementPassed = false;
		while (true) {
			parent = parent.getParent();
			if (null == parent) {
				break;
			}
			if ((parent instanceof Block) && statementPassed) {
				break;
			}
			if (parent instanceof Statement) {
				statementPassed = true;
			}
		}
		return (Block) parent;
	}

	private static Statement getParentStatement(final ASTNode node) {
		ASTNode parent = node;
		while (true) {
			parent = parent.getParent();
			if (null == parent) {
				break;
			}
			if ((parent instanceof Statement)
					&& (parent.getParent() instanceof Block)) {
				break;
			}
		}
		return (Statement) parent;
	}

	private Expression dissolveExpression(final Expression expression) {

		if (null == expression) {
			return null;
		}

		if ((expression instanceof SimpleName)
				|| (expression instanceof NullLiteral)
				|| (expression instanceof NumberLiteral)
				|| (expression instanceof StringLiteral)
				|| (expression instanceof BooleanLiteral)
				|| (expression instanceof CharacterLiteral)) {
			return null;
		}

		final Block parentBlock = getParentBlock(expression);
		final Statement parentStatement = getParentStatement(expression);

		// generate a new artificial variable name
		final String newIdentifier = "$" + this.generatedID.getAndIncrement();

		// make a new variable declaration statement
		final VariableDeclarationFragment fragment = this.ast
				.newVariableDeclarationFragment();
		fragment.setName(this.ast.newSimpleName(newIdentifier));
		final Expression newLeftExpression = (Expression) ASTNode.copySubtree(
				this.ast, expression);
		fragment.setInitializer(newLeftExpression);
		final VariableDeclarationStatement newStatement = this.ast
				.newVariableDeclarationStatement(fragment);
		newStatement.setType(this.ast.newSimpleType(this.ast
				.newSimpleName("Object")));
		final ListRewrite listRewriter = this.astRewriter.getListRewrite(
				parentBlock, Block.STATEMENTS_PROPERTY);
		listRewriter.insertBefore(newStatement, parentStatement, null);

		// dissolve the existing complex expression
		final Expression newSimpleName = this.ast.newSimpleName(newIdentifier);
		this.astRewriter.replace(expression, newSimpleName, null);

		return newSimpleName;
	}

	@Override
	public boolean visit(SimpleName node) {
		System.out.println(node.getIdentifier());
		return super.visit(node);
	}

}
