import java.io.InputStream;
import java.net.*;
import java.net.Proxy;

public class Test2 {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://baidu.com");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost",20003));
        URLConnection connection = url.openConnection(proxy);
        connection.connect();

        InputStream inputStream = connection.getInputStream();
        while(inputStream.read()!=-1){

        }

    }
}
