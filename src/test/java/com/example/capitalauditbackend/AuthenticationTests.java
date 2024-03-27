package com.example.capitalauditbackend;

import com.example.capitalauditbackend.Utilities.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationTests {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private List<String> tokenList;
    private static final String[] usernames = {"Alice", "Bob", "Charlie", "David", "Eva", "Frank", "Grace", "Henry", "Ivy", "Jack"};
    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        tokenList = new ArrayList<>();
        int n = 10;
        for (int i = 0; i < n; i++) {
            String token = Authentication.generateToken(usernames[i]);
            tokenList.add(token);
        }
    }

    @Test
    void generateTokenTest()
    {
        assertNotNull(tokenList);
    }

    @Test
    void decodeTokenTest()
    {
        assertNotNull(tokenList);
        for (String token : tokenList) {
            Claims claims = Authentication.decodeToken(token);
            assertNotNull(claims);
        }
    }

    @Test
    void checkExpiredtest()
    {
        int n = 10;
        for (int i = 0; i < n; i++)
        {
            Claims claims = new DefaultClaims();
            claims.setIssuedAt(new Date()); // Set issued date
            claims.setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)); // Set expiration date

            boolean isExpired = Authentication.checkExpired(claims);
            assertTrue(isExpired);

            Claims claim = new DefaultClaims();
            claim.setIssuedAt(new Date()); // Set issued date
            claim.setExpiration(new Date(System.currentTimeMillis() - 9000 / 100));
            boolean isNotExpired = Authentication.checkExpired(claim);
            assertFalse(isNotExpired);
        }
    }

}
