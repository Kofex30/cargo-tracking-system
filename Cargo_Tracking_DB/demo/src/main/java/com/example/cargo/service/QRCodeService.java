package com.example.cargo.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.UUID;

@Service
public class QRCodeService {
    // Bu metot benzersiz QR kod içeriği üretiyor (örneğin UUID kullanarak)
    public String generateUniqueQRCode() {
        return UUID.randomUUID().toString();
    }
    // İstersen, BufferedImage şeklinde QR kod görseli üretmek için:
    public BufferedImage generateQRCodeImage(String barcodeText) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                int grayValue = (bitMatrix.get(x, y) ? 0 : 1) * 255;
                int color = (grayValue << 16) | (grayValue << 8) | grayValue;
                image.setRGB(x, y, color);
            }
        }
        return image;
    }
}
