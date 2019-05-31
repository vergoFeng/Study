##回顾
Toolbar有两种使用方式
* 作为ActionBar来使用
* 作为独立的控件来使用。

所以在Toolbar中添加菜单也有两种方式。

##添加菜单
####新建菜单资源
在res下新建menu文件夹用来存放菜单资源xml。

    <menu xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto">
        <item
            android:id="@+id/toolbar_search"
            android:icon="@mipmap/ic_search"
            android:title="搜索"
            android:orderInCategory="0"
            app:actionViewClass="android.support.v7.widget.SearchView"
            app:showAsAction="ifRoom|collapseActionView" />
    
        <item
            android:id="@+id/toolbar_collection"
            android:icon="@mipmap/ic_launcher"
            android:orderInCategory="1"
            android:title="收藏"
            app:showAsAction="never" />
        <item
            android:id="@+id/toolbar_share"
            android:orderInCategory="2"
            android:title="分享"
            app:showAsAction="never" />
        <item
            android:id="@+id/toolbar_fontsize"
            android:orderInCategory="3"
            android:title="字号"
            app:showAsAction="never" />
    </menu>

* orderInCategory
  设置菜单项的排列顺序，必须设置大于等于0的整数值。数值小的排列在前，如果值相等，则按照xml中的顺序展现。
* title
  菜单项的标题。
* icon
  菜单项的图标。
* showAsAction
  该属性有五个值，可以混合使用。
  * always
    总是显示在Toolbar上。
  * ifRoom
    如果Toolbar上还有空间，则显示，否则会隐藏在溢出列表中。
  * never
    永远不会显示在Toolbar上，只会在溢出列表中出现。
  * withText
    文字和图标一起显示。
  * collapseActionView
    声明了这个操作视窗应该被折叠到一个按钮中，当用户选择这个按钮时，这个操作视窗展开。一般要配合ifRoom一起使用才会有效。

####Activity中添加
#####Toolbar作为ActionBar使用
重写Activity的onCreateOptionsMenu方法并实现相关逻辑

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
监听菜单项的事件重写Activity的onOptionsItemSelected方法

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_collection:
                Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_fontsize:
                Toast.makeText(this, "字号", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_model:
                Toast.makeText(this, "模式", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

#####Toolbar作为独立控件使用

    toolbar.inflateMenu(R.menu.menu_main);
监听菜单项的事件则通过setOnMenuItemClickListener设置

    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.toolbar_collection:
                    Toast.makeText(MainActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.toolbar_share:
                    Toast.makeText(MainActivity.this, "分享", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.toolbar_fontsize:
                    Toast.makeText(MainActivity.this, "字号", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.toolbar_model:
                    Toast.makeText(MainActivity.this, "模式", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });
#####效果

![菜单效果](http://upload-images.jianshu.io/upload_images/680151-92812d1fd1166a02.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/360)

####弹出菜单样式调整
上面效果图会发现弹出的菜单是深灰色的背景，可能与当前页面风格不搭，Toolbar提供了setPopupTheme方法和对应的popupTheme属性来设置弹出菜单样式。
#####定义style样式资源

    <style name="AppTheme.PopupOverlay" parent="ThemeOverlay.AppCompat.Light" >
        <item name="android:textColor">@color/colorPrimary</item>
    </style>
该样式继承于ThemeOverlay.AppCompat.Light，我修改了文本色属性。

#####布局中设置
这里通过popupTheme属性来修改样式

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <android.support.v7.widget.Toolbar
            android:id="@+id/main_toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="@color/colorPrimary"
            app:popupTheme="@style/AppTheme.PopupOverlay"/>
    </RelativeLayout>
要注意的是popupTheme并非Android本身SDK中的属性，而是来自于支持包，所以添加命名空间

    xmlns:app="http://schemas.android.com/apk/res-auto"

#####效果

![样式调整效果](http://upload-images.jianshu.io/upload_images/680151-13e777b043af2665.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/360)

##总结
关于Toolbar的使用以及样式调整就暂时整理到这，还有很多细节方面还得在实际运用中去积累。
最后贴张总的效果图

![Toolbar](http://upload-images.jianshu.io/upload_images/680151-a298c6828384aec8.gif?imageMogr2/auto-orient/strip)
