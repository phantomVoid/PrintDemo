package net.printer.printdemo16.utils;

import java.util.List;

/**
 * 作者：  pyfysf
 * <p>
 * qq:  337081267
 * <p>
 * CSDN:    http://blog.csdn.net/pyfysf
 * <p>
 * 个人博客：    http://wintp.top
 * <p>
 * 时间： 2018/05/2018/5/10 8:36
 * <p>
 * 邮箱：  pyfysf@163.com
 */
public class WordInfo {
    /**
     * log_id : 1861715874821339263
     * words_result_num : 2
     * words_result : [{"words":"我不服"},{"words":"除了我自己,谁还能打败我"}]
     */

    private long log_id;
    private int words_result_num;
    private List<WordsResultBean> words_result;
    private Integer error_code;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public static class WordsResultBean {
        /**
         * words : 我不服
         */

        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
