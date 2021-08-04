package zw.co.cassavasmartech.ecocashchatbotcore.cpg;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckSumGeneratorImpl implements CheckSumGenerator {

    private final ResourceLoader resourceLoader;
    @Value("${ecocash.chatbot.core.cpg-api.config.keystoreType}")
    private String keystoreProvider;

    @Value("${ecocash.chatbot.core.cpg-api.config.keystoreLocation}")
    private String keystoreLocation;

    @Value("${ecocash.chatbot.core.cpg-api.config.keystorePassword}")
    private String keystorePassword;

    @Value("${ecocash.chatbot.core.cpg-api.config.keystoreAlias}")
    private String keystoreAlias;

    @Override
    public String generateCheckSum(TransactionRequest request) {

        try {

            final KeyStore keystore =KeyStore.getInstance(keystoreProvider);
            final InputStream keyStoreInputStream = resourceLoader.getResource(keystoreLocation).getInputStream();

            final char[] keystorePassArr = keystorePassword.toCharArray();

            keystore.load(keyStoreInputStream, keystorePassArr);

            keyStoreInputStream.close();

            final KeyStore.ProtectionParameter keyPass = new KeyStore.PasswordProtection(keystorePassArr);

            final KeyStore.PrivateKeyEntry privKeyEntry = (KeyStore.PrivateKeyEntry)keystore.getEntry(keystoreAlias, keyPass);

            final PrivateKey privateKey =privKeyEntry.getPrivateKey();

            final X509Certificate cert = (X509Certificate)keystore.getCertificate(keystoreAlias);

            final Signature signature =Signature.getInstance(cert.getSigAlgName());

            signature.initVerify(cert.getPublicKey());

            final String saltValue = appendCheckSumFields(request);
            signature.initSign(privateKey); // not present on documentation
            signature.update(saltValue.getBytes()) ;

            byte[] signedInfo = signature.sign();

            return Base64.getEncoder().encodeToString(signedInfo);

        } catch (IOException | SignatureException | CertificateException | NoSuchAlgorithmException | InvalidKeyException |
                UnrecoverableEntryException | KeyStoreException e) {
            log.error("Exception on generating checksum ", e);
            return "";
        }

    }
    private String appendCheckSumFields(TransactionRequest transactionRequest) {
        final StringBuilder builder = new StringBuilder(transactionRequest.getField1());
        builder.append(transactionRequest.getField2());
        builder.append(transactionRequest.getField7());
        builder.append(transactionRequest.getField10());
        return builder.toString();
    }

}
