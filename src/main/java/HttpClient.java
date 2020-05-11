import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpClient {

    String host;
    int port;

    public HttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public StringBuilder send() throws IOException {
        Socket socket = new Socket(host,port);

        socket.setKeepAlive(false);//设置为短链接

        OutputStream outputStream = socket.getOutputStream();
        //http协议
        outputStream.write("GET / HTTP/1.1".getBytes());
        outputStream.write("\r\n".getBytes());//回车换行

        outputStream.write(("Accept: */*").getBytes());
        outputStream.write("\r\n".getBytes());//回车换行

        outputStream.write(("Host: "+host).getBytes());
        outputStream.write("\r\n".getBytes());//回车换行

        //结束换行
        outputStream.write("\r\n".getBytes());//回车换行

        StringBuilder sb = new StringBuilder();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[100];
        int len=0;
        try {
            //不知道为什么，都不到-1，一直阻塞，所以只能设置超时时间来终止阻塞
            while((len=inputStream.read(bytes))!=-1){
                sb.append(new String(bytes,0,len));
            }
        }finally {
            inputStream.close();
            outputStream.close();
            return sb;
        }

    }

    public static void main(String[] args) throws IOException {

        String host="luyingjie.cn";
        int port=80;

        HttpClient httpClient = new HttpClient(host, port);
        StringBuilder sb = httpClient.send();
        System.out.println(sb.toString());

    }
}
