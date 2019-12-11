package com.anshi.linhaitransport.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;


/**
 * created by yulu 2017年2月20日 21:51:24
 */
public class SharedPreferenceUtils {
    public static final String FILE_NAME = "commonfish";
    public static String getString(Context context, String key){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getString(key,"");
    }
    //�������
    public static int getInt(Context context, String key){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getInt(key,0);
    }
    //���С��
    public static float getFloat(Context context, String key){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getFloat(key,0);
    }
    //��ò���
    public static long getLong(Context context, String key){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getLong(key,0);
    }
    // ֵ
    public static boolean getBoolean(Context context, String key){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }
    //�����ַ���
    public static void saveString(Context context, String key, String value){
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }
    //��������
    public static void saveInt(Context context, String key, int value){
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }
    //���渡��
    public static void saveLong(Context context, String key, long value){
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, value)
                .apply();
    }
    public static void saveFloat(Context context, String key, float value){
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putFloat(key, value)
                .apply();
    }
    //���沼��
    public static void saveBoolean(Context context, String key, boolean value){
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }
    public static void deleteString(Context context, String key){
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(key)
                .apply();
    }
    public static void clear(Context context){
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }
    /**
     * 存储List集合
     * @param context 上下文
     * @param key 存储的键
     * @param list 存储的集合
     */
    public static void putList(Context context, String key, List<? extends Serializable> list) {
        try {
              put(context,key,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取List集合
     * @param context 上下文
     * @param key 键
     * @param <E> 指定泛型
     * @return List集合
     */
    public static <E extends Serializable> List<E> getList(Context context, String key) {
        try {
            return (List<E>) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**存储对象*/
    private static void put(Context context, String key, Object obj)
            throws IOException
    {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos  = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        saveString(context, key, objectStr);
    }

    /**获取对象*/
    private static Object get(Context context, String key)
            throws IOException, ClassNotFoundException
    {
        String wordBase64 = getString(context, key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[] objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais     = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }
}
