package parser.ast.expression.primary;

import parser.ast.AST;
import parser.ast.expression.PrimaryExpression;
import parser.util.BytesUtil;
import parser.visitor.Visitor;

/**
 * @author Dagon0577
 * @date 2020/5/15
 */
public class Identifier extends PrimaryExpression {
    private Identifier parent;
    private byte[] idText;
    private boolean withOpenQuate;

    private String idUpperCache;

    public Identifier(Identifier parent, byte[] idText) {
        this(parent, idText, idText.length > 2 && idText[0] == '`' && idText[idText.length - 1] == '`');
    }

    public Identifier(Identifier parent, byte[] idText, boolean withOpenQuate) {
        this.parent = parent;
        this.idText = idText;
        this.withOpenQuate = withOpenQuate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Identifier getParent() {
        return parent;
    }

    public void setParent(Identifier parent) {
        this.parent = parent;
    }

    public byte[] getIdText() {
        return idText;
    }

    /**
     * 是否被开单引号所包括
     */
    public boolean isWithOpenQuate() {
        return withOpenQuate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Identifier) {
            Identifier that = (Identifier)obj;
            if (objEquals(this.parent, that.parent)) {
                return BytesUtil.equalsIgnoreCase(this.idText, that.idText, this.withOpenQuate, that.withOpenQuate);
            }
        }
        return false;
    }

    private boolean objEquals(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    @Override
    public boolean replace(AST from, AST to) {
        boolean result = false;
        if (parent != null) {
            if (parent.equals(from)) {
                parent = (Identifier)to;
                result = true;
            } else {
                result |= parent.replace(from, to);
            }
        }
        return result;
    }

    @Override
    public boolean removeSchema(byte[] schema) {
        return trimParent(2, schema) == PARENT_TRIMED;
    }

    /**
     * trim not happen because parent in given level is not exist
     */
    private static final int PARENT_ABSENT = 0;
    /**
     * trim happen
     */
    private static final int PARENT_TRIMED = 1;
    /**
     * trim not happen because parent in given not equals to given name
     */
    private static final int PARENT_IGNORED = 2;

    private int trimParent(int level, byte[] trimSchema) {
        Identifier id = this;
        for (int i = 1; i < level; ++i) {
            if (id.parent == null) {
                return PARENT_ABSENT;
            }
            id = id.parent;
        }
        if (id.parent == null) {
            return PARENT_ABSENT;
        }
        if (trimSchema != null && !BytesUtil
            .equalsIgnoreCase(id.parent.getIdText(), trimSchema, id.parent.isWithOpenQuate(), false)) {
            return PARENT_IGNORED;
        } else {
            id.parent = null;
            return PARENT_TRIMED;
        }
    }

    public String getIdUpperUnescape() {
        if (idUpperCache == null) {
            if (withOpenQuate) {
                byte[] data = new byte[idText.length - 2];
                System.arraycopy(idText, 1, data, 0, data.length);
                idUpperCache = new String(data).toUpperCase();
            } else {
                idUpperCache = new String(idText).toUpperCase();
            }
        }
        return idUpperCache;
    }
}
