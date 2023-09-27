package com.RTU.gourmetgamble.requests;

import com.RTU.gourmetgamble.models.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class ProductAPI {
    public List<Product> generateProductFromAPI(){
        List<Product> productList = new ArrayList<>();;
        try {
            // Define the API URL
            String apiUrl = "https://www.themealdb.com/api/json/v1/1/list.php?i=list";

            // Create an HTTP GET request to the API URL
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Check the response code
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read the JSON response
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON data
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract and print values
                JSONArray meals = jsonResponse.getJSONArray("meals");
                for (int i = 0; i < meals.length(); i++) {
                    JSONObject meal = meals.getJSONObject(i);

                    // Check if "idIngredient" is present and not null
                    if (!meal.isNull("idIngredient")) {
                        String idString = meal.getString("idIngredient");

                        try {
                            Long idLong = Long.parseLong(idString);
                            String name = meal.getString("strIngredient");
                            productList.add(new Product(idLong, name));
                            System.out.println("id: " + idLong);
                            System.out.println("Product name: " + name);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format for ingredient: " + idString);
                        }
                    } else {
                        System.out.println("Missing or null 'idIngredient' field in JSON.");
                    }

                    System.out.println(); // Add a separator between recipes
                }
            } else {
                System.out.println("HTTP GET request failed with response code: " + conn.getResponseCode());
            }

            // Close the connection
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    public void printProductSet(List<Product> list){
        for (Product p: list) {
            System.out.println("Id: " + p.getId());
            System.out.println("Name: " + p.getName());
        }
    }
    public static void main(String[] args) {
        ProductAPI p = new ProductAPI();
        p.printProductSet(p.generateProductFromAPI());
    }
}
