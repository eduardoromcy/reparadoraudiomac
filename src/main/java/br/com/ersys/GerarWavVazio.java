package br.com.ersys;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;

public class GerarWavVazio {

    public static void main(String[] args) throws Exception {
        // 1 segundo de silêncio
        byte[] silencio = new byte[44100 * 2];
        AudioFormat format = new AudioFormat(
                44100, // sample rate
                16,    // bits
                1,     // mono
                true,  // signed
                false  // little endian
        );
        ByteArrayInputStream bais =
                new ByteArrayInputStream(silencio);
        AudioInputStream ais =
                new AudioInputStream(
                        bais,
                        format,
                        silencio.length / format.getFrameSize()
                );
        File arquivo = new File("som.wav");
        AudioSystem.write(
                ais,
                AudioFileFormat.Type.WAVE,
                arquivo
        );
        System.out.println("som.wav criado!");
    }

}