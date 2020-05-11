import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestSpeed {

    //没有缓冲：2784ms
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\yingjie.lu\\Desktop\\test.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\yingjie.lu\\Desktop\\test3.txt");
        long startTime = System.currentTimeMillis(); // 获取开始时间
        int buff=0;
        byte[] bytes = new byte[100];
        while((buff=fileInputStream.read(bytes))!=-1){
            //System.out.println(buff);//输出的是ASCII码
            fileOutputStream.write(bytes,0,buff);
        }
        //关闭输入流和输出流
        fileInputStream.close();
        fileOutputStream.close();
        long endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println(endTime-startTime);
    }
}
