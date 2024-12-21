package com.example.rsser.Utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.util.HashMap;
import java.util.Map;

public class QRUtil {

    /**
     * 生成二维码
     *
     * @param content 二维码内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return 生成的二维码位图
     */
    public static Bitmap generateQRCode(String content, int width, int height) {
        try {
            // 配置参数
            QRCodeWriter writer = new QRCodeWriter();
            Map<com.google.zxing.EncodeHintType, Object> hints = new HashMap<>();
            hints.put(com.google.zxing.EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(com.google.zxing.EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            // 通过编码生成二维码矩阵
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建位图并填充像素颜色
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // 给位图中的每个像素赋值颜色（黑或白）
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 如果出错，返回空
        }
    }

    /**
     * 从 Bitmap 中解析二维码内容
     *
     * @param bitmap 二维码位图
     * @return 解析出的内容
     */
    public static String decodeQRCode(Bitmap bitmap) {
        try {
            // 将 Bitmap 转换为 int 数组
            int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            // 使用 RGBLuminanceSource 处理图像像素
            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);

            // 将数据封装为 BinaryBitmap 并解析
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();

            Result result = reader.decode(binaryBitmap); // 解析二维码内容

            return result.getText(); // 返回解析的文本内容
        } catch (ReaderException e) {
            e.printStackTrace();
            return null; // 如果解析失败，返回空
        }
    }
}