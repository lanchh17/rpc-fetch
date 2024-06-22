package com.example.LC_receipt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.UUID;



@RestController
public class ReceiptController{

    //Hashmap containing id and number of points
    HashMap<String,Integer> receiptPoints = new HashMap<String,Integer>();
    
    @PostMapping("/receipts/process")
    public processResponse processReceipt(@RequestBody Receipt receipt){
        int points = 0;
        
        //One point for every alphanumeric character in the retailer name.
        for (int i =0;i<receipt.getRetailer().length();i++){
            char c = receipt.getRetailer().charAt(i);
            if (Character.isDigit(c) || Character.isLetter(c)){
                points +=1;
            }
        }
        //50 points if the total is a round dollar amount with no cents.
        if (receipt.getTotal()%1 ==0){
            points +=50;
        } 
        //25 points if the total is a multiple of 0.25.
        if ((receipt.getTotal())%0.25 == 0){
            points +=25;
        }
        int itemsLength = receipt.getItems().length;
        //5 points for every two items on the receipt.
        points += (itemsLength/2)*5;

        //If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned.
        for(int i = 0;i< receipt.getItems().length;i++){
            //trim the short description
            String shortDesc = receipt.getItems()[i].getShortDescription().trim();
            Float price = receipt.getItems()[i].getPrice();
            if (shortDesc.length()%3 ==0){
                points += Math.ceil(price*0.2);
            }
        }

        //6 points if the day in the purchase date is odd.
        String da = receipt.getPurchaseDate().substring(receipt.getPurchaseDate().length()-2);
        int day = Integer.parseInt(da);
        if (day%2==1){
            points +=6;
        }
        //10 points if the time of purchase is after 2:00pm and before 4:00pm.

        float time = Float.valueOf(receipt.getPurchaseTime().replace(":", "."));
        if ((time > 14.0) && (time < 16.0)){
            points +=10;
        }

        String uuid = UUID.randomUUID().toString();

        this.receiptPoints.put(uuid,points);
        
        return new processResponse(uuid);
    }

    @GetMapping("/receipts/{id}/points")
    public pointsResponse getPoints(@PathVariable(required=false,name="id") String id){
        if (id != "id"){
            if (!receiptPoints.containsKey(id)){
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
                );
            }
            else{
                int points = receiptPoints.get(id);
                return new pointsResponse(points);
            }
        }
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "entity not found"
        );
    }

}