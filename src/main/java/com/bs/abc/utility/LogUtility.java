/**
 * 
 */
package com.bs.abc.utility;

import com.bs.abc.sysout.Slf4jLoggerPrintStream;
import com.bs.abc.sysout.Slf4jLoggerErrorStream;

import java.math.BigInteger;
import java.security.SecureRandom;
public class LogUtility {

  // Console to Slf4j bridge
  static {
    System.setOut(new Slf4jLoggerPrintStream(System.out));
    System.setErr(new Slf4jLoggerErrorStream(System.err));
  }

  /**
   * This works by choosing 130 bits from a cryptographically secure random bit generator, and encoding them in base-32.
   * 128 bits is considered to be cryptographically strong, but each digit in a base 32 number can encode 5 bits, so 128
   * is rounded up to the next multiple of 5. This encoding is compact and efficient, with 5 random bits per character.
   * Compare this to a random UUID, which only has 3.4 bits per character in standard layout, and only 122 random bits
   * in total.
   * 
   * @return
   */
  public static final String nextTransactionId() {
    SecureRandom random = new SecureRandom();
    return new BigInteger(130, random).toString(32);
  }

}
