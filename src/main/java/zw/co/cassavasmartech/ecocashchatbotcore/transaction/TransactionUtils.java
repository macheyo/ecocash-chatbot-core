package zw.co.cassavasmartech.ecocashchatbotcore.transaction;

import java.util.Date;
import java.util.Random;

public class TransactionUtils {

    private static volatile TransactionUtils instance;

    private static final Random generator = new Random();

    private TransactionUtils() {

    }

    /**
     * Type safe for accessing utility classes. Implements double locking
     */
    public static TransactionUtils getInstance() {
        if (instance == null) {
            synchronized (TransactionUtils.class) {
                if (instance == null) {
                    instance = new TransactionUtils();
                }
            }
        }
        return instance;
    }

    public String generateTransactionReference() {
        return Formats.DateFormat.TIME_MILLI_SECONDS.format(new Date()) + "" + generator.nextInt(100);
    }

    private static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

}