package br.com.ersys;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class RepararAudioApp {

    private static Clip clip;

    static void main() {
        // Esconde do Dock
        System.setProperty("apple.awt.UIElement", "true");
        reiniciarServicoAudioMacOS();
        tocarSomLoop();
        iniciarRepararAudioAppTray();
    }

    public static void tocarSomLoop() {
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
            System.out.println("Erro na aplicação " + e);
        }
    }

    public static void pararCorrecaoAudio() {
        try {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        } catch (Exception e) {
            System.out.println("Erro na aplicação " + e);
        }
    }

    public static void reiniciarCorrecaoAudio() {
        pararCorrecaoAudio();
        reiniciarServicoAudioMacOS();
        tocarSomLoop();
    }

    public static void reiniciarServicoAudioMacOS() {
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
            System.out.println("Erro na aplicação " + e);
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
            int exitCode;
            try (Process process = pb.start()) {
                exitCode = process.waitFor();
            }
            System.out.println("Execução com usuário/senha. Exit code: " + exitCode);
            return exitCode == 0;
        } catch (Exception e) {
            System.out.println("Erro na aplicação" + e);
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
            int exitCode;
            try (Process process = pb.start()) {
                exitCode = process.waitFor();
            }
            System.out.println("Fallback executado. Exit code: " + exitCode);
            if (exitCode == 1) {
                System.out.println(
                        "Falha por falta de privilégios para reiniciar o servi;co de áudio. Encerrando aplicação."
                );
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Erro na aplicação" + e);
            System.exit(1);
        }
    }

    public static void iniciarRepararAudioAppTray() {
        if (!SystemTray.isSupported()) {
            return;
        }
        try {
            SystemTray tray =
                    SystemTray.getSystemTray();
            PopupMenu popup =
                    new PopupMenu();
            MenuItem reiniciar =
                    new MenuItem("Reiniciar correção de áudio");
            MenuItem parar =
                    new MenuItem("Parar correção de áudio");
            MenuItem sair =
                    new MenuItem("Sair");
            reiniciar.addActionListener(e -> {
                reiniciarCorrecaoAudio();
            });
            parar.addActionListener(e -> {
                pararCorrecaoAudio();
            });
            sair.addActionListener(e -> {
                pararCorrecaoAudio();
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
            System.out.println("Erro na aplicação" + e);
        }
    }

    private static Image criarIcone() {
        return Toolkit.getDefaultToolkit()
                .getImage(
                        RepararAudioApp.class.getResource("/RepararAudioApp.png")
                );
    }

    private static File getConfigFile() throws Exception {
        String executablePath =
                new File(
                        RepararAudioApp.class
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
