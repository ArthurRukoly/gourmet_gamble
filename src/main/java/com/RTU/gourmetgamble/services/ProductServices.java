package com.RTU.gourmetgamble.services;

import com.RTU.gourmetgamble.models.Product;
import com.RTU.gourmetgamble.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductServices {

    private final ProductRepository productRepository;

    public List<Long> getProductsIds(String[] arrayWithProductNames){
        List<Long> productsIds = new ArrayList<>();
        for (String s : arrayWithProductNames) {
            if (s.length() > 1) {
                productsIds.add(productRepository.getIdFromName(removeBracketsAndQuotes(s)));
            }
        }
        return productsIds;
    }

    public String removeFirstAndLastChars(String input) {
        if (input.length() <= 4) {
            // Handle cases where the input has 0 to 3 characters
            return "";
        }
        return input.substring(2, input.length() - 2);
    }

    public String[] splitStringWithCommasAndPreserveQuotes(String input) {
        List<String> resultList = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|[^,]+").matcher(input);

        while (matcher.find()) {
            String match = matcher.group(1) != null ? matcher.group(1) : matcher.group();
            resultList.add(match);
        }

        return resultList.toArray(new String[0]);
    }


    public String removeBracketsAndQuotes(String input) {
        // Remove square brackets
        String withoutBrackets = input.replaceAll("\\[|\\]", "");

        // Remove single or double quotes
        String withoutQuotes = withoutBrackets.replaceAll("[\"']", "");

        return withoutQuotes;
    }
    public List<Product> getProductsFromIds(List<Long> ProductIds){
        List<Product> ingredients = new ArrayList<>();
        for (Long id : ProductIds) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
            ingredients.add(product);
        }
        return ingredients;
    }
    public List<String> transformJSarrayToJava(String selectedProductsJson) {
        // Decode the URL-encoded input string
        String decodedInput = null;
        try {
            decodedInput = URLDecoder.decode(selectedProductsJson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Check if the input starts with "selectedProducts" and contains a list
        if (decodedInput != null && decodedInput.startsWith("selectedProducts=")) {
            String jsonPart = decodedInput.substring("selectedProducts=".length());

            // Remove leading and trailing square brackets
            if (jsonPart.startsWith("[") && jsonPart.endsWith("]")) {
                jsonPart = jsonPart.substring(1, jsonPart.length() - 1);
            }

            // Split by commas and trim whitespace
            String[] parts = jsonPart.split(",");
            List<String> result = new ArrayList<>();

            for (String part : parts) {
                try {
                    String decodedPart = URLDecoder.decode(part.trim(), "UTF-8");
                    result.add(decodedPart);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        // If the input doesn't match the expected format, return an empty list
        return new ArrayList<>();
    }


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

//    public List<Product> getProductsByIds(List<Long> productIds) {
//        List<Product> products = productRepository.findAllById(productIds);
//        return products;
//    }

    public void saveProductListToDatabase(List<Product> list){
        for (Product product : list) {
            productRepository.save(product);
        }
    }

}
