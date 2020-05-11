import com.sun.jndi.toolkit.url.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestProxy {

    ServerSocket serverSocket;
    ThreadPoolExecutor threadPoolExecutor;

    public TestProxy() {
        try {
            serverSocket = new ServerSocket(20003);

            threadPoolExecutor = new ThreadPoolExecutor(5,
                    10,1, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //开始代理
    public void startProxy() throws IOException {
        while(true) {
            Socket accept = serverSocket.accept();
            threadPoolExecutor.submit(()->{
                try {
                    InputStream inputStream = accept.getInputStream();
                    byte[] bytes = new byte[100];
                    int len=0;
                    StringBuilder requestHeaderSB = new StringBuilder();
                    //读取请求头
                    do{
                        len=inputStream.read(bytes);
                        requestHeaderSB.append(new String(bytes,0,len));
//                        System.out.print((new String(bytes,0,len)));
                    }while(len==100);

                    //解析请求头,生成map
                    Map<String,String> map = processRequestHeader(requestHeaderSB);

                    //获取到代理url的响应头和响应内容
                    StringBuilder sb=processProxyURL(map.get(null));

                    //响应客户端
                    OutputStream outputStream = accept.getOutputStream();
                    outputStream.write(sb.toString().getBytes());

                    //关闭流
                    outputStream.close();
                    inputStream.close();
                    accept.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private StringBuilder processProxyURL(String s) throws IOException {

        //保存响应头和响应数据
        StringBuilder sb = new StringBuilder();

        //连接到需要被代理的链接
        URL url = new URL(s);
        URLConnection connection = url.openConnection();
        //设置代理链接请求头字段
//        connection.setRequestProperty("Accept",acceptType);
        connection.connect();
        Map<String, List<String>> headerFields = connection.getHeaderFields();

        //响应头
        sb.append(headerFields.get(null).get(0));//状态码
        sb.append("\r\n");//回车换行

        Iterator<String> iterator = headerFields.keySet().iterator();//每个头字段
        while(iterator.hasNext()){
            String next = iterator.next();
            if(next==null || next.equals("Transfer-Encoding")) continue;
            List<String> fields = headerFields.get(next);

            sb.append(next);//key
            sb.append(": ");
            if(fields.size()==1){//value
                sb.append(fields.get(0));
            }else{
                for(int index=1;index<fields.size();index++){
                    sb.append(";");
                    sb.append(fields.get(index));
                }
            }
            sb.append("\r\n");//回车换行
        }
        //全部写完后，回车换行表示结束，接下来是内容
        sb.append("\r\n");//回车换行

        //将原样数据的返回给客户端
        InputStream inputStream = connection.getInputStream();
        byte[] bytes = new byte[100];
        int len=0;
        do{
            len=inputStream.read(bytes);
            sb.append(new String(bytes,0,len));
//            System.out.print(new String(bytes,0,len));
        }while(len!=-1);

        //关闭流
        inputStream.close();

        //返回响应头和响应内容
        return sb;
    }

    public static void main(String[] args) throws IOException {
        TestProxy testProxy = new TestProxy();
        testProxy.startProxy();
    }

    //处理请求头
    public Map<String,String> processRequestHeader(StringBuilder sb){
        String header=sb.toString();
        String[] split = header.split("\r\n");
//        String url=null;
//        String acceptType=null;

        HashMap<String,String> map = new HashMap<>();
        //请求行
        String requestHeader = split[0];

//        String[] arr = requestHeader.split(" ");
//        map.put();
//
//        map.put(null,requestHeader);
//
//
//        for(String s:split){
//            if(s.)
//
//            if(s.endsWith("HTTP/1.1")){
//                String[] strs = s.split(" ");
//                if(s.startsWith("GET") || s.startsWith("POST")){ //http
//                    url=strs[1];
//                    if(!strs[1].contains("http")){
//                        url="http://"+strs[1];
//                    }
//                }else if(s.startsWith("CONNECT") ){ //https
//                    url=strs[1];
//                    if(!strs[1].contains("443")){
//                        url="https://"+strs[1].split(":")[0];
//                    }
//                }else{
//                    url=strs[1];
//                }
//            }
//        }
//        System.out.println(url);

        return null;
    }

}
