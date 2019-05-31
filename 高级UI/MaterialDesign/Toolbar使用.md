##简介
Toolbar是Android 5.0中引入的一个新控件。好比是ActionBar升级版，但是相比ActionBar，Toolbar变得更加自由，可以放到任何位置。
##使用
由于我们平常开发app要兼容5.0以下的手机，所以使用Toolbar必须引用appcompat-v7兼容包，Android studio新建的工程默认是引用了appcompat-v7。
####1.布局

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <android.support.v7.widget.Toolbar
            android:id="@+id/main_toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="@color/colorPrimary"/>
    </RelativeLayout>
####2.style样式设置
使用Toolbar替代ActionBar，使用的主题必须是没有ActionBar的，否则会造成冲突。因此修改style.xml中主题样式，继承Theme.AppCompat.NoActionBar

    <style name="AppTheme" parent="Theme.AppCompat.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="android:windowBackground">@android:color/white</item>
    </style>
####3.Activity中使用
#####(1) 将Toolbar当作ActionBar来使用。
这种情况一般发生在你想利用ActionBar现有的一些功能（比如能够显示菜单中的操作项，响应菜单点击事件，使用ActionBarDrawerToggle等），但是又想获得比actionbar更多的控制权限。

那么当前Activity需要继承AppCompatActivity，调用setSupportActionBar方法传入Toolbar的实例对象。

    public class MainActivity extends AppCompatActivity {
        @BindView(R.id.main_toolbar)
        Toolbar toolbar;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
    
            setSupportActionBar(toolbar);
        }
    }
#####(2) 将Toolbar当作一个独立的控件来使用。
这种情况当前Activity可以不继承AppCompatActivity。

    public class MainActivity extends Activity {
        @BindView(R.id.main_toolbar)
        Toolbar toolbar;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
    
            toolbar.setTitle("Example");
        }
    }
以上步骤完成后，Toolbar就添加到了页面中。

![Toolbar效果](http://upload-images.jianshu.io/upload_images/680151-5e96d463076f1272.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/360)

##Toolbar配置
Toolbar在ActionBar原有的设计基础上又将标题栏分为了多个区域，如下从Google找到的一张示例图所示：

![配置](http://upload-images.jianshu.io/upload_images/680151-bd9c18ef97d737d0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480)

大抵来说，配置常用的几个元素就如图中所示：
#####(1) setNavigationIcon
设置导航按钮，比如作为返回按钮。
在Toolbar当作ActionBar来使用的情况下：
注意setNavigationIcon需要放在 setSupportActionBar之后才会生效。

可以使用ActionBar原有方法来添加一个系统的返回按钮。

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

可以通过setNavigationOnClickListener设置其点击事件。

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity.this, "toolbar", Toast.LENGTH_SHORT).show();
        }
    });

![返回按钮](http://upload-images.jianshu.io/upload_images/680151-c9ce90440f4112ab.gif?imageMogr2/auto-orient/strip)

#####(2) setLogo
设置图标（图中写错了，应该是setLogo）
#####(3) setTitle
设置主标题。
在Toolbar当作ActionBar来使用的情况下：
setDisplayShowTitleEnabled需要设置为false，setTitle才能有效。

    getSupportActionBar().setDisplayShowTitleEnabled(false);

#####(4) setSubtitle
设置副标题
#####(5) setOnMenuItemClickListener
设置菜单的点击事件，如果Toolbar当作ActionBar来使用的话，还可以直接在onOptionsItemSelected(MenuItem item)中设置。

#####测试代码如下：

    toolbar.setNavigationIcon(R.mipmap.icon_back);
    toolbar.setLogo(R.mipmap.ic_launcher);
    toolbar.setTitle("Toolbar");
    toolbar.setSubtitle("subtitle");

![配置效果](http://upload-images.jianshu.io/upload_images/680151-c43d0b4c4f834ad3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/360)

##总结
关于Toolbar的初步使用就这么多，在Toolbar当作ActionBar使用的情况下，能用ActionBar原有方法实现的功能尽量用其方法实现，不能实现的再考虑使用Toolbar的方法，举个例子，像菜单构建设置监听什么的，直接使用Activity提供的方法就好了。
