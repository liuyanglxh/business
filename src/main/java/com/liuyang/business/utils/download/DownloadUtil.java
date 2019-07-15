package com.liuyang.business.utils.download;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtil {

    public static void main(String[] args) throws Exception {
        String source ="https://ugcbsy.qq.com/uwMROfz2r57EIaQXGdGnC2deB3d8zuLRhDFyeUweKwuaKjKr/szg_63278430_50001_d07148998ee74bcdb54188e6724b90d8.f622.mp4?sdtfrom=v1010&amp;guid=9dab4353b657cea61107650d6ce07a0b&amp;vkey=697CBF1CAAB9F8863D8622C892B06EA04BFC75150BFBC9B33E2DCC801D9BC854B1A464D3DF2380F6A7C76D3641FD119DE61CFBC065233864DEE664164627186DF32DAC879CB4B08FE78780E9B4920970BAE36A3D05F099F01643BED079C2EDF8885F0CB7B3C192EC230DF18F9441484EA8B0B0D38E43C2E73FB264612E62D40D";
        String imgLac = "images/image.mp4";
        System.out.println(downloadFile(source, imgLac));
    }

    /**
     * @param fileUrl   远程地址
     * @param fileLocal 本地路径
     */
    public static boolean downloadFile(String fileUrl, String fileLocal) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        System.out.println(code);
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        //读文件流
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
        byte[] buffer = new byte[2048];
        int count;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        return true;
    }

}
