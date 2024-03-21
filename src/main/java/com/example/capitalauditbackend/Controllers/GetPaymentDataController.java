package com.example.capitalauditbackend.Controllers;

import com.example.capitalauditbackend.DOA.DatabaseConnector;
import com.example.capitalauditbackend.Utilities.Authentication;
import com.example.capitalauditbackend.model.PaymentData;
import com.example.capitalauditbackend.model.user;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GetPaymentDataController {


    private final Gson gson = new Gson();
    @GetMapping("/getPaymentData")
    public ResponseEntity<String> login(@RequestHeader HttpHeaders headers) {
        return getPaymentDataHandler(headers);
    }

    private ResponseEntity<String> getPaymentDataHandler(HttpHeaders headers)
    {
        System.out.println("Executing getPaymentData Handler...");
        String token = headers.getFirst("access_token");
        System.out.println(token);
        Claims claim = Authentication.decodeToken(token);
        if(Authentication.tokenAuthenticator(claim))
        {
            DatabaseConnector db = DatabaseConnector.getInstance();
            int user_id = user.getUser_id();
            try
            {
                List<PaymentData> paymentDataList = db.getPaymentData(user_id);
                System.out.println("getPaymentData Handler success");
                return response(0, "Success.", paymentDataList);

            }
            catch (Exception e)
            {
                return response(1, "Error when trying to fetch data", null);

            }
        }
        else
        {
            System.out.println(Authentication.tokenAuthenticator(claim));
            return response(1, "Authentication failed. Try to sign in again.", null);
        }
    }

    private ResponseEntity<String> response(int result, String message, List<PaymentData> paymentDataList)
    {
        if (result == 0)
        {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            for (PaymentData paymentData : paymentDataList) {
                System.out.println(paymentData);
            }
            responseBody.put("paymentDataList", paymentDataList);
            return ResponseEntity.ok().body(gson.toJson(responseBody));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
    }

}
