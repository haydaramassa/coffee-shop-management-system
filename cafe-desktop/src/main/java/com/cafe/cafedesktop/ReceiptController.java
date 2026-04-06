package com.cafe.cafedesktop;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;

public class ReceiptController {

    @FXML
    private AnchorPane printableRoot;

    @FXML
    private Label cafeNameLabel;
    @FXML
    private Label orderIdLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label cashierLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label paidLabel;
    @FXML
    private Label changeLabel;

    @FXML
    private TableView<ReceiptItemModel> receiptTable;
    @FXML
    private TableColumn<ReceiptItemModel, String> colProductName;
    @FXML
    private TableColumn<ReceiptItemModel, Integer> colQuantity;
    @FXML
    private TableColumn<ReceiptItemModel, String> colUnitPrice;
    @FXML
    private TableColumn<ReceiptItemModel, String> colLineTotal;
    @FXML
    private Button printButton;

    @FXML
    public void initialize() {
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colLineTotal.setCellValueFactory(new PropertyValueFactory<>("lineTotal"));
        receiptTable.setPlaceholder(new Label("No receipt items"));
    }

    public void loadReceiptData(String orderId,
                                String customer,
                                String cashier,
                                String date,
                                String time,
                                String total,
                                String paid,
                                String change,
                                ObservableList<ReceiptItemModel> items) {

        orderIdLabel.setText(orderId);
        customerLabel.setText(customer);
        cashierLabel.setText(cashier);
        dateLabel.setText(date);
        timeLabel.setText(time);
        totalLabel.setText(total);
        paidLabel.setText(paid);
        changeLabel.setText(change);
        receiptTable.setItems(items);
    }

    @FXML
    public void printReceipt() {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null) {
            return;
        }

        boolean proceed = job.showPrintDialog(printableRoot.getScene().getWindow());
        if (!proceed) {
            return;
        }

        boolean oldVisible = printButton.isVisible();
        boolean oldManaged = printButton.isManaged();

        try {
            printButton.setVisible(false);
            printButton.setManaged(false);

            PageLayout pageLayout = job.getPrinter().createPageLayout(
                    Paper.A4,
                    javafx.print.PageOrientation.PORTRAIT,
                    Printer.MarginType.DEFAULT
            );

            double scaleX = pageLayout.getPrintableWidth() / printableRoot.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / printableRoot.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY);

            printableRoot.setScaleX(scale);
            printableRoot.setScaleY(scale);

            boolean success = job.printPage(pageLayout, printableRoot);
            if (success) {
                job.endJob();
            }

        } finally {
            printableRoot.setScaleX(1);
            printableRoot.setScaleY(1);
            printButton.setVisible(oldVisible);
            printButton.setManaged(oldManaged);
        }
    }
}

