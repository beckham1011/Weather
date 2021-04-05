package com.springboot.msisdn.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import com.springboot.msisdn.entity.BulkRegistration;
import com.springboot.msisdn.validation.BulkRegistrationValidation;

import java.util.List;

public class BulkRegistrationServiceImpl {

    public static void main(String[] args) {
        BulkRegistrationValidation bulkRegistrationValidation = new BulkRegistrationValidation();
        CsvReader reader = CsvUtil.getReader();
        // CSV file path
        String path = "F:/SmallTestHibernate/BulkRegistrationTestInputOuputConsoleLog/Input-BulkRegistration.csv";
        List<BulkRegistration> bulkRegistrationList = reader.read(ResourceUtil.getUtf8Reader(path), BulkRegistration.class);
        try {
            bulkRegistrationValidation.validateBulkRegistrationList(bulkRegistrationList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
