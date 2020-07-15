package mysql.charset;

import java.nio.charset.Charset;

/**
 * @author Dagon0577
 * @date 2020/6/23
 */
public class MySqlCharset {
    public static final Charset UTF8_FOR_JAVA = Charset.forName("UTF-8");
    public static final Charset LATIN1_FOR_JAVA = Charset.forName("ISO-8859-1");
    public static final Charset GBK_FOR_JAVA = Charset.forName("GBK");

    public static Charset getCharsetForJava(String charset) {
        switch (charset.toUpperCase()) {
            case "UTF8":
            case "UTF8MB4":
            case "BINARY":
                return UTF8_FOR_JAVA;
            case "LATIN1":
                return LATIN1_FOR_JAVA;
            case "GBK":
                return GBK_FOR_JAVA;
            default:
                return UTF8_FOR_JAVA;
        }
    }
}
