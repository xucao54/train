package priv.xucao.trains.common;

import java.util.regex.Pattern;

/**
 * 常量定义
 * @author xucao
 * @date 2020/11/21
 */
public interface ConstantInterface {

    /**
     * 输入路线规则的正则判断条件
     */
    Pattern CONDITION_PATTERN = Pattern.compile("[A-E]*");


    /**
     * 输入车站合规性正则判断条件
     */
    Pattern STATION_PATTERN = Pattern.compile("[A-E]");

    /**
     * 参数校验通过时返回
     */
    String CHECK_PASSED = "CHECK_PASSED";

    /**
     * 输入参数不合规时返回
     */
    String PARAM_INVALID = "参数不合规，请输入A-E之间的连续字母";

    /**
     * 输入起始车站不存在
     */
    String START_STATION_INVALID = "起始车站不存在，请输入A-E之间最多一个字母";


    /**
     * 输入终点车站不存在
     */
    String TERMINAL_STATION_INVALID = "终点车站不存在，请输入A-E之间最多一个字母";



    /**
     * 未找到用户定义的路线
     */
    String ROUTE_NO_SUCH = "NO SUCH ROUTE";


}
