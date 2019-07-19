package com.vergo.skin.library;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.vergo.skin.library.model.SkinCache;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Created by Fenghj on 2019/7/19.</p>
 */
public class SkinManager {
    private static volatile SkinManager instance;
    private static final String ADD_ASSET_PATH = "addAssetPath"; // 反射方法名

    private Application mApplication;
    private Resources appResources; // 用于加载app内置资源
    private Resources skinResources; // 用于加载皮肤包资源

    private String skinPackageName; // 皮肤包资源所在包名（注：皮肤包不在app内，也不限包名）
    private boolean isDefaultSkin = true;  // 应用默认皮肤（app内置）
    private Map<String, SkinCache> cacheSkin;

    private SkinManager(Application application) {
        mApplication = application;
        appResources = application.getResources();
        cacheSkin = new HashMap<>();
    }

    public static SkinManager init(Application application) {
        if(instance == null) {
            synchronized (SkinManager.class) {
                if(instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
        return instance;
    }

    public static SkinManager getInstance() {
        if(instance == null) {
            throw new RuntimeException("you should init SkinManager frist");
        } else {
            return instance;
        }
    }

    /**
     * 加载皮肤包资源，皮肤包资源为空则加载app内置资源
     * @param skinPath 皮肤包路径
     */
    public void loaderSkinResources(String skinPath) {
        // 如果皮肤包资源路径地址为空或者
        if(TextUtils.isEmpty(skinPath) || !new File(skinPath).exists()) {
            isDefaultSkin = true;
            return;
        }
        // 优化：app冷启动、热启动可以取缓存对象
        if (cacheSkin.containsKey(skinPath)) {
            isDefaultSkin = false;
            SkinCache skinCache = cacheSkin.get(skinPath);
            if (null != skinCache) {
                skinResources = skinCache.getSkinResources();
                skinPackageName = skinCache.getSkinPackageName();
                return;
            }
        }

        try {
            // 创建资源管理器（此处不能用：application.getAssets）
            AssetManager assetManager = AssetManager.class.newInstance();
            // 由于AssetManager中的addAssetPath和setApkAssets方法都被@hide，目前只能通过反射去执行方法
            Method addAssetPath = assetManager.getClass().getDeclaredMethod(ADD_ASSET_PATH, String.class);
            // 设置私有方法可访问
            addAssetPath.setAccessible(true);
            // 执行addAssetPath方法
            addAssetPath.invoke(assetManager, skinPath);
            //==============================================================================
            // 如果还是担心@hide限制，可以反射addAssetPathInternal()方法，参考源码366行 + 387行
            //==============================================================================

            // 创建加载外部的皮肤包(net163.skin)文件Resources（注：依然是本应用加载）
            skinResources = new Resources(assetManager, appResources.getDisplayMetrics(),
                    appResources.getConfiguration());
            skinPackageName = mApplication.getPackageManager().getPackageArchiveInfo(skinPath,
                    PackageManager.GET_ACTIVITIES).packageName;

            isDefaultSkin = TextUtils.isEmpty(skinPackageName);

            if (!isDefaultSkin) {
                cacheSkin.put(skinPath, new SkinCache(skinResources, skinPackageName));
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常，预判：通过skinPath获取skinPacakageName失败！
            isDefaultSkin = true;
        }
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }

    /**
     * 参考：resources.arsc资源映射表
     * 通过ID值获取资源 Name 和 Type
     * 如果没有皮肤包则加载app内置资源ID，反之加载皮肤包指定资源ID
     *
     * @param resourceId 资源ID值
     */
    private int getSkinResourceIds(int resourceId) {
        if(isDefaultSkin()) return resourceId;

        String resourceName = appResources.getResourceEntryName(resourceId);
        String resourceType = appResources.getResourceTypeName(resourceId);

        int skinResourceId = skinResources.getIdentifier(resourceName, resourceType, skinPackageName);

        isDefaultSkin = skinResourceId == 0;
        return skinResourceId == 0 ? resourceId : skinResourceId;
    }

    //==============================================================================================

    public int getColor(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getColor(ids) : skinResources.getColor(ids);
    }

    public ColorStateList getColorStateList(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getColorStateList(ids) : skinResources.getColorStateList(ids);
    }

    // mipmap和drawable统一用法（待测）
    public Drawable getDrawableOrMipMap(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getDrawable(ids) : skinResources.getDrawable(ids);
    }

    public String getString(int resourceId) {
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getString(ids) : skinResources.getString(ids);
    }

    // 返回值特殊情况：可能是color / drawable / mipmap
    public Object getBackgroundOrSrc(int resourceId) {
        // 需要获取当前属性的类型名Resources.getResourceTypeName(resourceId)再判断
        String resourceTypeName = appResources.getResourceTypeName(resourceId);

        switch (resourceTypeName) {
            case "color":
                return getColor(resourceId);

            case "mipmap": // drawable / mipmap
            case "drawable":
                return getDrawableOrMipMap(resourceId);
        }
        return null;
    }

    // 获得字体
    public Typeface getTypeface(int resourceId) {
        // 通过资源ID获取资源path，参考：resources.arsc资源映射表
        String skinTypefacePath = getString(resourceId);
        // 路径为空，使用系统默认字体
        if (TextUtils.isEmpty(skinTypefacePath)) return Typeface.DEFAULT;
        return isDefaultSkin ? Typeface.createFromAsset(appResources.getAssets(), skinTypefacePath)
                : Typeface.createFromAsset(skinResources.getAssets(), skinTypefacePath);
    }
}
