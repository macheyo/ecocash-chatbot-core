package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.*;

public enum Intent {
    usecase_welcome (new WelcomeIntentHandler()),
    usecase_pinreset (new PinresetIntentHandler()),
    usecase_pinreset_security_questions_affirmative_intent (new PinresetSecurityQuestionsAffirmativeHandler()),
    usecase_verify_get_msisdn (new VerificationGetMsisdnIntentHandler()),
    usecase_verify_get_msisdn_fallback (new VerificationGetMsisdnFallbackIntentHandler()),
    usecase_verify_get_otp (new VerificationGetOTPIntentHandler()),

    ;
    private final IntentHandlerAdapter intentHandlerAdapter;
    Intent(IntentHandlerAdapter intentHandlerAdapter) {
        this.intentHandlerAdapter = intentHandlerAdapter;
    }

    /** Return a Function's adapter by name; null if unknown. */
    public static IntentHandlerAdapter lookup(String name) {
        Intent usecase;
        try {
            usecase = Enum.valueOf(Intent.class, name);
        } catch (RuntimeException e) {
            return null;
        }
        return usecase.intentHandlerAdapter;
    }
}
