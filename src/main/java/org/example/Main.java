package org.example;

import com.github.t9t.minecraftrconclient.RconClient;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;
import javax.swing.*;
import java.awt.*;
public class Main {
    static{
        nu.pattern.OpenCV.loadLocally();
    }
    public static JLabel label = new JLabel("Progress:");
    public static void main(String[] args) {
        try (RconClient client = RconClient.open("localhost", 25575, "1111")) {

            JFrame jframe = new JFrame("Conve  rt...");
            JProgressBar progressBar = new JProgressBar(0, 100);
            jframe.getContentPane().setLayout(new BorderLayout());
            jframe.getContentPane().add(label, BorderLayout.NORTH);
            jframe.getContentPane().add(progressBar, BorderLayout.CENTER);
            jframe.setSize(300, 100);
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jframe.setVisible(true);

            String inputFilePath = "C:\\new\\qqq.mp4";
            // 動画ファイルを開く
            VideoCapture videoCapture = new VideoCapture(inputFilePath);
            if (!videoCapture.isOpened()) {
                System.out.println("Error: Could not open video file.");
                return;
            }

            Mat frame = new Mat();
            long totalFrames = (long) videoCapture.get(7); // CV_CAP_PROP_FRAME_COUNT = 7
            int frameWidth = (int) videoCapture.get(3); // CV_CAP_PROP_FRAME_WIDTH = 3
            int frameHeight = (int) videoCapture.get(4); // CV_CAP_PROP_FRAME_HEIGHT = 4
            int processedFrames = 0;

                for (int frameCount = 0; frameCount < totalFrames; frameCount++) {
                    videoCapture.read(frame);

                    // フレームが空の場合は処理終了
                    if (frame.empty()) {
                        break;
                    }

                    // 画像の幅と高さを取得
                    int width = frame.cols();
                    int height = frame.rows();
                    // 画像のすべてのドットをRGBに分解してテキストファイルに書き込む
                    for (int y = 0; y < frameHeight; y++) {
                        for (int x = 0; x < frameWidth; x++) {
                            // ピクセルの色情報を取得
                            double[] pixelColor = frame.get(y, x);
                            // 新しい画像に色情報を設定
                            double posX = Math.floor(x * 0.1 * 10000) / 10000; // 4桁目で切り捨て
                            double posY = Math.floor((frameHeight - y - 1) * 0.1 * 10000) / 10000;
                            // 中心座標の色情報を取得
                            Scalar centerColor = new Scalar(frame.get(y, x));

                            if (centerColor.val[2] != 0 && centerColor.val[1] != 0 && centerColor.val[0] != 0) {
                                if (centerColor.val[2] != 2 && centerColor.val[1] != 2 && centerColor.val[0] != 2) {
                                    if (centerColor.val[2] != 1 && centerColor.val[1] != 1 && centerColor.val[0] != 1) {
                                        if (centerColor.val[2] != 1 && centerColor.val[1] != 2 && centerColor.val[0] != 1) {
                                            if (centerColor.val[2] != 2 && centerColor.val[1] != 2 && centerColor.val[0] != 1) {

                                                double xCoord = 160 + posX;
                                                double yCoord = 84 + posY;

                                                client.sendCommand(String.format("particle dust %f %f %f 1 %f %f %f 0 0 0 0 3", pixelColor[2] / 255.0, pixelColor[1] / 255.0, pixelColor[0] / 255.0, xCoord, yCoord, 132.0));
                                            }}}}}}}
                    processedFrames++;
                    double progress = (double) processedFrames / totalFrames * 100;
                    progressBar.setValue((int) progress);
                    label.setText("Progress: " + processedFrames + " / " + totalFrames);
                }
            // メモリの解放
            frame.release();
            videoCapture.release();
            System.out.println("画像のRGB情報をファイルに書き込みました。");
        }
    }
}