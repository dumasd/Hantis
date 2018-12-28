package com.thinkerwolf.hantis.executor;

import com.thinkerwolf.hantis.common.Param;

import java.util.List;

public class BatchResult {
    private String sql;
    private List<Param> params;
    private int[] updateCounts;

    public BatchResult() {
    }

    public BatchResult(String sql, List <Param> params) {
        this.sql = sql;
        this.params = params;
    }

    public int[] getUpdateCounts() {
        return updateCounts;
    }

    public void setUpdateCounts(int[] updateCounts) {
        this.updateCounts = updateCounts;
    }
}
