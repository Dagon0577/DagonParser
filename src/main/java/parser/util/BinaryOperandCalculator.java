package parser.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public interface BinaryOperandCalculator {
    Number calculate(Integer integer1, Integer integer2);

    Number calculate(Long long1, Long long2);

    Number calculate(BigInteger bigint1, BigInteger bigint2);

    Number calculate(BigDecimal bigDecimal1, BigDecimal bigDecimal2);
}
