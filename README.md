# JoeRxBus
使用RxJava编写的Eventbus—RxBus
<br>Author Joe

## 介绍
使用RxJava编写的EventBus，本身使用Rxjava就可以很方便的实现Eventbus的功能。我在基础上进行了扩展，<br>
实现了和AndroidEventBus类似的，使用注解的方式指定观察者，不同的在于AndroidEventBus注解是在方法上使用，JoeRxBus是在变量上使用，只要使实现了Action1接口的变量就行。<br>
已经完成Sticky模式的注册和使用。registerSticky可以对普通的post进行反应；postSticky的消息只会对之后registerSticky的对象有效，如需要，用post使之前的观察者生效。<br>

## 不足
sticky事件的移除是根据tag进行，Sticky事件中同一tag下的事件均会被移除。

## 使用说明
和AndroidEventBus类似，使用RxBus.getInstance().register()方式注册，在合适的地方进行unRegister。<br>
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.getInstance().register(this);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Subscriber(tag = "test", mode = ThreadMode.MAIN)
    public Action1 action = new Action1() {
        @Override
        public void call(Object o) {
            Log.d("Demo", "action:" + o.toString());
        }
    };

    @Subscriber(tag = "test2", mode = ThreadMode.NEW_THREAD)
    public Action1 action2 = new Action1() {
        @Override
        public void call(Object o) {
            Log.d("Demo", "action2:" + o.toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }
```
在需要的地方使用RxBus.getInstance().post(Object,String)方法进行事件发布。

### 记
因为主要的线程控制，订阅发布的逻辑都已经由Rxjava实现，因此，我们只要完成可观察对象(Observable)和观察者(Observer)的对应关系即可。<br>
代码逻辑非常简单，实现类也不多，一共也就7个。
