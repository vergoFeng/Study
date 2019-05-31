##简介
Navigation Drawer是在 Material Design 中推出的一种侧滑导航菜单栏控件。包含两个部分，一部分是侧滑（DrawerLayout），一部分是导航菜单栏（NavigationView）。

##AS新建
利用Android Studio可以快速建立这个控件
* 在新建项目时，在最后选择Navigation Drawer Activity

![新建项目时](http://upload-images.jianshu.io/upload_images/680151-51b206b1886deb77.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/640)

* 在新建Activity时，选择Navigation Drawer Activity

![新建Activity时](http://upload-images.jianshu.io/upload_images/680151-ab9cee0b87ddcff7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/640)

##DrawerLayout
DrawerLayout布局中，由两部分组成，一部分是内容布局，一部分是侧滑菜单布局。其中侧滑菜单布局通过设置 **android:layout_gravity** 属性，来控制是左侧滑，还是右侧滑。参考实例代码

    <android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".ui.MainActivity">
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
            <android.support.v7.widget.Toolbar
                android:id="@+id/main_toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="@color/colorPrimary"
                app:popupTheme="@style/AppTheme.PopupOverlay"/>
            <TextView
                android:id="@+id/content_tv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="@dimen/activity_horizontal_margin"
                android:text="Hello World!"/>
        </LinearLayout>
    
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_gravity="start"
            android:gravity="center"
            android:background="@android:color/white"
            android:text="导航菜单页左"/>
    
        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_gravity="end"
            android:gravity="center"
            android:background="@android:color/white"
            android:text="导航菜单页右"/>
    </android.support.v4.widget.DrawerLayout>

至此，侧滑效果就实现了。

![侧滑效果](http://upload-images.jianshu.io/upload_images/680151-6964cee08db9e0e5.gif?imageMogr2/auto-orient/strip)

DrawerLayout中也有一些常会用到的方法，来控制例如打开、关闭菜单，监听滑动事件等，这里暂时列举一些，还是得多看api和源码。

    //打开左侧菜单，同理右侧就是 GravityCompat.END
    drawerLayout.openDrawer(GravityCompat.START);
    //关闭左侧菜单，同理右侧就是 GravityCompat.END
    drawerLayout.closeDrawer(GravityCompat.START);
    //设置抽屉打开时，剩余挡住内容区域部分的颜色
    drawerLayout.setScrimColor(Color.parseColor("#4cff0000"));
    //设置抽屉锁定模式 LOCK_MODE_LOCKED_OPEN:锁定 无法滑动
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, GravityCompat.START);
    //监听滑动事件
    drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            //抽屉滑动时回调
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            //抽屉打开后回调
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            //抽屉关闭后回调
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            //抽屉滑动状态改变时回调
            switch (newState) {
                case DrawerLayout.STATE_DRAGGING:
                    //拖动状态
                    break;
                case DrawerLayout.STATE_IDLE:
                    //静止状态
                    break;
                case DrawerLayout.STATE_SETTLING:
                    //设置状态
                    break;
                default:
                    break;
            } 
        }
    });

##NavigationView
NavigationView是兼容包中提供用来实现导航菜单控件。使用menu资源填充数据，可以更简单高效的实现导航菜单。
#####添加依赖

    compile 'com.android.support:design:24.1.0'

#####布局中引用

    <android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/drawerlayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".ui.MainActivity">
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
            <android.support.v7.widget.Toolbar
                android:id="@+id/main_toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="@color/colorPrimary"
                app:popupTheme="@style/AppTheme.PopupOverlay"/>
            <TextView
                android:id="@+id/content_tv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="@dimen/activity_horizontal_margin"
                android:text="Hello World!"/>
        </LinearLayout>
    
        <android.support.design.widget.NavigationView
            android:id="@+id/navigation_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_gravity="start"
            app:headerLayout="@layout/navigation_drawer_header"
            app:menu="@menu/navigation_drawer_menu"/>
    </android.support.v4.widget.DrawerLayout>

NavigationView分为两部分，一部分是headerLayout，一部分是menu。headerLayout就是对应菜单的顶部部分，一般用来显示用户信息什么的，menu则对应实际的菜单选项。

#####定义headerLayout

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:background="@color/colorPrimary">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentBottom="true"
            android:layout_margin="10dp"
            android:text="HeaderLayout"
            android:textColor="@android:color/white"
            android:textSize="18sp" />
    </RelativeLayout>

#####定义menu

    <menu xmlns:android="http://schemas.android.com/apk/res/android">
    
        <group android:checkableBehavior="single">
            <item
                android:id="@+id/item_dync"
                android:icon="@mipmap/ic_menu_dync_selected"
                android:title="首页" />
            <item
                android:id="@+id/item_explore"
                android:icon="@mipmap/ic_menu_explore_selected"
                android:title="发现" />
            <item
                android:id="@+id/item_message"
                android:icon="@mipmap/ic_menu_message_selected"
                android:title="消息" />
            <item
                android:id="@+id/item_person"
                android:icon="@mipmap/ic_menu_person_selected"
                android:title="我的" />
        </group>
    
        <item android:title="其他">
            <menu>
                <item
                    android:id="@+id/subitem_01"
                    android:icon="@mipmap/ic_launcher"
                    android:title="分享" />
                <item
                    android:id="@+id/subitem_02"
                    android:icon="@mipmap/ic_launcher"
                    android:title="设置" />
                <item
                    android:id="@+id/subitem_03"
                    android:icon="@mipmap/ic_launcher"
                    android:title="反馈" />
            </menu>
        </item>
    </menu>

这样NavigationView就添加成功，效果如下：

![NavigationView效果](http://upload-images.jianshu.io/upload_images/680151-123468088132f0e1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/360)

#####Menu Item 的点击监听
直接使用 NavigationView 的 setNavigationItemSelectedListener() 方法即可

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.item_dync:
                    break;
                case R.id.item_explore:
                    break;
                //......
            }
            return true;
        }
    });

##总结
Navigation Drawer的用法还是比较简单的，但是NavigationView的封装性太高，个人觉得不是特别实用，相对自己定义界面可能来的比较方便，或许是自己用的不到家。

关于抽屉效果，个人觉得目前而言随着手机的屏幕越来越大，导致用户操作起来不是很方便，现在主流的设计还是底部导航栏。
