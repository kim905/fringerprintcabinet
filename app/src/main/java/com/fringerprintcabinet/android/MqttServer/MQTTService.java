package com.fringerprintcabinet.android.MqttServer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.fringerprintcabinet.android.MainActivity;
import com.fringerprintcabinet.android.MyDatabase;
import com.fringerprintcabinet.android.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static android.widget.Toast.LENGTH_SHORT;

public class MQTTService extends Service {

    public static final String TAG = MQTTService.class.getSimpleName();

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;


    private String host = "tcp://127.0.0.1:1883";
    private String userName = "test";
    private String passWord = "test";
    private static String myTopic = "publish";      //要订阅的主题
    private String clientId = "androidId";//客户端标识
    private com.fringerprintcabinet.android.MqttServer.IGetMessageCallBack IGetMessageCallBack;


    @Override
    public void onCreate() {
        super.onCreate();
//            Log.e(getClass().getName(), "onCreate");
        Log.d("TAG","初始化");
        init();
    }

    //发布主题
    public static void publish(String msg){
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        try {
            if (client != null){
                Log.d("TAG","返回：" + topic + msg + qos + retained);
                client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(20);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());     //将字符串转换为字符串数组

        // last will message
        boolean doConnect = false;//true -> false
        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
//            Log.e(getClass().getName(), "message是:" + message);
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            Log.d("TAG","进入LWT");
            // 最后的遗嘱
            // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
            //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
            //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。

            try {
                conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
                Log.d("TAG","LWT执行成功");
            } catch (Exception e) {
//                    Log.i(TAG, "Exception Occured", e);
                doConnect = true; //false -> true
                iMqttActionListener.onFailure(null, e);
            }
        }

        if (doConnect) {//LWT异常，再次连接
            doClientConnection();
            Log.d("TAG","LWT异常");
        }

    }


    @Override
    public void onDestroy() {
        stopSelf();//停止服务
        try {
            client.disconnect();//断开连接
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /** 连接MQTT服务器 */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNormal()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
                Log.d("TAG","连接成功");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
//                Log.i(TAG, "连接成功 ");
            Toast.makeText(MQTTService.this,"连接成功",LENGTH_SHORT).show();
            try {
                // 成功后，订阅myTopic话题
                client.subscribe(myTopic,1);
                //成功后，发布主题

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            Toast.makeText(MQTTService.this,"连接失败",LENGTH_SHORT).show();
            arg1.printStackTrace();
            // 连接失败，重连
        }
    };

    // MQTT监听并且接受消息 subscribe topic
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            //从MQTTServiceAPI中获取message置给回调函数
            String str1 = new String(message.getPayload());//getPayload以字节数组的形式返回有效负载
            if (IGetMessageCallBack != null){
                IGetMessageCallBack.setMessage(str1);
            }
//                String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
//                Log.i(TAG, "messageArrived:" + str1);
//                Log.i(TAG, str2);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
        }
    };

    /** 判断网络是否连接 */
    private boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Toast.makeText(MQTTService.this,"MQTT 当前网络名称" + name,LENGTH_SHORT).show();
//                Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Toast.makeText(MQTTService.this,"MQTT 没有可用网络",LENGTH_SHORT).show();
//                Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
//            Log.e(getClass().getName(), "onBind");
        return new CustomBinder();
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack){
        this.IGetMessageCallBack = IGetMessageCallBack;
    }

    //返回MQTTService绑定服务
    public class CustomBinder extends Binder {//习惯绑定
        public MQTTService getService(){
            return MQTTService.this;
        }
    }

    public  void toCreateNotification(String message){//订阅主题调用
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this,MQTTService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//3、创建一个通知，属性太多，使用构造器模式

        Notification notification = builder
                .setTicker("测试标题")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContentText(message)
                .setContentInfo("")
                .setContentIntent(pendingIntent)//点击后才触发的意图，“挂起的”意图
                .setAutoCancel(true)        //设置点击之后notification消失
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(0, notification);
        notificationManager.notify(0, notification);

    }
}
