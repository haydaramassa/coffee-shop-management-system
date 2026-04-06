package com.cafe.cafedesktop;

import com.cafe.cafedesktop.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {

    private final ApiService apiService = new ApiService();

    private String loggedInUsername;
    private String loggedInRole = "ADMIN";
    private String selectedImagePath = "";
    private double paidAmount = 0.0;
    private Long lastCreatedOrderId = null;
    private double lastPaidAmount = 0.0;
    private double lastChangeAmount = 0.0;

    private final ObservableList<MenuItemModel> menuCartList = FXCollections.observableArrayList();
    private ReceiptArchiveModel selectedReceiptArchive;

    private CustomerOrderModel selectedCustomerOrder;
    private final ObservableList<CustomerModel> customersList = FXCollections.observableArrayList();
    private CustomerModel selectedCustomerModel;
    private final ObservableList<ProductModel> inventoryMasterList = FXCollections.observableArrayList();
    private final ObservableList<CustomerModel> customersMasterList = FXCollections.observableArrayList();

    private UserModel selectedUserModel;
    private final ObservableList<UserModel> usersMasterList = FXCollections.observableArrayList();

    private final String[] questionList = {
            "What`s your favourite color?",
            "What`s your favourite food?",
            "When`s your birthday?"
    };
    private CategoryAdminModel selectedCategoryAdminModel;
    private final ObservableList<CategoryAdminModel> categoriesMasterList = FXCollections.observableArrayList();

    @FXML
    private AnchorPane main_form;
    @FXML
    private AnchorPane side_form;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;
    @FXML
    private Button inventory_btn;
    @FXML
    private Button menu_btn;
    @FXML
    private Button customers_btn;
    @FXML
    private Button users_btn;

    @FXML
    private AnchorPane dashboard_form;
    @FXML
    private AnchorPane inventory_form;
    @FXML
    private AnchorPane menu_form;
    @FXML
    private AnchorPane customers_form;
    @FXML
    private AnchorPane users_form;

    @FXML
    private Label dashboard_NC;
    @FXML
    private Label dashboard_TI;
    @FXML
    private Label dashboard_TotalI;
    @FXML
    private Label dashboard_NSP;

    @FXML
    private AreaChart<String, Number> dashboard_IncomeChart;
    @FXML
    private BarChart<String, Number> dashboard_CustomerChart;

    @FXML
    private Button reset_btn;
    @FXML
    private Button archive_Btn;

    @FXML
    private TableView<ProductModel> inventory_tableView;
    @FXML
    private TableColumn<ProductModel, Long> inventory_col_productID;
    @FXML
    private TableColumn<ProductModel, String> inventory_col_productName;
    @FXML
    private TableColumn<ProductModel, String> inventory_col_type;
    @FXML
    private TableColumn<ProductModel, Integer> inventory_col_stock;
    @FXML
    private TableColumn<ProductModel, Double> inventory_col_price;
    @FXML
    private TableColumn<ProductModel, String> inventory_col_status;
    @FXML
    private TableColumn<ProductModel, String> inventory_col_date;

    @FXML
    private TextField inventory_productID;
    @FXML
    private TextField inventory_productName;
    @FXML
    private TextField inventory_stock;
    @FXML
    private TextField inventory_price;

    @FXML
    private ComboBox<CategoryModel> inventory_type;
    @FXML
    private ComboBox<String> inventory_status;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;
    @FXML
    private Button inventory_addBtn;
    @FXML
    private Button inventory_updateBtn;
    @FXML
    private Button inventory_clearBtn;
    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private ScrollPane menu_scrollPane;
    @FXML
    private FlowPane menu_gridPane;

    @FXML
    private TableView<MenuItemModel> menu_tableView;
    @FXML
    private TableColumn<MenuItemModel, String> menu_col_productName;
    @FXML
    private TableColumn<MenuItemModel, Integer> menu_col_quantity;
    @FXML
    private TableColumn<MenuItemModel, Double> menu_col_price;

    @FXML
    private Label menu_total;
    @FXML
    private TextField menu_amount;
    @FXML
    private Label menu_change;

    @FXML
    private Button menu_payBtn;
    @FXML
    private Button menu_removeBtn;
    @FXML
    private Button menu_receiptBtn;

    @FXML
    private AnchorPane customers_content;
    @FXML
    private AnchorPane Archive_Of_Receipts_Form;

    @FXML
    private TableView<CustomerModel> customers_tableView;
    @FXML
    private TableColumn<CustomerModel, Long> customers_col_customerID;
    @FXML
    private TableColumn<CustomerModel, String> customers_col_total;
    @FXML
    private TableColumn<CustomerModel, String> customers_col_date;
    @FXML
    private TableColumn<CustomerModel, String> customers_col_cashier;

    @FXML
    private TableView<ReceiptArchiveModel> tblReceipts;
    @FXML
    private TableColumn<ReceiptArchiveModel, Long> colReceiptId;
    @FXML
    private TableColumn<ReceiptArchiveModel, String> colCustomerId;
    @FXML
    private TableColumn<ReceiptArchiveModel, String> colReceiptDate;
    @FXML
    private TableColumn<ReceiptArchiveModel, Double> colReceiptTotal;
    @FXML
    private TableColumn<ReceiptArchiveModel, String> colReceiptCashier;

    @FXML
    private TableView<ReceiptItemArchiveModel> tblReceiptItems;
    @FXML
    private TableColumn<ReceiptItemArchiveModel, String> colItemCustomerId;
    @FXML
    private TableColumn<ReceiptItemArchiveModel, String> colItemType;
    @FXML
    private TableColumn<ReceiptItemArchiveModel, Integer> colItemQty;
    @FXML
    private TableColumn<ReceiptItemArchiveModel, Double> colItemPrice;

    @FXML
    private TextField customer_name;
    @FXML
    private TextField customer_phone;
    @FXML
    private TextField customer_notes;

    @FXML
    private Button customer_addBtn;
    @FXML
    private Button customer_updateBtn;
    @FXML
    private Button customer_clearBtn;
    @FXML
    private Button customer_deleteBtn;

    @FXML
    private Button archive_printDetailsBtn;
    @FXML
    private Button archive_printAllReceiptsBtn;
    @FXML
    private Label details_cashierLabel;

    @FXML
    private ComboBox<CustomerModel> menu_customerComboBox;

    @FXML
    private TableView<CustomerOrderModel> customer_orders_table;
    @FXML
    private TableColumn<CustomerOrderModel, Long> customer_orders_col_id;
    @FXML
    private TableColumn<CustomerOrderModel, Double> customer_orders_col_total;
    @FXML
    private TableColumn<CustomerOrderModel, String> customer_orders_col_cashier;
    @FXML
    private TableColumn<CustomerOrderModel, String> customer_orders_col_date;
    @FXML
    private Label customer_orders_summary;
    @FXML
    private Button customer_openReceiptBtn;

    @FXML
    private TableView<CategoryAdminModel> categories_tableView;
    @FXML
    private TableColumn<CategoryAdminModel, Long> categories_col_id;
    @FXML
    private TableColumn<CategoryAdminModel, String> categories_col_name;
    @FXML
    private TableColumn<CategoryAdminModel, String> categories_col_description;
    @FXML
    private TableColumn<CategoryAdminModel, String> categories_col_createdAt;

    @FXML
    private TextField category_id;
    @FXML
    private TextField category_name;
    @FXML
    private TextField category_description;
    @FXML
    private TextField category_search;

    @FXML
    private Button category_addBtn;
    @FXML
    private Button category_updateBtn;
    @FXML
    private Button category_deleteBtn;
    @FXML
    private Button category_clearBtn;

    @FXML
    private TextField inventory_search;
    @FXML
    private TextField customer_search;

    @FXML
    private TextField archive_search;
    @FXML
    private TextField archive_fromDate;
    @FXML
    private TextField archive_toDate;
    @FXML
    private Button archive_filterBtn;
    @FXML
    private Button archive_clearFilterBtn;

    @FXML
    private TableView<UserModel> users_tableView;
    @FXML
    private TableColumn<UserModel, Long> users_col_id;
    @FXML
    private TableColumn<UserModel, String> users_col_username;
    @FXML
    private TableColumn<UserModel, String> users_col_role;
    @FXML
    private TableColumn<UserModel, String> users_col_status;
    @FXML
    private TableColumn<UserModel, String> users_col_createdAt;

    @FXML
    private TextField user_username;
    @FXML
    private PasswordField user_password;
    @FXML
    private ComboBox<String> user_role;
    @FXML
    private ComboBox<String> user_question;
    @FXML
    private TextField user_answer;
    @FXML
    private TextField user_search;

    @FXML
    private Button user_addBtn;
    @FXML
    private Button user_roleBtn;
    @FXML
    private Button user_toggleBtn;
    @FXML
    private Button user_clearBtn;

    @FXML
    public void initialize() {
        setupInventoryTable();
        setupMenuTable();
        setupReceiptArchiveTable();
        setupReceiptItemsTable();
        setupCustomersTable();
        setupCustomerOrdersTable();
        setupUsersTable();
        setupCategoriesTable();

        if (menu_tableView != null) {
            menu_tableView.setOnMouseClicked(event -> menuSelectOrder());
        }

        if (tblReceipts != null) {
            tblReceipts.setOnMouseClicked(event -> archiveSelectReceipt());
        }

        if (menu_scrollPane != null) {
            menu_scrollPane.setFitToWidth(true);
            menu_scrollPane.setPannable(true);
        }

        if (inventory_status != null) {
            inventory_status.setDisable(true);
        }

        if (inventory_productID != null) {
            inventory_productID.setEditable(false);
        }

        if (inventory_stock != null) {
            inventory_stock.textProperty().addListener((obs, oldVal, newVal) -> updateStatusFromStock());
        }

        if (inventory_search != null) {
            inventory_search.textProperty().addListener((obs, oldVal, newVal) -> filterInventoryTable());
        }

        if (customer_search != null) {
            customer_search.textProperty().addListener((obs, oldVal, newVal) -> filterCustomersTable());
        }

        if (user_search != null) {
            user_search.textProperty().addListener((obs, oldVal, newVal) -> filterUsersTable());
        }

        if (category_search != null) {
            category_search.textProperty().addListener((obs, oldVal, newVal) -> filterCategoriesTable());
        }

        if (category_id != null) {
            category_id.setEditable(false);
        }

        loadUserRoleList();
        loadUserQuestionList();
        loadCustomersComboBox();
        loadMenuCards();
        loadCustomersTableData();

        showForm("menu");
    }

    public void setUserSession(String loggedInUsername, String role) {
        this.loggedInUsername = loggedInUsername;
        this.loggedInRole = normalizeRole(role);

        if (username != null) {
            username.setText(loggedInUsername);
        }

        applyRolePermissions();

        if (isAdmin()) {
            loadAdminOnlyData();
            showForm("dashboard");
        } else {
            showForm("menu");
        }
    }

    public void setUsername(String loggedInUsername) {
        setUserSession(loggedInUsername, "ADMIN");
    }

    public void setDashboardData(long users, long categories, long products, long customers, long orders, String sales) {
        if (dashboard_NC != null) dashboard_NC.setText(String.valueOf(customers));
        if (dashboard_TI != null) dashboard_TI.setText(sales + "$");
        if (dashboard_TotalI != null) dashboard_TotalI.setText(sales + "$");
        if (dashboard_NSP != null) dashboard_NSP.setText(String.valueOf(orders));
    }

    @FXML
    public void switchForm(ActionEvent event) {
        Object source = event.getSource();

        if (source == dashboard_btn) {
            if (!requireAdminAccess("Dashboard")) return;
            showForm("dashboard");
        } else if (source == inventory_btn) {
            if (!requireAdminAccess("Inventory")) return;
            showForm("inventory");
        } else if (source == menu_btn) {
            showForm("menu");
        } else if (source == customers_btn) {
            showForm("customers");
        } else if (source == users_btn) {
            if (!requireAdminAccess("Users Management")) return;
            showForm("users");
        }
    }

    private void showForm(String formName) {
        if (!isAdmin() && ("dashboard".equals(formName) || "inventory".equals(formName) || "users".equals(formName))) {
            formName = "menu";
        }

        if (dashboard_form != null) dashboard_form.setVisible(false);
        if (inventory_form != null) inventory_form.setVisible(false);
        if (menu_form != null) menu_form.setVisible(false);
        if (customers_form != null) customers_form.setVisible(false);
        if (users_form != null) users_form.setVisible(false);

        switch (formName) {
            case "dashboard" -> {
                if (dashboard_form != null) dashboard_form.setVisible(true);
            }
            case "inventory" -> {
                if (inventory_form != null) inventory_form.setVisible(true);
            }
            case "menu" -> {
                if (menu_form != null) menu_form.setVisible(true);
            }
            case "customers" -> {
                if (customers_form != null) customers_form.setVisible(true);
                if (customers_content != null) customers_content.setVisible(true);
                if (Archive_Of_Receipts_Form != null) Archive_Of_Receipts_Form.setVisible(false);
            }
            case "users" -> {
                if (users_form != null) users_form.setVisible(true);
            }
        }
    }

    @FXML
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            Stage stage = (Stage) main_form.getScene().getWindow();

            ApiService.clearAuthCredentials();

            stage.setMaximized(false);
            stage.setMinWidth(900);
            stage.setMinHeight(600);

            App.setRoot("/fxml/login.fxml", 900, 600, "Cafe Desktop");

            stage.setWidth(900);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.setResizable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "ADMIN";
        }
        return role.trim().toUpperCase();
    }

    private boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(loggedInRole);
    }

    private boolean requireAdminAccess(String featureName) {
        if (isAdmin()) {
            return true;
        }

        showInfo("Access denied. " + featureName + " is available for admin only.");
        return false;
    }

    private void applyRolePermissions() {
        boolean admin = isAdmin();

        if (dashboard_btn != null) {
            dashboard_btn.setVisible(admin);
            dashboard_btn.setManaged(admin);
            dashboard_btn.setDisable(!admin);
        }

        if (inventory_btn != null) {
            inventory_btn.setVisible(admin);
            inventory_btn.setManaged(admin);
            inventory_btn.setDisable(!admin);
        }

        if (users_btn != null) {
            users_btn.setVisible(admin);
            users_btn.setManaged(admin);
            users_btn.setDisable(!admin);
        }

        if (reset_btn != null) {
            reset_btn.setVisible(admin);
            reset_btn.setManaged(admin);
        }

        if (archive_Btn != null) {
            archive_Btn.setVisible(admin);
            archive_Btn.setManaged(admin);
        }

        if (!admin) {
            if (dashboard_form != null) dashboard_form.setVisible(false);
            if (inventory_form != null) inventory_form.setVisible(false);
            if (users_form != null) users_form.setVisible(false);

            if (menu_btn != null) menu_btn.setLayoutY(178.0);
            if (customers_btn != null) customers_btn.setLayoutY(227.0);
        } else {
            if (menu_btn != null) menu_btn.setLayoutY(276.0);
            if (customers_btn != null) customers_btn.setLayoutY(325.0);
        }
    }

    private void loadAdminOnlyData() {
        loadInventoryStatusList();
        loadInventoryTypeList();
        loadInventoryData();
        loadReceiptArchiveData();
        loadDashboardData();
        loadIncomeChart();
        loadOrdersChart();
        loadUsersData();
        loadCategoriesData();
    }

    private void setupInventoryTable() {
        if (inventory_col_productID != null) inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (inventory_col_productName != null) inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (inventory_col_type != null) inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        if (inventory_col_stock != null) inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        if (inventory_col_price != null) inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        if (inventory_col_status != null) inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        if (inventory_col_date != null) inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void setupMenuTable() {
        if (menu_col_productName != null) menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        if (menu_col_quantity != null) menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        if (menu_col_price != null) menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        if (menu_tableView != null) menu_tableView.setItems(menuCartList);
    }

    private void setupReceiptArchiveTable() {
        if (colReceiptId != null) colReceiptId.setCellValueFactory(new PropertyValueFactory<>("receiptId"));
        if (colCustomerId != null) colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        if (colReceiptDate != null) colReceiptDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        if (colReceiptTotal != null) colReceiptTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        if (colReceiptCashier != null) colReceiptCashier.setCellValueFactory(new PropertyValueFactory<>("cashier"));
    }

    private void setupReceiptItemsTable() {
        if (colItemCustomerId != null) colItemCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        if (colItemType != null) colItemType.setCellValueFactory(new PropertyValueFactory<>("type"));
        if (colItemQty != null) colItemQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        if (colItemPrice != null) colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void setupCustomersTable() {
        if (customers_col_customerID != null) customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (customers_col_total != null) customers_col_total.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (customers_col_date != null) customers_col_date.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        if (customers_col_cashier != null) customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    private void setupCustomerOrdersTable() {
        if (customer_orders_col_id != null) customer_orders_col_id.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        if (customer_orders_col_total != null) customer_orders_col_total.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        if (customer_orders_col_cashier != null) customer_orders_col_cashier.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        if (customer_orders_col_date != null) customer_orders_col_date.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        if (customer_orders_table != null) {
            customer_orders_table.setOnMouseClicked(event ->
                    selectedCustomerOrder = customer_orders_table.getSelectionModel().getSelectedItem());
        }
    }

    private void setupUsersTable() {
        if (users_col_id != null) users_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (users_col_username != null) users_col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        if (users_col_role != null) users_col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        if (users_col_status != null) users_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        if (users_col_createdAt != null) users_col_createdAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    private void loadInventoryStatusList() {
        if (inventory_status != null) {
            inventory_status.setItems(FXCollections.observableArrayList("Available", "Inactive"));
        }
    }

    private void loadInventoryTypeList() {
        try {
            String json = apiService.getAllCategories();
            ObservableList<CategoryModel> categories = FXCollections.observableArrayList();

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                if (inventory_type != null) inventory_type.setItems(categories);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long id = Long.parseLong(extractValue(item, "id"));
                String name = extractStringValue(item, "name");

                categories.add(new CategoryModel(id, name));
            }

            if (inventory_type != null) inventory_type.setItems(categories);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInventoryData() {
        try {
            String json = apiService.getAllProducts();
            ObservableList<ProductModel> list = FXCollections.observableArrayList();

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                inventoryMasterList.clear();
                if (inventory_tableView != null) inventory_tableView.setItems(list);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long id = Long.parseLong(extractValue(item, "id"));
                String name = extractStringValue(item, "name");
                String type = extractStringValue(item, "categoryName");
                Integer stock = Integer.parseInt(extractValue(item, "quantity"));
                Double price = Double.parseDouble(extractValue(item, "price"));
                Boolean active = Boolean.parseBoolean(extractValue(item, "active"));
                String date = extractStringValue(item, "createdAt");
                String imageUrl = extractStringValue(item, "imageUrl");

                list.add(new ProductModel(
                        id,
                        name,
                        type,
                        stock,
                        price,
                        active ? "Available" : "Inactive",
                        date,
                        imageUrl
                ));
            }

            inventoryMasterList.setAll(list);
            if (inventory_tableView != null) {
                inventory_tableView.setItems(FXCollections.observableArrayList(inventoryMasterList));
            }
            filterInventoryTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<ProductModel> getAvailableProducts() {
        ObservableList<ProductModel> list = FXCollections.observableArrayList();

        try {
            String json = apiService.getAllProducts();
            String trimmed = json.trim();

            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                return list;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long id = Long.parseLong(extractValue(item, "id"));
                String name = extractStringValue(item, "name");
                String type = extractStringValue(item, "categoryName");
                Integer stock = Integer.parseInt(extractValue(item, "quantity"));
                Double price = Double.parseDouble(extractValue(item, "price"));
                Boolean active = Boolean.parseBoolean(extractValue(item, "active"));
                String date = extractStringValue(item, "createdAt");
                String imageUrl = extractStringValue(item, "imageUrl");

                if (active && stock > 0) {
                    list.add(new ProductModel(
                            id,
                            name,
                            type,
                            stock,
                            price,
                            "Available",
                            date,
                            imageUrl
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void loadMenuCards() {
        try {
            if (menu_gridPane == null) return;

            menu_gridPane.getChildren().clear();
            menu_gridPane.setHgap(15);
            menu_gridPane.setVgap(15);

            ObservableList<ProductModel> products = getAvailableProducts();

            for (ProductModel product : products) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/product-card.fxml"));
                Node card = loader.load();

                ProductCardController controller = loader.getController();
                controller.setData(product, this::addProductToCart);

                menu_gridPane.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserRoleList() {
        if (user_role != null) {
            user_role.setItems(FXCollections.observableArrayList("ADMIN", "CASHIER"));
        }
    }

    private void loadUserQuestionList() {
        if (user_question != null) {
            List<String> listQ = new ArrayList<>();
            for (String q : questionList) {
                listQ.add(q);
            }
            user_question.setItems(FXCollections.observableArrayList(listQ));
        }
    }

    private void loadUsersData() {
        try {
            String json = apiService.getAllUsers();
            ObservableList<UserModel> list = FXCollections.observableArrayList();

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                usersMasterList.clear();
                if (users_tableView != null) users_tableView.setItems(list);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long id = Long.parseLong(extractValue(item, "id"));
                String usernameValue = extractStringValue(item, "username");
                String role = extractStringValue(item, "role");
                boolean enabled = Boolean.parseBoolean(extractValue(item, "enabled"));
                String createdAt = extractStringValue(item, "createdAt");

                list.add(new UserModel(
                        id,
                        usernameValue,
                        role,
                        enabled ? "Enabled" : "Disabled",
                        createdAt,
                        enabled
                ));
            }

            usersMasterList.setAll(list);
            if (users_tableView != null) {
                users_tableView.setItems(FXCollections.observableArrayList(usersMasterList));
            }
            filterUsersTable();

        } catch (Exception e) {
            showInfo("Could not load users: " + e.getMessage());
        }
    }

    private void updateStatusFromStock() {
        if (inventory_stock == null || inventory_status == null) return;

        String stockText = inventory_stock.getText();

        if (stockText == null || stockText.isBlank()) {
            inventory_status.setValue("Inactive");
            return;
        }

        try {
            int stock = Integer.parseInt(stockText.trim());
            inventory_status.setValue(stock > 0 ? "Available" : "Inactive");
        } catch (NumberFormatException e) {
            inventory_status.setValue("Inactive");
        }
    }

    @FXML
    public void inventorySelectData(MouseEvent event) {
        if (inventory_tableView == null) return;

        ProductModel selected = inventory_tableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (inventory_productID != null) inventory_productID.setText(String.valueOf(selected.getId()));
        if (inventory_productName != null) inventory_productName.setText(selected.getName());
        if (inventory_stock != null) inventory_stock.setText(String.valueOf(selected.getStock()));
        if (inventory_price != null) inventory_price.setText(String.valueOf(selected.getPrice()));

        if (inventory_type != null) {
            for (CategoryModel category : inventory_type.getItems()) {
                if (category.getName().equalsIgnoreCase(selected.getType())) {
                    inventory_type.setValue(category);
                    break;
                }
            }
        }

        if (inventory_status != null) inventory_status.setValue(selected.getStatus());
        selectedImagePath = selected.getImageUrl() == null ? "" : selected.getImageUrl();

        try {
            if (inventory_imageView != null) {
                if (!selectedImagePath.isBlank()) {
                    inventory_imageView.setImage(new Image("http://localhost:8080" + selectedImagePath, true));
                } else {
                    inventory_imageView.setImage(null);
                }
            }
        } catch (Exception e) {
            if (inventory_imageView != null) inventory_imageView.setImage(null);
        }
    }

    @FXML
    public void inventoryImportBtn() {
        if (!requireAdminAccess("Inventory import")) return;

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Product Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(inventory_importBtn.getScene().getWindow());
            if (selectedFile == null) return;

            String uploadedImageUrl = apiService.uploadImage(selectedFile);
            selectedImagePath = uploadedImageUrl;

            String fullImageUrl = "http://localhost:8080" + uploadedImageUrl;
            if (inventory_imageView != null) {
                inventory_imageView.setImage(new Image(fullImageUrl, true));
            }

            showInfo("Image uploaded successfully");
        } catch (Exception e) {
            showInfo("Could not upload image: " + e.getMessage());
        }
    }

    @FXML
    public void inventoryAddBtn() {
        if (!requireAdminAccess("Inventory add")) return;

        if (inventory_productName.getText().isBlank()
                || inventory_stock.getText().isBlank()
                || inventory_price.getText().isBlank()
                || inventory_type.getValue() == null) {
            showInfo("Please fill all required fields");
            return;
        }

        if (!isValidStock()) return;
        if (!confirmAction("Confirm Add", "Do you want to add this product?")) return;

        try {
            CategoryModel selectedCategory = inventory_type.getValue();
            boolean isActive = "Available".equals(inventory_status.getValue());

            String result = apiService.addProduct(
                    inventory_productName.getText(),
                    inventory_productName.getText(),
                    Double.parseDouble(inventory_price.getText()),
                    Integer.parseInt(inventory_stock.getText()),
                    selectedCategory.getId(),
                    selectedImagePath,
                    isActive
            );

            if ("SUCCESS".equals(result)) {
                showInfo("Product added successfully");
                clearInventoryFields();
                loadInventoryData();
                loadMenuCards();
                loadDashboardData();
                loadIncomeChart();
                loadOrdersChart();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not add product: " + e.getMessage());
        }
    }

    @FXML
    public void inventoryUpdateBtn() {
        if (!requireAdminAccess("Inventory update")) return;

        if (inventory_tableView == null) return;
        ProductModel selected = inventory_tableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showInfo("Please select a product first");
            return;
        }

        if (inventory_productName.getText().isBlank()
                || inventory_stock.getText().isBlank()
                || inventory_price.getText().isBlank()
                || inventory_type.getValue() == null) {
            showInfo("Please fill all required fields");
            return;
        }

        if (!isValidStock()) return;
        if (!confirmAction("Confirm Update", "Do you want to save these changes?")) return;

        try {
            CategoryModel selectedCategory = inventory_type.getValue();
            boolean isActive = "Available".equals(inventory_status.getValue());

            String result = apiService.updateProduct(
                    selected.getId(),
                    inventory_productName.getText(),
                    inventory_productName.getText(),
                    Double.parseDouble(inventory_price.getText()),
                    Integer.parseInt(inventory_stock.getText()),
                    selectedCategory.getId(),
                    selectedImagePath,
                    isActive
            );

            if ("SUCCESS".equals(result)) {
                showInfo("Product updated successfully");
                clearInventoryFields();
                loadInventoryData();
                loadMenuCards();
                loadDashboardData();
                loadIncomeChart();
                loadOrdersChart();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not update product: " + e.getMessage());
        }
    }

    @FXML
    public void inventoryDeleteBtn() {
        if (!requireAdminAccess("Inventory delete")) return;

        if (inventory_tableView == null) return;
        ProductModel selected = inventory_tableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showInfo("Please select a product first");
            return;
        }

        if (!confirmAction("Confirm Delete", "Are you sure you want to delete this product?")) return;

        try {
            String result = apiService.deleteProduct(selected.getId());

            if ("SUCCESS".equals(result)) {
                showInfo("Product deleted successfully");
                clearInventoryFields();
                loadInventoryData();
                loadMenuCards();
                loadDashboardData();
                loadIncomeChart();
                loadOrdersChart();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not delete product: " + e.getMessage());
        }
    }

    @FXML
    public void inventoryClearBtn() {
        clearInventoryFields();
    }

    private void clearInventoryFields() {
        if (inventory_productID != null) inventory_productID.clear();
        if (inventory_productName != null) inventory_productName.clear();
        if (inventory_stock != null) inventory_stock.clear();
        if (inventory_price != null) inventory_price.clear();

        selectedImagePath = "";

        if (inventory_type != null) inventory_type.getSelectionModel().clearSelection();
        if (inventory_status != null) inventory_status.setValue("Inactive");
        if (inventory_imageView != null) inventory_imageView.setImage(null);
    }

    private boolean isValidStock() {
        try {
            int stock = Integer.parseInt(inventory_stock.getText().trim());
            if (stock < 0) {
                showInfo("Stock cannot be negative");
                return false;
            }
            return true;
        } catch (Exception e) {
            showInfo("Stock must be a valid number");
            return false;
        }
    }

    @FXML
    public void menuSelectOrder() {
        if (menu_tableView != null) {
            menu_tableView.getSelectionModel().getSelectedItem();
        }
    }

    private void addProductToCart(ProductModel product, int qty) {
        if (qty <= 0) {
            showInfo("Quantity must be greater than zero");
            return;
        }

        Optional<MenuItemModel> existingItem = menuCartList.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            MenuItemModel item = existingItem.get();
            int newQty = item.getQuantity() + qty;

            if (newQty > product.getStock()) {
                showInfo("Requested quantity exceeds available stock");
                return;
            }

            item.setQuantity(newQty);
            item.setPrice(product.getPrice() * newQty);
            if (menu_tableView != null) menu_tableView.refresh();
        } else {
            if (qty > product.getStock()) {
                showInfo("Requested quantity exceeds available stock");
                return;
            }

            menuCartList.add(new MenuItemModel(
                    product.getId(),
                    product.getName(),
                    qty,
                    product.getPrice() * qty
            ));
        }

        updateMenuTotal();
    }

    @FXML
    public void menuAmount() {
        if (menu_amount == null || menu_amount.getText() == null || menu_amount.getText().isBlank()) {
            return;
        }

        try {
            double enteredAmount = Double.parseDouble(menu_amount.getText().trim());
            paidAmount += enteredAmount;

            double total = getMenuTotalValue();
            double change = paidAmount - total;

            if (menu_change != null) menu_change.setText(String.format("%.2f$", change));
            menu_amount.clear();

        } catch (Exception e) {
            showInfo("Please enter a valid amount");
            menu_amount.clear();
        }
    }

    @FXML
    public void menuRemoveBtn() {
        if (menu_tableView == null) return;

        MenuItemModel selected = menu_tableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showInfo("Please select an item from the order table first");
            return;
        }

        if (!confirmAction("Confirm Remove", "Do you want to remove this item from the order?")) return;

        menuCartList.remove(selected);
        menu_tableView.refresh();
        updateMenuTotal();
        updateMenuChange();
    }

    @FXML
    public void menuPayBtn() {
        if (menuCartList.isEmpty()) {
            showInfo("There are no items in the order");
            return;
        }

        if (!isAmountEnough()) return;
        if (!validateCartAgainstLatestStock()) return;
        if (!confirmAction("Confirm Payment", "Do you want to complete this order?")) return;

        try {
            CustomerModel selectedCustomer = menu_customerComboBox != null ? menu_customerComboBox.getValue() : null;
            Long customerId = selectedCustomer != null ? selectedCustomer.getId() : null;

            double totalValue = getMenuTotalValue();
            double changeValue = paidAmount - totalValue;

            String result = apiService.createOrder(loggedInUsername, customerId, menuCartList);
            lastCreatedOrderId = apiService.extractOrderIdFromOrderResponse(result);

            lastPaidAmount = paidAmount;
            lastChangeAmount = changeValue;

            showInfo("Order created successfully");
            clearMenuOrder();
            loadMenuCards();

            if (isAdmin()) {
                loadInventoryData();
                loadReceiptArchiveData();
                loadDashboardData();
                loadIncomeChart();
                loadOrdersChart();
            }

        } catch (Exception e) {
            showInfo("Could not complete order: " + e.getMessage());
        }
    }

    @FXML
    public void menuReceiptBtn() {
        if (lastCreatedOrderId == null) {
            showInfo("No receipt available yet. Please complete a payment first.");
            return;
        }

        try {
            String receiptJson = apiService.getReceiptByOrderId(lastCreatedOrderId);

            String orderId = extractValue(receiptJson, "orderId");
            String customerName = extractStringValue(receiptJson, "customerName");
            String cashierName = extractStringValue(receiptJson, "cashierName");
            String createdAt = extractStringValue(receiptJson, "createdAt");
            String totalAmount = extractValue(receiptJson, "totalAmount") + "$";

            String dateOnly = createdAt;
            String timeOnly = "-";

            if (createdAt != null && createdAt.contains("T")) {
                String[] parts = createdAt.split("T");
                if (parts.length == 2) {
                    dateOnly = parts[0];
                    timeOnly = parts[1];
                }
            }

            String paidText = String.format("%.2f$", lastPaidAmount);
            String changeText = String.format("%.2f$", lastChangeAmount);

            ObservableList<ReceiptItemModel> items = FXCollections.observableArrayList();

            String itemsPart = extractArray(receiptJson, "items");
            if (itemsPart != null && !itemsPart.isBlank()) {
                String[] objects = itemsPart.split("\\},\\s*\\{");

                for (String obj : objects) {
                    String item = obj.trim();
                    if (!item.startsWith("{")) item = "{" + item;
                    if (!item.endsWith("}")) item = item + "}";

                    String productName = extractStringValue(item, "productName");
                    Integer quantity = Integer.parseInt(extractValue(item, "quantity"));
                    String unitPrice = extractValue(item, "unitPrice") + "$";
                    String lineTotal = extractValue(item, "lineTotal") + "$";

                    items.add(new ReceiptItemModel(productName, quantity, unitPrice, lineTotal));
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/receipt-view.fxml"));
            AnchorPane root = loader.load();

            ReceiptController controller = loader.getController();
            controller.loadReceiptData(
                    orderId,
                    customerName == null || customerName.isBlank() ? "Walk-in Customer" : customerName,
                    cashierName,
                    dateOnly,
                    timeOnly,
                    totalAmount,
                    paidText,
                    changeText,
                    items
            );

            Stage stage = new Stage();
            stage.setTitle("Receipt");
            stage.setScene(new Scene(root, 580, 500));
            stage.setMinWidth(580);
            stage.setMinHeight(500);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            showInfo("Could not load receipt: " + e.getMessage());
        }
    }

    private double getMenuTotalValue() {
        return menuCartList.stream()
                .mapToDouble(MenuItemModel::getPrice)
                .sum();
    }

    private void updateMenuTotal() {
        double total = getMenuTotalValue();
        if (menu_total != null) menu_total.setText(String.format("%.2f$", total));
        updateMenuChange();
    }

    private void updateMenuChange() {
        double total = getMenuTotalValue();
        double change = paidAmount - total;
        if (menu_change != null) menu_change.setText(String.format("%.2f$", change));
    }

    private boolean isAmountEnough() {
        double total = getMenuTotalValue();

        if (paidAmount < total) {
            showInfo("The paid amount is less than the total");
            return false;
        }

        return true;
    }

    private void clearMenuOrder() {
        menuCartList.clear();
        if (menu_tableView != null) menu_tableView.refresh();
        if (menu_total != null) menu_total.setText("0.0$");
        if (menu_amount != null) menu_amount.clear();
        if (menu_change != null) menu_change.setText("0.0$");
        paidAmount = 0.0;

        if (menu_customerComboBox != null && !menu_customerComboBox.getItems().isEmpty()) {
            menu_customerComboBox.setValue(menu_customerComboBox.getItems().get(0));
        }
    }

    private boolean validateCartAgainstLatestStock() {
        try {
            for (MenuItemModel item : menuCartList) {
                String productJson = apiService.getProductById(item.getProductId());
                int latestStock = Integer.parseInt(extractValue(productJson, "quantity"));

                if (item.getQuantity() > latestStock) {
                    showInfo("Product '" + item.getProductName() + "' no longer has enough stock. Available now: " + latestStock);
                    if (isAdmin()) {
                        loadInventoryData();
                    }
                    loadMenuCards();
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            showInfo("Could not validate stock before payment: " + e.getMessage());
            return false;
        }
    }

    @FXML
    public void monthlyReset() {
        if (!requireAdminAccess("Monthly reset")) return;
    }

    @FXML
    public void openArchiveForm() {
        if (!requireAdminAccess("Archive")) return;

        if (customers_form != null) {
            customers_form.setVisible(true);

            if (customers_content != null) {
                customers_content.setVisible(false);
            }

            if (Archive_Of_Receipts_Form != null) {
                Archive_Of_Receipts_Form.setVisible(true);
            }

            if (archive_search != null) archive_search.clear();
            if (archive_fromDate != null) archive_fromDate.clear();
            if (archive_toDate != null) archive_toDate.clear();

            loadReceiptArchiveData();
            if (tblReceiptItems != null) tblReceiptItems.getItems().clear();
            if (details_cashierLabel != null) details_cashierLabel.setText("Cashier : ");
            selectedReceiptArchive = null;
        }
    }

    @FXML
    public void archivePrintDetails() {
        if (!requireAdminAccess("Archive details")) return;

        if (selectedReceiptArchive == null) {
            showInfo("Please select a receipt first");
            return;
        }

        lastCreatedOrderId = selectedReceiptArchive.getReceiptId();
        menuReceiptBtn();
    }

    @FXML
    public void archivePrintAllReceipts() {
        if (!requireAdminAccess("Archive print")) return;
    }

    @FXML
    public void archiveSelectReceipt() {
        if (tblReceipts == null) return;

        selectedReceiptArchive = tblReceipts.getSelectionModel().getSelectedItem();

        if (selectedReceiptArchive == null) return;

        try {
            String receiptJson = apiService.getReceiptByOrderId(selectedReceiptArchive.getReceiptId());

            String cashierName = extractStringValue(receiptJson, "cashierName");
            if (details_cashierLabel != null) details_cashierLabel.setText("Cashier : " + cashierName);

            ObservableList<ReceiptItemArchiveModel> items = FXCollections.observableArrayList();

            String customerName = extractStringValue(receiptJson, "customerName");
            if (customerName == null || customerName.isBlank()) {
                customerName = "Walk-in Customer";
            }

            String itemsPart = extractArray(receiptJson, "items");
            if (itemsPart != null && !itemsPart.isBlank()) {
                String[] objects = itemsPart.split("\\},\\s*\\{");

                for (String obj : objects) {
                    String item = obj.trim();
                    if (!item.startsWith("{")) item = "{" + item;
                    if (!item.endsWith("}")) item = item + "}";

                    String productName = extractStringValue(item, "productName");
                    Integer qty = Integer.parseInt(extractValue(item, "quantity"));
                    Double price = Double.parseDouble(extractValue(item, "lineTotal"));

                    items.add(new ReceiptItemArchiveModel(customerName, productName, qty, price));
                }
            }

            if (tblReceiptItems != null) tblReceiptItems.setItems(items);

        } catch (Exception e) {
            showInfo("Could not load receipt details: " + e.getMessage());
        }
    }

    private boolean confirmAction(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait()
                .filter(buttonType -> buttonType == ButtonType.OK)
                .isPresent();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return "0";

        start += pattern.length();

        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        int end = start;
        boolean inQuotes = start < json.length() && json.charAt(start) == '"';

        if (inQuotes) {
            start++;
            end = json.indexOf('"', start);
            return end == -1 ? "" : json.substring(start, end);
        } else {
            while (end < json.length() && ",}".indexOf(json.charAt(end)) == -1) {
                end++;
            }
            return json.substring(start, end).trim();
        }
    }

    private String extractStringValue(String json, String key) {
        return extractValue(json, key);
    }

    private String extractArray(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);

        if (start == -1) return "";

        start += pattern.length();

        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        if (start >= json.length() || json.charAt(start) != '[') {
            return "";
        }

        start++;
        int bracketCount = 1;
        int end = start;

        while (end < json.length() && bracketCount > 0) {
            char c = json.charAt(end);
            if (c == '[') bracketCount++;
            if (c == ']') bracketCount--;
            end++;
        }

        return json.substring(start, end - 1).trim();
    }

    private void loadReceiptArchiveData() {
        try {
            String json = apiService.getOrdersArchive();
            ObservableList<ReceiptArchiveModel> list = FXCollections.observableArrayList();

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                if (tblReceipts != null) tblReceipts.setItems(list);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long receiptId = Long.parseLong(extractValue(item, "orderId"));
                String customerName = extractStringValue(item, "customerName");
                String date = extractStringValue(item, "createdAt");
                Double total = Double.parseDouble(extractValue(item, "totalAmount"));
                String cashier = extractStringValue(item, "cashierName");

                list.add(new ReceiptArchiveModel(receiptId, customerName, date, total, cashier));
            }

            if (tblReceipts != null) tblReceipts.setItems(list);

        } catch (Exception e) {
            showInfo("Could not load receipts archive: " + e.getMessage());
        }
    }

    private void loadDashboardData() {
        try {
            String json = apiService.getDashboardStats();

            long totalUsers = Long.parseLong(extractValue(json, "totalUsers"));
            long totalCategories = Long.parseLong(extractValue(json, "totalCategories"));
            long totalProducts = Long.parseLong(extractValue(json, "totalProducts"));
            long totalCustomers = Long.parseLong(extractValue(json, "totalCustomers"));
            long totalOrders = Long.parseLong(extractValue(json, "totalOrders"));
            String totalSales = extractValue(json, "totalSales");

            setDashboardData(totalUsers, totalCategories, totalProducts, totalCustomers, totalOrders, totalSales);

        } catch (Exception e) {
            showInfo("Could not load dashboard data: " + e.getMessage());
        }
    }

    private void loadIncomeChart() {
        try {
            if (dashboard_IncomeChart == null) return;

            String json = apiService.getDashboardSalesChart();

            dashboard_IncomeChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Income");

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                dashboard_IncomeChart.getData().add(series);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                String label = extractStringValue(item, "label");
                double value = Double.parseDouble(extractValue(item, "value"));

                series.getData().add(new XYChart.Data<>(label, value));
            }

            dashboard_IncomeChart.getData().add(series);

        } catch (Exception e) {
            showInfo("Could not load income chart: " + e.getMessage());
        }
    }

    private void loadOrdersChart() {
        try {
            if (dashboard_CustomerChart == null) return;

            String json = apiService.getDashboardOrdersChart();

            dashboard_CustomerChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Orders");

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                dashboard_CustomerChart.getData().add(series);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                String label = extractStringValue(item, "label");
                double value = Double.parseDouble(extractValue(item, "value"));

                series.getData().add(new XYChart.Data<>(label, value));
            }

            dashboard_CustomerChart.getData().add(series);

        } catch (Exception e) {
            showInfo("Could not load orders chart: " + e.getMessage());
        }
    }

    private void loadCustomersComboBox() {
        try {
            customersList.clear();

            String json = apiService.getAllCustomers();
            String trimmed = json.trim();

            CustomerModel walkIn = new CustomerModel(null, "Walk-in Customer", "", "", "");
            customersList.add(walkIn);

            if (trimmed.length() >= 2 && !trimmed.equals("[]")) {
                String content = trimmed.substring(1, trimmed.length() - 1);
                String[] objects = content.split("\\},\\s*\\{");

                for (String obj : objects) {
                    String item = obj.trim();
                    if (!item.startsWith("{")) item = "{" + item;
                    if (!item.endsWith("}")) item = item + "}";

                    Long id = Long.parseLong(extractValue(item, "id"));
                    String name = extractStringValue(item, "name");
                    String phone = extractStringValue(item, "phone");
                    String notes = extractStringValue(item, "notes");
                    String createdAt = extractStringValue(item, "createdAt");

                    customersList.add(new CustomerModel(id, name, phone, notes, createdAt));
                }
            }

            if (menu_customerComboBox != null) {
                menu_customerComboBox.setItems(customersList);
                menu_customerComboBox.setValue(walkIn);
            }

        } catch (Exception e) {
            showInfo("Could not load customers list: " + e.getMessage());
        }
    }

    private void loadCustomersTableData() {
        try {
            ObservableList<CustomerModel> tableList = FXCollections.observableArrayList();

            String json = apiService.getAllCustomers();
            String trimmed = json.trim();

            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                customersMasterList.clear();
                if (customers_tableView != null) customers_tableView.setItems(tableList);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long id = Long.parseLong(extractValue(item, "id"));
                String name = extractStringValue(item, "name");
                String phone = extractStringValue(item, "phone");
                String notes = extractStringValue(item, "notes");
                String createdAt = extractStringValue(item, "createdAt");

                tableList.add(new CustomerModel(id, name, phone, notes, createdAt));
            }

            customersMasterList.setAll(tableList);
            if (customers_tableView != null) {
                customers_tableView.setItems(FXCollections.observableArrayList(customersMasterList));
            }
            filterCustomersTable();

        } catch (Exception e) {
            showInfo("Could not load customers table: " + e.getMessage());
        }
    }

    @FXML
    public void customerSelectData(MouseEvent event) {
        if (customers_tableView == null) return;

        selectedCustomerModel = customers_tableView.getSelectionModel().getSelectedItem();

        if (selectedCustomerModel == null) return;

        if (customer_name != null) customer_name.setText(selectedCustomerModel.getName());
        if (customer_phone != null) customer_phone.setText(selectedCustomerModel.getPhone());
        if (customer_notes != null) customer_notes.setText(selectedCustomerModel.getNotes());

        loadCustomerOrders(selectedCustomerModel.getId());
    }

    @FXML
    public void customerAddBtn() {
        if (customer_name.getText().isBlank()) {
            showInfo("Customer name is required");
            return;
        }

        if (!confirmAction("Confirm Add", "Do you want to add this customer?")) return;

        try {
            String result = apiService.addCustomer(
                    customer_name.getText(),
                    customer_phone.getText(),
                    customer_notes.getText()
            );

            if ("SUCCESS".equals(result)) {
                showInfo("Customer added successfully");
                customerClearBtn();
                loadCustomersTableData();
                loadCustomersComboBox();
                if (isAdmin()) {
                    loadDashboardData();
                }
            } else {
                showInfo(result);
            }

        } catch (Exception e) {
            showInfo("Could not add customer: " + e.getMessage());
        }
    }

    @FXML
    public void customerUpdateBtn() {
        if (selectedCustomerModel == null) {
            showInfo("Please select a customer first");
            return;
        }

        if (customer_name.getText().isBlank()) {
            showInfo("Customer name is required");
            return;
        }

        if (!confirmAction("Confirm Update", "Do you want to update this customer?")) return;

        try {
            String result = apiService.updateCustomer(
                    selectedCustomerModel.getId(),
                    customer_name.getText(),
                    customer_phone.getText(),
                    customer_notes.getText()
            );

            if ("SUCCESS".equals(result)) {
                showInfo("Customer updated successfully");
                customerClearBtn();
                loadCustomersTableData();
                loadCustomersComboBox();
            } else {
                showInfo(result);
            }

        } catch (Exception e) {
            showInfo("Could not update customer: " + e.getMessage());
        }
    }

    @FXML
    public void customerDeleteBtn() {
        if (selectedCustomerModel == null) {
            showInfo("Please select a customer first");
            return;
        }

        if (!confirmAction("Confirm Delete", "Are you sure you want to delete this customer?")) return;

        try {
            String result = apiService.deleteCustomer(selectedCustomerModel.getId());

            if ("SUCCESS".equals(result)) {
                showInfo("Customer deleted successfully");
                customerClearBtn();
                loadCustomersTableData();
                loadCustomersComboBox();
                if (isAdmin()) {
                    loadDashboardData();
                }
            } else {
                showInfo(result);
            }

        } catch (Exception e) {
            showInfo("Could not delete customer: " + e.getMessage());
        }
    }

    @FXML
    public void customerClearBtn() {
        if (customer_name != null) customer_name.clear();
        if (customer_phone != null) customer_phone.clear();
        if (customer_notes != null) customer_notes.clear();
        selectedCustomerModel = null;
        selectedCustomerOrder = null;
        if (customers_tableView != null) customers_tableView.getSelectionModel().clearSelection();
        if (customer_orders_table != null) customer_orders_table.getItems().clear();
        if (customer_orders_summary != null) {
            customer_orders_summary.setText("Orders: 0 | Total Spent: 0.0$ | Last Order: -");
        }
    }

    private void loadCustomerOrders(Long customerId) {
        try {
            ObservableList<CustomerOrderModel> list = FXCollections.observableArrayList();

            if (customerId == null) {
                if (customer_orders_table != null) customer_orders_table.setItems(list);
                if (customer_orders_summary != null) customer_orders_summary.setText("Orders: 0 | Total Spent: 0.0$ | Last Order: -");
                return;
            }

            String json = apiService.getCustomerOrders(customerId);
            String trimmed = json.trim();

            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                if (customer_orders_table != null) customer_orders_table.setItems(list);
                if (customer_orders_summary != null) customer_orders_summary.setText("Orders: 0 | Total Spent: 0.0$ | Last Order: -");
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            double totalSpent = 0.0;
            String lastOrderDate = "-";

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long orderId = Long.parseLong(extractValue(item, "orderId"));
                Double totalAmount = Double.parseDouble(extractValue(item, "totalAmount"));
                String cashierName = extractStringValue(item, "cashierName");
                String createdAt = extractStringValue(item, "createdAt");

                list.add(new CustomerOrderModel(orderId, totalAmount, cashierName, createdAt));

                totalSpent += totalAmount;
                lastOrderDate = createdAt;
            }

            if (customer_orders_table != null) customer_orders_table.setItems(list);
            if (customer_orders_summary != null) {
                customer_orders_summary.setText(
                        "Orders: " + list.size() +
                                " | Total Spent: " + String.format("%.2f$", totalSpent) +
                                " | Last Order: " + lastOrderDate
                );
            }

        } catch (Exception e) {
            showInfo("Could not load customer orders: " + e.getMessage());
        }
    }

    @FXML
    public void customerOpenSelectedReceipt() {
        if (selectedCustomerOrder == null) {
            showInfo("Please select an order first");
            return;
        }

        lastCreatedOrderId = selectedCustomerOrder.getOrderId();
        menuReceiptBtn();
    }

    @FXML
    public void inventorySearchChanged() {
        filterInventoryTable();
    }

    @FXML
    public void customerSearchChanged() {
        filterCustomersTable();
    }

    @FXML
    public void userSearchChanged() {
        filterUsersTable();
    }

    private void filterInventoryTable() {
        if (inventory_tableView == null) return;

        String searchText = inventory_search == null ? "" : inventory_search.getText();

        if (searchText == null || searchText.isBlank()) {
            inventory_tableView.setItems(FXCollections.observableArrayList(inventoryMasterList));
            return;
        }

        String keyword = searchText.trim().toLowerCase();
        ObservableList<ProductModel> filteredList = FXCollections.observableArrayList();

        for (ProductModel product : inventoryMasterList) {
            String name = product.getName() == null ? "" : product.getName().toLowerCase();
            String type = product.getType() == null ? "" : product.getType().toLowerCase();
            String status = product.getStatus() == null ? "" : product.getStatus().toLowerCase();

            if (name.contains(keyword) || type.contains(keyword) || status.contains(keyword)) {
                filteredList.add(product);
            }
        }

        inventory_tableView.setItems(filteredList);
    }

    private void filterCustomersTable() {
        if (customers_tableView == null) return;

        String searchText = customer_search == null ? "" : customer_search.getText();

        if (searchText == null || searchText.isBlank()) {
            customers_tableView.setItems(FXCollections.observableArrayList(customersMasterList));
            return;
        }

        String keyword = searchText.trim().toLowerCase();
        ObservableList<CustomerModel> filteredList = FXCollections.observableArrayList();

        for (CustomerModel customer : customersMasterList) {
            String name = customer.getName() == null ? "" : customer.getName().toLowerCase();
            String phone = customer.getPhone() == null ? "" : customer.getPhone().toLowerCase();
            String notes = customer.getNotes() == null ? "" : customer.getNotes().toLowerCase();

            if (name.contains(keyword) || phone.contains(keyword) || notes.contains(keyword)) {
                filteredList.add(customer);
            }
        }

        customers_tableView.setItems(filteredList);
    }

    private void filterUsersTable() {
        if (users_tableView == null) return;

        String searchText = user_search == null ? "" : user_search.getText();

        if (searchText == null || searchText.isBlank()) {
            users_tableView.setItems(FXCollections.observableArrayList(usersMasterList));
            return;
        }

        String keyword = searchText.trim().toLowerCase();
        ObservableList<UserModel> filteredList = FXCollections.observableArrayList();

        for (UserModel user : usersMasterList) {
            String usernameValue = user.getUsername() == null ? "" : user.getUsername().toLowerCase();
            String role = user.getRole() == null ? "" : user.getRole().toLowerCase();
            String status = user.getStatus() == null ? "" : user.getStatus().toLowerCase();

            if (usernameValue.contains(keyword) || role.contains(keyword) || status.contains(keyword)) {
                filteredList.add(user);
            }
        }

        users_tableView.setItems(filteredList);
    }

    @FXML
    public void archiveFilterBtn() {
        if (!requireAdminAccess("Archive filter")) return;

        try {
            String keyword = archive_search != null ? archive_search.getText().trim() : "";
            String fromDate = archive_fromDate != null ? archive_fromDate.getText().trim() : "";
            String toDate = archive_toDate != null ? archive_toDate.getText().trim() : "";

            loadReceiptArchiveDataFiltered(keyword, fromDate, toDate);

            if (tblReceiptItems != null) tblReceiptItems.getItems().clear();
            if (details_cashierLabel != null) details_cashierLabel.setText("Cashier : ");
            selectedReceiptArchive = null;

        } catch (Exception e) {
            showInfo("Could not filter archive: " + e.getMessage());
        }
    }

    @FXML
    public void archiveClearFilterBtn() {
        if (!requireAdminAccess("Archive clear filter")) return;

        if (archive_search != null) archive_search.clear();
        if (archive_fromDate != null) archive_fromDate.clear();
        if (archive_toDate != null) archive_toDate.clear();

        loadReceiptArchiveData();

        if (tblReceiptItems != null) tblReceiptItems.getItems().clear();
        if (details_cashierLabel != null) details_cashierLabel.setText("Cashier : ");
        selectedReceiptArchive = null;
    }

    private void loadReceiptArchiveDataFiltered(String keyword, String fromDate, String toDate) {
        try {
            String json = apiService.getOrdersArchiveFiltered(keyword, fromDate, toDate);
            ObservableList<ReceiptArchiveModel> list = FXCollections.observableArrayList();

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                if (tblReceipts != null) tblReceipts.setItems(list);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long receiptId = Long.parseLong(extractValue(item, "orderId"));
                String customerName = extractStringValue(item, "customerName");
                String date = extractStringValue(item, "createdAt");
                Double total = Double.parseDouble(extractValue(item, "totalAmount"));
                String cashier = extractStringValue(item, "cashierName");

                list.add(new ReceiptArchiveModel(
                        receiptId,
                        customerName,
                        date,
                        total,
                        cashier
                ));
            }

            if (tblReceipts != null) tblReceipts.setItems(list);

        } catch (Exception e) {
            showInfo("Could not load filtered archive: " + e.getMessage());
        }
    }

    @FXML
    public void userSelectData(MouseEvent event) {
        if (users_tableView == null) return;

        selectedUserModel = users_tableView.getSelectionModel().getSelectedItem();
        if (selectedUserModel == null) return;

        if (user_username != null) user_username.setText(selectedUserModel.getUsername());
        if (user_password != null) user_password.clear();
        if (user_role != null) user_role.setValue(selectedUserModel.getRole());
        if (user_question != null) user_question.getSelectionModel().clearSelection();
        if (user_answer != null) user_answer.clear();
    }

    @FXML
    public void userAddBtn() {
        if (!requireAdminAccess("Create user")) return;

        if (user_username == null || user_password == null || user_role == null || user_question == null || user_answer == null) {
            return;
        }

        if (user_username.getText().isBlank()
                || user_password.getText().isBlank()
                || user_role.getValue() == null
                || user_question.getValue() == null
                || user_answer.getText().isBlank()) {
            showInfo("Please fill all fields to create a new user");
            return;
        }

        if (!confirmAction("Create User", "Do you want to create this user?")) return;

        try {
            String result = apiService.adminCreateUser(
                    user_username.getText().trim(),
                    user_password.getText(),
                    user_role.getValue(),
                    user_question.getValue(),
                    user_answer.getText().trim()
            );

            if ("SUCCESS".equals(result)) {
                showInfo("User created successfully");
                userClearBtn();
                loadUsersData();
                loadDashboardData();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not create user: " + e.getMessage());
        }
    }

    @FXML
    public void userUpdateRoleBtn() {
        if (!requireAdminAccess("Update user role")) return;

        if (selectedUserModel == null) {
            showInfo("Please select a user first");
            return;
        }

        if (user_role == null || user_role.getValue() == null) {
            showInfo("Please choose a role");
            return;
        }

        String newRole = user_role.getValue().trim().toUpperCase();

        if (selectedUserModel.getUsername().equalsIgnoreCase(loggedInUsername)
                && !"ADMIN".equals(newRole)) {
            showInfo("You cannot change your own active session from ADMIN to CASHIER.");
            return;
        }

        if (!confirmAction("Update Role", "Do you want to update this user's role?")) return;

        try {
            String result = apiService.updateUserRole(selectedUserModel.getId(), newRole);

            if ("SUCCESS".equals(result)) {
                showInfo("User role updated successfully");
                loadUsersData();
                userClearBtn();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not update role: " + e.getMessage());
        }
    }

    @FXML
    public void userToggleBtn() {
        if (!requireAdminAccess("Enable / Disable user")) return;

        if (selectedUserModel == null) {
            showInfo("Please select a user first");
            return;
        }

        if (selectedUserModel.getUsername().equalsIgnoreCase(loggedInUsername)) {
            showInfo("You cannot disable your own active account.");
            return;
        }

        boolean newEnabled = !Boolean.TRUE.equals(selectedUserModel.getEnabled());
        String actionText = newEnabled ? "enable" : "disable";

        if (!confirmAction("Update User Status", "Do you want to " + actionText + " this user?")) return;

        try {
            String result = apiService.updateUserEnabled(selectedUserModel.getId(), newEnabled);

            if ("SUCCESS".equals(result)) {
                showInfo("User status updated successfully");
                loadUsersData();
                userClearBtn();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not update user status: " + e.getMessage());
        }
    }

    @FXML
    public void userClearBtn() {
        if (user_username != null) user_username.clear();
        if (user_password != null) user_password.clear();
        if (user_role != null) user_role.getSelectionModel().clearSelection();
        if (user_question != null) user_question.getSelectionModel().clearSelection();
        if (user_answer != null) user_answer.clear();
        if (users_tableView != null) users_tableView.getSelectionModel().clearSelection();
        selectedUserModel = null;
    }
    private void setupCategoriesTable() {
        if (categories_col_id != null) categories_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (categories_col_name != null) categories_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (categories_col_description != null) categories_col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        if (categories_col_createdAt != null) categories_col_createdAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    private void loadCategoriesData() {
        try {
            String json = apiService.getAllCategories();
            ObservableList<CategoryAdminModel> list = FXCollections.observableArrayList();

            String trimmed = json.trim();
            if (trimmed.length() < 2 || trimmed.equals("[]")) {
                categoriesMasterList.clear();
                if (categories_tableView != null) categories_tableView.setItems(list);
                return;
            }

            String content = trimmed.substring(1, trimmed.length() - 1);
            String[] objects = content.split("\\},\\s*\\{");

            for (String obj : objects) {
                String item = obj.trim();
                if (!item.startsWith("{")) item = "{" + item;
                if (!item.endsWith("}")) item = item + "}";

                Long id = Long.parseLong(extractValue(item, "id"));
                String name = extractStringValue(item, "name");
                String description = extractStringValue(item, "description");
                String createdAt = extractStringValue(item, "createdAt");

                list.add(new CategoryAdminModel(id, name, description, createdAt));
            }

            categoriesMasterList.setAll(list);
            if (categories_tableView != null) {
                categories_tableView.setItems(FXCollections.observableArrayList(categoriesMasterList));
            }

            filterCategoriesTable();

        } catch (Exception e) {
            showInfo("Could not load categories: " + e.getMessage());
        }
    }

    @FXML
    public void categorySelectData(MouseEvent event) {
        if (categories_tableView == null) return;

        selectedCategoryAdminModel = categories_tableView.getSelectionModel().getSelectedItem();
        if (selectedCategoryAdminModel == null) return;

        if (category_id != null) category_id.setText(String.valueOf(selectedCategoryAdminModel.getId()));
        if (category_name != null) category_name.setText(selectedCategoryAdminModel.getName());
        if (category_description != null) category_description.setText(selectedCategoryAdminModel.getDescription());
    }

    @FXML
    public void categoryAddBtn() {
        if (!requireAdminAccess("Create category")) return;

        if (category_name == null || category_name.getText().isBlank()) {
            showInfo("Category name is required");
            return;
        }

        if (!confirmAction("Create Category", "Do you want to create this category?")) return;

        try {
            String result = apiService.createCategory(
                    category_name.getText().trim(),
                    category_description == null ? "" : category_description.getText().trim()
            );

            if ("SUCCESS".equals(result)) {
                showInfo("Category created successfully");
                categoryClearBtn();
                loadCategoriesData();
                loadInventoryTypeList();
                loadDashboardData();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not create category: " + e.getMessage());
        }
    }

    @FXML
    public void categoryUpdateBtn() {
        if (!requireAdminAccess("Update category")) return;

        if (selectedCategoryAdminModel == null) {
            showInfo("Please select a category first");
            return;
        }

        if (category_name == null || category_name.getText().isBlank()) {
            showInfo("Category name is required");
            return;
        }

        if (!confirmAction("Update Category", "Do you want to update this category?")) return;

        try {
            String result = apiService.updateCategory(
                    selectedCategoryAdminModel.getId(),
                    category_name.getText().trim(),
                    category_description == null ? "" : category_description.getText().trim()
            );

            if ("SUCCESS".equals(result)) {
                showInfo("Category updated successfully");
                categoryClearBtn();
                loadCategoriesData();
                loadInventoryTypeList();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not update category: " + e.getMessage());
        }
    }

    @FXML
    public void categoryDeleteBtn() {
        if (!requireAdminAccess("Delete category")) return;

        if (selectedCategoryAdminModel == null) {
            showInfo("Please select a category first");
            return;
        }

        if (!confirmAction("Delete Category", "Are you sure you want to deactivate this category?")) return;

        try {
            String result = apiService.deleteCategory(selectedCategoryAdminModel.getId());

            if ("SUCCESS".equals(result)) {
                showInfo("Category deleted successfully");
                categoryClearBtn();
                loadCategoriesData();
                loadInventoryTypeList();
                loadInventoryData();
                loadMenuCards();
                loadDashboardData();
            } else {
                showInfo(result);
            }
        } catch (Exception e) {
            showInfo("Could not delete category: " + e.getMessage());
        }
    }

    @FXML
    public void categoryClearBtn() {
        if (category_id != null) category_id.clear();
        if (category_name != null) category_name.clear();
        if (category_description != null) category_description.clear();
        if (categories_tableView != null) categories_tableView.getSelectionModel().clearSelection();
        selectedCategoryAdminModel = null;
    }

    private void filterCategoriesTable() {
        if (categories_tableView == null) return;

        String searchText = category_search == null ? "" : category_search.getText();

        if (searchText == null || searchText.isBlank()) {
            categories_tableView.setItems(FXCollections.observableArrayList(categoriesMasterList));
            return;
        }

        String keyword = searchText.trim().toLowerCase();
        ObservableList<CategoryAdminModel> filteredList = FXCollections.observableArrayList();

        for (CategoryAdminModel category : categoriesMasterList) {
            String name = category.getName() == null ? "" : category.getName().toLowerCase();
            String description = category.getDescription() == null ? "" : category.getDescription().toLowerCase();

            if (name.contains(keyword) || description.contains(keyword)) {
                filteredList.add(category);
            }
        }

        categories_tableView.setItems(filteredList);
    }
}