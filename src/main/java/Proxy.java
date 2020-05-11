import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Proxy {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10000);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
                10,1, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000));

        while(true) {
            Socket accept = serverSocket.accept();
            threadPoolExecutor.submit(()->{
                try {
                    InputStream inputStream = accept.getInputStream();
                    byte[] bytes = new byte[100];
                    int len=0;
                    while((len=inputStream.read(bytes))!=-1){
                        System.out.println(new String(bytes,0,len));
                    }
                    inputStream.close();
                    accept.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
