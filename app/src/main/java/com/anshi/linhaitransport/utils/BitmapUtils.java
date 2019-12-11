package com.anshi.linhaitransport.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.UUID;

public class BitmapUtils {
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator+"共享钓点图片"+File.separator;


    private static Bitmap scaleWithWH(Bitmap src, double w, double h) {
 if(w ==0|| h ==0|| src ==null) {
    return src;
    }   else{
  // 记录src的宽高
     int width = src.getWidth();
  int height = src.getHeight();
  // 创建一个matrix容器
  Matrix matrix =new Matrix();
 // 计算缩放比例
 float scaleWidth = (float) (w / width);
 float scaleHeight = (float) (h / height);
 // 开始缩放
 matrix.postScale(scaleWidth, scaleHeight);
 // 创建缩放后的图片
  return Bitmap.createBitmap(src,0,0, width, height, matrix,true);
}
    }


    public Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
     Resources resources = gContext.getResources();
      float scale = resources.getDisplayMetrics().density;
      Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
      bitmap = scaleWithWH(bitmap, 300*scale, 300*scale);
      Bitmap.Config bitmapConfig = bitmap.getConfig();
     // set default bitmap config if none
     if(bitmapConfig == null) {
      bitmapConfig = Bitmap.Config.ARGB_8888;
     }
    // resource bitmaps are imutable,
    // so we need to convert it to mutable one
    bitmap = bitmap.copy(bitmapConfig, true);
    Canvas canvas = new Canvas(bitmap);
    // new antialised Paint
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
     // text color - #3D3D3D
     paint.setColor(Color.RED);
     paint.setTextSize((int) (18 * scale));
     paint.setDither(true); //获取跟清晰的图像采样
     paint.setFilterBitmap(true);//过滤一些
     Rect bounds = new Rect();
     paint.getTextBounds(gText, 0, gText.length(), bounds);
     int x = 30;
     int y = 30;
     canvas.drawText(gText, x * scale, y * scale, paint);
     return bitmap;
    }

    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }


    /*
     * bitmap转base64
     * */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        //c.drawColor(Color.WHITE);
        /*如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    public static String viewSaveToImage(View view, Context context) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        FileOutputStream fos;
        String imagePath = "";
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录

                File file = new File(Environment.getExternalStorageDirectory()+File.separator+"Download", Calendar.getInstance().getTimeInMillis()+".png");
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            } else
                throw new Exception("创建文件失败!");

            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Uri uri = Uri.fromFile(new File(imagePath));
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(context, "保存在:"+imagePath, Toast.LENGTH_LONG).show();
        }

        view.destroyDrawingCache();
        return imagePath;
    }


    public static Bitmap getHttpBitmap(String url) {
        Bitmap bitmap = null;
        try
        {
            URL pictureUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) pictureUrl.openConnection();
            InputStream in = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static String saveBitmap2file(Bitmap bmp, Context context) {

        String savePath;
        String fileName = generateFileName() + ".jpg";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            Toast.makeText(context, "保存失败！", Toast.LENGTH_SHORT).show();
            return null;
        }
        File filePic = new File(savePath , fileName);
        try {
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            //Toast.makeText(context, "保存成功,位置:" + filePic.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            Toast.makeText(context,"保存在"+savePath, Toast.LENGTH_SHORT).show();
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath+fileName)));
        return filePic.getAbsolutePath();
    }
 /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static String compressImage(Context context,Bitmap image) {
        if (null!=image){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 90;
            while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset(); // 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            }
            Log.e("压缩之后的大小",baos.toByteArray().length / 1024+"");
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
            return saveBitmap2file(bitmap,context);
        }
        return  null;
    }
    /**
     *
     * @param inSampleSize  可以根据需求计算出合理的inSampleSize
     */
    public static String compress(int inSampleSize,String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置此参数是仅仅读取图片的宽高到options中，不会将整张图片读到内存中，防止oom
        options.inJustDecodeBounds = true;
        Bitmap emptyBitmap = BitmapFactory.decodeFile(filePath, options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap resultBitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        File file = new File(SD_PATH, generateFileName() + ".jpg");
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmp;
    }

    /**
     * 处理旋转后的图片
     * @param originpath 原图路径
     * @return 返回修复完毕后的图片路径
     */
    public static Bitmap amendRotatePhoto(String originpath) {

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);

        // 把原图压缩后得到Bitmap对象

        // 修复图片被旋转的角度

        // 保存修复后的图片并返回保存后的图片路径
        return rotaingImageView(angle, BitmapFactory.decodeFile(originpath));
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    private static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }


}
