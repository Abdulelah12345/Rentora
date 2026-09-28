package com.example.rentora.Service;

import com.example.rentora.Model.Product;
import com.example.rentora.Model.User;
import com.example.rentora.Repository.ProductRepository;
import com.example.rentora.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;




    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean addProduct(Integer userId, Product product) {

        User user = userRepository.findUserById(userId);
        if (user == null) {
            return false;
        }


        product.setOwnerId(userId);
        productRepository.save(product);
        return true;
    }

    public boolean updateProduct(Integer id, Product product) {
        Product oldProduct = productRepository.findProductById(id);
        if (oldProduct == null) {
            return false;
        }

        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setPricePerDay(product.getPricePerDay());
        oldProduct.setDeposit(product.getDeposit());
        oldProduct.setAvailable(product.getAvailable());

        productRepository.save(oldProduct);
        return true;
    }

    public boolean deleteProduct(Integer id) {
        Product oldProduct = productRepository.findProductById(id);
        if (oldProduct == null) {
            return false;
        }

        productRepository.delete(oldProduct);
        return true;
    }
    public List<Product> searchProductsByName(String name) {
        if (name == null) {
            return null;
        }
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Double calculateEstimatedPrice(Integer productId, int days) {

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return null;
        }

        if (days <= 0) {
            return -1.0;
        }


        double total = (days * product.getPricePerDay()) + product.getDeposit();

        return total;
    }


    public int changeProductAvailability(Integer productId, Integer ownerId) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return 1;
        }

        if (!product.getOwnerId().equals(ownerId)) {
            return 2;
        }


        if (product.getAvailable().equals(true)) {
            product.setAvailable(false);
        } else {
            product.setAvailable(true);
        }

        productRepository.save(product);
        return 0;
    }


    public int changeProductPrice(Integer productId, Integer ownerId, double newPrice) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return 1;
        }


        if (!product.getOwnerId().equals(ownerId)) {
            return 2;
        }


        if (newPrice <= 0) {
            return 3;
        }

        product.setPricePerDay(newPrice);
        productRepository.save(product);
        return 0;
    }


// I have to change openAI to get something better
@Value("${gemini.api.key}")
private String geminiApiKey;

    public String getAiPriceSuggestion(String productName, Double originalPrice) {


        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent?key=" + geminiApiKey.trim();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "اقترح سعر تأجير يومي مناسب بالريال السعودي لمنتج إسمه: " + productName +
                " (سعره الأصلي عند الشراء: " + originalPrice + " ريال). " +
                "اعطني الإجابة في سطرين فقط: السعر اليومي الموصى به، ونصيحة تسويقية بسيطة للمالك.";

        String jsonBody = "{"
                + "\"contents\": [{"
                + "  \"parts\": [{\"text\": \"" + prompt + "\"}]"
                + "}]"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String responseText = response.getBody();

            if (responseText != null && responseText.contains("\"text\": \"")) {
                int startIndex = responseText.indexOf("\"text\": \"") + 9;

                int endIndex = responseText.indexOf("thoughtSignature", startIndex);

                if (endIndex != -1) {
                    endIndex = responseText.lastIndexOf("\"", endIndex - 1);
                } else {
                    endIndex = responseText.indexOf("\"\n", startIndex);
                    if (endIndex == -1) {
                        endIndex = responseText.indexOf("\"", startIndex);
                    }
                }

                String aiAnswer = responseText.substring(startIndex, endIndex);
                return aiAnswer.replace("\\n", "\n").replace("\\\"", "\"");
            }

            return "تعذر استخراج اقتراح السعر حالياً.";

        } catch (Exception e) {

            return "سعر التأجير المقترح تقريبياً: " + (originalPrice * 0.02) + "Gemini API Error: " + e.getMessage() + " ريال/يوم.";
        }
    }


}
