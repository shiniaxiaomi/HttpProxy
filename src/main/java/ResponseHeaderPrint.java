import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResponseHeaderPrint {
    public static void main(String[] args) throws IOException {

        String buff="http://luyingjie.cn";
//        String buff="https://baidu.com";
//        String buff="https://www.csdn.net";

        URL url = new URL(buff);
        URLConnection connection = url.openConnection();
        connection.connect();

        Map<String, List<String>> headerFields = connection.getHeaderFields();
        Iterator<String> iterator = headerFields.keySet().iterator();
        while(iterator.hasNext()){
            String next = iterator.next();
            List<String> fields = headerFields.get(next);

            if(fields.size()>1){
                System.out.print(next+": "+fields.get(0));
                for(int index=1;index<fields.size();index++){
                    System.out.print(";");
                    System.out.println(fields.get(index));
                }
            }else{
                System.out.println(next+": "+fields.get(0));
            }


        }

    }
}
