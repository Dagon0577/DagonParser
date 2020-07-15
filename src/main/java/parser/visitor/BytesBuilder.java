package parser.visitor;

import parser.token.Token;
import parser.util.BytesUtil;

/**
 * @author Dagon0577
 * @date 2020/7/15
 */
public class BytesBuilder {
    private int index = 0;
    private int size;
    private byte[] sql;

    public BytesBuilder(int length) {
        this.sql = new byte[length * 2];
        this.size = sql.length;
    }

    public BytesBuilder append(byte[] data) {
        checkLength(data.length);
        return appendWithoutCheck(data);
    }

    public byte[] getDataAndFreeMemory() {
        if (sql == null) {
            return null;
        }
        if (index == size) {
            return sql;
        }
        byte[] values = new byte[index];
        for (int i = 0; i < index; i++) {
            values[i] = sql[i];
        }
        sql = null;
        return values;
    }

    public BytesBuilder append(byte[] datas, long info) {
        int size = BytesUtil.getSize(info);
        checkLength(size);
        int offset = BytesUtil.getOffset(info);
        for (int i = 0; i < size; i++) {
            sql[index] = datas[offset + i];
            index++;
        }
        return this;
    }

    public BytesBuilder append(String data) {
        if (data == null) {
            return append(Token.getBytes(Token.LITERAL_NULL));
        }
        return append(data.getBytes());
    }

    /**
     * <pre>
     * type
     *  0 -> 两边都加空格
     *  1 -> 前面加空格
     *  2 -> 后面加空格
     * </pre>
     */
    public BytesBuilder append(byte[] datas, int type) {
        switch (type) {
            case 0:
                checkLength(datas.length + 2);
                sql[index++] = ' ';
                appendWithoutCheck(datas);
                sql[index++] = ' ';
                break;
            case 1:
                checkLength(datas.length + 1);
                sql[index++] = ' ';
                appendWithoutCheck(datas);
                break;
            case 2:
                checkLength(datas.length + 1);
                appendWithoutCheck(datas);
                sql[index++] = ' ';
                break;
        }
        return this;
    }

    public BytesBuilder append(Object c) {
        if (c == null) {
            return append(Token.getBytes(Token.LITERAL_NULL));
        } else if (c instanceof byte[]) {
            return append((byte[])c);
        }
        return append(String.valueOf(c));
    }

    public BytesBuilder append(char c) {
        checkLength(1);
        sql[index++] = (byte)c;
        return this;
    }

    private BytesBuilder appendWithoutCheck(byte[] data) {
        for (int i = 0, length = data.length; i < length; i++) {
            sql[index++] = data[i];
        }
        return this;
    }

    private void checkLength(int length) {
        int needed = index + length;
        if (needed > this.size) {
            int newSize = (int)(this.size > length ? this.size * 2 : this.size * 2 + length);
            if (newSize < needed) {
                newSize = needed;
            }
            byte[] tmp = new byte[newSize];
            for (int i = 0; i < index; i++) {
                tmp[i] = sql[i];
            }
            this.sql = tmp;
            this.size = newSize;
        }
    }

    public int getIndex() {
        return this.index;
    }

    public void isLastSemicolon() {
        if (sql[index - 1] == ';') {
            sql[index - 1] = 0;
            index--;
        }
    }
}
