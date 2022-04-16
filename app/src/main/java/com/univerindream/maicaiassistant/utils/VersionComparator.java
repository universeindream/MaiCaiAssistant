package com.univerindream.maicaiassistant.utils;

import com.blankj.utilcode.util.StringUtils;

import java.io.Serializable;
import java.util.Comparator;

public class VersionComparator implements Comparator<String>, Serializable {
    private static final long serialVersionUID = 8083701245147495562L;

    /**
     * 单例
     */
    public static final VersionComparator INSTANCE = new VersionComparator();

    /**
     * 默认构造
     */
    public VersionComparator() {
    }

    // -----------------------------------------------------------------------------------------------------

    /**
     * 比较两个版本<br>
     * null版本排在最小：即：
     * <pre>
     * compare(null, "v1") &lt; 0
     * compare("v1", "v1")  = 0
     * compare(null, null)   = 0
     * compare("v1", null) &gt; 0
     * compare("1.0.0", "1.0.2") &lt; 0
     * compare("1.0.2", "1.0.2a") &lt; 0
     * compare("1.13.0", "1.12.1c") &gt; 0
     * compare("V0.0.20170102", "V0.0.20170101") &gt; 0
     * </pre>
     *
     * @param version1 版本1
     * @param version2 版本2
     */
    @Override
    public int compare(String version1, String version2) {
        if (StringUtils.equals(version1, version2)) {
            return 0;
        }
        if (version1 == null && version2 == null) {
            return 0;
        } else if (version1 == null) {// null视为最小版本，排在前
            return -1;
        } else if (version2 == null) {
            return 1;
        }

        final String[] v1s = version1.split("\\.");
        final String[] v2s = version2.split("\\.");

        int diff = 0;
        int minLength = Math.min(v1s.length, v2s.length);// 取最小长度值
        String v1;
        String v2;
        for (int i = 0; i < minLength; i++) {
            v1 = v1s[i];
            v2 = v2s[i];
            // 先比较长度
            diff = v1.length() - v2.length();
            if (0 == diff) {
                diff = v1.compareTo(v2);
            }
            if (diff != 0) {
                //已有结果，结束
                break;
            }
        }

        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        return (diff != 0) ? diff : v1s.length - v2s.length;
    }
}
