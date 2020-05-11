import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 访问代理服务器，并携带上真实访问地址，然后接受但会的内容
 */
public class HttpProxy {

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(20003);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                10,1, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000));

        while(true){
            Socket socket = serverSocket.accept();

            //将任务提交给线程池处理
            threadPoolExecutor.submit(()->{
                BufferedReader socketReader = null;
                BufferedWriter socketWriter = null;
                BufferedReader reader = null;
                try {
                    socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String s=null;
                try {
                    //直到收到连续两个回车换行后则停止（请求头数据）
                    String requestHeader=socketReader.readLine();
                    System.out.println(requestHeader);
                    //获取到要代理的网址
                    String proxyUrl=requestHeader.split(" ")[1];
//                    while(!(s=socketReader.readLine()).equals("")){
//
//                    }
                    //解析请求头

                    //代理请求
                    URL url = new URL(proxyUrl);
//                    URL url = new URL("http://luyingjie.cn");
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    Map<String, List<String>> headerFields = connection.getHeaderFields();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    //========================================================
                    //返回请求（响应头+响应内容）
                    //创建响应头
                    StringBuilder responseHeader = new StringBuilder();
                    responseHeader.append(headerFields.get(null).get(0));//http协议 http状态码
                    responseHeader.append("\r\n");
                    Iterator<String> iterator = headerFields.keySet().iterator();
                    while(iterator.hasNext()){
                        String next = iterator.next();
                        if(next==null || next.equals("Transfer-Encoding")) continue;
                        List<String> fields = headerFields.get(next);

                        responseHeader.append(next);
                        responseHeader.append(": ");
                        responseHeader.append(fields.get(0));
                        for(int index=1;index<fields.size();index++){
                            responseHeader.append(";");
                            responseHeader.append(fields.get(index));
                        }
                        responseHeader.append("\r\n");
                    }
                    responseHeader.append("\r\n");
                    //创建消息体
                    String buff=null;
                    StringBuilder html=new StringBuilder();
                    while((buff=reader.readLine())!=null){
                        html.append(buff);
                    }
                    //========================================================

                    //发送响应头
                    socketWriter.write(responseHeader.toString());
                    //发送回车换行,表示响应的消息头结束
                    socketWriter.write("\r\n");//很重要
                    //写消息主体
                    socketWriter.write(html.toString());
                    //将缓存数据全部写出
                    socketWriter.flush();

//                    System.out.println(responseHeader.toString());
//                    System.out.println(html.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //关闭与访问网站的连接关闭
                    try {
                        if(reader!=null){
                            reader.close();
                        }
                        if(socketWriter!=null){
                            socketWriter.close();
                        }
                        if(socketReader!=null){
                            socketReader.close();
                        }

                        //关闭socket
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });

        }







//
//        while (true) {
//            len = bis.read(bytes);
//            System.out.println(len);
//            String s = new String(bytes, 0, len);//读取数据
//            System.out.println(s);
//
//            if(len<1024){
//                break;
//            }
//        }
//
//        //请求转发的网址,获取页面内容
//        URL url = new URL("http://luyingjie.cn");
//        URLConnection connection = url.openConnection();
//        DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
//        byte[] b = new byte[1024];
//        //读取页面内容
//        StringBuilder html = new StringBuilder();
//        while ((len = dataInputStream.read(b)) != -1) {
//            String s = new String(b, 0, len);//读取数据
//            html.append(s);
//        }
//
//        //拼接响应头
//        StringBuilder sb = new StringBuilder();
//        sb.append("HTTP/1.1 200 OK\r\n");
//        sb.append("Data:"+new Date()+"\r\n");
//        sb.append("Content-Type:text/html\r\n\r\n");
//
//        sb.append(html);
//
//        System.out.println(sb.toString());
//
//        //回写请求头+内容
//        bos.write(sb.toString().getBytes());






    }

}
