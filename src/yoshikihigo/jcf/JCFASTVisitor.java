package yoshikihigo.jcf;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

public class JCFASTVisitor extends ASTVisitor {

	final private AtomicInteger pseudVariableID;
	final private AST ast;
	final private ASTRewrite astRewriter;
	final private boolean aggresive;
	private boolean nameResolving;
	private boolean changed;
	private CONTEXT context;

	public JCFASTVisitor(final AST ast, final AtomicInteger pseudVariableID, final ASTRewrite astRewriter,
			final boolean aggresive, final boolean nameResolving) {
		this.ast = ast;
		this.pseudVariableID = pseudVariableID;
		this.astRewriter = astRewriter;
		this.aggresive = aggresive;
		this.nameResolving = nameResolving;
		this.changed = false;
		this.context = CONTEXT.NORMAL;
	}

	@Override
	public boolean visit(final ArrayAccess node) {

		Optional.ofNullable(node.getIndex()).ifPresent(i -> {
			if (null == this.dissolveExpression(i)) {
				i.accept(this);
			}
		});

		Optional.ofNullable(node.getArray()).ifPresent(a -> {
			if (null == this.dissolveExpression(a)) {
				a.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final ArrayCreation node) {

		@SuppressWarnings("unchecked")
		final List<Expression> expressions = node.dimensions();
		expressions.stream().forEach(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final ArrayInitializer node) {

		@SuppressWarnings("unchecked")
		final List<Expression> expressions = node.expressions();
		expressions.stream().forEach(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}

		});

		return false;
	}

	@Override
	public boolean visit(final AssertStatement node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		Optional.ofNullable(node.getMessage()).ifPresent(m -> {
			if (null == this.dissolveExpression(m)) {
				m.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final CastExpression node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final ClassInstanceCreation node) {

		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		Optional.ofNullable(node.getAnonymousClassDeclaration()).ifPresent(e -> e.accept(this));

		return false;
	}

	@Override
	public boolean visit(final ConditionalExpression node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		Optional.ofNullable(node.getThenExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		Optional.ofNullable(node.getElseExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final ConstructorInvocation node) {

		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final DoStatement node) {

		node.getBody().accept(this);

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		return false;
	}

	@Override
	public boolean visit(final EnhancedForStatement node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		node.getBody().accept(this);

		return false;
	}

	@Override
	public boolean visit(final ExpressionMethodReference node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(ForStatement node) {

		@SuppressWarnings("unchecked")
		final List<Expression> initializers = node.initializers();
		initializers.stream().forEach(e -> {
			// do not call dissolveExpression(e)!
			e.accept(this);
		});

		this.context = CONTEXT.LOOPUPDATER;

		@SuppressWarnings("unchecked")
		final List<Expression> updaters = node.updaters();
		updaters.stream().forEach(e -> {
			// do not call dissolveExpression(e)!
			e.accept(this);
		});

		this.context = CONTEXT.LOOPCONDITION;

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		this.context = CONTEXT.NORMAL;

		node.getBody().accept(this);

		return false;
	}

	@Override
	public boolean visit(final IfStatement node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		Optional.ofNullable(node.getThenStatement()).ifPresent(e -> e.accept(this));
		Optional.ofNullable(node.getElseStatement()).ifPresent(e -> e.accept(this));

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(final InfixExpression node) {

		Optional.ofNullable(node.getLeftOperand()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		Optional.ofNullable(node.getRightOperand()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		Optional.ofNullable(node.extendedOperands()).ifPresent(e -> {
			e.stream().forEach(f -> {
				final Expression g = (Expression) f;
				if (null == this.dissolveExpression(g)) {
					g.accept(this);
				}
			});
		});

		return false;
	}

	@Override
	public boolean visit(final InstanceofExpression node) {

		Optional.ofNullable(node.getLeftOperand()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final MethodInvocation node) {

		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final PostfixExpression node) {

		Optional.ofNullable(node.getOperand()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final PrefixExpression node) {

		Optional.ofNullable(node.getOperand()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final ReturnStatement node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final SuperConstructorInvocation node) {

		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(a -> {
			if (null == this.dissolveExpression(a)) {
				a.accept(this);
			}
		});

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(node.getExpression())) {
				e.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final SuperMethodInvocation node) {

		@SuppressWarnings("unchecked")
		final List<Expression> arguments = node.arguments();
		arguments.stream().forEach(a -> {
			if (null == this.dissolveExpression(a)) {
				a.accept(this);
			}
		});

		return false;
	}

	@Override
	public boolean visit(final SwitchStatement node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		@SuppressWarnings("unchecked")
		final List<Statement> statements = node.statements();
		statements.stream().forEach(s -> s.accept(this));

		return false;
	}

	@Override
	public boolean visit(final SynchronizedStatement node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		node.getBody().accept(this);

		return false;
	}

	@Override
	public boolean visit(final ThrowStatement node) {

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (null == this.dissolveExpression(e)) {
				e.accept(this);
			}
		});

		return false;
	}

	public boolean visit(final WhileStatement node) {

		this.context = CONTEXT.LOOPCONDITION;

		Optional.ofNullable(node.getExpression()).ifPresent(e -> {
			if (this.aggresive && (null != this.dissolveExpression(e))) {
				return;
			}
			e.accept(this);

		});

		this.context = CONTEXT.NORMAL;

		node.getBody().accept(this);

		return false;
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
			if (parent instanceof Statement && parent.getParent() instanceof Block) {
				break;
			}
		}
		return (Statement) parent;
	}

	private Expression dissolveExpression(final Expression expression) {

		if (null == expression) {
			return null;
		}

		if ((expression instanceof SimpleName) || (expression instanceof NullLiteral)
				|| (expression instanceof NumberLiteral) || (expression instanceof StringLiteral)
				|| (expression instanceof BooleanLiteral) || (expression instanceof CharacterLiteral)
				|| (expression instanceof ThisExpression) || (expression instanceof VariableDeclarationExpression)) {
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

		Expression targetExpression = expression;
		if (expression instanceof ParenthesizedExpression) {
			targetExpression = ((ParenthesizedExpression) expression).getExpression();
			if (null == targetExpression) {
				targetExpression = expression;
			}
		}

		// generate a new artificial variable name
		final String newIdentifier = "$" + this.pseudVariableID.getAndIncrement();

		// make a new variable declaration statement
		if ((CONTEXT.NORMAL == this.context) || (CONTEXT.LOOPCONDITION == this.context)) {

			final ITypeBinding binding = targetExpression.resolveTypeBinding();
			final Type resolvedType = this.resolveBinding(binding);

			final VariableDeclarationFragment fragment = this.ast.newVariableDeclarationFragment();
			fragment.setName(this.ast.newSimpleName(newIdentifier));
			final Expression newRightExpression = (Expression) ASTNode.copySubtree(this.ast, targetExpression);
			fragment.setInitializer(newRightExpression);
			final VariableDeclarationStatement newStatement = this.ast.newVariableDeclarationStatement(fragment);
			newStatement.setType(resolvedType);
			final ListRewrite variableDeclarationInserter = this.astRewriter.getListRewrite(parentBlock,
					Block.STATEMENTS_PROPERTY);
			variableDeclarationInserter.insertBefore(newStatement, parentStatement, null);
		}

		// processing for while- and for-statement
		if ((CONTEXT.LOOPCONDITION == this.context) || (CONTEXT.LOOPUPDATER == this.context)) {
			this.doForLoopBlock(parentStatement, targetExpression, newIdentifier);
		}

		// replace the existing expression with a new variable reference
		final Expression newSimpleName = this.ast.newSimpleName(newIdentifier);
		this.astRewriter.replace(expression, newSimpleName, null);
		this.changed = true;

		return newSimpleName;
	}

	private void doForLoopBlock(final Statement loopStatement, final Expression expression, final String identifier) {

		Statement innerStatement = null;
		if (loopStatement instanceof WhileStatement) {
			innerStatement = ((WhileStatement) loopStatement).getBody();
		} else if (loopStatement instanceof ForStatement) {
			innerStatement = ((ForStatement) loopStatement).getBody();
		} else if (loopStatement instanceof LabeledStatement) {
			this.doForLoopBlock(((LabeledStatement) loopStatement).getBody(), expression, identifier);
			return;
		} else {
			return;
		}

		final Assignment a = this.ast.newAssignment();
		a.setLeftHandSide(this.ast.newSimpleName(identifier));
		final Expression rightExpression = (Expression) ASTNode.copySubtree(this.ast, expression);
		a.setRightHandSide(rightExpression);
		final ExpressionStatement s = this.ast.newExpressionStatement(a);

		if (innerStatement instanceof Block) {
			final ListRewrite inserter = this.astRewriter.getListRewrite((Block) innerStatement,
					Block.STATEMENTS_PROPERTY);
			inserter.insertLast(s, null);
		}

		final Block block = this.ast.newBlock();
		final ListRewrite inserter = this.astRewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
		inserter.insertFirst(innerStatement, null);
		inserter.insertLast(s, null);
	}

	private Type resolveBinding(final ITypeBinding binding) {

		if (!this.nameResolving) {
			final Type object = this.ast.newSimpleType(this.ast.newSimpleName("Object"));
			return object;
		}

		if (null == binding) {
			final Type java = this.ast.newSimpleType(this.ast.newSimpleName("java"));
			final Type lang = this.ast.newQualifiedType(java, this.ast.newSimpleName("lang"));
			final Type object = this.ast.newQualifiedType(lang, this.ast.newSimpleName("Object"));
			return object;
		}

		else if (binding.isArray()) {
			final int dimension = binding.getDimensions();
			final ITypeBinding elementBinding = binding.getElementType();
			final Type elementType = this.resolveBinding(elementBinding);
			return this.ast.newArrayType(elementType, dimension);
		}

		else {

			IPackageBinding packageName = null;
			String typeName = null;
			if (binding.isAnonymous()) {
				packageName = null;
				typeName = "Object"; // XXX shouldn't be Object
			} else if (binding.isTypeVariable()) {
				packageName = null;
				typeName = "Object"; // XXX shouldn't be Object
			} else if (binding.isCapture()) {
				packageName = null;
				typeName = "Object"; // XXX shouldn't be Object
			} else if (binding.isWildcardType()) {
				packageName = null;
				typeName = "Object";
			} else if (binding.isNullType()) {
				packageName = null;
				typeName = "Object";
			} else {
				packageName = binding.getPackage();
				typeName = binding.getErasure().getName();
			}
			Type type = null;
			if (null != packageName && !packageName.getName().isEmpty()) {
				type = this.ast.newNameQualifiedType(this.ast.newName(packageName.getName()),
						this.ast.newSimpleName(typeName));
			}

			else {
				switch (typeName) {
				case "boolean":
					type = this.ast.newPrimitiveType(PrimitiveType.BOOLEAN);
					break;
				case "byte":
					type = this.ast.newPrimitiveType(PrimitiveType.BYTE);
					break;
				case "char":
					type = this.ast.newPrimitiveType(PrimitiveType.CHAR);
					break;
				case "double":
					type = this.ast.newPrimitiveType(PrimitiveType.DOUBLE);
					break;
				case "float":
					type = this.ast.newPrimitiveType(PrimitiveType.FLOAT);
					break;
				case "int":
					type = this.ast.newPrimitiveType(PrimitiveType.INT);
					break;
				case "long":
					type = this.ast.newPrimitiveType(PrimitiveType.LONG);
					break;
				case "short":
					type = this.ast.newPrimitiveType(PrimitiveType.SHORT);
					break;
				default:
					type = this.ast.newSimpleType(this.ast.newSimpleName(typeName));
					break;
				}
			}

			if (!binding.isParameterizedType()) {
				return type;
			}

			else {
				final ParameterizedType pType = this.ast.newParameterizedType(type);
				final ListRewrite typeArgumentsRewriter = this.astRewriter.getListRewrite(pType,
						ParameterizedType.TYPE_ARGUMENTS_PROPERTY);
				for (final ITypeBinding typeArgumentBinding : binding.getTypeArguments()) {
					final Type typeArgument = this.resolveBinding(typeArgumentBinding);
					typeArgumentsRewriter.insertLast(typeArgument, null);
				}
				return pType;
			}
		}
	}

	public boolean isChanged() {
		return this.changed;
	}
}
