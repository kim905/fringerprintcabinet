package com.fringerprintcabinet.android.MqttServer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/*public class Client extends AppCompatActivity implements Runnable{
    private MqttAndroidClient mMqttClient;
    private String mTopic = "publish";//主题
    private Integer mQos = 0;//服务质量
    private boolean mRetained = false;//保留

    @Override
    public void run() {
        ConnectMQTT1();//连接服务器
        publishMsgToServer("hello,world");//发布主题
    }

    private android.os.Handler handler = new Handler(){
        public void HandlerMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1: //开机校验更新回传
                    break;
                case 2:  // 反馈回传

                    break;
                case 3:  //MQTT 收到消息回传   UTF8Buffer msg=new UTF8Buffer(object.toString());
                    Toast.makeText(Client.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 30:  //连接失败
                    Toast.makeText(Client.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case 31:   //连接成功
                    Toast.makeText(Client.this, "连接成功", Toast.LENGTH_SHORT).show();
                    try {
                        mMqttClient.subscribe(mTopic, 1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };


    public Client() {

    }


    //连接服务器
    private void ConnectMQTT1() {
        String clientId = "android Client";//连接时使用的clientId
        mMqttClient = new MqttAndroidClient(Client.this, "127.0.0.1:1883", clientId);
        MqttConnectOptions options = new MqttConnectOptions();//设置连接
        options.setCleanSession(true);//清除缓存
        options.setConnectionTimeout(60);//设置超时时间，单位：秒
        options.setKeepAliveInterval(60);//心跳包发送间隔，单位：秒
        options.setUserName("test");
        options.setPassword("test".toCharArray());
        mMqttClient.setCallback(new MqttCallback() {
            @Override//连接丢失后,会执行这里
            public void connectionLost(Throwable cause) {
                //断开重连
//                                        Handler mHandler;
//                                        mHandler.sendEmptyMessageDelayed(MSG_DELAY_CONNECT_MQTT, 10000);

            }

            @Override //获取的消息会执行这里
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override //订阅主题后会执行这里
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });

        //进行连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mMqttClient.connect(options, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            try {

                                //连接成功后订阅主题
                                mMqttClient.connect(options);
                                mMqttClient.subscribe(mTopic, mQos);
                                Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            //断开重连
                        }
                    });
                } catch (MqttSecurityException e) {
                    //安全问题连接失败
                    e.printStackTrace();
                } catch (MqttException e) {
                    //连接失败原因
                    e.printStackTrace();
                }
            }
        });
    }

        //发送消息给服务器  发布主题
        private void publishMsgToServer(String msg){
            try {
//                Log.d("testNull", "" + mMqttClient);
                mMqttClient.publish(mTopic, msg.getBytes(), mQos, mRetained, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }


//    private void aquireDataPaths(){//接受服务器数据
//        try {
//            String urlPath= "http://106.53.81.62:18083/#/plugins";
//            URL url = new URL(urlPath);
//            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
//            int responseCode = conn.getResponseCode();
//            if (responseCode == 200){//返回码：200 ，请求已成功，请求所希望的响应头或数据体将随此响应返回。
//                InputStream is = conn.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                identity identity = new identity();
//                String line = null;
//                while ((line = reader.readLine()) != null){
//                    identity.setFingerprint(line);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
*/

