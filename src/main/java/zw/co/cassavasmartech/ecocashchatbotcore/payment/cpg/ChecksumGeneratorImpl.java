package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.esb.commons.data.TransactionRequest;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class ChecksumGeneratorImpl implements ChecksumGenerator {

    @Autowired
    private Environment environment;

    @Autowired
    private ResourceLoader resourceLoader;

    public String generateCheckSum(TransactionRequest request) throws Exception {
        final String keyStoreProvider = environment.getProperty("ecosure.moovah.checksum.keystore.type");
        final String keyStoreLocation = environment.getProperty("ecosure.moovah.checksum.keystore.location");
        final String keyStorePassCode = environment.getProperty("ecosure.moovah.checksum.keystore.password");
        final String keyStoreAlias = environment.getProperty("ecosure.moovah.checksum.keystore.alias");
        final StringBuilder builder = new StringBuilder(request.getField1());
        builder.append(request.getField2());
        builder.append(request.getField7());
        builder.append(request.getField10());
        final KeyStore keyStore = KeyStore.getInstance(keyStoreProvider);
        final InputStream keyStoreInputStream = resourceLoader.getResource(keyStoreLocation).getInputStream();
        keyStore.load(keyStoreInputStream, keyStorePassCode.toCharArray());
        keyStoreInputStream.close();
        final KeyStore.ProtectionParameter keyPass = new KeyStore.PasswordProtection(keyStorePassCode.toCharArray());
        final KeyStore.PrivateKeyEntry privKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyStoreAlias, keyPass);
        final PrivateKey privateKey = privKeyEntry.getPrivateKey();
        final X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyStoreAlias);
        final Signature signature = Signature.getInstance(cert.getSigAlgName());
        signature.initSign(privateKey);
        signature.update(builder.toString().getBytes());
        final byte[] result = signature.sign();
        return java.util.Base64.getEncoder().encodeToString(result);
    }


    @Override
    public String generateReference(final String msisdn, final Date requestTimeStamp) {
        final StringBuilder builder = new StringBuilder();
        builder.append(msisdn)
                .append(".")
                .append(new SimpleDateFormat("yyMMdd").format(requestTimeStamp.getTime()))
                .append(".")
                .append(new SimpleDateFormat("HHmms").format(requestTimeStamp.getTime()));
        return builder.toString();
    }
}
