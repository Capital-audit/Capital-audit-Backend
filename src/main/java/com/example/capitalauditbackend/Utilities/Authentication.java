package com.example.capitalauditbackend.Utilities;
import com.example.capitalauditbackend.DOA.DatabaseConnector;
import com.example.capitalauditbackend.model.user;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class Authentication {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000); // Token expiry time (1 week)

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims decodeToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            //log parsing error
            return null;
        }
    }

    public static boolean tokenAuthenticator(Claims claim)
    {
        if (claim == null)
        {
            return false;
        }
        String username = claim.getSubject();
        System.out.println("Failed at date");
        if(checkExpired(claim))
        {
            DatabaseConnector db = DatabaseConnector.getInstance();
            System.out.println("Failed at db");
            System.out.println(username);
            if(db == null)
            {
                return false;
            }
            if(db.checkUsername(username))
             {
                 user.setUsername(username);
                 int id = db.getUserId(username);
                 System.out.println("Failed at doing smth");
                 if(id != 0){
                    user.setUser_id(id);
                 }
                 else
                 {
                     return false;
                 }

                 return true;
             }
        }
        else
        {
            return false;
        }
        return false;
    }


    public static boolean checkExpired(Claims claim)
    {
        Date expirationDate = claim.getExpiration();
        Date currentDate = new Date();
        return expirationDate.after(currentDate);
    }
}
