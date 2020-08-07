package net.printer.printdemo16.utils;

/**
 * 作者：  pyfysf
 * <p>
 * qq:  337081267
 * <p>
 * CSDN:    http://blog.csdn.net/pyfysf
 * <p>
 * 个人博客：    http://wintp.top
 * <p>
 * 时间： 2018/05/2018/5/10 8:12
 * <p>
 * 邮箱：  pyfysf@163.com
 */
public class ConstantValue {


    /**
     * 百度扫描图片识别文字API    访问URL
     */
    //通用
    public static final String BAIDU_INTER_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
    //高精度
//    public static final String BAIDU_INTER_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
    //通用含生僻字
//    public static final String BAIDU_INTER_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_enhanced";
    /**
     * 百度扫描图片识别文字API  AK 和  SK
     */
    public static final String BAIDU_AK = "dzVSefGAVCRlnBZyQrjn2nde";
    public static final String BAIDU_SK = "U91eGkTr2qoUruRtzGyFCO4Qvbs63arW";


    /**
     * 获取百度token访问URL
     */
    public static final String BAIDU_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id="
            + BAIDU_AK
            + "&client_secret=" + BAIDU_SK;

}
