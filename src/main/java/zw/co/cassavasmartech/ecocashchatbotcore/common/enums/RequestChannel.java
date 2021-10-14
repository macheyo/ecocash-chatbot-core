package zw.co.cassavasmartech.ecocashchatbotcore.common.enums;

public enum RequestChannel {

    WEB("WEB"), MOBILE("MOBILE"), USSD("USSD"), SYSTEM("SYSTEM");

    private String channel;

    RequestChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return this.channel;
    }

}
