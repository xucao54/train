import priv.xucao.trains.service.TrainsService;
import priv.xucao.trains.service.impl.TrainsServiceImpl;

/**
 * 测试主类
 * @author xucao
 * @date 2020/11/22
 */
public class TrainsTest {
    public static void main(String[] args) {
        //模拟创建火车服务对象
        TrainsService trainsService = new TrainsServiceImpl();
        //路线ABC的距离。
        String abcDistance = trainsService.getDistance("ABC");
        System.out.println("输出＃1："+abcDistance);
        //路线AD的距离。
        String adDistance = trainsService.getDistance("AD");
        System.out.println("输出＃2："+adDistance);
        //路线ADC的距离。
        String adcDistance = trainsService.getDistance("ADC");
        System.out.println("输出＃3："+adcDistance);
        //路线AEBCD的距离。
        String aebcdDistance = trainsService.getDistance("AEBCD");
        System.out.println("输出＃4："+aebcdDistance);
        //路线AED的距离。
        String aedDistance = trainsService.getDistance("AED");
        System.out.println("输出＃5："+aedDistance);
        //从C到C的旅行次数，最多3站。
        String ccMaxStation3 = trainsService.getRouteCount("C","C",3,null,null,false);
        System.out.println("输出＃6："+ccMaxStation3);
        //从A到C的行程数，恰好有4个停靠点
        String acAppointStation4 = trainsService.getRouteCount("A","C",null,4,null,false);
        System.out.println("输出＃7："+acAppointStation4);
        //从A到C的最短路线的长度（就行进距离而言）。
        String acMinDistance = trainsService.getRouteCount("A","C",null,null,null,true);
        System.out.println("输出＃8："+acMinDistance);
        //从B到B的最短路线的长度（就行进距离而言）。
        String bbMinDistance = trainsService.getRouteCount("B","B",null,null,null,true);
        System.out.println("输出＃9："+bbMinDistance);
        //从C到C且距离小于30的不同路线的数量。
        String ccMaxDistance30 = trainsService.getRouteCount("C","C",null,null,30,false);
        System.out.println("输出＃10："+ccMaxDistance30);
    }
}
