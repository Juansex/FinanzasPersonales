package com.example.last_but_not_least;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FinanzasPersonalesApp extends Application {
    private TableView<Registro> tableView;
    private ObservableList<Registro> registros;

    private TextField montoTextField; // Corrección: Declarar como variable de instancia
    private TextField descripcionTextField;
    private ChoiceBox<String> tipoChoiceBox;
    private DatePicker datePicker;
    private Label balanceLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        tableView = new TableView<>();
        registros = FXCollections.observableArrayList();

        TableColumn<Registro, Double> montoColumn = new TableColumn<>("Monto");
        montoColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));

        TableColumn<Registro, String> descripcionColumn = new TableColumn<>("Descripción");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Registro, String> tipoColumn = new TableColumn<>("Tipo");
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Registro, LocalDate> fechaColumn = new TableColumn<>("Fecha");
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tableView.getColumns().addAll(montoColumn, descripcionColumn, tipoColumn, fechaColumn);

        ChoiceBox<String> tipoChoiceBox = new ChoiceBox<>();
        tipoChoiceBox.getItems().addAll("Ingreso", "Gasto");
        tipoChoiceBox.setValue("Ingreso");

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        TextField montoTextField = new TextField();
        montoTextField.setPromptText("Monto");

        TextField descripcionTextField = new TextField();
        descripcionTextField.setPromptText("Descripción");

        Button agregarButton = new Button("Agregar");
        agregarButton.setOnAction(e -> {
            double monto = Double.parseDouble(montoTextField.getText());
            String descripcion = descripcionTextField.getText();
            String tipo = tipoChoiceBox.getValue();
            LocalDate fecha = datePicker.getValue();

            Registro registro = new Registro(monto, descripcion, tipo, fecha);
            registros.add(registro);

            actualizarBalance();
            limpiarCampos();
        });

        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setOnAction(e -> {
            Registro seleccionado = tableView.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                registros.remove(seleccionado);
                actualizarBalance();
            }
        });

        HBox ingresoGastoBox = new HBox(10, tipoChoiceBox, datePicker, montoTextField, descripcionTextField, agregarButton, eliminarButton);

        Button gastosButton = new Button("Ver Gastos");
        gastosButton.setOnAction(e -> mostrarGastos());

        Button ingresosButton = new Button("Ver Ingresos");
        ingresosButton.setOnAction(e -> mostrarIngresos());

        Button todosButton = new Button("Ver Todos");
        todosButton.setOnAction(e -> mostrarTodos());

        HBox botonesBox = new HBox(10, gastosButton, ingresosButton, todosButton);

        balanceLabel = new Label("Balance: 0.0");

        VBox vbox = new VBox(10, ingresoGastoBox, botonesBox, balanceLabel);
        vbox.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
        borderPane.setBottom(vbox);

        Scene scene = new Scene(borderPane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Finanzas Personales");
        primaryStage.show();

        // Mostrar los registros más recientes por defecto
        mostrarTodos();
    }

    private void mostrarGastos() {
        tableView.setItems(registros.filtered(registro -> registro.getTipo().equals("Gasto")));
    }

    private void mostrarIngresos() {
        tableView.setItems(registros.filtered(registro -> registro.getTipo().equals("Ingreso")));
    }

    private void mostrarTodos() {
        tableView.setItems(registros);
    }

    private void actualizarBalance() {
        double ingresos = registros.filtered(registro -> registro.getTipo().equals("Ingreso"))
                .stream().mapToDouble(Registro::getMonto).sum();

        double gastos = registros.filtered(registro -> registro.getTipo().equals("Gasto"))
                .stream().mapToDouble(Registro::getMonto).sum();

        double balance = ingresos - gastos;
        balanceLabel.setText("Balance: " + balance);
    }

    private void limpiarCampos() {
        montoTextField.clear();
        descripcionTextField.clear();
    }
    public static class Registro {
        private double monto;
        private String descripcion;
        private String tipo;
        private LocalDate fecha;

        public Registro(double monto, String descripcion, String tipo, LocalDate fecha) {
            this.monto = monto;
            this.descripcion = descripcion;
            this.tipo = tipo;
            this.fecha = fecha;
        }

        public double getMonto() {
            return monto;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getTipo() {
            return tipo;
        }

        public LocalDate getFecha() {
            return fecha;
        }
    }
}
