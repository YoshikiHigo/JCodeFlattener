package yoshikihigo.jcf;

import java.util.List;

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

	final private int pseudVariableID;
	final private AST ast;
	final private ASTRewrite astRewriter;
	private boolean changed;

	public JCFASTVisitor(final AST ast, final int pseudVariableID,
			ASTRewrite astRewriter) {
		this.ast = ast;
		this.pseudVariableID = pseudVariableID;
		this.astRewriter = astRewriter;
		this.changed = false;
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
	public void endVisit(final ArrayAccess node) {
		this.dissolveExpression(node.getArray());
		this.dissolveExpression(node.getIndex());
		super.endVisit(node);
	}

	@Override
	public void endVisit(final ArrayCreation node) {
		@SuppressWarnings("unchecked")
		final List<Expression> expressions = node.dimensions();
		expressions.stream().forEach(e -> this.dissolveExpression(e));
		super.endVisit(node);
	}

	@Override
	public void endVisit(final ArrayInitializer node) {
		@SuppressWarnings("unchecked")
		final List<Expression> expressions = node.expressions();
		expressions.stream().forEach(e -> this.dissolveExpression(e));
		super.endVisit(node);
	}

	@Override
	public void endVisit(ArrayType node) {
		super.endVisit(node);
	}

	@Override
	public void endVisit(final AssertStatement node) {
		this.dissolveExpression(node.getExpression());
		this.dissolveExpression(node.getMessage());
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
	public void endVisit(final CastExpression node) {
		this.dissolveExpression(node.getExpression());
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
	public void endVisit(final ClassInstanceCreation node) {
		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(e -> this.dissolveExpression(e));
		this.dissolveExpression(node.getExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(CompilationUnit node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(final ConditionalExpression node) {
		this.dissolveExpression(node.getExpression());
		this.dissolveExpression(node.getThenExpression());
		this.dissolveExpression(node.getElseExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(final ConstructorInvocation node) {
		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(e -> this.dissolveExpression(e));
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
	public void endVisit(final DoStatement node) {
		this.dissolveExpression(node.getExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(EmptyStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(EnhancedForStatement node) {
		this.dissolveExpression(node.getExpression());
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
	public void endVisit(final ExpressionMethodReference node) {
		this.dissolveExpression(node.getExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(final ExpressionStatement node) {
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
	public void endVisit(final ForStatement node) {

		// TODO for expression and initializers

		this.dissolveExpression(node.getExpression());

		// TODO for expression and updaters

		super.endVisit(node);
	}

	@Override
	public void endVisit(final IfStatement node) {
		this.dissolveExpression(node.getExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(ImportDeclaration node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(final InfixExpression node) {
		this.dissolveExpression(node.getLeftOperand());
		this.dissolveExpression(node.getRightOperand());
		super.endVisit(node);
	}

	@Override
	public void endVisit(final InstanceofExpression node) {
		this.dissolveExpression(node.getLeftOperand());
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

		final Expression expression = node.getExpression();
		this.dissolveExpression(expression);

		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(e -> this.dissolveExpression(e));

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
	public void endVisit(final ParenthesizedExpression node) {
		this.dissolveExpression(node.getExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(final PostfixExpression node) {
		this.dissolveExpression(node.getOperand());
		super.endVisit(node);
	}

	@Override
	public void endVisit(final PrefixExpression node) {
		this.dissolveExpression(node.getOperand());
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
	public void endVisit(final ReturnStatement node) {
		this.dissolveExpression(node.getExpression());
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

		this.dissolveExpression(node.getExpression());

		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(a -> this.dissolveExpression(a));

		super.endVisit(node);
	}

	@Override
	public void endVisit(SuperFieldAccess node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(SuperMethodInvocation node) {
		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(a -> this.dissolveExpression(a));
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
	public void endVisit(final SwitchStatement node) {
		this.dissolveExpression(node.getExpression());
		super.endVisit(node);
	}

	@Override
	public void endVisit(SynchronizedStatement node) {
		this.dissolveExpression(node.getExpression());
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
	public void endVisit(final ThrowStatement node) {
		this.dissolveExpression(node.getExpression());
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
		super.endVisit(node);
	}

	@Override
	public void endVisit(VariableDeclarationStatement node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	@Override
	public void endVisit(final VariableDeclarationFragment node) {
		super.endVisit(node);
	}

	@Override
	public void endVisit(final WhileStatement node) {
		this.dissolveExpression(node.getExpression());
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
			if (parent instanceof Statement
					&& parent.getParent() instanceof Block) {
				break;
			}
		}
		return (Statement) parent;
	}

	private Expression dissolveExpression(final Expression expression) {

		if (this.changed) {
			return null;
		}

		if (null == expression) {
			return null;
		}

		if ((expression instanceof SimpleName)
				|| (expression instanceof NullLiteral)
				|| (expression instanceof NumberLiteral)
				|| (expression instanceof StringLiteral)
				|| (expression instanceof BooleanLiteral)
				|| (expression instanceof CharacterLiteral)
				|| (expression instanceof ThisExpression)
				|| (expression instanceof VariableDeclarationExpression)) {
			return null;
		}

		if (expression instanceof PrefixExpression) {
			final PrefixExpression prefixExpression = (PrefixExpression) expression;
			if (prefixExpression.getOperand() instanceof NumberLiteral) {
				return null;
			}
		}

		final Block parentBlock = getParentBlock(expression);
		final Statement parentStatement = getParentStatement(expression);

		if (null == parentBlock) {
			return null;
		}

		// generate a new artificial variable name
		final String newIdentifier = "_GENARATED_$" + this.pseudVariableID;

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
		final ListRewrite variableDeclarationInserter = this.astRewriter
				.getListRewrite(parentBlock, Block.STATEMENTS_PROPERTY);
		variableDeclarationInserter.insertBefore(newStatement, parentStatement,
				null);

		// processing for while- and for-statement
		this.doForLoopBlock(parentStatement, expression, newIdentifier);

		// replace the existing expression with a new variable reference
		final Expression newSimpleName = this.ast.newSimpleName(newIdentifier);
		this.astRewriter.replace(expression, newSimpleName, null);
		this.changed = true;

		return newSimpleName;
	}

	private void doForLoopBlock(final Statement loopStatement,
			final Expression expression, final String identifier) {

		Statement innerStatement = null;
		if (loopStatement instanceof WhileStatement) {
			innerStatement = ((WhileStatement) loopStatement).getBody();
		} else if (loopStatement instanceof ForStatement) {
			innerStatement = ((ForStatement) loopStatement).getBody();
		} else if (loopStatement instanceof LabeledStatement) {
			this.doForLoopBlock(((LabeledStatement) loopStatement).getBody(),
					expression, identifier);
			return;
		} else {
			return;
		}

		final Assignment a = this.ast.newAssignment();
		a.setLeftHandSide(this.ast.newSimpleName(identifier));
		final Expression rightExpression = (Expression) ASTNode.copySubtree(
				this.ast, expression);
		a.setRightHandSide(rightExpression);
		final ExpressionStatement s = this.ast.newExpressionStatement(a);

		if (innerStatement instanceof Block) {
			final ListRewrite inserter = this.astRewriter.getListRewrite(
					(Block) innerStatement, Block.STATEMENTS_PROPERTY);
			inserter.insertLast(s, null);
		}

		final Block block = this.ast.newBlock();
		final ListRewrite inserter = this.astRewriter.getListRewrite(block,
				Block.STATEMENTS_PROPERTY);
		inserter.insertFirst(innerStatement, null);
		inserter.insertLast(s, null);
	}

	public boolean isChanged() {
		return this.changed;
	}
}
