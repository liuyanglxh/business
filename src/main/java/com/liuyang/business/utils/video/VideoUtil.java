package com.liuyang.business.utils.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;

/**
 * 视频工具
 *
 * @author
 */
public class VideoUtil {

    private static final Logger logger = LoggerFactory.getLogger(VideoUtil.class);

    public static void main(String[] args) throws Exception {
        String filePath = "/Users/liuyang/Downloads/S08E06.End.1080p.HD中英双字[最新电影www.66e.cc].mp4";
        File video = new File(filePath);
        long l = System.currentTimeMillis();
        fetchPics(video, 10);
        System.out.println("花费时间：" + (System.currentTimeMillis() - l));
    }

    /**
     * 按等间距抓取视频截图
     *
     * @param file   视频文件
     * @param picNum 要抓取的数量
     */
    public static void fetchPics(File file, int picNum) throws Exception {
        FFmpegFrameGrabber videoFile = new FFmpegFrameGrabber(file);
        videoFile.start();
        int length = videoFile.getLengthInFrames();
        int passFrames = 10;//跳过前面的若干帧避免黑屏
        for (int i = 0; i < passFrames; i++) {
            videoFile.grabFrame();
        }

        int interval = (length - passFrames) / picNum;//截图间隔
        int index = 0;//当前抓取截图的帧数
        int nextIndex = index + interval;//下一次抓取的帧数
        int imageNo = 0;//图片名称序号
        int grabedFrames = 0;

        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imgSuffix = "jpg";
        File targetFile;

        while (index < length && grabedFrames < picNum) {
            targetFile = new File("images/got" + (++imageNo) + ".jpg");

            for (int i = 0; i < interval; i++) {
                videoFile.grabFrame();
            }

            Buffer[] buffers = null;
            Frame frame = null;
            while (buffers == null && index < nextIndex) {
                frame = videoFile.grabFrame();
                index++;
                if (frame != null) {
                    buffers = frame.image;
                }
            }

            if (buffers == null) {
                continue;
            }

            BufferedImage srcBi = converter.getBufferedImage(frame);
            int owidth = srcBi.getWidth();
            int oheight = srcBi.getHeight();
            // 对截取的帧进行等比例缩放
            int width = 1125;
            int height = (int) (((double) width / owidth) * oheight);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            try {
                ImageIO.write(bi, imgSuffix, targetFile);
                grabedFrames++;
            } catch (Exception e) {
                logger.error("error write images ", e);
            }

            while (index < nextIndex) {
                videoFile.grabFrame();
                index++;
            }
            nextIndex += interval;
        }
        videoFile.close();
    }

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param file      源视频文件
     * @param framefile 截取帧的图片存放路径
     * @throws Exception
     */
    public static void fetchPic(File file, String framefile, int passFrames) throws Exception {
        FFmpegFrameGrabber videoFile = new FFmpegFrameGrabber(file);
        videoFile.start();
        int length = videoFile.getLengthInFrames();

        File targetFile = new File(framefile);
        int i = 0;
        Frame frame = null;
        while (i < length) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            frame = videoFile.grabFrame();
            if ((i > passFrames) && (frame.image != null)) {
                break;
            }
            i++;
        }

        String imgSuffix = "jpg";
        if (framefile.indexOf('.') != -1) {
            String[] arr = framefile.split("\\.");
            if (arr.length >= 2) {
                imgSuffix = arr[1];
            }
        }

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 1125;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        try {
            ImageIO.write(bi, imgSuffix, targetFile);
        } catch (Exception e) {
            logger.error("error write images ", e);
        }
        videoFile.stop();
    }

    /**
     * 获取视频时长，单位为秒
     *
     * @param file
     * @return 时长（s）
     */
    public static Long getVideoTime(File file) {
        Long times = 0L;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
            ff.start();
            times = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (Exception e) {
            logger.error("error get video time ", e);
        }
        return times;
    }
}
