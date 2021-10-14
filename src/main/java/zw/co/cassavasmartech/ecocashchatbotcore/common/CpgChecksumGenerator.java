package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg.ChecksumRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg.ChecksumResponse;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;

@Slf4j
public class CpgChecksumGenerator {

    public static ChecksumResponse getChecksum(ChecksumRequest request) {
        try (FileInputStream keystoreInputStream = new FileInputStream(request.getKeyStoreLocation())) {
            KeyStore keystore = KeyStore.getInstance(request.getKeystoreType());
            String keyStorePassword =request.getKeystorePass();
            char[] keystorePassArr = keyStorePassword.toCharArray();
            keystore.load(keystoreInputStream, keystorePassArr);
            keystoreInputStream.close();
            KeyStore.ProtectionParameter keyPass = new KeyStore.PasswordProtection(keystorePassArr);
            String alias = request.getKeyStoreAlias();
            KeyStore.PrivateKeyEntry privKeyEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(alias, keyPass);
            PrivateKey privateKey = privKeyEntry.getPrivateKey();
            X509Certificate cert = (X509Certificate) keystore.getCertificate(alias);
            Signature signature = Signature.getInstance(cert.getSigAlgName());
            signature.initSign(privateKey);
            StringBuilder saltBuilder = new StringBuilder();
            saltBuilder.append(request.getVendorCode());
            saltBuilder.append(request.getVendorKey());
            saltBuilder.append(request.getTransactionType());
            saltBuilder.append(request.getTransactionReference());
            String saltValue = saltBuilder.toString();
            signature.update(saltValue.getBytes());
            byte[] signedInfo = signature.sign();
            return  new ChecksumResponse(Base64.encodeBase64String(signedInfo));
        } catch (Exception f) {
            log.error("ERROR THROWN GENERATING CPG CHECKSUM FOR :{}, {}",request.getTransactionReference(), f.getMessage());
            return new ChecksumResponse("");
        }
    }
}

