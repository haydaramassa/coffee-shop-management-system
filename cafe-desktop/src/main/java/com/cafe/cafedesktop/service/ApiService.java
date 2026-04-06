package com.cafe.cafedesktop.service;

import com.cafe.cafedesktop.MenuItemModel;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static String authUsername;
    private static String authPassword;

    public String login(String username, String password) throws IOException {
        URL url = new URL(BASE_URL + "/users/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
            {
              "username": "%s",
              "password": "%s"
            }
            """.formatted(escapeJson(username), escapeJson(password));

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            setAuthCredentials(username, password);
            return extractJsonValue(response, "role");
        } else if (statusCode == 401) {
            return "INVALID_CREDENTIALS";
        } else {
            return "LOGIN_FAILED";
        }
    }

    public String register(String username, String password, String securityQuestion, String securityAnswer) throws IOException {
        URL url = new URL(BASE_URL + "/users/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
            {
              "username": "%s",
              "password": "%s",
              "role": "CASHIER",
              "securityQuestion": "%s",
              "securityAnswer": "%s"
            }
            """.formatted(
                escapeJson(username),
                escapeJson(password),
                escapeJson(securityQuestion),
                escapeJson(securityAnswer)
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else if (statusCode == 409) {
            return "Username already exists";
        } else {
            return "Registration failed. Status code: " + statusCode;
        }
    }

    public static void setAuthCredentials(String username, String password) {
        authUsername = username;
        authPassword = password;
    }

    public static void clearAuthCredentials() {
        authUsername = null;
        authPassword = null;
    }

    private void applyAuth(HttpURLConnection connection) {
        if (authUsername == null || authPassword == null) {
            return;
        }

        String raw = authUsername + ":" + authPassword;
        String encoded = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + encoded);
    }

    public String getDashboardStats() throws IOException {
        URL url = new URL(BASE_URL + "/dashboard");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch dashboard. Status code: " + statusCode);
        }
    }

    public String getDashboardSalesChart() throws IOException {
        URL url = new URL(BASE_URL + "/dashboard/sales-chart");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch sales chart. Status code: " + statusCode);
        }
    }

    public String getDashboardOrdersChart() throws IOException {
        URL url = new URL(BASE_URL + "/dashboard/orders-chart");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch orders chart. Status code: " + statusCode);
        }
    }

    public String getAllProducts() throws IOException {
        URL url = new URL(BASE_URL + "/products");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch products. Status code: " + statusCode);
        }
    }

    public String getProductById(long id) throws IOException {
        URL url = new URL(BASE_URL + "/products/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch product. Status code: " + statusCode);
        }
    }

    public String addProduct(String name, String description, double price, int quantity,
                             long categoryId, String imageUrl, boolean active) throws IOException {
        URL url = new URL(BASE_URL + "/products");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
            {
              "name": "%s",
              "description": "%s",
              "price": %s,
              "quantity": %s,
              "imageUrl": "%s",
              "categoryId": %s,
              "active": %s
            }
            """.formatted(
                escapeJson(name),
                escapeJson(description),
                price,
                quantity,
                escapeJson(imageUrl == null ? "" : imageUrl),
                categoryId,
                active
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else if (statusCode == 404) {
            return "Category not found";
        } else {
            return "Add failed. Status code: " + statusCode;
        }
    }

    public String updateProduct(long id, String name, String description, double price, int quantity,
                                long categoryId, String imageUrl, boolean active) throws IOException {
        URL url = new URL(BASE_URL + "/products/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
            {
              "name": "%s",
              "description": "%s",
              "price": %s,
              "quantity": %s,
              "imageUrl": "%s",
              "categoryId": %s,
              "active": %s
            }
            """.formatted(
                escapeJson(name),
                escapeJson(description),
                price,
                quantity,
                escapeJson(imageUrl == null ? "" : imageUrl),
                categoryId,
                active
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else if (statusCode == 404) {
            return "Product or category not found";
        } else {
            return "Update failed. Status code: " + statusCode;
        }
    }

    public String deleteProduct(long id) throws IOException {
        URL url = new URL(BASE_URL + "/products/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else if (statusCode == 404) {
            return "Product not found";
        } else {
            return "Delete failed. Status code: " + statusCode;
        }
    }

    public String getAllCategories() throws IOException {
        URL url = new URL(BASE_URL + "/categories");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch categories. Status code: " + statusCode);
        }
    }

    public String uploadImage(File file) throws IOException {
        String boundary = "----Boundary" + System.currentTimeMillis();

        URL url = new URL(BASE_URL + "/uploads");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream output = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true)) {

            String fileName = file.getName();

            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileName).append("\"\r\n");
            writer.append("Content-Type: application/octet-stream\r\n\r\n");
            writer.flush();

            try (FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush();
            }

            writer.append("\r\n");
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.flush();
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            String response = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return extractJsonValue(response, "imageUrl");
        } else {
            throw new IOException("Image upload failed. Status code: " + statusCode);
        }
    }

    public Long getUserIdByUsername(String username) throws IOException {
        URL url = new URL(BASE_URL + "/users/by-username?username=" + encode(username));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            String json = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String value = extractJsonValue(json, "id");

            if (value == null || value.isBlank()) {
                throw new IOException("Could not extract user id from response: " + json);
            }

            return Long.parseLong(value);
        } else {
            throw new IOException("Failed to fetch user id. Status code: " + statusCode);
        }
    }

    public String createOrder(String username, Long customerId, ObservableList<MenuItemModel> cartItems) throws IOException {
        Long userId = getUserIdByUsername(username);

        StringBuilder itemsJson = new StringBuilder();

        for (int i = 0; i < cartItems.size(); i++) {
            MenuItemModel item = cartItems.get(i);

            itemsJson.append("""
            {
              "productId": %s,
              "quantity": %s
            }
            """.formatted(item.getProductId(), item.getQuantity()));

            if (i < cartItems.size() - 1) {
                itemsJson.append(",");
            }
        }

        String customerPart = customerId == null ? "\"customerId\": null," : "\"customerId\": " + customerId + ",";

        String jsonBody = """
        {
          %s
          "userId": %s,
          "items": [%s]
        }
        """.formatted(customerPart, userId, itemsJson);

        URL url = new URL(BASE_URL + "/orders");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            String errorBody;
            try {
                errorBody = new String(connection.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                errorBody = "";
            }
            throw new IOException("Order creation failed. Status code: " + statusCode + " " + errorBody);
        }
    }

    public Long extractOrderIdFromOrderResponse(String json) {
        String value = extractJsonValue(json, "id");
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.parseLong(value);
    }

    public String getReceiptByOrderId(Long orderId) throws IOException {
        URL url = new URL(BASE_URL + "/orders/" + orderId + "/receipt");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch receipt. Status code: " + statusCode);
        }
    }

    public String getAllOrders() throws IOException {
        URL url = new URL(BASE_URL + "/orders");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch orders. Status code: " + statusCode);
        }
    }

    public String getOrdersArchive() throws IOException {
        URL url = new URL(BASE_URL + "/orders/archive/all");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch orders archive. Status code: " + statusCode);
        }
    }

    public String getOrdersArchiveFiltered(String keyword, String fromDate, String toDate) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/orders/archive/all");

        boolean hasParam = false;

        if (keyword != null && !keyword.isBlank()) {
            urlBuilder.append(hasParam ? "&" : "?")
                    .append("keyword=")
                    .append(encode(keyword));
            hasParam = true;
        }

        if (fromDate != null && !fromDate.isBlank()) {
            urlBuilder.append(hasParam ? "&" : "?")
                    .append("fromDate=")
                    .append(encode(fromDate));
            hasParam = true;
        }

        if (toDate != null && !toDate.isBlank()) {
            urlBuilder.append(hasParam ? "&" : "?")
                    .append("toDate=")
                    .append(encode(toDate));
        }

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch filtered orders archive. Status code: " + statusCode);
        }
    }

    public String getAllCustomers() throws IOException {
        URL url = new URL(BASE_URL + "/customers");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch customers. Status code: " + statusCode);
        }
    }

    public String addCustomer(String name, String phone, String notes) throws IOException {
        URL url = new URL(BASE_URL + "/customers");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
        {
          "name": "%s",
          "phone": "%s",
          "notes": "%s"
        }
        """.formatted(
                escapeJson(name),
                escapeJson(phone == null ? "" : phone),
                escapeJson(notes == null ? "" : notes)
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else {
            return "Add customer failed. Status code: " + statusCode;
        }
    }

    public String updateCustomer(long id, String name, String phone, String notes) throws IOException {
        URL url = new URL(BASE_URL + "/customers/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
        {
          "name": "%s",
          "phone": "%s",
          "notes": "%s"
        }
        """.formatted(
                escapeJson(name),
                escapeJson(phone == null ? "" : phone),
                escapeJson(notes == null ? "" : notes)
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else {
            return "Update customer failed. Status code: " + statusCode;
        }
    }

    public String deleteCustomer(long id) throws IOException {
        URL url = new URL(BASE_URL + "/customers/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else if (statusCode == 404) {
            return "Customer not found";
        } else {
            return "Delete customer failed. Status code: " + statusCode;
        }
    }

    public String getCustomerOrders(long customerId) throws IOException {
        URL url = new URL(BASE_URL + "/customers/" + customerId + "/orders");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch customer orders. Status code: " + statusCode);
        }
    }

    public String resetPassword(String username, String securityQuestion, String securityAnswer, String newPassword) throws IOException {
        URL url = new URL(BASE_URL + "/users/forgot-password");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
            {
              "username": "%s",
              "securityQuestion": "%s",
              "securityAnswer": "%s",
              "newPassword": "%s"
            }
            """.formatted(
                escapeJson(username),
                escapeJson(securityQuestion),
                escapeJson(securityAnswer),
                escapeJson(newPassword)
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else if (statusCode == 404) {
            return "User not found";
        } else if (statusCode == 400) {
            return "Invalid reset data";
        } else {
            return "Reset password failed. Status code: " + statusCode;
        }
    }

    public String verifyResetData(String username, String securityQuestion, String securityAnswer) throws IOException {
        URL url = new URL(BASE_URL + "/users/verify-reset-data");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
            {
              "username": "%s",
              "securityQuestion": "%s",
              "securityAnswer": "%s"
            }
            """.formatted(
                escapeJson(username),
                escapeJson(securityQuestion),
                escapeJson(securityAnswer)
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else if (statusCode == 404) {
            return "User not found";
        } else if (statusCode == 400) {
            return "Invalid verification data";
        } else {
            return "Verification failed. Status code: " + statusCode;
        }
    }

    public String getAllUsers() throws IOException {
        URL url = new URL(BASE_URL + "/users");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Failed to fetch users. Status code: " + statusCode);
        }
    }

    public String adminCreateUser(String username, String password, String role,
                                  String securityQuestion, String securityAnswer) throws IOException {
        URL url = new URL(BASE_URL + "/users/admin-create");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
    {
      "username": "%s",
      "password": "%s",
      "role": "%s",
      "securityQuestion": "%s",
      "securityAnswer": "%s"
    }
    """.formatted(
                escapeJson(username),
                escapeJson(password),
                escapeJson(role),
                escapeJson(securityQuestion),
                escapeJson(securityAnswer)
        );

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        }

        String errorBody = "";
        try {
            if (connection.getErrorStream() != null) {
                errorBody = new String(connection.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {
        }

        String serverMessage = extractJsonValue(errorBody, "message");

        if (serverMessage != null && !serverMessage.isBlank()) {
            return serverMessage;
        }

        return "Create user failed. Status code: " + statusCode;
    }
    public String updateUserRole(long userId, String role) throws IOException {
        URL url = new URL(BASE_URL + "/users/" + userId + "/role");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
        {
          "role": "%s"
        }
        """.formatted(escapeJson(role));

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else {
            return "Update role failed. Status code: " + statusCode;
        }
    }

    public String updateUserEnabled(long userId, boolean enabled) throws IOException {
        URL url = new URL(BASE_URL + "/users/" + userId + "/enabled");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
        {
          "enabled": %s
        }
        """.formatted(enabled);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        } else {
            return "Update user status failed. Status code: " + statusCode;
        }
    }

    public String createCategory(String name, String description) throws IOException {
        URL url = new URL(BASE_URL + "/categories");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
        {
          "name": "%s",
          "description": "%s"
        }
        """.formatted(
                escapeJson(name),
                escapeJson(description == null ? "" : description)
        );

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        }

        String serverMessage = readServerMessage(connection);
        if (serverMessage != null && !serverMessage.isBlank()) {
            return serverMessage;
        }

        return "Create category failed. Status code: " + statusCode;
    }

    public String updateCategory(long id, String name, String description) throws IOException {
        URL url = new URL(BASE_URL + "/categories/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = """
        {
          "name": "%s",
          "description": "%s"
        }
        """.formatted(
                escapeJson(name),
                escapeJson(description == null ? "" : description)
        );

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        }

        String serverMessage = readServerMessage(connection);
        if (serverMessage != null && !serverMessage.isBlank()) {
            return serverMessage;
        }

        return "Update category failed. Status code: " + statusCode;
    }

    public String deleteCategory(long id) throws IOException {
        URL url = new URL(BASE_URL + "/categories/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        applyAuth(connection);

        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();

        if (statusCode >= 200 && statusCode < 300) {
            return "SUCCESS";
        }

        String serverMessage = readServerMessage(connection);
        if (serverMessage != null && !serverMessage.isBlank()) {
            return serverMessage;
        }

        return "Delete category failed. Status code: " + statusCode;
    }

    private String readServerMessage(HttpURLConnection connection) {
        try {
            if (connection.getErrorStream() == null) {
                return "";
            }

            String errorBody = new String(connection.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);

            String message = extractJsonValue(errorBody, "message");
            if (message != null && !message.isBlank()) {
                return message;
            }

            return errorBody;
        } catch (Exception e) {
            return "";
        }
    }

    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);

        if (start == -1) {
            return "";
        }

        start += pattern.length();

        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        if (start >= json.length()) {
            return "";
        }

        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf('"', start);
            return end == -1 ? "" : json.substring(start, end);
        } else {
            int end = start;
            while (end < json.length() && ",}".indexOf(json.charAt(end)) == -1) {
                end++;
            }
            return json.substring(start, end).trim();
        }
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}