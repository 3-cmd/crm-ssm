import cn.hutool.core.lang.UUID;
import org.junit.Test;

public class Test1 {
    @Test
    public void test1(){
        String uuid = UUID.randomUUID().toString().replace("-","");
        System.out.println(uuid);
    }
}
