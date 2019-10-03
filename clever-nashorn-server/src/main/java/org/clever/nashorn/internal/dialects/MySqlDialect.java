package org.clever.nashorn.internal.dialects;

import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-10-03 12:16 <br/>
 */
public class MySqlDialect extends AbstractDialect {
    @Override
    public String doBuildPaginationSql(String originalSql, long offset, long limit, Map<String, Object> paramMap, String firstMark, String secondMark) {
        return originalSql + " LIMIT " + (COLON + firstMark) + COMMA + (COLON + secondMark);
    }
}
