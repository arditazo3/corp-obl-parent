package com.tx.co.common.utils;

import com.tx.co.back_office.company.domain.Company;

import java.util.List;

public class UtilStatic {

    public static int getIndexByPropertyCompanyList(Long idCompany, List<Company> comparableList) {

        for (int i = 0; i < comparableList.size(); i++) {
            Company company = comparableList.get(i);

            if (company !=null && company.getIdCompany().compareTo(idCompany) == 0) {
                return i;
            }
        }
        return -1;// not there is list
    }

}
