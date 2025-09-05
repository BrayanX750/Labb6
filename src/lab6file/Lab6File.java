/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab6file;

/**
 *
 * @author Usuario
 */
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Lab6File extends JFrame {
    private JTextPane areaTexto;
    private JComboBox<String> comboFuentes;
    private JComboBox<Integer> comboTamanos;
    private JFileChooser selector;
    private File archivoActual;

    public Lab6File() {
        setTitle("Editor de texto");
        setSize(900, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        areaTexto = new JTextPane();
        JScrollPane scroll = new JScrollPane(areaTexto);

        JPanel barra1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botonAbrir = new JButton("Abrir");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonNegrita = new JButton("B");
        botonNegrita.setFont(new Font("Arial", Font.BOLD, 14));
        JButton botonCursiva = new JButton("I");
        botonCursiva.setFont(new Font("Arial", Font.ITALIC, 14));
        JButton botonSubrayado = new JButton("U");

        comboFuentes = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        comboTamanos = new JComboBox<>(new Integer[]{12, 14, 16, 18, 24, 36, 48, 64, 72, 92, 120});

        barra1.add(botonAbrir);
        barra1.add(botonGuardar);
        barra1.add(botonNegrita);
        barra1.add(botonCursiva);
        barra1.add(botonSubrayado);
        barra1.add(new JLabel("Fuente"));
        barra1.add(comboFuentes);
        barra1.add(new JLabel("Tamaño"));
        barra1.add(comboTamanos);

        JPanel barra2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paleta = new JPanel(new GridLayout(2, 8, 4, 4));
        Color[] colores = {
                Color.black, Color.darkGray, Color.gray, Color.lightGray,
                Color.red, Color.orange, Color.yellow, Color.green,
                Color.cyan, Color.blue, new Color(25,25,112), new Color(65,105,225),
                Color.magenta, Color.pink, new Color(139,69,19), Color.white
        };
        for (Color c : colores) {
            JButton b = new JButton();
            b.setBackground(c);
            b.setPreferredSize(new Dimension(22, 22));
            b.setBorder(BorderFactory.createLineBorder(Color.darkGray));
            b.addActionListener(e -> aplicarColor(c));
            paleta.add(b);
        }
        barra2.add(new JLabel("Color"));
        barra2.add(paleta);

        JPanel arriba = new JPanel();
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        arriba.add(barra1);
        arriba.add(barra2);

        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        

        add(arriba, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);

        botonNegrita.addActionListener(e -> toggleEstilo("negrita"));
        botonCursiva.addActionListener(e -> toggleEstilo("cursiva"));
        botonSubrayado.addActionListener(e -> toggleEstilo("subrayado"));
        comboFuentes.addActionListener(e -> aplicarFuente());
        comboTamanos.addActionListener(e -> aplicarTamano());
        botonAbrir.addActionListener(e -> abrirArchivo());
        botonGuardar.addActionListener(e -> guardarArchivo());
        
        selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter("Documentos .docx", "docx"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (archivoActual != null) GestorArchivos.guardar(areaTexto.getStyledDocument(), archivoActual);
                dispose();
                System.exit(0);
            }
        });
    }

    private StyledEditorKit kit() {
        return (StyledEditorKit) areaTexto.getEditorKit();
    }

    private MutableAttributeSet input() {
        return kit().getInputAttributes();
    }

    private void toggleEstilo(String cual) {
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();
        MutableAttributeSet attrs = input();

        boolean b = StyleConstants.isBold(attrs);
        boolean i = StyleConstants.isItalic(attrs);
        boolean u = StyleConstants.isUnderline(attrs);

        SimpleAttributeSet s = new SimpleAttributeSet();
        if (cual.equals("negrita")) StyleConstants.setBold(s, !b);
        if (cual.equals("cursiva")) StyleConstants.setItalic(s, !i);
        if (cual.equals("subrayado")) StyleConstants.setUnderline(s, !u);

        if (inicio != fin) {
            areaTexto.getStyledDocument().setCharacterAttributes(inicio, fin - inicio, s, false);
        }
        attrs.addAttributes(s);
        areaTexto.setCharacterAttributes(s, false);
    }

    private void aplicarFuente() {
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();
        String fuente = (String) comboFuentes.getSelectedItem();
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setFontFamily(s, fuente);
        if (inicio != fin) {
            areaTexto.getStyledDocument().setCharacterAttributes(inicio, fin - inicio, s, false);
        }
        input().addAttributes(s);
        areaTexto.setCharacterAttributes(s, false);
    }

    private void aplicarTamano() {
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();
        Integer t = (Integer) comboTamanos.getSelectedItem();
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setFontSize(s, t);
        if (inicio != fin) {
            areaTexto.getStyledDocument().setCharacterAttributes(inicio, fin - inicio, s, false);
        }
        input().addAttributes(s);
        areaTexto.setCharacterAttributes(s, false);
    }

    private void aplicarColor(Color color) {
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setForeground(s, color);
        if (inicio != fin) {
            areaTexto.getStyledDocument().setCharacterAttributes(inicio, fin - inicio, s, false);
        }
        input().addAttributes(s);
        areaTexto.setCharacterAttributes(s, false);
    }

    private void guardarArchivo() {
        if (archivoActual == null) {
            if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                archivoActual = asegurarExtension(selector.getSelectedFile());
            }
        } else {
            archivoActual = asegurarExtension(archivoActual);
        }
        if (archivoActual != null) {
            if (GestorArchivos.guardar(areaTexto.getStyledDocument(), archivoActual)) {
                JOptionPane.showMessageDialog(this, "Archivo guardado");
            }
        }
    }

    private void abrirArchivo() {
        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoActual = selector.getSelectedFile();
            StyledDocument doc = GestorArchivos.abrir(archivoActual);
            if (doc != null) areaTexto.setStyledDocument(doc);
            else JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo");
        }
    }

    private File asegurarExtension(File f) {
        String n = f.getName().toLowerCase();
        if (!n.endsWith(".docx")) return new File(f.getParentFile(), f.getName() + ".docx");
        return f;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Lab6File().setVisible(true));
    }
}