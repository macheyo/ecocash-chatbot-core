package zw.co.cassavasmartech.ecocashchatbotcore.transaction;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formats {

    public static String FormatCurrency(java.lang.Number number) {
        return Number.CURRENCY.format(number);
    }

    public enum DateFormat {

        FULL("dd-MMM-yyyy HH:mm:ss"), MEDIUM("dd-MMM-yyyy"), SHORT("dd-MM-yyyy"), ddMMYYY("ddMMyyyy"),
        YYYY_MM_dd("yyyy-MM-dd"), TIME_MILLI_SECONDS("HHmmssSSSS"), DATE_TIME_MILLI_SECONDS("yyMMddHHmmssSSSS");

        private final String formatText;

        DateFormat(String formatText) {
            this.formatText = formatText;
        }

        public String format(Date date) {
            return new SimpleDateFormat(formatText).format(date);
        }

        public String getFormatText() {
            return formatText;
        }
    }

    private enum Style {
        PERCENT_STYLE, DOUBLE_STYLE;
    }

    public enum Number {

        CURRENCY("#,##0.00"), PERCENT(Style.PERCENT_STYLE), INT("0"), DOUBLE(Style.DOUBLE_STYLE);

        private static final int DEFAULT_FRACTION_DIGITS = 2;
        private final String formatText;
        private final Style style;

        private Number(String formatText) {
            this.formatText = formatText;
            this.style = null;
        }

        private Number(Style style) {
            this.style = style;
            this.formatText = null;
        }

        public String format(java.lang.Number number) {
            return format(number, DEFAULT_FRACTION_DIGITS);
        }

        public String format(java.lang.Number number, int fractionDigits) {
            return getFormat(fractionDigits).format(number);
        }

        private NumberFormat getFormat(int maximumFractionDigits) {
            NumberFormat format;
            if (formatText != null) {
                format = new DecimalFormat(formatText);
            } else {
                switch (style) {
                    case PERCENT_STYLE:
                        format = NumberFormat.getPercentInstance();
                        break;
                    case DOUBLE_STYLE:
                        format = NumberFormat.getNumberInstance();
                        break;
                    default:
                        format = NumberFormat.getNumberInstance();
                        break;
                }
            }
            format.setMaximumFractionDigits(maximumFractionDigits);
            return format;
        }
    }
}
