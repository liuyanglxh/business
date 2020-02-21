package com.liuyang.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by lx on 16/12/12.
 */
public class GzipUtil {
    private static Logger logger = LoggerFactory.getLogger(GzipUtil.class);

    /**
     * 根据前2个字节是否是 0x8b1f判断是否是gzip压缩的数据
     *
     * @param value
     * @return
     */
    public static boolean isGzip(String value) {
        try {
            if (StringUtils.isEmpty(value)) {
                return false;
            }

            byte[] bytes = value.getBytes("ISO-8859-1");
            if (bytes.length < 2) {
                return false;
            }
            int magic = ((bytes[1] & 0xff) << 8) | (bytes[0] & 0xff);
            return magic == GZIPInputStream.GZIP_MAGIC;
        } catch (Exception e) {
            logger.error("isGzip check error", e);
        }
        return false;
    }

    /**
     * 字符串的压缩
     *
     * @param str 待压缩的字符串
     * @return 返回压缩后的字符串
     * @throws IOException
     */
    public static String compress(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String comStr = str;

        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            try {
                gzip.close();
                comStr = out.toString("ISO-8859-1");
            } catch (IOException e) {
                logger.error("gzip close error", e);
            }
        } catch (IOException e) {
            logger.error("compress failed", e);
        }
        return comStr;
    }

    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     */
    public static String unCompress(String str) {
        if (StringUtils.isEmpty(str) || !isGzip(str)) {
            return str;
        }
        String unComStr = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream gzip = null;

        try {
            in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            // 使用默认缓冲区大小创建新的输入流
            gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n = 0;
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }

            unComStr = out.toString();
        } catch (IOException e) {
            logger.error("unCompress failed", e);
        } finally {
            if (null != gzip) {
                try {
                    gzip.close();

                } catch (IOException e) {
                    logger.error("gzip close error", e);
                }
            }

        }
        return unComStr;
    }

    /**
     * 使用gzip进行压缩
     */
    public static String gzip(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return new sun.misc.BASE64Encoder().encode(out.toByteArray());
    }

    /**
     * <p>Description:使用gzip进行解压缩</p>
     *
     * @param compressedStr
     * @return
     */
    public static String gunzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }
}
