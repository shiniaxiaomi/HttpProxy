import java.io.DataInputStream;
import java.io.InputStream;
import java.net.*;

public class Test1 {

    public static void main(String[] args) throws Exception {



        URL url = new URL("https://baidu.com");

        //访问网址时通过创建的代理进行访问
//        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("localhost",20003));
//        URLConnection connection = url.openConnection(proxy);

        URLConnection connection = url.openConnection();
        connection.connect();

        InputStream inputStream = connection.getInputStream();

        DataInputStream dataInputStream = new DataInputStream(inputStream);


        byte[] bytes = new byte[100];
        int len=0;
        while((len=dataInputStream.read(bytes))!=-1){
            System.out.println(new String(bytes,0,len));
        }


        inputStream.close();

    }
}
