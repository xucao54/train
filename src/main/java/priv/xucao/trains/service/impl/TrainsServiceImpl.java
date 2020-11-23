package priv.xucao.trains.service.impl;

import priv.xucao.trains.common.ConstantInterface;
import priv.xucao.trains.common.TrainsUtils;
import priv.xucao.trains.service.TrainsService;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 能力实现层
 * @author xucao
 * @date 2020/11/21
 */
public class TrainsServiceImpl implements TrainsService{

    /**
     * 车站间距离映射
     */
    private final static Map<String,Integer> STATION_DISTANCE = new HashMap<>(16);


    static {
        STATION_DISTANCE.put("AB", 5);
        STATION_DISTANCE.put("BC", 4);
        STATION_DISTANCE.put("CD", 8);
        STATION_DISTANCE.put("DC", 8);
        STATION_DISTANCE.put("DE", 6);
        STATION_DISTANCE.put("AD", 5);
        STATION_DISTANCE.put("CE", 2);
        STATION_DISTANCE.put("EB", 3);
        STATION_DISTANCE.put("AE", 7);
    }

    /**
     * set集合用于保存条件内符合的不重复的线路方案
     */
    private Set<String> tempSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * 用于保存点位间的最小距离
     */
    private Integer curMinLength = 0;


    /**
     * 根据传入点位连接的路径，获取此路径的距离
     *
     * @param condition 传入A-E之间任意组合排列的连续点位路径，例:ACD、ADCB
     * @return String 返回当前传入路径的距离
     */
    @Override
    public String getDistance(String condition) {
        //定义返回结果
        String result;
        //入参合规性校验
        boolean validity =  TrainsUtils.checkParamForGetDistance(condition);
        if(validity){
            //参数合规性校验通过，此时开始计算距离
            result = getWay(condition);
        }else{
            //参数不合规
            result = ConstantInterface.PARAM_INVALID;
        }
        return result;
    }


    /**
     * 根据条件进行路线查找
     *
     * @param startStation    起始站
     * @param terminalStation 终点站
     * @param maxPassStation  最多经停站
     * @param appointStation 指定经停站数
     * @param maxDistance 线路最大长度
     * @param queryMinRoute 表示查询两点之间的最短路线长度
     * @return String  返回符合条件的值
     */
    @Override
    public String getRouteCount(String startStation, String terminalStation, Integer maxPassStation,  Integer appointStation , Integer maxDistance , Boolean queryMinRoute){
        //定义为空的返回结果
        String result;
        //调用公共方法检验入参
        String checkResult = TrainsUtils.checkParamForStation(startStation,terminalStation);
        //定义变量，控制查询指定经停站数的逻辑
        boolean queryAppoint = false;
        //输入参数校验是否通过
        if(ConstantInterface.CHECK_PASSED.equals(checkResult)){
            //判断是否查询指定经停站数的线路
            if(appointStation != null && appointStation > 0){
                queryAppoint = true;
                //此时将指定经停站数赋值给最大经过站数，表示查询此范围区间
                maxPassStation = appointStation;
            }
            //涉及到静态成员变量的操作，可能存在线程安全问题，此处代码块加上锁
            Lock lock = new ReentrantLock();
            //开始加锁
            lock.lock();
            try{
                //调用方法递归查找，将符合条件的线路存在tempSet
                range(startStation,"",new StringBuffer(startStation),0,terminalStation,maxDistance,maxPassStation,queryMinRoute);
                //判断是否查询指定经停站数的线路
                if(queryAppoint){
                    Integer meetRouteCount = 0;
                    //遍历区间内的路线，统计符合指定站点数的路线
                    for(String route : tempSet){
                        if(route.length() == appointStation + 1){
                            meetRouteCount++;
                        }
                    }
                    result = meetRouteCount.toString();
                }else{
                    if(queryMinRoute){
                        result = curMinLength.toString();
                    }else{
                        result = String.valueOf(tempSet.size());
                    }
                }
            } finally {
                //释放锁
                lock.unlock();
                //将静态变量的值置为初始值
                curMinLength = 0;
                tempSet = Collections.synchronizedSet(new HashSet<>());
            }

        }else{
            result = checkResult;
        }
        return result;
    }


    /**
     * 根据当前传入路线，判断是否存在、以及距离
     * @param way 路线
     * @return String 若此路线存在则返回距离，否则返回 NO SUCH ROUTE
     */
    private static String getWay(String way){
        String result = null;
        Integer distance = 0;
        //路径按照车站进行分离
        List<String> stationList = TrainsUtils.splitRoute(way);
        //遍历用户的车站途径轨迹
        for(String station : stationList){
            if(STATION_DISTANCE.containsKey(station)){
                //说明本次路线匹配到存在路线，改变控制变量并增加本次途径站点间距离
                distance = distance + STATION_DISTANCE.get(station);
                result = distance.toString();
            }else{
                //证明未找到此车站的线路
                result = ConstantInterface.ROUTE_NO_SUCH;
                break;
            }
        }
        return result;
    }

    /**
     * 根据条件，递归查找符合条件的线路，并将线路信息存放在tempSet
     * @param startStation 当前车站区间的起点车站，初次调用时为始发站
     * @param terminalStation 当前车站区间的终点车站 ，初次调用时为空
     * @param stringBuffer 当前线路
     * @param curDistance 当前线路的总距离
     * @param targetStation  目标车站
     * @param maxDistance 指定线路方案的最大长度
     * @param maxPassStation 指定线路方案的最大经停车站
     */
    private  void range(String startStation , String terminalStation , StringBuffer stringBuffer , Integer curDistance,String targetStation, Integer maxDistance , Integer maxPassStation,Boolean queryMinRoute) {
        //定义过渡变量保存当前线路
        String tempBuffer;
        //如果当前线路的长度超出最大长度的条件限制，则本次递归结束
        if(maxDistance != null && curDistance>=maxDistance){
            return;
        }
        //如果当前线路的经停车站超出最大经停车站的条件限制，则本次递归结束
        else if(maxPassStation != null && stringBuffer.length() > maxPassStation){
            return;
        }else if(queryMinRoute && curMinLength != 0 && curDistance>curMinLength){
            return;
        } else{
            //递归继续，将本次行程添加到线路中
            tempBuffer = stringBuffer.append(terminalStation).toString();
        }
        //如果当前车站区间的终点车站为目标终点站，则记录本条线路
        if(tempBuffer.endsWith(targetStation) && tempBuffer.length()>1){
            if(curMinLength == 0){
                //表示第一次匹配到合适线路
                curMinLength = curDistance;
            }else{
                //后续匹配到合适线路之后，应与当前保存的最短路线做比较，如果本次距离更短，则更新保存的最短距离
                curMinLength = curMinLength>curDistance?curDistance:curMinLength;
            }
            tempSet.add(tempBuffer);
        }
        //遍历车站信息
        for(Map.Entry<String, Integer> entry:STATION_DISTANCE.entrySet()){
            //匹配出发站为当前出发站的区间车站信息--可能匹配多个
            if(entry.getKey().startsWith(startStation)){
                String site = entry.getKey();
                //以当前长度为副本，加上本次的距离为新的长度，继续递归
                Integer distance = curDistance +entry.getValue();
                //递归继续查找
                range(String.valueOf(site.charAt(1)),String.valueOf(site.charAt(1)),new StringBuffer(tempBuffer),distance,targetStation,maxDistance,maxPassStation,queryMinRoute);
            }
        }
    }
}
