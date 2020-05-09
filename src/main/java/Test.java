import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception {

        BufferedReader reader = null;

        //代理请求
        URL url = new URL("http://luyingjie.cn");
//        URL url = new URL("http://luyingjie.cn");
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
                responseHeader.append(fields.get(index));
                responseHeader.append(";");
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
    }
}
