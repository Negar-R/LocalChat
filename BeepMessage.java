package localchat;

import java.io.File;

import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;


public class BeepMessage implements Message {    
    @Override
    public void execute() {
        System.out.println("[BEEP]");

        this.play_beep();
    }

    private void play_beep() {
        try {
            File audioFile = new File("./notif.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioStream);
                
            audioClip.start();
            
            Thread.sleep(1000);
                
            audioClip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}