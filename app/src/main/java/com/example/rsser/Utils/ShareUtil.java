package com.example.rsser.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.rsser.DAO.Source;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareUtil {
    private Context context;

    public ShareUtil(Context context) {
        this.context = context;
    }

    /**
     * 分享文本
     * @param title 分享标题
     * @param content 分享内容
     */
    public void shareText(String title, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    /**
     * 分享二维码
     * @param content 二维码内容
     * @param title 分享标题
     */
    public void shareQRCode(String content, String title) {
        // 生成二维码
        Bitmap qrCodeBitmap = QRUtil.generateQRCode(content, 500, 500);

        try {
            // 创建临时文件保存二维码
            File qrCodeFile = createTempFile("qrcode", ".png");
            saveBitmapToFile(qrCodeBitmap, qrCodeFile);

            // 获取文件 URI
            Uri contentUri = getFileUri(qrCodeFile);

            // 分享二维码
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "二维码内容：" + content);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(shareIntent, title));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "二维码分享失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * RSS源分享
     * @param source RSS源对象
     */
    public void shareRSSSource(Source source) {
        // 构建分享的文本内容
        String shareText = String.format("RSS订阅源：\n标题：%s\n链接：%s\n描述：%s",
                source.getTitle(),
                source.getUrl(),
                source.getDescription());

        shareText(source.getTitle(), shareText);
    }

    /**
     * RSS源二维码分享
     * @param source RSS源对象
     */
    public void shareRSSSourceQRCode(Source source) {
        shareQRCode(source.getUrl(), "分享RSS源二维码");
    }

    /**
     * 分享图片
     * @param imageFile 图片文件
     * @param title 分享标题
     */
    public void shareImage(File imageFile, String title) {
        try {
            // 获取文件 URI
            Uri contentUri = getFileUri(imageFile);

            // 分享图片
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(shareIntent, title));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "图片分享失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建临时文件
     * @param prefix 文件名前缀
     * @param suffix 文件扩展名
     * @return 创建的临时文件
     * @throws IOException 文件创建异常
     */
    private File createTempFile(String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix, context.getCacheDir());
    }

    /**
     * 将 Bitmap 保存到文件
     * @param bitmap 位图
     * @param file 目标文件
     * @throws IOException 文件写入异常
     */
    private void saveBitmapToFile(Bitmap bitmap, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
    }

    /**
     * 获取文件的安全 URI
     * @param file 目标文件
     * @return 文件 URI
     */
    private Uri getFileUri(File file) {
        return FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".fileprovider",
                file
        );
    }
}