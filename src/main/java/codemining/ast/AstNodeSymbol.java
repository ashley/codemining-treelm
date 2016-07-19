package codemining.ast;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.SignificanceLevel;

/**
 * A single AST Node Symbol
 *
 * @author Miltiadis Allamanis <m.allamanis@ed.ac.uk>
 *
 */
public class AstNodeSymbol implements Serializable {

	/**
	 * String annotation containing the node.
	 */
	public static final String CURRENT_NODE_ANNOTATION = "NODE_ANNOTATION";

	/**
	 * The current node in a multi-node.
	 */
	public static final String CURRENT_NODE_MULTI_PROPERTY = "CURRENT_NODE";

	/**
	 * A Node Type to represent a continuation for binarizing child lists to
	 * rules.
	 */
	public static final int MULTI_NODE = -1;

	/**
	 * The next property of a multi-node
	 */
	public static final String NEXT_PROPERTY = "NEXT";

	private static final long serialVersionUID = -8684027920801300413L;

	public static final int TEMPLATE_NODE = -2;

	/*
	ADDING_ATTRIBUTE_MODIFIABILITY(SignificanceLevel.LOW, false, false),
    ADDING_CLASS_DERIVABILITY(SignificanceLevel.LOW, false, false),
    ADDING_METHOD_OVERRIDABILITY(SignificanceLevel.LOW, false, false),
    ADDITIONAL_CLASS(SignificanceLevel.LOW, true, false),
    ADDITIONAL_FUNCTIONALITY(SignificanceLevel.LOW, true, false),
    ADDITIONAL_OBJECT_STATE(SignificanceLevel.LOW, true, false),
    ALTERNATIVE_PART_DELETE(SignificanceLevel.MEDIUM, true, false),
    ALTERNATIVE_PART_INSERT(SignificanceLevel.MEDIUM, true, false),
    ATTRIBUTE_RENAMING(SignificanceLevel.MEDIUM, false, true), // newEntity
    ATTRIBUTE_TYPE_CHANGE(SignificanceLevel.HIGH, false, true), // root
    CLASS_RENAMING(SignificanceLevel.MEDIUM, false, true), // rootEntity
    COMMENT_DELETE(SignificanceLevel.NONE, true, false),
    COMMENT_INSERT(SignificanceLevel.NONE, true, false),
    COMMENT_MOVE(SignificanceLevel.NONE, true, false),
    COMMENT_UPDATE(SignificanceLevel.NONE, true, false),
    CONDITION_EXPRESSION_CHANGE(SignificanceLevel.MEDIUM, true, false),
    DECREASING_ACCESSIBILITY_CHANGE(SignificanceLevel.HIGH, false, true), // changedEntity
    DOC_DELETE(SignificanceLevel.NONE, false, false),
    DOC_INSERT(SignificanceLevel.NONE, false, false),
    DOC_UPDATE(SignificanceLevel.NONE, false, false),
    INCREASING_ACCESSIBILITY_CHANGE(SignificanceLevel.MEDIUM, false, false),
    METHOD_RENAMING(SignificanceLevel.MEDIUM, false, true), // newEntity
    PARAMETER_DELETE(SignificanceLevel.HIGH, false, true), // root
    PARAMETER_INSERT(SignificanceLevel.HIGH, false, true), // root
    PARAMETER_ORDERING_CHANGE(SignificanceLevel.HIGH, false, true), // root
    PARAMETER_RENAMING(SignificanceLevel.MEDIUM, false, false),
    PARAMETER_TYPE_CHANGE(SignificanceLevel.HIGH, false, true), // root
    PARENT_CLASS_CHANGE(SignificanceLevel.CRUCIAL, false, false),
    PARENT_CLASS_DELETE(SignificanceLevel.CRUCIAL, false, false),
    PARENT_CLASS_INSERT(SignificanceLevel.CRUCIAL, false, false),
    PARENT_INTERFACE_CHANGE(SignificanceLevel.CRUCIAL, false, false),
    PARENT_INTERFACE_DELETE(SignificanceLevel.CRUCIAL, false, false),
    PARENT_INTERFACE_INSERT(SignificanceLevel.CRUCIAL, false, false),
    REMOVED_CLASS(SignificanceLevel.HIGH, true, true), // changedEntity
    REMOVED_FUNCTIONALITY(SignificanceLevel.HIGH, true, true), // changedEntity
    REMOVED_OBJECT_STATE(SignificanceLevel.HIGH, true, true), // changedEntity
    REMOVING_ATTRIBUTE_MODIFIABILITY(SignificanceLevel.HIGH, false, true), // root
    REMOVING_CLASS_DERIVABILITY(SignificanceLevel.CRUCIAL, false, false),
    REMOVING_METHOD_OVERRIDABILITY(SignificanceLevel.CRUCIAL, false, false),
    RETURN_TYPE_CHANGE(SignificanceLevel.HIGH, false, true), // root
    RETURN_TYPE_DELETE(SignificanceLevel.HIGH, false, true), // root
    RETURN_TYPE_INSERT(SignificanceLevel.HIGH, false, true), // root
    STATEMENT_DELETE(SignificanceLevel.MEDIUM, true, false),
    STATEMENT_INSERT(SignificanceLevel.LOW, true, false),
    STATEMENT_ORDERING_CHANGE(SignificanceLevel.LOW, true, false),
    STATEMENT_PARENT_CHANGE(SignificanceLevel.MEDIUM, true, false),
    STATEMENT_UPDATE(SignificanceLevel.LOW, true, false),
    UNCLASSIFIED_CHANGE(SignificanceLevel.NONE, true, false); */
	public static final int UNK_SYMBOL = Integer.MIN_VALUE;

	public static final Function<Integer, String> DEFAULT_NODETYPE_TO_STRING = new Function<Integer, String>() {

		@Override
		public String apply(final Integer nodeType) {
			return nodeType.toString();
		}
	};

	/**
	 * A map of annotations to their respective values. Annotations are not
	 * structural properties of the node
	 */
	private SortedMap<String, Object> annotations = Maps.newTreeMap();

	/**
	 * A list of the child properties that contain the node children
	 */
	private List<String> childProperties = Lists.newArrayList();

	/**
	 * The type of the node.
	 */
	public final int nodeType;

	/**
	 * A map of properties to their respective values
	 */
	private SortedMap<String, Object> simplePropValues = Maps.newTreeMap();

	public AstNodeSymbol(ChangeType ct) {
		nodeType = (ct.ordinal() * -1) - 3;
	}
	public AstNodeSymbol(final int type) {
		nodeType = type;
	}

	public synchronized void addAnnotation(final String annotation, final Object value) {
		annotations.put(checkNotNull(annotation), checkNotNull(value));
	}

	public synchronized void addChildProperty(final String propertyName) {
		checkNotNull(propertyName);
		childProperties.add(propertyName);
	}

	/**
	 * Add a simple property to the symbol.
	 *
	 * @param propertyName
	 * @param value
	 */
	public synchronized void addSimpleProperty(final String propertyName, final Object value) {
		checkNotNull(propertyName);
		checkNotNull(value);
		simplePropValues.put(propertyName, value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AstNodeSymbol)) {
			return false;
		}
		final AstNodeSymbol other = (AstNodeSymbol) obj;

		if (nodeType != other.nodeType) {
			return false;
		}

		if (!simplePropValues.equals(other.simplePropValues)) {
			return false;
		}

		if (!annotations.equals(other.annotations)) {
			return false;
		}

		return childProperties.equals(other.childProperties);
	}

	public final Object getAnnotation(final String annotation) {
		return annotations.get(annotation);
	}

	public String getChildProperty(int i) {
		return childProperties.get(i);
	}

	public Set<String> getSimpleProperties() {
		return simplePropValues.keySet();
	}

	public final Object getSimpleProperty(final String property) {
		return simplePropValues.get(property);
	}

	public final boolean hasAnnotation(final String annotation) {
		return annotations.containsKey(annotation);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nodeType, simplePropValues, childProperties, annotations);
	}

	public final boolean hasSimpleProperty(final String property) {
		return simplePropValues.containsKey(property);
	}

	public void lockFromChanges() {
		childProperties = ImmutableList.copyOf(childProperties);
		simplePropValues = ImmutableSortedMap.copyOf(simplePropValues);
		annotations = ImmutableSortedMap.copyOf(annotations);
	}

	public final int nChildProperties() {
		return childProperties.size();
	}

	@Override
	public String toString() {
		return toString(DEFAULT_NODETYPE_TO_STRING);
	}

	public String toString(final Function<Integer, String> nodeTypeToString) {
		final StringBuffer buf = new StringBuffer();
		if (nodeType == MULTI_NODE) {
			buf.append("Type : MULTI_NODE");
		} else if (nodeType == UNK_SYMBOL) {
			buf.append("Type : UNK");
		} else if (nodeType == TEMPLATE_NODE) {
			buf.append("Type : TEMPLATE_NODE");
		} else {
			buf.append("Type : " + nodeTypeToString.apply(nodeType));
		}

		if (simplePropValues.size() > 0) {
			buf.append(" Simple Props: [");
			for (final Entry<String, Object> propEntry : simplePropValues.entrySet()) {
				final String prop = propEntry.getKey();
				buf.append(prop + ":" + propEntry.getValue() + ", ");
			}
			buf.append(']');
		}

		if (childProperties.size() > 0) {
			buf.append(" Child Props: [");
			for (final String prop : childProperties) {
				buf.append(prop + ", ");
			}
			buf.append(']');
		}

		if (annotations.size() > 0) {
			buf.append(" Annotations: [");
			for (final Entry<String, Object> annotation : annotations.entrySet()) {
				buf.append(annotation.getKey() + ":" + annotation.getValue() + ", ");
			}
			buf.append(']');
		}
		return buf.toString();
	}
}