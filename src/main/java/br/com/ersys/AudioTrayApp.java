package br.com.ersys;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        String usuario = null;
        String senha = null;
        try {
            File config = getConfigFile();
            if (config.exists()) {
                try (
                        BufferedReader br =
                             new BufferedReader(
                                     new FileReader(config))) {
                    usuario = br.readLine();
                    senha = br.readLine();
                }
            }
            boolean executouComSenha =
                    executarComUsuarioSenha(
                            usuario,
                            senha
                    );
            if (!executouComSenha) {
                System.out.println(
                        "Falhou com usuário/senha. Tentando fallback..."
                );
                executarFallback();
            }
            // Espera o macOS subir o áudio novamente
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean executarComUsuarioSenha(
            String usuario,
            String senha
    ) {
        try {
            if (usuario == null || senha == null) {
                return false;
            }
            String script =
                    "do shell script " +
                            "\"sudo killall coreaudiod\" " +
                            "user name \"" + usuario + "\" " +
                            "password \"" + senha + "\" " +
                            "with administrator privileges";
            ProcessBuilder pb =
                    new ProcessBuilder(
                            "osascript",
                            "-e",
                            script
                    );
            Process process = pb.start();
            int exitCode = process.waitFor();
            System.out.println(
                    "Execução com usuário/senha. Exit code: "
                            + exitCode
            );
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void executarFallback() {
        try {
            String script =
                    "do shell script " +
                            "\"killall coreaudiod\" " +
                            "with administrator privileges";
            ProcessBuilder pb =
                    new ProcessBuilder(
                            "osascript",
                            "-e",
                            script
                    );
            Process process = pb.start();
            int exitCode = process.waitFor();
            System.out.println(
                    "Fallback executado. Exit code: "
                            + exitCode
            );
            if (exitCode == 1) {
                System.err.println(
                        "Falha por falta de privilégios para reiniciar o servi;co de áudio. Encerrando aplicação."
                );
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
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
                tray.remove(
                        tray.getTrayIcons()[0]
                );
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
        return Toolkit.getDefaultToolkit()
                .getImage(
                        AudioTrayApp.class.getResource("/RepararAudioApp.png")
                );
    }

    private static File getConfigFile() throws Exception {
        String executablePath =
                new File(
                        AudioTrayApp.class
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI()
                ).getAbsolutePath();
        File jarFile = new File(executablePath);
        File appDir = jarFile
                .getParentFile()      // app
                .getParentFile()      // Contents
                .getParentFile();     // RepararAudioApp.app
        return new File(
                appDir.getParentFile(),
                "config.ini"
        );
    }

}