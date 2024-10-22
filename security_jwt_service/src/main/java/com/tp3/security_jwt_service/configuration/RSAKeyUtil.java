package com.tp3.security_jwt_service.configuration;

import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
        import java.security.interfaces.RSAPublicKey;
        import java.security.spec.X509EncodedKeySpec;
        import java.util.Base64;

public class RSAKeyUtil {

    public static RSAPublicKey getPublicKey(Resource resource) throws Exception {
        byte[] keyBytes = Files.readAllBytes(resource.getFile().toPath());
        String publicKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

        // Utiliser un KeyFactory pour obtenir RSAPublicKey
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
