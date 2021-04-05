package com.springboot.msisdn.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CsvUtil {

    /**
     * 解析csv文件并转成bean
     * @param clazz 类
     * @param <T> 泛型
     * @return 泛型bean集合
     */
    public static  <T> List<T> getCsvData(InputStream inputStream, Class<T> clazz) {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(inputStream, "gbk");
        } catch (Exception e) {
        }

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(clazz);

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(in)
                .withSeparator(',')
                .withQuoteChar('\'')
                .withMappingStrategy(strategy).build();
        return csvToBean.parse();
    }
}
