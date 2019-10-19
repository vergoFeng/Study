![logo.png](http://upload-images.jianshu.io/upload_images/680151-a7950f3f81e1f520.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)

ButterKnife框架使用可以方便我们不用写大量的重复繁琐的findViewById和setOnClickListener等代码，它采用依赖注入的方式，通过注解的方式让view和代码中的对象绑定起来。

GitHub地址：[https://github.com/JakeWharton/butterknife](https://github.com/JakeWharton/butterknife)

<!-- more -->

## ButterKnife使用

### 添加依赖

由于8.0.0之后的版本和之前的版本有差异，这里主要是8.1.0最新版本的添加方法，注意两个步骤都要完成：

##### 1.Project的build.gradle中添加：

```
dependencies { 
	classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
}
```

##### 2.App的build.gradle中添加：

```
apply plugin: 'com.neenbedankt.android-apt'
    
dependencies {
	compile 'com.jakewharton:butterknife:8.1.0'
	apt 'com.jakewharton:butterknife-compiler:8.1.0'
}
```

### Activity中使用

```java
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.title_tv) TextView titleTv;
    @BindView(R.id.back_btn) Button backBtn;
    @BindView(R.id.logo_img) ImageView logoImg;

    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
```

### Activity中使用

注意：ButterKnife.bind(this)必须在setContentView之后。

### Fragment中使用

```java
public class ListFragment extends Fragment{
    @BindView(R.id.title_tv) TextView titleTv;
    @BindView(R.id.listview) ListView listView;
    private Unbinder unbinder;
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onDestroyView() {    
        super.onDestroyView();    
        unbinder.unbind();
    }
}
```
由于Fragment的生命周期不同于Activity，当在CreateView方法中绑定视图时，需要在onDestoryView中把对应的视图设置为null，这时需要解绑ButterKnife。

### ViewHolder中使用

```java
static class ViewHolder {
    @BindView(R.id.name_tv) TextView nameTv;
    @BindView(R.id.content_tv) TextView contentTv;
    @BindView(R.id.head_img) ImageView headImg;

    public ViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
```

### 事件监听绑定

```java
//点击事件
@OnClick(R.id.submit)
public void buttonClick(Button button){
    //TODO ...
}

//listview item点击事件
@OnItemClick(R.id.listview)
public void itemClick(ListView listView){
    //TODO ...
}

//多个控件具有相同的点击事件
@OnClick({ R.id.btn1, R.id.btn2, R.id.btn3 })
public void buttonsClick(Button button){
    //TODO ...
}
```
ps：方法中的参数是可选的，但如果存在，必须是这个控件类或者控件类的父类。

### 资源绑定

可以用@BindBool，@BindColor，@BindDimen，@BindDrawable，@BindInt和@BindString通过绑定R.bool以及其他对应id来进行资源的预定义。

```java
@BindString(R.string.title) String title; 
@BindDrawable(R.drawable.graphic) Drawable graphic; 
@BindColor(R.color.red) int red; 
@BindDimen(R.dimen.spacer) Float spacer;
//...
```

通过这种方式，就可以把资源直接赋值给变量，从而不再需要初始化。

### 可选绑定
默认情况下，@bind和监听器绑定都必须有一个目标view，当butter knife找不到对应的view时会抛出一个异常。为了防止这种异常情况的发生，可以在绑定的字段前面使用@Nullable注解，在绑定的方法前面则可使用@Option注解，来表明对应的是一个可选绑定。

注：任何名为@Nullable第三方的注解都可以对字段起作用，这里推荐使用Android的”support-annotations“ library提供的@Nullable注解。

```java
@Nullable @BindView(R.id.might_not_be_there) TextView mightNotBeThere;
@Optional @OnClick(R.id.maybe_missing) 
void onMaybeMissingClicked() { 
	// TODO ...
}
```

### 其他
butter knife也提供了一个findById方法，如果要在某些情况下查找某些子view，可以使用它来简化代码。

```java
View view = LayoutInflater.from(context).inflate(R.layout.thing, null);
TextView firstName = ButterKnife.findById(view, R.id.first_name);
TextView lastName = ButterKnife.findById(view, R.id.last_name);
```

### 混淆

```
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
  @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
  @butterknife.* <methods>;
}
```

## 参考资料

官方文档：[https://jakewharton.github.io/butterknife/](https://jakewharton.github.io/butterknife/)
