package com.cafe.cafedesktop;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.function.BiConsumer;

public class ProductCardController {

    @FXML
    private ImageView product_imageView;

    @FXML
    private Label product_name;

    @FXML
    private Label product_type;

    @FXML
    private Label product_price;

    @FXML
    private Label product_stock;

    @FXML
    private Button product_addBtn;

    @FXML
    private Spinner<Integer> product_spinner;

    private ProductModel product;
    private BiConsumer<ProductModel, Integer> onAddCallback;

    public void setData(ProductModel product, BiConsumer<ProductModel, Integer> onAddCallback) {
        this.product = product;
        this.onAddCallback = onAddCallback;

        product_name.setText(product.getName());
        product_type.setText(product.getType() == null || product.getType().isBlank() ? "Uncategorized" : product.getType());
        product_price.setText(product.getPrice() + " $");
        product_stock.setText("Stock: " + (product.getStock() == null ? 0 : product.getStock()));

        int maxQty = product.getStock() != null && product.getStock() > 0 ? product.getStock() : 1;
        product_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxQty, 1));

        try {
            if (product.getImageUrl() != null && !product.getImageUrl().isBlank()) {
                product_imageView.setImage(new Image("http://localhost:8080" + product.getImageUrl(), true));
            } else {
                product_imageView.setImage(null);
            }
        } catch (Exception e) {
            product_imageView.setImage(null);
        }
    }

    @FXML
    public void addBtn() {
        Integer qty = product_spinner.getValue();
        if (qty == null) {
            qty = 1;
        }

        if (onAddCallback != null && product != null) {
            onAddCallback.accept(product, qty);
        }
    }
}