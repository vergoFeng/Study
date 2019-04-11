##简介 

> TabLayout provides a horizontal layout to display tabs.

源码注释中表示，TabLayout是提供了一个水平的布局来展示标签。通常我们用来做选项卡这类效果。平常我们有用过开源库 PagerSlidingTabStrip 和 ViewPagerIndicator 来实现效果。
##简单使用
#####1.导入兼容包

    compile 'com.android.support:design:24.1.1'

#####2.layout中添加

    <android.support.design.widget.TabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

#####3.java中使用

    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText("头条"));
        tabLayout.addTab(tabLayout.newTab().setText("社会"));
        tabLayout.addTab(tabLayout.newTab().setText("娱乐"));
        tabLayout.addTab(tabLayout.newTab().setText("体育"));
        tabLayout.addTab(tabLayout.newTab().setText("科技"));
        tabLayout.addTab(tabLayout.newTab().setText("财经"));
    }

java代码中，通过addTab()方法来添加选项，有4个重载方法：

![addTab()](http://upload-images.jianshu.io/upload_images/680151-2d09ea4110d4ce7b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480)

看过源码知道，不管使用哪一个方法，最终都会调用最后一个方法，参数最多的。
（1） Tab tab：就是Tab类实例。
（2） int position：指定添加的Tab插入的位置。
（3） boolean setSelected：指定添加的Tab是否为选中状态
关于Tab类的一些设置方法，下面会说明。

#####4.效果

![TabLayout简单使用](http://upload-images.jianshu.io/upload_images/680151-4d32b42a63e33eff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480)

##TabLayout样式调整
#####属性
在layout中添加的时候会发现有如下可设置的属性：

![TabLayout属性](http://upload-images.jianshu.io/upload_images/680151-94aaf4d50f04ab8c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)

* tabBackground：设置整个TabLayout背景
* tabIndicatorColor：设置指示器的颜色
* tabIndicatorHeight：设置指示器的高度
* tabTextColor：设置未选中项中的字体颜色
* tabSelectedTextColor：设置选中项中的字体颜色
* tabTextAppearance：设置style改变字体属性
* tabMode：设置Tablayout的布局模式，有两个值
fixed：固定的，不能滑动，很多标签的时候会被挤压
scrollable：可以滑动的
默认是fixed
* tabGravity：设置TabLayout的布局方式，有两个值 
fill：充满
center：居中
默认是fill，且只有当tabMode为fixed时才有效
* tabMaxWidth：设置tab项最大的宽度
* tabMinWidth：设置tab项最小的宽度
* tabContentStart：设置TabLayout开始位置的偏移量
* paddingStart，paddingEnd：设置整个TabLayout的内边距
* tabPadding，tabPaddingStart，tabPaddingEnd，tabPaddingTop，tabPaddingBottom：设置tab项的内边距

示例：

    <android.support.design.widget.TabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:tabBackground="@color/colorPrimary"
        app:tabIndicatorColor="@android:color/white"
        app:tabIndicatorHeight="4dp"
        app:tabSelectedTextColor="#ffffffff"
        app:tabMode="scrollable"
        app:tabTextAppearance="@style/AppTheme.TabLayout.TextAppearance"/>

#####style中添加样式
同样，我们可以在style中添加一个样式，给TabLayout设置style属性。
######style.xml

    <style name="AppTheme.TabLayout" parent="Widget.Design.TabLayout">
        <item name="tabMode">scrollable</item>
        <item name="tabIndicatorColor">@android:color/white</item>
        <item name="tabIndicatorHeight">4dp</item>
        <item name="tabTextAppearance">@style/AppTheme.TabLayout.TextAppearance</item>
        <item name="tabBackground">@color/colorPrimary</item>
        <item name="tabSelectedTextColor">@android:color/white</item>
    </style>
    <style name="AppTheme.TabLayout.TextAppearance" parent="TextAppearance.Design.Tab">
        <item name="android:textSize">16sp</item>
        <item name="android:textColor">#b2ffffff</item>
        <item name="textAllCaps">false</item>
    </style>
 
######layout

    <android.support.design.widget.TabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        style="@style/AppTheme.TabLayout"/>

#####效果

![样式调整效果](http://upload-images.jianshu.io/upload_images/680151-3b70fb0c42fd453d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480)

##Tab类
Tab类是TabLayout中的静态内部类，源码：

![Tab静态内部类](http://upload-images.jianshu.io/upload_images/680151-38462c036839904a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480)

看源码知道Tab类的构造方法是私有的，不能直接new对象，注释也表明了使用方法，通过newTab()方法来创建实例。

Tab类的一些设置方法，看下图：

![Tab相关设置方法](http://upload-images.jianshu.io/upload_images/680151-5292133712995b4d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480)

* setContentDescription：设置tab标签内容描述
* seCustomView：设置一个自定义view来显示标签
* setIcon：给tab设置一个icon
* setTag：给tab设置一个标签
* setText：设置tab的文本内容

示例

    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setContentDescription("one");
        tab1.setIcon(R.mipmap.ic_launcher);
        tab1.setText("头条");
        tab1.setTag(1);
        tabLayout.addTab(tab1);

        //自定义view
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
        ImageView iv_tab = (ImageView) view.findViewById(R.id.iv_tab);
        TextView tv_tab = (TextView) view.findViewById(R.id.tv_tab);
        iv_tab.setImageResource(R.mipmap.ic_launcher);
        tv_tab.setText("社会");
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setCustomView(view);
        tab2.setContentDescription("two");
        tab2.setTag(2);
        tabLayout.addTab(tab2);

        tabLayout.addTab(tabLayout.newTab().setText("娱乐"));
        tabLayout.addTab(tabLayout.newTab().setText("体育"));
        tabLayout.addTab(tabLayout.newTab().setText("科技"));
        tabLayout.addTab(tabLayout.newTab().setText("财经"));
    }

自定义view的布局：

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:orientation="horizontal">
        <ImageView
            android:id="@+id/iv_tab"
            android:layout_width="30dp"
            android:scaleType="fitXY"
            android:layout_height="30dp"/>
        <TextView
            android:id="@+id/tv_tab"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="#b2ffffff"
            android:layout_marginLeft="3dp"
            android:gravity="center" />
    </LinearLayout>

效果

![Tab设置效果](http://upload-images.jianshu.io/upload_images/680151-c43994a2fb909cc2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/480)

##TabLayout+ViewPager

使用TabLayout时，更多的是用场景是配合ViewPager一起使用，通过TabLayout的setupWithViewPager()方法，使两者关联起来。

示例：
######layout布局

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        <android.support.v7.widget.Toolbar
            android:id="@+id/main_toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="@color/colorPrimary"
            android:theme="@style/AppTheme.AppBarOverlay"
            app:popupTheme="@style/AppTheme.PopupOverlay"/>
        <android.support.design.widget.TabLayout
            android:id="@+id/tablayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            style="@style/AppTheme.TabLayout"/>
        <android.support.v4.view.ViewPager
            android:id="@+id/viewpager"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
    </LinearLayout>

######创建Fragment
（1）布局

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:id="@+id/title_tv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:textSize="20sp"
            android:gravity="center">
        </TextView>
    </LinearLayout>

（2）java类

    public class TabFragment extends Fragment {
    
        public static final String PAGE_TITLE = "PAGE_TITLE";
        private String title;
    
        public static TabFragment newInstance(String title) {
            Bundle bundle = new Bundle();
            bundle.putString(PAGE_TITLE, title);
            TabFragment tabFragment = new TabFragment();
            tabFragment.setArguments(bundle);
            return tabFragment;
        }
    
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            title = getArguments().getString(PAGE_TITLE);
    
        }
    
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab, container, false);
            TextView titleTv = (TextView) view.findViewById(R.id.title_tv);
            titleTv.setText(title);
            return view;
        }
    }

######创建ViewPager适配器

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final String[] mTitles = {"头条", "社会", "娱乐", "体育", "科技", "财经"};
    
        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    
        @Override
        public Fragment getItem(int position) {
            return TabFragment.newInstance(mTitles[position]);
        }
    
        @Override
        public int getCount() {
            return mTitles.length;
        }
    
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

######Activity中关联

    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

######效果

![TabLayout+ViewPager](http://upload-images.jianshu.io/upload_images/680151-d117362be8911d88.gif?imageMogr2/auto-orient/strip)
