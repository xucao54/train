package priv.xucao.trains.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类提取
 * @author xucao
 * @date 2020/11/21
 */
public class TrainsUtils {

    /**
     * 点位距离获取方法的入参有效性检验
     * @param param 传入的点位路径
     * @return boolean 返回入参校验结果
     */
    public static boolean checkParamForGetDistance(String param){
        boolean result = false;
        //判断非空
        if(param != null && param.trim().length() > 0){
            //正则表达式校验格式
            result = ConstantInterface.CONDITION_PATTERN.matcher(param).matches();
        }
        return result;
    }


    /**
     * 校验起始站和终点站已及最大经停站的有效性
     * @param startStation    起始站
     * @param terminalStation 终点站
     * @return String 验证通过时返回CHECK_PASSED，否则返回失败原因
     */
    public static String checkParamForStation(String startStation, String terminalStation){
        //控制变量，检测当前输入参数的合规性
        String result = ConstantInterface.CHECK_PASSED;
        //校验输入起始车站有效性
        if(!TrainsUtils.checkStationValidity(startStation)){
            result = ConstantInterface.START_STATION_INVALID;
        }
        //校验输入终点车站有效性
        if(!TrainsUtils.checkStationValidity(terminalStation)){
            result = ConstantInterface.TERMINAL_STATION_INVALID;
        }
        return result;
    }


    /**
     * 检验车站的有效性
     * @param param 车站点位
     * @return boolean 返回输入车站的合规性
     */
    public static boolean checkStationValidity(String param){
        boolean result = false;
        //判断非空
        if(param != null && param.trim().length() > 0){
            //正则表达式校验格式
            result = ConstantInterface.STATION_PATTERN.matcher(param).matches();
        }
        return result;
    }


    /**
     * 将路径按照车站进行分隔
     * @param route 传入的路线
     * @return List 返回途径车站结果集
     */
    public static List<String> splitRoute(String route){
        //创建返回对象
        List result = new ArrayList();
        //将字符串分隔成字符数组
        char[] stationChars = route.toCharArray();
        //遍历数组获取途径各个车站的路径
        for (int i = 0; i< stationChars.length; i++){
            //说明为最后一个车站，循环结束
            if(i == stationChars.length - 1){
                break;
            }
            //拼接本次车站信息添加到返回结果集
            result.add(String.valueOf(stationChars[i])+String.valueOf(stationChars[i+1]));
        }
        return result;
    }

    private TrainsUtils(){

    }
}
