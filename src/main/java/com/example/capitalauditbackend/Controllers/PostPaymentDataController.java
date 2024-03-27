package com.example.capitalauditbackend.Controllers;

import com.example.capitalauditbackend.DOA.DatabaseConnector;
import com.example.capitalauditbackend.Utilities.Authentication;
import com.example.capitalauditbackend.model.user;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
@RestController
public class PostPaymentDataController {
    private final Gson gson = new Gson();

    @PostMapping("/postPaymentData")
    public ResponseEntity<String> postPayment(@RequestHeader HttpHeaders headers, @RequestBody String jsonString)
    {
        return postPaymentHandler(jsonString, headers);
    }
    private ResponseEntity<String> postPaymentHandler(String jsonString, HttpHeaders headers)
    {

        System.out.println("postPaymentHandler executed...");
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String token = headers.getFirst("access_token");
        Claims claim = Authentication.decodeToken(token);
        System.out.println(jsonObject.toString());
        if(Authentication.tokenAuthenticator(claim))
        {
            DatabaseConnector db = new DatabaseConnector();
        int price = jsonObject.get("price").getAsInt();
            String category = jsonObject.get("category").getAsString();
            boolean debit_credit = jsonObject.get("debitCredit").getAsBoolean();
            boolean cleared = jsonObject.get("cleared").getAsBoolean();
            String date = jsonObject.get("date").getAsString();
            if(date == null)
            {
                response(1, "Corrupted/Incorrect data. Try again.");
            }
            else
            {
                int user_id = user.getUser_id();
                db.PostPaymentData(price, category, debit_credit, cleared, date, user_id);
                System.out.println("postPaymentHandler executed...");
                return response(0, "Success.");
            }
        }
        else
        {
            return response(1, "Authentication failed. Try to sign in again.");
        }
        return response(1, "Error with backend");
    }

    private static ResponseEntity<String> response(int result, String message)
    {
        if (result == 0)
        {
            JsonObject successResponse = new JsonObject();
            successResponse.addProperty("success", true);
            successResponse.addProperty("message", "Payment successful.");
            return ResponseEntity.ok().body(successResponse.toString());

        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
    }
}
