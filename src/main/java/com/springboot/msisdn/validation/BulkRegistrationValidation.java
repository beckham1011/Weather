package com.springboot.msisdn.validation;

import com.springboot.msisdn.entity.BulkRegistration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class BulkRegistrationValidation {

    private static final Logger log = LogManager.getLogger();

    private static Set<String> MSISDNSet = new HashSet<>();

    private List<String> validationErrorList = Collections.synchronizedList(new ArrayList<>());


    private static final String IDCARD_NUMBER_REGEX = "[a-zA-Z0-9]+";
    Pattern IDCARD_NAME_PATTERN = Pattern.compile(IDCARD_NUMBER_REGEX);
    private static final String CHARACTER_NAME_REGEX = "[a-zA-Z]+";
    Pattern NAME_PATTERN = Pattern.compile(CHARACTER_NAME_REGEX);
    private DateTimeFormatter sourceDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    // Line -> Success or failure
    private Map<Integer, Boolean> validateResultIndexMap = new HashMap<>();

    //MSISDN -> Line number list
    private Map<String, List<Integer>> duplicatedMsisdnLineNumberMap = new HashMap<>();

    // Line ->  validate error list
    Map<Integer, List<String>> registrationResultMsgListLineNumberMap = new HashMap<>();

    File f = new File("F:/SmallTestHibernate/BulkRegistrationTestInputOuputConsoleLog/MSISDN.txt");
    FileWriter fw;

    {
        try {
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validateBulkRegistrationList(List<BulkRegistration> bulkRegistrationList) {
        for (int i = 0; i < bulkRegistrationList.size(); i++) {
            BulkRegistration bulkRegistration = bulkRegistrationList.get(i);
            String msisdn = bulkRegistration.getMsisdn();
            validate(bulkRegistration);
            if (StringUtils.isNotBlank(msisdn) && duplicatedMsisdnLineNumberMap.containsKey(msisdn)) {
                validationErrorList.add(formatMSISDNDuplicatedLog(i));
                List<Integer> indexList = duplicatedMsisdnLineNumberMap.get(msisdn);
                indexList.forEach(idx -> {

                    //In registrationResultMsgListLineNumberMap: 1, other validate error, 2 success
                    Set<String> reasonList = new HashSet<>(registrationResultMsgListLineNumberMap.get(idx));

                    //IF all validate pass, but the msisdn duplicated NOW. Success msg will be clear, Add error msg.
                    //it can be seen as hook method.
                    if (validateResultIndexMap.get(idx)) {
                        reasonList.clear();
                        validateResultIndexMap.put(idx, Boolean.FALSE);
                    }
                    reasonList.add(formatMSISDNDuplicatedLog(idx));
                    registrationResultMsgListLineNumberMap.put(idx, new ArrayList<>(reasonList));
                });
                indexList.add(i);
                duplicatedMsisdnLineNumberMap.put(msisdn, indexList);
            } else {
                List<Integer> indexList = new ArrayList<>();
                indexList.add(i);
                duplicatedMsisdnLineNumberMap.put(msisdn, indexList);
            }

            if (validationErrorList.isEmpty()) {
                List<String> successLogList = new ArrayList<>();
                String howToCall = "F".equals(bulkRegistration.getGender()) ? "Mr" : "Miss";
                String successLog = MessageFormat.format("Welcome {0} {1} Bulk Registration Success .\n",
                        howToCall, bulkRegistration.getName());
                successLogList.add(successLog);
                registrationResultMsgListLineNumberMap.put(i, successLogList);
                validateResultIndexMap.put(i, Boolean.TRUE);
            } else {
                List<String> errorList = new ArrayList<>(validationErrorList);
                registrationResultMsgListLineNumberMap.put(i, errorList);
                validateResultIndexMap.put(i, Boolean.FALSE);
            }
            validationErrorList.clear();
        }
        try {
            printLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printLog() throws IOException {
        //output to MSISDN file
        registrationResultMsgListLineNumberMap.forEach((key, value) -> {
            if (!validateResultIndexMap.get(key)) {
                saveErrorToFile(key, value);
                log.info("");
                log.info("Line " + key + " registration failure, reasons as below: ");
                value.forEach(log::info);
            } else {
                log.info("Line " + key + " registration success");
            }
        });

        fw.flush();
        fw.close();

        //Send SMS
        validateResultIndexMap.forEach((bulkKey, value) -> {
            if (value) {
                log.info("Registration success send SMS Log:");
                log.info(registrationResultMsgListLineNumberMap.get(bulkKey));
            }
        });
    }

    private void saveErrorToFile(final Integer lineNumber, List<String> validationErrorList) {
        try {
            fw.append("\n\nLine Number " + lineNumber + " registration error as below:\n");
            validationErrorList.forEach(line -> {
                try {
                    fw.append(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fw.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void validate(BulkRegistration bulkRegistration) {
        validateAllField(bulkRegistration);
        validateName(bulkRegistration.getName());
        validateDateOfBirth(bulkRegistration.getDateOfBirth());
        validateGender(bulkRegistration.getGender());
        validateAddress(bulkRegistration.getAddress());
        validateIDNumber(bulkRegistration.getIdNumber());
        validateSimType(bulkRegistration.getSimType());
        validateMsisdn(bulkRegistration.getMsisdn());
    }

    private void validateAllField(BulkRegistration bulkRegistration) {
        if (StringUtils.isBlank(bulkRegistration.getMsisdn())) {
            validationErrorList.add("MSISDN field should not be empty.\n");
        }
        if (StringUtils.isBlank(bulkRegistration.getName())) {
            validationErrorList.add("Name field should not be empty.\n");
        }

        if (StringUtils.isBlank(bulkRegistration.getDateOfBirth())) {
            validationErrorList.add("DATE_OF_BIRTH field should not be empty.\n");
        }
        if (StringUtils.isBlank(bulkRegistration.getGender())) {
            validationErrorList.add("Gender field should not be empty.\n");
        }
        if (StringUtils.isBlank(bulkRegistration.getAddress())) {
            validationErrorList.add("Address field should not be empty.\n");
        }
        if (StringUtils.isBlank(bulkRegistration.getIdNumber())) {
            validationErrorList.add("ID_NUMBER field should not be empty.\n");
        }
        if (StringUtils.isBlank(bulkRegistration.getSimType())) {
            validationErrorList.add("SIM_TYPE field should not be empty.\n");
        }
        if (StringUtils.isBlank(bulkRegistration.getName())) {
            validationErrorList.add("Name field should not be empty.\n");
        }
    }

    private void validateName(String name) {
        if (StringUtils.isBlank(name)) return;

        if (!NAME_PATTERN.matcher(name).matches()) {
            validationErrorList.add("Name shouldn't have any special character.\n");
        }
    }

    private void validateDateOfBirth(String dateOfBirth) {
        if (StringUtils.isBlank(dateOfBirth)) return;
        LocalDate today = LocalDate.now();
        LocalDate dateTime = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        if (!dateTime.isBefore(today)) {
            validationErrorList.add("DATE_OF_BIRTH shouldn't be TODAY or FUTURE.\n");
        }
    }

    private void validateGender(String gender) {
        if (StringUtils.isBlank(gender)) return;
        if (!("F".equals(gender) || "M".equals(gender))) {
            validationErrorList.add("Gender can only be F or M.\n");
        }
    }

    private void validateAddress(String address) {
        if (StringUtils.isBlank(address)) return;
        if (address.length() < 20) {
            validationErrorList.add("Address must at least be 20 characters long.\n");
        }
    }

    private void validateIDNumber(String idNumber) {
        if (StringUtils.isBlank(idNumber)) return;
        if (!IDCARD_NAME_PATTERN.matcher(idNumber).matches()) {
            validationErrorList.add("ID_NUMBER should be a mix of characters & numbers.\n");
        }
    }

    private void validateSimType(String simType) {
        if (StringUtils.isBlank(simType)) return;
        if (!("PREPAID".equals(simType) || "POSTPAID".equals(simType))) {
            validationErrorList.add("SIM_TYPE can only be PREPAID or POSTPAID.\n");
        }
    }

    private void validateMsisdn(String msisdn) {
        if (StringUtils.isBlank(msisdn)) return;
        if (!MSISDNSet.contains(msisdn)) {
            validationErrorList.add(MessageFormat
                    .format("MSISDN {0} should comply to country's standard (e.g. +66).", msisdn));
        }
    }

    static {
        MSISDNSet.add("86");
        MSISDNSet.add("66");
        MSISDNSet.add("852");
        MSISDNSet.add("886");
        MSISDNSet.add("60");
        MSISDNSet.add("62");
        MSISDNSet.add("65");
        MSISDNSet.add("81");
        MSISDNSet.add("82");
        MSISDNSet.add("7");
        MSISDNSet.add("91");
        MSISDNSet.add("92");
        MSISDNSet.add("26");
    }

    private String formatMSISDNDuplicatedLog(Integer index) {
        return MessageFormat.format("Line Number {0} MSISDN can not be duplicated.", index);
    }

}
