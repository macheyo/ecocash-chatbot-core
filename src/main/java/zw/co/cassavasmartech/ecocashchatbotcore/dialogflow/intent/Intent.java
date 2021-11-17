package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent;

import org.hibernate.cfg.annotations.reflection.XMLContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.DefaultFallbackIntentHandler;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.PaymentIntentHandler;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.airtime.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.billPayment.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.merchantPayment.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.pinreset.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.sendMoney.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.statement.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.transactionReversal.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.verification.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.welcome.WelcomeIntentHandler;

public enum Intent {
    usecase_welcome (new WelcomeIntentHandler()),
    default_fallback_intent(new DefaultFallbackIntentHandler()),
    usecase_payment_intent(new PaymentIntentHandler()),
    usecase_verify_get_msisdn (new VerificationGetMsisdnIntentHandler()),
    usecase_verify_get_msisdn_fallback (new VerificationGetMsisdnFallbackIntentHandler()),
    usecase_verify_unregistered_msisdn_confirmation_affirmative(new UnregisteredMsisdnConfirmationAffirmativeIntentHandler()),
    usecase_verify_unregistered_msisdn_confirmation_negative(new UnregisteredMsisdnConfirmationNegativeIntentHandler()),
    usecase_verify_register_confirmation_negative(new RegisterConfirmationNegativeIntentHandler()),//TODO create intent in dialogflow
    usecase_verify_register_confirmation_affirmative(new RegisterConfirmationAffirmativeIntentHandler()),//TODO create intent in dialogflow
    usecase_verify_get_otp (new VerificationGetOTPIntentHandler()),
    usecase_pinreset (new PinResetIntentHandler()),
    usecase_pinreset_security_questions_affirmative_intent (new PinResetSecurityQuestionsAffirmativeHandler()),
    usecase_pinreset_security_questions_fallback(new PinResetSecurityQuestionsFallbackIntentHandler()),
    usecase_pinreset_security_questions_answer(new PinResetSecurityQuestionsAnswer()),
    usecase_pinreset_security_questions_more_negative(new PinResetSecurityQuestionsMoreNegativeIntentHandler()),
    usecase_pinreset_security_questions_more_affirmative(new PinresetSecurityQuestionsMoreAffirmativeIntentHandler()),
    usecase_pinreset_security_questions_more_fallback(new PinResetSecurityQuestionsMoreFallbackIntentHandler()),
    usecase_pay_biller_scenario1(new PayBillScenario1IntentHandler()),
    usecase_pay_biller_scenario2(new PayBillScenario2IntentHandler()),
    usecase_pay_biller_get_biller_amount(new PayBillGetBillAmountIntentHandler()),
    usecase_pay_biller_get_biller_amount_fallback(new PayBillGetBillAmountFallbackIntentHandler()),
    usecase_pay_biller_get_biller_code(new PayBillGetBillCodeIntentHandler()),
    usecase_pay_biller_get_biller_code_fallback(new PayBillGetBillCodeFallbackIntentHandler()),
    usecase_pay_biller_get_account_intent(new PayBillGetAccountIntentIntentHandler()),
    usecase_pay_biller_get_account_intent_fallback(new PayBillGetAccountIntentFallbackIntentHandler()),
    usecase_pay_biller_get_biller_confirmation_affirmative(new PayBillConfirmationAffirmativeIntentHandler()),
    usecase_pay_biller_get_biller_confirmation_negative(new PayBillConfirmationNegativeIntentHandler()),
    usecase_pay_biller_get_biller_confirmation_fallback(new PayBillConfirmationFallbackIntentHandler()),
    usecase_pay_biller_confirmation_negative_yes(new PayBillConfirmationNegativeYesIntentHandler()),
    usecase_pay_biller_confirmation_negative_no(new PayBillConfirmationNegativeNoIntentHandler()),
    usecase_pay_biller_more_affirmative(new PayBillMoreAffirmativeIntentHandler()),
    usecase_pay_biller_more_negative(new PayBillMoreNegativeIntentHandler()),
    usecase_pay_biller_more_fallback(new PayBillMoreFallbackIntentHandler()),
    usecase_statement_scenario_1(new StatementScenario1IntentHandler()),
    usecase_statement_scenario_2(new StatementScenario2IntentHandler()),
    usecase_statement_scenario_3(new StatementScenario3IntentHandler()),
    usecase_statement_charge_confirmation(new StatementChargeConfirmation()),
    usecase_statement_end_date(new StatementEndDateIntentHandler()),
    usecase_statement_start_date(new StatementStartDateIntentHandler()),
    usecase_statement_start_date_fallback(new StatementStartDateFallbackIntentHandler()),
    usecase_statement_end_date_fallback(new StatementEndDateFallbackIntentHandler()),
    usecase_statement_confirmation_affirmative(new StatementConfirmationAffirmative()),
//    usecase_statement_confirmation_negative(new StatementConfirmationNegative()),
//    usecase_statement_confirmation_fallback(new StatementConfirmationFallback()),
    usecase_statement_more_affirmative(new StatementMoreAffirmativeIntentHandler()),
    usecase_statement_more_negative(new StatementMoreNegativeIntentHandler()),
    usecase_statement_more_fallback(new StatementMoreFallbackIntentHandler()),
    usecase_pay_merchant_scenario1(new PayMerchantScenario1IntentHandler()),
    usecase_pay_merchant_scenario2(new PayMerchantScenario2IntentHandler()),
    usecase_pay_merchant_get_msisdn(new PayMerchantGetMsisdnIntentHandler()),//TODO purge intent
    usecase_pay_merchant_get_msisdn_fallback(new PayMerchantGetMsisdnFallbackIntentHandler()),//TODO purge this intent
    usecase_pay_merchant_get_name(new PayMerchantGetNameIntentHandler()),
    usecase_pay_merchant_get_amount(new PayMerchantGetAmountIntentHandler()),
    usecase_pay_merchant_get_amount_fallback(new PayMerchantGetAmountFallbackIntentHandler()),
    usecase_pay_merchant_confirmation_affirmative(new PayMerchantConfirmationAffirmativeIntentHandler()),
    usecase_pay_merchant_confirmation_fallback(new PayMerchantConfirmationFallbackIntentHandler()),
    usecase_pay_merchant_confirmation_negative(new PayMerchantConfirmationNegativeIntentHandler()),
    usecase_pay_merchant_confirmation_negative_yes(new PayMerchantConfirmationNegativeYesIntentHandler()),
    usecase_pay_merchant_confirmation_negative_no(new PayMerchantConfirmationNegativeNoIntentHandler()),
    usecase_pay_merchant_more_affirmative(new PayMerchantMoreAffirmativeIntentHandler()),
    usecase_pay_merchant_more_negative(new PayMerchantMoreNegativeIntentHandler()),
    usecase_pay_merchant_more_fallback(new PayMerchantMoreFallbackIntentHandler()),
    usecase_send_airtime_scenario1(new SendAirtimeScenario1IntentHandler()),
    usecase_send_airtime_scenario2(new SendAirtimeScenario2IntentHandler()),
    usecase_send_airtime_myself(new SendAirtimeMyselfIntentHandler()),
    usecase_send_airtime_myself_fallback(new SendAirtimeMyselfFallbackIntentHandler()),
    usecase_send_airtime_myself_negative(new SendAirtimeMyselfNegativeIntentHandler()),
    usecase_send_airtime_get_beneficiary_msisdn(new SendAirtimeGetBeneficiaryMsisdnIntentHandler()),
    usecase_send_airtime_get_beneficiary_msisdn_fallback(new SendAirtimeGetBeneficiaryMsisdnFallbackIntentHandler()),
    usecase_send_airtime_get_beneficiary_amount(new SendAirtimeGetBeneficiaryAmountIntentHandler()),
    usecase_send_airtime_get_beneficiary_amount_fallback(new SendAirtimeGetBeneficiaryAmountFallbackIntentHandler()),
    usecase_send_airtime_confirmation_affirmative(new SendAirtimeConfirmationAffirmativeIntentHandler()),
    usecase_send_airtime_confirmation_negative(new SendAirtimeConfirmationNegativeIntentHandler()),
    usecase_send_airtime_confirmation_negative_yes(new SendAirtimeConfirmationNegativeYesIntentHandler()),
    usecase_send_airtime_confirmation_negative_no(new SendAirtimeConfirmationNegativeNoIntentHandler()),
    usecase_send_airtime_confirmation_fallback(new SendAirtimeConfirmationFallbackIntentHandler()),
    usecase_send_airtime_more_affirmative(new SendAirtimeMoreAffirmativeIntentHandler()),
    usecase_send_airtime_more_negative(new SendAirtimeMoreNegativeIntentHandler()),
    usecase_send_airtime_more_fallback(new SendAirtimeMoreFallbackIntentHandler()),
    usecase_send_money_scenario1(new SendMoneyScenario1IntentHandler()),
    usecase_send_money_scenario2(new SendMoneyScenario2IntentHandler()),
    usecase_send_money_get_beneficiary_msisdn(new SendMoneyGetBeneficiaryMsisdnIntentHandler()),
    usecase_send_money_get_beneficiary_amount(new SendMoneyGetBeneficiaryAmountIntentHandler()),
    usecase_send_money_confirmation_affirmative(new SendMoneyConfirmationAffirmativeIntentHandler()),
    usecase_send_money_confirmation_negative(new SendMoneyConfirmationNegativeIntentHandler()),
    usecase_send_money_confirmation_fallback(new SendMoneyConfirmationFallbackIntentHandler()),
    usecase_send_money_confirmation_negative_no(new SendMoneyConfirmationNegativeNoIntentHandler()),
    usecase_send_money_confirmation_negative_yes(new SendMoneyConfirmationNegativeYesIntentHandler()),
    usecase_send_money_more_affirmative(new SendMoneyMoreAffirmativeIntentHandler()),
    usecase_send_money_more_negative(new SendMoneyMoreNegativeIntentHandler()),
    usecase_send_money_more_fallback(new SendMoneyMoreFallbackIntentHandler()),
    usecase_send_money_get_beneficiary_msisdn_fallback(new SendMoneyGetBeneficiaryMsisdnFallbackIntentHandler()),
    usecase_send_money_get_beneficiary_amount_fallback(new SendMoneyGetBeneficiaryAmountFallbackIntentHandler()),
    usecase_send_money_tariff(new SendMoneyTarriffIntent()),
    usecase_transaction_reversal_intent(new TransactionReversalIntentIntentHandler()),
    usecase_transaction_reversal_reverse(new TransactionReversalReverseIntentHandler()),
    usecase_transaction_reversal_reverse_reference(new TransactionReversalReverseReferenceIntentHandler()),
    usecase_transaction_reversal_reverse_affirmative(new TransactionReversalReverseAffirmativeIntentHandler()),
    usecase_transaction_reversal_approve(new TransactionReversalApproveIntentHandler()),
    usecase_transaction_reversal_approve_reference(new TransactionReversalApproveReferenceIntentHandler()),
    usecase_transaction_reversal_approval_affirmative(new TransactionReversalApprovalAffirmativeIntentHandler()),
    usecase_transaction_reversal_approval_confirmation_fallback(new TransactionReversalApprovalConfirmationFallbackIntentHandler()),
    usecase_transaction_reversal_approval_negative(new TransactionReversalApprovalNegativeIntentHandler()),
    usecase_transaction_reversal_approval_reference(new TransactionReversalApprovalReferenceIntentHandler()),
    usecase_transaction_reversal_approval_reference_fallback(new TransactionReversalApprovalReferenceFallbackIntentHandler()),
    usecase_transaction_reversal_fallback(new TransactionReversalFallbackIntentHandler()),
    usecase_transaction_reversal_more_affirmative(new TransactionReversalMoreAffirmativeIntentHandler()),
    usecase_transaction_reversal_more_fallback(new TransactionReversalMoreFallbackIntentHandler()),
    usecase_transaction_reversal_more_negative(new TransactionReversalMoreNegativeIntentHandler()),
    usecase_transaction_reversal_reverse_confirmation_fallback(new TransactionReversalReverseConfirmationFallbackIntentHandler()),
    usecase_transaction_reversal_reverse_negative(new TransactionReversalReverseNegativeIntentHandler()),
    usecase_transaction_reversal_reverse_reference_fallback(new TransactionReversalReverseReferenceFallbackIntentHandler()),
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
