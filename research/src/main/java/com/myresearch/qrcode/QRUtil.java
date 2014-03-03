package com.myresearch.qrcode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRUtil {

 /**
   * ���루���ı����ɶ�ά�룩
  * 
  * @param content ��ά���е�����
  * @param width ��ά��ͼƬ���
  * @param height ��ά��ͼƬ�߶�
  * @param imagePath ��ά��ͼƬ���λ��
  * @return ͼƬ��ַ
  */
 public static String encode(String content, int width, int height, String imagePath) {

  Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
  // ���ñ�������Ϊutf-8
  hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
  // ���ö�ά�������������ΪH����ߣ�
  hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

  BitMatrix byteMatrix = null;
  try {
   // ���ɶ�ά��
   byteMatrix = new MultiFormatWriter().encode(content,
     BarcodeFormat.QR_CODE, width, height, hints);
   File file = new File(imagePath);
   MatrixToImageWriter.writeToFile(byteMatrix, "png", file);
  } catch (IOException e) {
   e.printStackTrace();
  } catch (WriterException e) {
   e.printStackTrace();
  }

  return imagePath;

 }

 /**
  * ���루��ȡ��ά��ͼƬ�е��ı���Ϣ��
  * @param imagePath ��ά��ͼƬ·��
  * @return �ı���Ϣ
  */
 public static String decode(String imagePath) {
  // ���ص��ı���Ϣ
  String content = "";

  try {
   // ����ͼƬ�ļ�
   File file = new File(imagePath);
   if (!file.exists()) {
    return content;
   }
   BufferedImage image = null;
   image = ImageIO.read(file);
   if (null == image) {
    return content;
   }
   // ����
   LuminanceSource source = new BufferedImageLuminanceSource(image);
   BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
   Hashtable hints = new Hashtable();
   hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
   Result rs = new MultiFormatReader().decode(bitmap, hints);
   content = rs.getText();
  } catch (IOException e) {
   e.printStackTrace();
  } catch (ReaderException e) {
   e.printStackTrace();
  }
  return content;
 }

 /**
  * ͼƬ��ˮӡ
  * @param bgImage ����ͼ
  * @param waterImg ˮӡͼ
  * @param uniqueFlag ���ɵ���ͼƬ�����е�Ψһ��ʶ��������֤���ɵ�ͼƬ���Ʋ��ظ������Ϊ�ջ�Ϊnull,��ʹ�õ�ǰʱ����Ϊ��ʶ
  * @return ��ͼƬ·��
  */
 public static String addImageWater(String bgImage, String waterImg ,String uniqueFlag) {

  int x = 0;
  int y = 0;
  String newImgPath = "";

  if(null == uniqueFlag){
   uniqueFlag = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
  }else if(uniqueFlag.trim().length() < 1){
   uniqueFlag = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
  }

  try {
   File file = new File(bgImage);
   String fileName = file.getName();
   Image image = ImageIO.read(file);
   int width = image.getWidth(null);
   int height = image.getHeight(null);
   BufferedImage bufferedImage = new BufferedImage(width, height,
     BufferedImage.TYPE_INT_RGB);
   Graphics2D g = bufferedImage.createGraphics();
   g.drawImage(image, 0, 0, width, height, null);

   Image waterImage = ImageIO.read(new File(waterImg)); // ˮӡ�ļ�
   int width_water = waterImage.getWidth(null);
   int height_water = waterImage.getHeight(null);
   g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
     1));

   int widthDiff = width - width_water;
   int heightDiff = height - height_water;

   x = widthDiff / 2;
   y = heightDiff / 2;

   g.drawImage(waterImage, x, y, width_water, height_water, null); // ˮӡ�ļ�����
   g.dispose();



   if(bgImage.contains(fileName)){
    newImgPath = bgImage.replace(fileName, uniqueFlag+fileName);
   }
   File newImg = new File(newImgPath);
   ImageIO.write(bufferedImage, "png", newImg);

   File waterFile = new File(waterImg);

   if(file.exists()){
    file.delete();
   }

   if(waterFile.exists()){
    waterFile.delete();
   }
  } catch (IOException e) {
   e.printStackTrace();
  }

  return newImgPath;
 }

 /**
   * ͼƬ����
  * @param filePath ͼƬ·��
  * @param height ���ŵ��߶�
  * @param width ���ſ��
  * @param fill ������ʱ�Ƿ���� trueΪ��ף���ά���Ǻڰ�ɫ���������ʱ������Ϊtrue
  * @return ��ͼƬ·��
  */
 public static String resizeImg(String filePath, int width,int height, boolean fill) {

  String newImgPath = "";

  try {
   double ratio = 0; // ���ű���
   File f = new File(filePath);
   String fileName = f.getName();
   BufferedImage bi = ImageIO.read(f);
   Image itemp = bi.getScaledInstance(width, height,BufferedImage.SCALE_SMOOTH);

   if(height != 0 && width!= 0 ){
    // �������
    if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
     if (bi.getHeight() > bi.getWidth()) {
      ratio = (new Integer(height)).doubleValue()
        / bi.getHeight();
     } else {
      ratio = (new Integer(width)).doubleValue() / bi.getWidth();
     }
     AffineTransformOp op = new AffineTransformOp(AffineTransform
       .getScaleInstance(ratio, ratio), null);
     itemp = op.filter(bi, null);
    }
   }

   if (fill) {
    BufferedImage image = new BufferedImage(width, height,
      BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 0, width, height);
    if (width == itemp.getWidth(null)){
     g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
       itemp.getWidth(null), itemp.getHeight(null),
       Color.white, null);
    }else{
     g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
       itemp.getWidth(null), itemp.getHeight(null),
       Color.white, null);
    }

    g.dispose();
    itemp = image;
   }
   String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
   if(filePath.contains(fileName)){
    newImgPath = filePath.replace(fileName, now+fileName);
   }

   File newImg = new File(newImgPath);
   ImageIO.write((BufferedImage)itemp, "png", newImg);
  } catch (IOException e) {
   e.printStackTrace();
  }

  return newImgPath;
 }


 /**
  * ͼƬ��ӱ߿�
  * @param mainImgPath Ҫ�ӱ߿��ͼƬ
  * @param bgImgPath ����ͼ��ʵ�����ǽ�ͼƬ���ڱ���ͼ�ϣ�ֻ���ñ���ͼ�ı߿�Ч����
  * @return ������ɵ�ͼƬ·��
  */
 public static String addWaterBorder(String mainImgPath,String bgImgPath){

  String borderImgPath = "";

  try {
   File f = new File(mainImgPath);

   BufferedImage bi;

   bi = ImageIO.read(f);

   //����ͼ��������ͼ��4���أ�������Ϊ�һ��ı���ͼ�ı߿�Ч���Ĵ�С������4���أ�
   //��ͼ�ܱ߱ȱ���ͼ��4���������ܰѱ���ͼ�ı߿�Ч��������ʾ����
   int width = bi.getWidth();
   int height = bi.getHeight();

   int bgWidth = width+4;
   int bgHeight = height+4;

   String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
   borderImgPath = QRUtil.addImageWater(QRUtil.resizeImg(bgImgPath, bgHeight, bgWidth, true), mainImgPath,now);

   if(f.exists()){
    f.delete();
   }
  } catch (IOException e) {
   e.printStackTrace();
  }


  return borderImgPath;
 }

 public static void main(String[] args) {

  /**����һ��ʼ***********���ɳ����ά��*************/
  //��ά������
  String content = "http://www.taobao.com";
  //��ά����
  int width = 300;
  //��ά��߶�
  int height = 300;
  //��ά���ŵ�ַ
  //String imagePath = "D:\\��Ŀ\\��ά��\\git��ͼ.png";
  //���ɶ�ά��,���ص������ɺõĶ�ά��ͼƬ������·��
  String qrImgPath = QRUtil.encode(content, width, height, new File("qrcode.png").getAbsolutePath());
  /**����һ����***********������ɲ���ͼƬ�Ķ�ά�룬���ⲽ�Ѿ������*************/


  /**���ֶ���ʼ***********������ɴ�ͼƬ��ͼƬ�����߿�Ķ�ά�룬�⿪�ⲿ��ע��*************/

  //����ˮӡͼƬ,Ϊ��֤��ά��Ķ�ȡ��ȷ��ͼƬ��������ά��ͼƬ�����֮һ��������Ϊ����֮һ
//  String waterImgPath = QRUtil.resizeImg("d:/qr/heihei.jpg", width/6, height/6, true);
//  
//  //���ɴ���ͼƬ�Ķ�ά�룬���ص������ɺõĶ�ά��ͼƬ������·��
//  String qrImage = QRUtil.addImageWater(qrImgPath, waterImgPath,"thatway");
  /**���ֶ�����***********������ɴ�ͼƬ��ͼƬ�����߿�Ķ�ά�룬�⿪�ⲿ��ע��*************/


  /**��������ʼ�����������ܺͲ��ֶ����棩***********������ɴ�ͼƬ��ͼƬ���߿�Ķ�ά�룬�⿪�ⲿ��ע��****/

  //����ˮӡͼƬ,Ϊ��֤��ά��Ķ�ȡ��ȷ��ͼƬ��������ά��ͼƬ�����֮һ��������Ϊ����֮һ
  //d:/qr/heihei.png ��ͼƬ��Ҫ���ڶ�ά���м������ͼ
  //String waterImgPath = QRUtil.resizeImg("D:\\��Ŀ\\��ά��\\git��ͼ.png", width/6, height/6, true);

  //d:/qr/qr_bg.png����ͼƬ���Լ����ñ߿����Ч���ı߿��ͼ
  //String tempImg = QRUtil.addWaterBorder(waterImgPath, "D:\\��Ŀ\\��ά��\\���׼ܹ�ͼ.png");

  //���ɴ��б߿�ͼƬ�Ķ�ά��,���ص������ɺõĶ�ά��ͼƬ������·��
  //String qrImage = QRUtil.addImageWater(qrImgPath, tempImg,"thatway");
  /**����������***********������ɴ�ͼƬ��ͼƬ���߿�Ķ�ά�룬�⿪�ⲿ��ע��*************/



  /*******����һ�½���******/
  //System.out.println(QRUtil.decode(new File(qrImgPath)));;

 }

}