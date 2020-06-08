package app;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class Principal extends JFrame implements ActionListener, ItemListener {

    File f;
    JLabel lblOperacion;
    JLabel lblResultado;
    JLabel lblInfo;
    JTextField txt1;
    JTextField txt2;
    String operacionSeleccionada;
    JButton btnOperacion;
    JComboBox<String> cmb;
    JRadioButton rbSuma;
    JRadioButton rbResta;
    JRadioButton rbMultiplicacion;
    JRadioButton rbDivision;
    JRadioButton[] rbOperaciones;
    ButtonGroup grupo;
    String[] operaciones = { "+", "-", "*", "/" };
    String[] decimales = { "0", "1", "2", "4", "5" };

    public Principal() {

        super();
        setLayout(null);

        lblOperacion = new JLabel("+");
        lblOperacion.setSize(20, lblOperacion.getPreferredSize().height);
        lblOperacion.setLocation(150, 30);
        this.add(lblOperacion);

        lblResultado = new JLabel("=");
        lblResultado.setSize(20, lblResultado.getPreferredSize().height);
        lblResultado.setLocation(320, 30);
        this.add(lblResultado);

        lblInfo = new JLabel("No puedes dividir entre 0");
        lblInfo.setSize(150, lblInfo.getPreferredSize().height);
        lblInfo.setLocation(150, 60);
        lblInfo.setForeground(Color.RED);
        lblInfo.setVisible(false);
        this.add(lblInfo);

        txt1 = new JTextField();
        txt1.setSize(100, txt1.getPreferredSize().height);
        txt1.setLocation(10, 30);
        this.add(txt1);

        txt2 = new JTextField();
        txt2.setSize(100, txt2.getPreferredSize().height);
        txt2.setLocation(180, 30);
        this.add(txt2);

        btnOperacion = new JButton("Calcular");
        btnOperacion.setSize(100, btnOperacion.getPreferredSize().height);
        btnOperacion.setLocation(20, 60);
        btnOperacion.addActionListener(this);
        this.add(btnOperacion);

        cmb = new JComboBox<String>(decimales);
        cmb.setSize(100, cmb.getPreferredSize().height);
        cmb.setLocation(20, 100);
        cmb.setToolTipText("Selecciona la cantidad de decimales");
        cmb.addActionListener(this);
        this.add(cmb);

        grupo = new ButtonGroup();

        int x = 15;
        int y = 180;
        rbOperaciones = new JRadioButton[4];
        for (int i = 0; i < rbOperaciones.length; i++) {
            rbOperaciones[i] = new JRadioButton(operaciones[i]);
            rbOperaciones[i].setSize(110, rbOperaciones[i].getPreferredSize().height);
            rbOperaciones[i].setLocation(x, y);
            rbOperaciones[i].addItemListener(this);
            grupo.add(rbOperaciones[i]);
            rbOperaciones[0].setSelected(true);
            this.add(rbOperaciones[i]);

            x += 120;
            if (i == 1) {
                x = 15;
                y += 30;
            }
        }

        f = new File(System.getProperty("user.home") + System.getProperty("file.separator") + ".registro.txt");
        leer(f);

    }

    public void operaciones(double n1, double n2, int decimales, String operacion) {

        lblInfo.setVisible(false);
        double res = 0;

        if (operacion.equals("+")) {
            res = n1 + n2;

        } else if (operacion.equals("-")) {
            res = n1 - n2;

        } else if (operacion.equals("*")) {
            res = n1 * n2;

        } else {

            if (n2 != 0) {
                res = n1 / n2;
            } else {
                lblInfo.setVisible(true);
            }
        }

        if (decimales == 0) {
            lblResultado.setText(String.format("= %d", (int) res));
        } else {
            lblResultado.setText(String.format("= %." + decimales + "f", res));
        }
        lblResultado.setSize(lblResultado.getPreferredSize());
    }

    public void guardar() {
        if (f.exists()) {
            f.delete();
        }
        try (PrintWriter f = new PrintWriter(new FileWriter(
                System.getProperty("user.home") + System.getProperty("file.separator") + ".registro.txt", true))) {

            f.println(txt1.getText());
            f.println(txt2.getText());
            for (int i = 0; i < rbOperaciones.length; i++) {
                if (rbOperaciones[i].isSelected()) {
                    f.println(rbOperaciones[i].getText());
                }
            }
            f.println(cmb.getSelectedItem().toString());
        } catch (IOException e) {
            System.err.println("Error");
        }

    }

    public void leer(File archivo) {
        if (archivo.exists()) {
            String[] datos = new String[4];
            int cont = 0;
            try (Scanner f = new Scanner(archivo)) {
                while (f.hasNext()) {
                    datos[cont] = f.nextLine();
                    cont++;
                }
                operaciones(Double.parseDouble(datos[0]), Double.parseDouble(datos[1]), Integer.parseInt(datos[3]),
                        datos[2]);
                restaurar(datos);
            } catch (IOException exc) {
                System.err.println("Error de acceso al archivo: " + exc.getMessage());
            } catch (NumberFormatException exc) {
                System.err.println("tas crazy");
            }
        }
    }

    public void restaurar(String[] datos) {
        txt1.setText(datos[0]);
        txt2.setText(datos[1]);
        lblOperacion.setText(datos[2]);
        for (int i = 0; i < datos.length; i++) {

            if (cmb.getItemAt(i) == datos[3]) {
                cmb.getSelectedItem();

            }
        }
        for (int i = 0; i < rbOperaciones.length; i++) {

            if (rbOperaciones[i].getText().equals(datos[2])) {

                rbOperaciones[i].setSelected(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnOperacion) {
            int d = Integer.parseInt(cmb.getSelectedItem().toString());
            try {
                operaciones(Double.parseDouble(txt1.getText()), Double.parseDouble(txt2.getText()), d,
                        operacionSeleccionada);
            } catch (NumberFormatException exc) {
                System.err.println("Introduce número real.");
                lblInfo.setText("Introduce numeros");
                lblInfo.setVisible(true);
            }
            guardar();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JRadioButton rb = (JRadioButton) e.getSource();
        operacionSeleccionada = rb.getText();
        lblOperacion.setText(rb.getText());

    }
}