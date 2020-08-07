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
 * 时间： 2018/05/2018/5/10 8:34
 * <p>
 * 邮箱：  pyfysf@163.com
 */
public class TokenInfo {

    /**
     * access_token : 24.67af609b67f663650ed69a84518a0f72.2592000.1528504251.282335-11217555
     * session_key : 9mzdDcHuY4qqTxcI6X7Df9IwCoGQKbZmKV2yMzv7mtEdHP6EfDYgO2uL6Ujut7Rr29HRsZgQH1mtncgiYN1h9JZmxsmaAg==
     * scope : public vis-ocr_ocr brain_ocr_scope brain_ocr_general brain_ocr_general_basic brain_ocr_general_enhanced vis-ocr_business_license brain_ocr_webimage brain_all_scope brain_ocr_idcard brain_ocr_driving_license brain_ocr_vehicle_license vis-ocr_plate_number brain_solution brain_ocr_plate_number brain_ocr_accurate brain_ocr_accurate_basic brain_ocr_receipt brain_ocr_business_license brain_solution_iocr wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower lpq_开放 cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base
     * refresh_token : 25.81bce312cce25e0352d68929fcdc8c1b.315360000.1841272251.282335-11217555
     * session_secret : 6e04c5d4a6bda19b2c18216561ee16ea
     * expires_in : 2592000
     */

    private String access_token;
    private String session_key;
    private String scope;
    private String refresh_token;
    private String session_secret;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getSession_secret() {
        return session_secret;
    }

    public void setSession_secret(String session_secret) {
        this.session_secret = session_secret;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
