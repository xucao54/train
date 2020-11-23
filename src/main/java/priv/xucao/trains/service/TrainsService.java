package priv.xucao.trains.service;

/**
 * 能力接口定义
 * @author xucao
 * @date 2020/11/21
 */
public interface TrainsService {

    /**
     * 根据传入点位连接的路径，获取此路径的距离
     *
     * @param condition 传入A-E之间任意组合排列的连续点位路径，例:ACD、ADCB
     * @return String 返回当前传入路径的距离
     */
    String getDistance(String condition);

    /**
     * 根据条件进行路线查找
     *
     * @param startStation 起始站
     * @param terminalStation 终点站
     * @param maxPassStation 最多经停站
     * @param appointStation 指定经停站数
     * @param maxDistance 线路最大长度
     * @param queryMinRoute 表示查询两点之间的最短路线长度
     * @return String  返回符合条件的值
     */
    String getRouteCount(String startStation , String terminalStation , Integer maxPassStation , Integer appointStation , Integer maxDistance , Boolean queryMinRoute);
}
