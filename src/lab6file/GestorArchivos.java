/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6file;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import java.io.*;

public class GestorArchivos {
    public static boolean guardar(StyledDocument doc, File file) {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file))) {
            o.writeObject(doc);
            o.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static StyledDocument abrir(File file) {
        try (ObjectInputStream i = new ObjectInputStream(new FileInputStream(file))) {
            Object o = i.readObject();
            if (o instanceof StyledDocument) return (StyledDocument) o;
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}