package zw.co.cassavasmartech.ecocashchatbotcore.common.enums;

public enum ZimraValidationType {

    BPN("BPN"), ASSESSMENT("ASSESSMENT"), BPN_TAXCODE("BPN_TAXCODE");

    private String validationType;

    ZimraValidationType(String validationType) {
        this.validationType = validationType;
    }

    public String getValidationType() {
        return this.validationType;
    }
}
