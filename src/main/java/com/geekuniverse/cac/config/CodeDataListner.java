package com.geekuniverse.cac.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.geekuniverse.cac.dto.Outfall;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @name: CodeDataListner
 * @author: 谢诗宏
 * @date: 2023-05-25 16:31
 **/
@Slf4j
public class CodeDataListner extends AnalysisEventListener<Outfall> {
    /**
     * 缓存的数据
     */
    private static final List<Outfall> cachedDataList = new ArrayList<>();
    /**
     * 读取
     * @param code
     * @param analysisContext
     */
    @Override
    public void invoke(Outfall code, AnalysisContext analysisContext) {
        log.info("=========start========={}",code);
        cachedDataList.add(code);
    }

    /**
     * 执行完毕
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("=========end=========Code(扫累了)",Outfall.class);
    }

    public static List<Outfall> getData() {
        return cachedDataList;
    }
}
