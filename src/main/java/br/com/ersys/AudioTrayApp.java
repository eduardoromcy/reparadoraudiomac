package br.com.ersys;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class AudioTrayApp {
    private static Clip clip;

    public static void main(String[] args) {
        // Esconde do Dock
        System.setProperty("apple.awt.UIElement", "true");
        reiniciarAudioMacOS();
        tocarAudioLoop();
        iniciarTray();
    }

    public static void tocarAudioLoop() {
        try {
            URL url = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("audio/som.wav");
            if (url == null) {
                throw new RuntimeException("Arquivo não encontrado!");
            }
            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pararAudio() {
        try {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reiniciarAudio() {
        pararAudio();
        reiniciarAudioMacOS();
        tocarAudioLoop();
    }

    public static void reiniciarAudioMacOS() {
        try {
            String script =
                    "do shell script \"killall coreaudiod\" " +
                            "with administrator privileges";
            ProcessBuilder pb = new ProcessBuilder(
                    "osascript",
                    "-e",
                    script
            );
            Process process = pb.start();
            int exitCode = process.waitFor();
            System.out.println(
                    "coreaudiod reiniciado. Exit code: " +
                            exitCode
            );
            // Espera o macOS subir o áudio novamente
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void iniciarTray() {
        if (!SystemTray.isSupported()) {
            return;
        }
        try {
            SystemTray tray =
                    SystemTray.getSystemTray();
            PopupMenu popup =
                    new PopupMenu();
            MenuItem reiniciar =
                    new MenuItem("Reiniciar");
            MenuItem parar =
                    new MenuItem("Parar");
            MenuItem sair =
                    new MenuItem("Sair");
            reiniciar.addActionListener(e -> {
                reiniciarAudio();
            });
            parar.addActionListener(e -> {
                pararAudio();
            });
            sair.addActionListener(e -> {
                pararAudio();
                tray.remove(tray.getTrayIcons()[0]);
                System.exit(0);
            });
            popup.add(reiniciar);
            popup.add(parar);
            popup.addSeparator();
            popup.add(sair);
            TrayIcon trayIcon =
                    new TrayIcon(
                            criarIcone(),
                            "Reparador de áudio",
                            popup
                    );
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Image criarIcone() {
        BufferedImage image =
                new BufferedImage(
                        16,
                        16,
                        BufferedImage.TYPE_INT_ARGB
                );
        Graphics2D g = image.createGraphics();
        g.setColor(Color.ORANGE);
        g.fillOval(2, 2, 12, 12);
        g.dispose();
        return image;
    }

}