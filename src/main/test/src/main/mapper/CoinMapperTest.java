package src.main.mapper;

import com.alibaba.fastjson.JSON;
import com.git.poan.trade.ServerMain;
import com.git.poan.trade.entity.YieldCoin;
import com.git.poan.trade.mapper.YieldCoinMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: panbenxing
 * @Date: 2018/12/3
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerMain.class)
public class CoinMapperTest {

    @Resource
    private YieldCoinMapper yieldCoinMapper;

    @Test
    public void test() {
        List<YieldCoin> yieldCoins = yieldCoinMapper.selectAll();
        System.out.println(JSON.toJSONString(yieldCoins));
    }
}
