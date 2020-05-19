package com.corundumstudio.socketio.demo;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class DemoSocketClient {
    public static void main(String[] args) {
        String url = "http://localhost:8888/shark/";
        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};
            //失败重试次数
            options.reconnectionAttempts = 10;
            //失败重连的时间间隔
            options.reconnectionDelay = 1000;
            //连接超时时间(ms)
            options.timeout = 500;
            final Socket socket = IO.socket(url, options);
            //监听自定义msg事件
            socket.on("msg", objects -> System.out.println("client: 收到msg->" + Arrays.toString(objects)));



            //监听自定义订阅事件
            socket.on("chatevent", objects -> System.out.println("client: " + "订阅成功，收到反馈->" + Arrays.toString(objects)));
            socket.on(Socket.EVENT_CONNECT, objects -> {
                try {
                    socket.emit("chatevent",new JSONObject("{\"userName\":\"user907\",\"message\":\"666666\"}"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("client: " + "连接成功");
            });
            socket.on(Socket.EVENT_CONNECTING, objects -> System.out.println("client: " + "连接中"));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> System.out.println("client: " + "连接超时"));
            socket.on(Socket.EVENT_CONNECT_ERROR, objects -> System.out.println("client: " + "连接失败"));
            socket.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
