package com.rzm.commonlibrary.general;

import android.content.Context;
import android.util.Log;

import com.rzm.commonlibrary.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by renzhenming on 2017/8/12.
 * 没有考虑分包的情况，所以缺点就是每次修复都要将整个dex文件注入
 *
 * 热修复实现的一些思路：
 * 1.可以把出错的class重新打成一个fix.dex,但是这种情况除非不进行代码混淆，所以不可取
 * 2.分包，把不会出错的分成一个dex，（尽量不要混淆），其他的留在另一个dex，方法没有超过65535，name
 * Android studio 需要去官网找分包，而且运行的时候如果dex过大会影响启动速度
 * 3.直接下载整个dex包（本类的实现方式），然后进行注入修复，问题就是dex可能比较大 2M
 *
 * 相比于阿里的热修复有点在于：
 * 这总实现方式可以增加方法和类，而阿里的热修复是不可以的，但是这种方式尚不能增加资源文件
 * 实现插件开发后就可以了
 */

public class FixDexManager {
    private static final String TAG = "FixDexManager2";
    private final Context mContext;
    private final File mDexDir;

    public FixDexManager(Context context) {
        this.mContext = context;
        //获取系统能够访问的dex目录
        this.mDexDir = context.getDir("odex",Context.MODE_PRIVATE);
    }

    public void fixDex(String fixDexPath) throws Exception {
        //2.获取下载好的补丁dexElement
        //2.1.移动到系统能够访问的dex目录下  ClassLoader
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }
        //将这个文件拷贝到我们设置好的系统可以访问的那个dex目录中
        File desFile = new File(mDexDir, srcFile.getName());
        //文件名重复的可能性很小，所以不做覆盖了
        if (desFile.exists()) {
//            Log.d(TAG, "patch [" + fixDexPath + "] has be loaded.");
//            return;
            desFile.delete();
        }
        //执行完copy之后，修复好的dex文件就会被拷贝到desFile这个路径文件中
        FileUtil.copyFile(srcFile, desFile);// copy to patch's directory
        //2.2 ClassLoader 读取fixDex路径, 为什么加入集合，已启动可能就要修复，有可能存在多个dex
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(desFile);

        fixDexFiles(fixDexFiles);
    }

    //把dexElements注入到classLoader中
    private void injectDexElements(ClassLoader classLoader, Object dexElements) throws Exception{
        //先获取pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        //获取pathList中的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);

        //利用反射进行注入
        dexElementsField.set(pathList,dexElements);
    }

    private static Object combineArray(Object arrayLhs,Object arrayRhs){
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i+Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; k++) {
            if (k<i){
                Array.set(result,k,Array.get(arrayLhs,k));
            }else{
                Array.set(result,k,Array.get(arrayRhs,k-i));
            }
        }
        return result;
    }

    /**
     * 从classloader中获取dexElements
     * @param classLoader
     * @return
     */
    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception{
        //先获取pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);

        //获取pathList中的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        Object dexElements = dexElementsField.get(pathList);

        return dexElements;
    }

    public void loadFixDex() throws Exception{
        File[] dexFiles = mDexDir.listFiles();
        List<File> fixDexFiles = new ArrayList<>();
        for (File dexFile : dexFiles) {
            if (dexFile.getName().endsWith(".dex")){
                fixDexFiles.add(dexFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    /**
     * 修复dex
     * @param fixDexFiles
     */
    private void fixDexFiles(List<File> fixDexFiles) throws Exception{
        //1.先获取已经运行的dexElement
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Object applicationDexElements = getDexElementByClassLoader(applicationClassLoader);

        File optimizedDirectory = new File(mDexDir,"odex");
        if (!optimizedDirectory.exists()){
            optimizedDirectory.mkdirs();
        }

        //修复
        for (File fixDexFile : fixDexFiles) {
            //参数：
            // String dexPath,  dex路径
            // File optimizedDirectory,
            // String librarySearchPath,  so文件位置
            // ClassLoader parent   父classloader
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),//dex路径,必须要在应用目录下的odex文件中
                    optimizedDirectory,
                    null,
                    applicationClassLoader
            );

            Object fixDexElements = getDexElementByClassLoader(fixDexClassLoader);

            //3.将下载的dex插入到已经运行的dexElement的最前边,合并
            //applicationClassLoader 数组合并fixDexElements数组
            applicationDexElements = combineArray(fixDexElements, applicationDexElements);

            //把合并的数组注入到原来的类中 applicationClassLoader
            injectDexElements(applicationClassLoader, applicationDexElements);
        }
    }
}
