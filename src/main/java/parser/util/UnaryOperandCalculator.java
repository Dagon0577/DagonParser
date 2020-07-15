package parser.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Dagon0577
 * @date 2020/7/14
 */
public interface UnaryOperandCalculator {
    Number calculate(Integer num);

    Number calculate(Long num);

    Number calculate(BigInteger num);

    Number calculate(BigDecimal num);
}
